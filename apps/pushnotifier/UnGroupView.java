package nsn.mobile.apps.nsnnotifier;


import nsn.mobile.apps.adapter.MessageCursorAdapter;
import nsn.mobile.apps.nsnnotifier.service.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.text.ClipboardManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class UnGroupView extends Activity {

	private static final String TAG = UnGroupView.class.getSimpleName();
	
	protected SQLiteDatabase db;
	protected Cursor cursor;
    private String ttid="";
    private String gcount="";
    private int mtype;
    private TextView titletext;
    private NotificationManager notificationManager;
	protected ListAdapter adapter;
    public Context c;
    protected ListView lv;
    protected String s;
	protected String s2;
	String[] longpress={"Archieve","Copy message text","Delete","Delivery status"};
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "UnGrouped view service connection established");

			api = NotificationCollectorApi.Stub.asInterface(service);
			try {
				api.registerListener(collectorListener);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to add ungrouped view listener", e);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "UnGrouped view service connection closed");			
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
        c=this;
        setContentView(R.layout.ttbox);
        ttid = getIntent().getStringExtra("subject");
        gcount = getIntent().getStringExtra("unreadcount");
        mtype=getIntent().getIntExtra("mtype",0);
        titletext=(TextView) findViewById(R.id.texttt);
        titletext.setText(ttid);
        
        db = (new DatabaseHelper(this)).getWritableDatabase();

        lv=(ListView)findViewById(R.id.list2);
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Toast.makeText(getApplicationContext(), arg2+"", Toast.LENGTH_SHORT).show();
				
			}
		});
        lv.setOnItemLongClickListener( new OnItemLongClickListener() {
           	@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
           				final Dialog d = new Dialog(c,R.style.Dialog);
           				final View v1=arg1;
           				
         				s=cursor.getString(cursor.getColumnIndex("_id"));
         				Toast.makeText(getApplicationContext(), arg3+"---"+s, Toast.LENGTH_SHORT).show();
				         LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				         View v = li.inflate(R.layout.popup, null, false);
				         d.setContentView(v);d.setCancelable(true);d.show();
         				ListView l1=(ListView) d.findViewById(R.id.listView1);
         				TextView t1=(TextView) d.findViewById(R.id.title1);
         				t1.setText("Notification Options");
         				l1.setOnItemClickListener(new OnItemClickListener() 
						 {
         					public void onItemClick(AdapterView<?> arg4, View arg5, int arg6, long arg7)
         						    {
         						
         						if(arg6==0){
             						d.cancel();
             						 final Dialog d2 = new Dialog(c,R.style.Dialog);
       						       LayoutInflater lj = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       						         View v2 = lj.inflate(R.layout.confirmbox, null, false);
       						         
             						d2.setContentView(v2);
             						d2.setCancelable(true);
             						final CheckBox cb1=(CheckBox) d2.findViewById(R.id.checkBox1);
             						TextView txt1=(TextView)d2.findViewById(R.id.confirmtext);
             						TextView txt2=(TextView)d2.findViewById(R.id.title2);
             						txt2.setText("Confirm Archieve");
             						txt1.setText("This Notification will be achieved!");
             						Button bt1=(Button) d2.findViewById(R.id.del1);
             						bt1.setText("Archieve");
             						Button bt2=(Button) d2.findViewById(R.id.can1);
             						bt1.setOnClickListener(new OnClickListener() {
    									@Override
    									public void onClick(View arg0) {
    										d2.cancel();
    										db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+s+",3,datetime('now','localtime'),0,0)");//									    		      
    										db.execSQL("delete from notebox where _id="+s);
    								    	Cursor cursor1=db.rawQuery("select _id,status,subject from notebox where subject=? and mtype=?",new String[]{ttid,""+mtype});
    								    	if(cursor1.getCount()==0)
    								    	{onBackPressed();}
    								    	else
    								    	{
    								    		cursor.requery();
    								    		adapter = new MessageCursorAdapter(c, R.layout.singlett, cursor, new String[] {"sender","msg", "sent_time"},new int[] {R.id.sender1,R.id.textmsg, R.id.stime2});
    								    		lv.setAdapter(adapter);
    								    	
    								    	}
    									}});
             						bt2.setOnClickListener(new OnClickListener() {
    									@Override
    									public void onClick(View arg0) {
    										d2.cancel();
    									}});
             						cb1.setVisibility(View.GONE);
             						d2.show();
             						    }
         						
         						
         						else	if(arg6==2){
         						d.cancel();
         						 final Dialog d2 = new Dialog(c,R.style.Dialog);
   						       LayoutInflater lj = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   						         View v2 = lj.inflate(R.layout.confirmbox, null, false);
         						d2.setContentView(v2);
         						d2.setCancelable(true);
         						final CheckBox cb1=(CheckBox) d2.findViewById(R.id.checkBox1);
         						Button bt1=(Button) d2.findViewById(R.id.del1);
         						Button bt2=(Button) d2.findViewById(R.id.can1);
         						bt1.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										d2.cancel();
										db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+s+",5,datetime('now','localtime'),0,0)");//									    		      
										db.execSQL("delete from notebox where _id="+s);
								    	Cursor cursor1=db.rawQuery("select _id,status,subject from notebox where subject=? and mtype=?",new String[]{ttid,""+mtype});
								    	if(cursor1.getCount()==0)
								    	{onBackPressed();}
								    	else
								    	{cursor.requery();
								    	adapter = new MessageCursorAdapter(c, R.layout.singlett, cursor, new String[] {"sender","msg", "sent_time"},new int[] {R.id.sender1,R.id.textmsg, R.id.stime2});
								    	lv.setAdapter(adapter);
								    	}
									}});
         						bt2.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										d2.cancel();
									}});
         						cb1.setVisibility(View.GONE);
         						d2.show();
         						    }
         						else if(arg6==1)
         						{
         						   ClipboardManager nsnclipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
         						   TextView t1=(TextView) v1.findViewById(R.id.textmsg);
         						   nsnclipboard.setText(t1.getText());
         						  d.cancel();
         						 Toast.makeText(getApplicationContext(), "Text copied", Toast.LENGTH_SHORT).show();
         						}
         						
         						
         						
         						
         						
         						
         						
         						
         						    
         						    }}         				);
         				l1.setAdapter(new ArrayAdapter<String>(c,R.layout.textbox, longpress));
				return true;
			}});

        
        
        handler = new Handler();
        
        
        
        Intent intent = new Intent(NotificationCollectorService.class.getName());
              
        startService(intent); 
        
        bindService(intent, serviceConnection, 0);
        
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
        Log.i(TAG, "UnGroupd view activity created");
        
        loadList();
    }
    
    public void loadList()
    {	
    	cursor = this.db.rawQuery("select _id,sender,subject,msg," +
    			"case " +
    			"when month1='' then " +
    			"hours1 " +
    			"when year1=strftime('%Y','now') then " +
    			"hours1||', '||day1||' '||month1||'' " +
    			"else " +
    			"day1||' '||month1||''||year1 " +
    			"end " +
    			"as sent_time from(" +
    			"select _id,sender,subject,msg," +
        		"case" +
        		" when strftime('%Y-%m-%d',sent_time)= strftime('%Y-%m-%d','now') then ''" +
        		" when strftime('%m',sent_time)='01' then 'Jan'" +
        		" when strftime('%m',sent_time)='02' then 'Feb'" +
        		" when strftime('%m',sent_time)='03' then 'Mar'" +
        		" when strftime('%m',sent_time)='04' then 'Apr'" +
        		" when strftime('%m',sent_time)='05' then 'May'" +
        		" when strftime('%m',sent_time)='06' then 'Jun'" +
        		" when strftime('%m',sent_time)='07' then 'Jul'" +
        		" when strftime('%m',sent_time)='08' then 'Aug'" +
        		" when strftime('%m',sent_time)='09' then 'Sep'" +
        		" when strftime('%m',sent_time)='10' then 'Oct'" +
        		" when strftime('%m',sent_time)='11' then 'Nov'" +
        		" when strftime('%m',sent_time)='12' then 'Dec'" +
        		" end as month1," +
        		"case" +
        		" when strftime('%Y',sent_time)=strftime('%Y','now') then strftime('%Y',sent_time) else ', '||strftime('%Y',sent_time)" +
        		" end as year1," +
        		" case" +
        		" when strftime('%Y-%m-%d',sent_time)=strftime('%Y-%m-%d','now') then '' else strftime('%d',sent_time)" +
        		" end" +
        		" as day1," +
        		" CASE" +
        		"  WHEN (cast(strftime('%H', sent_time)as integer) - 12) = -12" +
        		"  THEN  '12:' || strftime('%M', sent_time) ||' '|| 'AM'" +
        		"  WHEN (cast(strftime('%H', sent_time)as integer) - 12) = 0" +
        		"  THEN '12:' || strftime('%M', sent_time) ||' '|| 'PM'" +
        		"  WHEN (cast(strftime('%H', sent_time)as integer) - 12) < 0" +
        		"  THEN  strftime('%H', sent_time) ||':'||" +
        		"   strftime('%M',sent_time) ||' '|| 'AM'" +
        		"  ELSE" +
        		"    (cast(strftime('%H', sent_time) as integer) - 12) ||':'|| strftime('%M', sent_time) ||' '|| 'PM'" +
        		"  END as hours1" +
        		" from (" +
        		" select _id,sender,subject,msg,sent_time from notebox where subject=? and mtype=? order by sent_time desc))", new String[]{ttid,""+mtype});
       
    	if(cursor.getCount()==0)
    	{
    		onBackPressed();
    	}
    	else
    	{
    	adapter = new MessageCursorAdapter(this, R.layout.singlett, cursor, new String[] {"sender","msg", "sent_time"},new int[] {R.id.sender1,R.id.textmsg, R.id.stime2});
 
    	lv.setAdapter(adapter);
    	
      
//        lv.setSelection(cursor.getCount()-1);
    	}
        //cursor.close();
        if(!gcount.equals("")){
        	updateNotificationStatus();
        }
    }
    
	@Override
	protected void onDestroy() {
		try {
			api.unregisterListener(collectorListener);
			unbindService(serviceConnection);
			cursor.close();
			db.close();
			
		} catch (Throwable t) {
			Log.w(TAG, "Failed to unbind from the service", t);
		}
		Log.i(TAG, "UnGroupd view activity destroyed");
		super.onDestroy();
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
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ungroupmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.delete1:
			s = "" + android.R.drawable.ic_menu_search;
			db.close();
			cursor.close();
			finish();
			intent = new Intent(c, MultiUngroup.class);
			intent.putExtra("TT_ID",ttid);
			intent.putExtra("type", 0);
			intent.putExtra("mtype",mtype);
			startActivity(intent);
			break;
		case R.id.archieve:
			db.close();
			cursor.close();
			finish();
			intent = new Intent(c, MultiUngroup.class);
			intent.putExtra("TT_ID",ttid);
			intent.putExtra("type", 1);
			intent.putExtra("mtype",mtype);
			startActivity(intent);
			break;
		default:

			break;
		}
		return true;
	}
	
	private void updateNotificationStatus() {
		handler.post( new Runnable() {
			@Override
			public void run() {
				try {
					Integer msgid=0;
					Cursor inscursor=db.rawQuery("select _id,status,subject from notebox where subject=? and mtype=? and status=? order by sent_time desc limit 1",new String[]{ttid,""+mtype,"1"});
			    	if (cursor.moveToFirst()) {
	    		      msgid = cursor.getInt(cursor.getColumnIndex("_id"));
	    		      db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("+msgid+",0,datetime('now','localtime'),1,0)");
			    	}
			    	db.execSQL("update notebox set status =0 where subject='"+ttid+"' and mtype="+mtype+" and status=1");
			    	
			    	inscursor=db.rawQuery("select _id,subject,msg,sum(status) as gcount,strftime('%s',sent_time,'utc')*1000 as sent_time from notebox where status=? group by subject order by sent_time desc",new String[]{"1"});
			    	int ncount=inscursor.getCount();
			    	
			    	if(ncount==0) {
			    		notificationManager.cancel(10001);
			    	} else if(ncount==1) {
			    		inscursor.moveToFirst();
			    		Notification notification = new Notification(android.R.drawable.ic_dialog_email, "You have new Notifications!", inscursor.getLong(4));
			    		Intent notificationIntent = new Intent(getBaseContext(), UnGroupView.class);
			    		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        notificationIntent.putExtra("subject",inscursor.getString(1));
				        notificationIntent.putExtra("unreadcount",Integer.toString(ncount)); 
				        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				        if(inscursor.getInt(3)>1){
					        notification.setLatestEventInfo(getBaseContext(), inscursor.getString(1), inscursor.getInt(3)+" unread notifications", pendingIntent);
				        } else {
					        notification.setLatestEventInfo(getBaseContext(), inscursor.getString(1), inscursor.getString(2), pendingIntent);
				        }
				        notificationManager.notify(10001, notification);
			    	} else {
			    		inscursor.moveToFirst();
			    		Notification notification = new Notification(android.R.drawable.ic_dialog_email, "You have new Notifications!", inscursor.getLong(4));
			    		Intent notificationIntent = new Intent(getBaseContext(), GroupView.class); 
				    	notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    	PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
				    	inscursor=db.rawQuery("select count(*) as unread from notebox where status=?",new String[]{"1"});
				    	inscursor.moveToFirst();
				        notification.setLatestEventInfo(getBaseContext(), "New Notifications", inscursor.getInt(0)+" unread notifications", pendingIntent);
				        notificationManager.notify(10001, notification);
			    	}
				} catch (Throwable t) {
					Log.e(TAG, "Error while updating the notification status", t);
				}
			}
		});
	}
	
	private void updateNotificationView() {
		handler.post( new Runnable() {
			@Override
			public void run() {
				try {
//					NotificationSearchResult result = api.getLatestSearchResult();
					cursor.requery();
//					lv.setSelection(cursor.getCount()-1);
					updateNotificationStatus();
				} catch (Throwable t) {
					Log.e(TAG, "Error while updating the UI with notifications", t);
				}
			}
		});
	}
}