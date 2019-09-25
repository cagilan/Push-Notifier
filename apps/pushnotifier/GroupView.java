package nsn.mobile.apps.nsnnotifier;


import nsn.mobile.apps.adapter.InboxCursorAdapter;
import nsn.mobile.apps.nsnnotifier.R;
import nsn.mobile.apps.nsnnotifier.service.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class GroupView extends Activity {

	private static final String TAG = GroupView.class.getSimpleName();
	  private int ttid;
	    private String title="";
	protected SQLiteDatabase db;
	public Cursor cursor;
	protected ListAdapter adapter;
	public Integer press = 0;
	public Integer lastpress = 0;
	protected ListView lv;
	public Context c;
	protected String s;
	protected String s2;
	String[] longpress = {"Archieve", "Delete"};
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "Grouped view service connection established");

			api = NotificationCollectorApi.Stub.asInterface(service);
			try {
				api.registerListener(collectorListener);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to add grouped view listener", e);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "Grouped view service connection closed");			
		}
	};
	
	private NotificationCollectorApi api;
		
	private Handler handler;
	
	private NotificationCollectorListener.Stub collectorListener = new NotificationCollectorListener.Stub() {
		@Override
		public void handleNotificationsUpdated() throws RemoteException {
			updateNotificationView();
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        ttid = getIntent().getIntExtra("mtype",0);
        title = getIntent().getStringExtra("title");
     
        db = (new DatabaseHelper(this)).getWritableDatabase();
        
        c = this;
        TextView tv=(TextView)findViewById(R.id.textinbox);
        tv.setText(title);
        lv = (ListView) findViewById(R.id.inboxlist);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				lastpress = arg2;
				Intent intent = new Intent(c, UnGroupView.class);
				Cursor cursor2 = (Cursor) adapter.getItem(arg2);
				intent.putExtra("subject", cursor2.getString(cursor2.getColumnIndex("subject")));
				intent.putExtra("unreadcount", cursor2.getString(cursor2.getColumnIndex("gcount")));
				intent.putExtra("mtype",ttid);
			
//				db.close();
//				cursor2.close();
				startActivity(intent);
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final Dialog d = new Dialog(c, R.style.Dialog);
				s = cursor.getString(cursor.getColumnIndex("subject"));
				s2 = cursor.getString(cursor.getColumnIndex("gcount"));

				LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = li.inflate(R.layout.popup, null, false);
				d.setContentView(v);
				d.setCancelable(true);
				d.show();
				ListView l1 = (ListView) d.findViewById(R.id.listView1);
				TextView t1 = (TextView) d.findViewById(R.id.title1);
				t1.setText(s);
				l1.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg4, View arg5,
							int arg6, long arg7) {
						if (arg6 == 0) {
							
							d.cancel();
							final Dialog d2 = new Dialog(c, R.style.Dialog);
							LayoutInflater lj = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View v2 = lj.inflate(R.layout.confirmbox, null, false);
							d2.setContentView(v2);
							d2.setCancelable(true);
							final CheckBox cb1 = (CheckBox) d2.findViewById(R.id.checkBox1);
							TextView txt1=(TextView)d2.findViewById(R.id.confirmtext);
     						TextView txt2=(TextView)d2.findViewById(R.id.title2);
     						txt2.setText("Confirm Archieve");
     						txt1.setText("All read notifications will be archieved!");
							Button bt1 = (Button) d2.findViewById(R.id.del1);
							bt1.setText("Archieve");
							Button bt2 = (Button) d2.findViewById(R.id.can1);
							bt1.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									d2.cancel();
									Cursor cursor1 = db
											.rawQuery("select _id,status,subject from notebox where subject=? and mtype="+ttid+" order by sent_time desc limit 1", new String[] { s });
									Integer mid;
								//	if (s2.equals("")) {
										if (cursor1.moveToFirst()) {
											mid = cursor1.getInt(cursor1.getColumnIndex("_id"));
											db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+ mid + ",3,datetime('now','localtime'),1,0)");
											db.execSQL("delete from notebox where subject='"+ s + "' and mtype="+ttid+" and status=0 and _id<="+mid);
										}
										cursor.requery();
								//	} else {
//										if (cb1.isChecked()) {
//											if (cursor1.moveToFirst()) {
//													mid = cursor1.getInt(cursor1.getColumnIndex("_id"));
//													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+ mid+ ",5,datetime('now','localtime'),1,1)");
//													db.execSQL("delete from notebox where subject='"+ s + "' and _id<="+mid);
//											}
//											cursor.requery();
//										} else {
//											cursor1 = db
//													.rawQuery(
//															"select _id,status,subject from notebox where subject=? and status=0 order by sent_time desc limit 1",new String[] { s });
//											if (cursor1.moveToFirst()) {
//													mid = cursor1.getInt(cursor1.getColumnIndex("_id"));
//													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+ mid+ ",5,datetime('now','localtime'),1,0)");
//												db.execSQL("delete from notebox where subject='"+ s + "' and status=0 and _id<="+mid);
//											}
//											cursor.requery();
//										}
								//	}
									
									if(cursor.getCount()==0) {
										LinearLayout empty=(LinearLayout) findViewById(R.id.empty);
										empty.setVisibility(View.VISIBLE);
										lv.setVisibility(View.GONE);
									}
								}
							});
							bt2.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									d2.cancel();
								}
							});
						//	if (s2.equals("")) {
								cb1.setVisibility(View.GONE);

						//	} else {
							//	cb1.setVisibility(View.VISIBLE);
						//	}

							d2.show();

						}
						else if(arg6==1){
							d.cancel();
							final Dialog d2 = new Dialog(c, R.style.Dialog);
							LayoutInflater lj = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View v2 = lj.inflate(R.layout.confirmbox, null, false);
							d2.setContentView(v2);
							d2.setCancelable(true);
							final CheckBox cb1 = (CheckBox) d2.findViewById(R.id.checkBox1);
							Button bt1 = (Button) d2.findViewById(R.id.del1);
							Button bt2 = (Button) d2.findViewById(R.id.can1);
							bt1.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									d2.cancel();
									Cursor cursor1 = db
											.rawQuery("select _id,status,subject from notebox where subject=?  and mtype="+ttid+" order by sent_time desc limit 1", new String[] { s });
									Integer mid;
									if (s2.equals("")) {
										if (cursor1.moveToFirst()) {
											mid = cursor1.getInt(cursor1.getColumnIndex("_id"));
											db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+ mid + ",5,datetime('now','localtime'),1,0)");
											db.execSQL("delete from notebox where subject='"+ s + "' and mtype="+ttid+" and _id<="+mid);
										}
										cursor.requery();
									} else {
										if (cb1.isChecked()) {
											if (cursor1.moveToFirst()) {
													mid = cursor1.getInt(cursor1.getColumnIndex("_id"));
													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+ mid+ ",5,datetime('now','localtime'),1,1)");
													db.execSQL("delete from notebox where subject='"+ s + "' and mtype="+ttid+" and _id<="+mid);
											}
											cursor.requery();
										} else {
											cursor1 = db
													.rawQuery(
															"select _id,status,subject from notebox where subject=? and status=0 order by sent_time desc limit 1",new String[] { s });
											if (cursor1.moveToFirst()) {
													mid = cursor1.getInt(cursor1.getColumnIndex("_id"));
													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+ mid+ ",5,datetime('now','localtime'),1,0)");
												db.execSQL("delete from notebox where subject='"+ s + "' and status=0 and mtype="+ttid+" and _id<="+mid);
											}
											cursor.requery();
										}
									}
									
									if(cursor.getCount()==0) {
										LinearLayout empty=(LinearLayout) findViewById(R.id.empty);
										empty.setVisibility(View.VISIBLE);
										lv.setVisibility(View.GONE);
									}
								}
							});
							bt2.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									d2.cancel();
								}
							});
							if (s2.equals("")) {
								cb1.setVisibility(View.GONE);

							} else {
								cb1.setVisibility(View.VISIBLE);
							}

							d2.show();

							
							
						}
					}

				});

				l1.setAdapter(new ArrayAdapter<String>(c, R.layout.textbox, longpress));
				return true;
			}
		});
        
        handler = new Handler();
        
        Intent intent = new Intent(NotificationCollectorService.class.getName());
              
        startService(intent);   
        
        bindService(intent, serviceConnection, 0);

        Log.i(TAG, "Grouped view activity created");
    }
    
    public void onResume() {
		super.onResume();
		
		cursor = this.db
				.rawQuery(
						"select _id,subject,msg,gcount,"
								+ "case "
								+ "when month1='' then "
								+ "hours1 "
								+ "else "
								+ "day1||' '||month1||''||year1 "
								+ "end "
								+ "as sent_time from("
								+ "select _id,subject,msg,gcount,"
								+ ""
								+ ""
								+ "case"
								+ " when strftime('%Y-%m-%d',sent_time)= strftime('%Y-%m-%d','now') then ''"
								+ " when strftime('%m',sent_time)='01' then 'Jan'"
								+ " when strftime('%m',sent_time)='02' then 'Feb'"
								+ " when strftime('%m',sent_time)='03' then 'Mar'"
								+ " when strftime('%m',sent_time)='04' then 'Apr'"
								+ " when strftime('%m',sent_time)='05' then 'May'"
								+ " when strftime('%m',sent_time)='06' then 'Jun'"
								+ " when strftime('%m',sent_time)='07' then 'Jul'"
								+ " when strftime('%m',sent_time)='08' then 'Aug'"
								+ " when strftime('%m',sent_time)='09' then 'Sep'"
								+ " when strftime('%m',sent_time)='10' then 'Oct'"
								+ " when strftime('%m',sent_time)='11' then 'Nov'"
								+ " when strftime('%m',sent_time)='12' then 'Dec'"
								+ " end"
								+ " as month1,"
								+ "case"
								+ " when strftime('%Y',sent_time)=strftime('%Y','now') then '' else ', '||strftime('%Y',sent_time)"
								+ " end"
								+ " as year1,"
								+ ""
								+ ""
								+ " case"
								+ " when strftime('%Y-%m-%d',sent_time)=strftime('%Y-%m-%d','now') then '' else strftime('%d',sent_time)"
								+ " end"
								+ " as day1,"
								+ " case"
								+ "  when (cast(strftime('%H', sent_time)as integer) - 12) = -12"
								+ "  THEN  '12:' || strftime('%M', sent_time) ||' '|| 'AM'"
								+ "  WHEN (cast(strftime('%H', sent_time)as integer) - 12) = 0"
								+ "  THEN '12:' || strftime('%M', sent_time) ||' '|| 'PM'"
								+ "  WHEN (cast(strftime('%H', sent_time)as integer) - 12) < 0"
								+ "  THEN  strftime('%H', sent_time) ||':'||"
								+ "    strftime('%M',sent_time) ||' '|| 'AM'"
								+ "  ELSE"
								+ "    (cast(strftime('%H', sent_time) as integer) - 12) ||':'|| strftime('%M', sent_time) ||' '|| 'PM'"
								+ "  END as hours1"
								+ " from ("
								+ "select _id,subject,msg,sent_time,case when gcount=0 then '' else gcount end as gcount from(select _id,subject, msg,sent_time,sum"
								+ "(status) as gcount, status from notebox where status<=? and mtype=? group by subject order by sent_time desc)))",
						new String[] { "1" ,""+ttid});
		if(cursor.getCount()==0) {
			LinearLayout empty=(LinearLayout) findViewById(R.id.empty);
			empty.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
		} else {
			LinearLayout empty=(LinearLayout) findViewById(R.id.empty);
			empty.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
		}
		adapter = new InboxCursorAdapter(this, R.layout.list_item1, cursor, new String[] { "subject", "msg", "gcount", "sent_time" }, new int[] { R.id.subject, R.id.msg, R.id.gcount, R.id.stime });
		lv.setAdapter(adapter);
		lv.setSelection(this.lastpress);
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		try {
			api.unregisterListener(collectorListener);
			unbindService(serviceConnection);
			cursor.close();
			db.close();
			
		} catch (Throwable t) {
			Log.w(TAG, "Failed to unbind from the service", t);
		}
		Log.i(TAG, "Grouped view activity destroyed");
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		if(cursor.getCount()==0)
		{
	   menu.clear();}
	    return true;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 super.onCreateOptionsMenu(menu);  
		if(cursor.getCount()!=0)
		{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.inboxmenu, menu);
		setMenuBackground();
		}
		return true;
	}
	  protected void setMenuBackground(){  
          
          Log.d(TAG, "Enterting setMenuBackGround");  
          getLayoutInflater().setFactory( new Factory() {  
                
              @Override  
              public View onCreateView( String name, Context context, AttributeSet attrs ) {  
                
                  if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {  
                        
                      try { // Ask our inflater to create the view  
                          LayoutInflater f = getLayoutInflater();  
                          final View view = f.createView( name, null, attrs );  
                          /*  
                           * The background gets refreshed each time a new item is added the options menu.  
                           * So each time Android applies the default background we need to set our own  
                           * background. This is done using a thread giving the background change as runnable 
                           * object 
                           */  
                          new Handler().post( new Runnable() {  
                              public void run () {  
                                  view.setBackgroundResource( R.drawable.btn_white_matte2);  
                              }  
                          } );  
                          return view;  
                      }  
                      catch ( InflateException e ) {}  
                      catch ( ClassNotFoundException e ) {}  
                  }  
                  return null;  
              }

			 
          });  
      } 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.delete1:
			
			intent = new Intent(c, CustomSelection.class);
			intent.putExtra("mtype", ttid);
			intent.putExtra("type",0);
			startActivity(intent);
			break;
		case R.id.settings:
			intent = new Intent(c, PreferenceSettings.class);
			startActivity(intent);
			break;
		case R.id.archieve:
			intent=new Intent(c,CustomSelection.class);
			intent.putExtra("mtype",ttid);
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		}

		return true;
	}

	private void updateNotificationView() {
		handler.post( new Runnable() {
			@Override
			public void run() {
				try {
					NotificationSearchResult result = api.getLatestSearchResult();
					if (!result.getNotifications().isEmpty()) {
						cursor.requery();
						if(lv.isShown()==false) {
							LinearLayout empty=(LinearLayout) findViewById(R.id.empty);
							empty.setVisibility(View.GONE);
							lv.setVisibility(View.VISIBLE);
						}

					}
				} catch (Throwable t) {
					Log.e(TAG, "Error while updating the UI with Notifications", t);
				}
			}
		});
	}
}