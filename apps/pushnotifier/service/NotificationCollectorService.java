package nsn.mobile.apps.nsnnotifier.service;
import nsn.mobile.apps.nsnnotifier.*;
import nsn.mobile.apps.nsnnotifier.communication.*;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NotificationCollectorService extends Service {
	
	private static final String TAG = NotificationCollectorService.class.getSimpleName();
	
	private String simno=null;
	
	private final Context ctx=this;
	
	private Timer timer;
	
	protected SQLiteDatabase db;
	
	private PowerManager.WakeLock wakeLock;
	
	private ConnectivityManager cm;
	
	private NotificationManager notificationManager;
	
	SharedPreferences spref;
		
	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			Log.i(TAG, "Searching for new notifications");
			
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				try {

					JSONObject record;
					JSONArray recordset;
					Boolean newnoti=false;
					String result=new String();
					NotificationSearchResult newSearchResult = new NotificationSearchResult();
					
					Cursor cursor=db.rawQuery("select _id,msgid,status,read_time,delinfo,readinfo from tbox order by _id asc",null);
	
		            if (cursor.moveToFirst()) {
		            	int rowid=0;
		            	recordset = new JSONArray();
		            	do {
		            	   rowid=cursor.getInt(0);
		    			   record = new JSONObject();
		    			   record.put("rif", cursor.getInt(5));
		    			   record.put("dif", cursor.getInt(4));
		    			   record.put("rtm", cursor.getString(3));
		    			   record.put("sta", cursor.getInt(2));
		    			   record.put("mid", cursor.getInt(1));
		    			   recordset.put(record);
		    		   } while(cursor.moveToNext());
		               result=NSNConnect.postContent(ctx, "upmsgsta", recordset.toString());
		               db.execSQL("delete from tbox where _id<="+rowid);
		    		}
	
					cursor = db.rawQuery("select max(_id) as msgid from notebox", null);
					if(cursor.moveToFirst()) {
						result=NSNConnect.getContent(ctx, "getnewmsg/"+simno+"/"+cursor.getInt(0));
					}
					
					if(!result.isEmpty()) {
						
						record = new JSONObject(result);
						
						if(record.getInt("acena")==1) {
							
							recordset = record.getJSONArray("data");
							
				            for (int i = 0; i < recordset.length(); i++) {
				            	record = new JSONObject(recordset.getString(i));
				                db.execSQL("insert into notebox(_id,mtype,subject,sender,msg,sent_time,receive_time,status) values("+record.getInt("mid")+", "+record.getInt("typ")+",'"+record.getString("sub")+"','"+record.getString("frm")+"','"+record.getString("msg")+"','"+record.getString("stm")+"',datetime('now','localtime'),"+record.getInt("sts")+")");
				    			Notifications Notification = new Notifications(record.getInt("mid"));
				    			newSearchResult.addNotification(Notification);
				            	if(newnoti==false && record.getInt("sts")==1) {
				            		newnoti=true;
				            	}
				            }
				            
							synchronized (latestSearchResultLock) {
								latestSearchResult = newSearchResult;
							}
						
							synchronized (listeners) {
								final int N = listeners.beginBroadcast();
								for (int i = 0; i < N; i++) {
									try {
										listeners.getBroadcastItem(i).handleNotificationsUpdated();
									} catch (RemoteException e) {
										Log.w(TAG, "Failed to notify listener " + listeners, e);
									}
								}
								listeners.finishBroadcast();
							}
							
				            if(newnoti==true) {
				            	
				            	db.execSQL("insert into tbox(msgid,status,read_time,delinfo) values("+record.getInt("mid")+",2,strftime('%Y-%m-%d %H:%M:%S','now','localtime'),1)");
				            
								cursor=db.rawQuery("select _id,subject,msg,sum(status) as gcount,strftime('%s',sent_time,'utc')*1000 as sent_time from notebox where status=? group by subject order by sent_time desc",new String[]{"1"});
						    	int ncount=cursor.getCount();
						    	cursor.moveToFirst();
						    	
						    	notificationManager.cancel(10001);
						    	Notification notification = new Notification(android.R.drawable.ic_dialog_email, "You have new Notifications!", cursor.getLong(4));
					    		//notification.defaults=Notification.DEFAULT_ALL;
						    	spref = getSharedPreferences("nsn.mobile.apps.nsnnotifier_preferences",MODE_PRIVATE);
						    	notification.defaults |= Notification.DEFAULT_LIGHTS;
						    	notification.sound = Uri.parse(spref.getString("ringtoneuri", "content://settings/system/notification_sound"));
						    	if(spref.getBoolean("vibration", false)) {
						    		notification.defaults |= Notification.DEFAULT_VIBRATE;
						    	}
					    		
					    		if(ncount>1) {
					    			Intent notificationIntent = new Intent(getBaseContext(), GroupView.class);
							    	notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							    	PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | Intent.FLAG_ACTIVITY_NEW_TASK);
							    	cursor=db.rawQuery("select count(*) as unread from notebox where status=?",new String[]{"1"});
							    	cursor.moveToFirst();
							        notification.setLatestEventInfo(getBaseContext(), "New Notifications", cursor.getInt(0)+" unread notifications", pendingIntent);
					    		} else {
					    			Intent notificationIntent = new Intent(getBaseContext(), UnGroupView.class);
						    		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							        notificationIntent.putExtra("subject",cursor.getString(1));
							        notificationIntent.putExtra("unreadcount",Integer.toString(ncount)); 
							        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FLAG_ACTIVITY_NEW_TASK);
						    		if(cursor.getInt(3)>1) {
									    notification.setLatestEventInfo(getBaseContext(), cursor.getString(1), cursor.getInt(3)+" unread notifications", pendingIntent);
							        } else {
							        	notification.setLatestEventInfo(getBaseContext(), cursor.getString(1), cursor.getString(2), pendingIntent);
							        }
					    		}
						    	notificationManager.notify(10001, notification);
				            }
						} else {
							spref = getSharedPreferences("MyPrefs",MODE_PRIVATE);
							SharedPreferences.Editor prefEditor = spref.edit();
							prefEditor.putString("Pacenable", record.getString("acena"));
							prefEditor.commit();
							Intent serviceIntent = new Intent(NotificationCollectorService.class.getName());
							ctx.stopService(serviceIntent);
							
						}
					}
		            
		            cursor.close();
					Log.d(TAG, "Retrieved " + newSearchResult.getNotifications().size() + " Notifications");
					System.gc();
				} catch (NSNConnectException e) {
					Log.e(TAG, "Failed to communication with server", e);
				} catch (Throwable t) {
					Log.e(TAG, "Failed to retrive notifications", t);
				}
			} else {
				Log.e(TAG, "No network connection");
			}
		}
	};
	
	private final Object latestSearchResultLock = new Object();
	
	private NotificationSearchResult latestSearchResult = new NotificationSearchResult();

	private RemoteCallbackList<NotificationCollectorListener> listeners = new RemoteCallbackList<NotificationCollectorListener>();
	
	private NotificationCollectorApi.Stub apiEndpoint = new NotificationCollectorApi.Stub() {
		
		@Override
		public NotificationSearchResult getLatestSearchResult() throws RemoteException {
			synchronized (latestSearchResultLock) {
				return latestSearchResult;
			}
		}
		
		@Override
		public void registerListener(NotificationCollectorListener listener) throws RemoteException {
			synchronized (listeners) {
				listeners.register(listener);
			}
		}

		@Override
		public void unregisterListener(NotificationCollectorListener listener) throws RemoteException {
			synchronized (listeners) {
				listeners.unregister(listener);
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		if (NotificationCollectorService.class.getName().equals(intent.getAction())) {
			Log.d(TAG, "Bound by intent " + intent);
			return apiEndpoint;
		} else {
			return null;
		}
	}
	
	 @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      // We want this service to continue running until it is explicitly
	      // stopped, so return sticky.
		 Log.i(TAG, "Service created, started running");
	      return START_STICKY;
	  } 
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");
		db = (new DatabaseHelper(getApplicationContext())).getWritableDatabase();
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		PowerManager pmgr = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pmgr.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();
		simno=getMyPhoneNumber();
		timer = new Timer("NotificationCollectorTimer");
		spref = getSharedPreferences("nsn.mobile.apps.nsnnotifier_preferences",MODE_PRIVATE);
		timer.scheduleAtFixedRate(updateTask, 5000, Long.parseLong(spref.getString("servertime", "60000")));
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service destroying");
		
		db.close();
		timer.cancel();
		timer = null;
		wakeLock.release();
		super.onDestroy();
	}
	
	private String getMyPhoneNumber() {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getSimSerialNumber();
	}
}
