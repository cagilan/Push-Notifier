package nsn.mobile.apps.nsnnotifier;


//import samples.employeedirectory.EmployeeDetails.EmployeeActionAdapter;

import nsn.mobile.apps.nsnnotifier.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;


import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
public class MultiUngroup extends Activity {
	

	//	protected TextView employeeNameText;
		protected TextView titletext;
	//	protected List<EmployeeAction> actions;
		protected ListAdapter adapter;
	    protected String ttid;
	    protected int mtype,type;
		protected Button del1;
		protected Button mark1;
		private String my_sel_items;
		protected String prior_condi;
	    public SQLiteDatabase db;
	    private String str[];
	    public Cursor cursor;
	    public Context c;
	    protected ListView lv;
	    protected String s;
		protected String s2;
		SparseBooleanArray a;
		String[] longpress={"Delete","Edit","Copy"};
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.multiungrouptt);
	        c=this;
	        ttid = getIntent().getStringExtra("TT_ID");
	        type=getIntent().getIntExtra("type",0);
	        mtype = getIntent().getIntExtra("mtype",0);
	        
	        
	        del1 = (Button) findViewById(R.id.del1);
	        titletext=(TextView) findViewById(R.id.mtitle);
	        if (type==0)
	        {
	        titletext.setText("Multi Delete - "+ttid);
	        del1.setText("Delete");
	        }
	        else
	        {
	        	titletext.setText("Multi Archieve - "+ttid);
	        	del1.setText("Archieve");
	        }
	        lv=(ListView)findViewById(R.id.list2);
	        
			mark1 = (Button) findViewById(R.id.mark1);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Integer b = 0;
					a = new SparseBooleanArray();
					a.clear();
					a = lv.getCheckedItemPositions();
					for (int i = 0; i < a.size(); i++) {
						if (a.valueAt(i) == true) {
							b = 1;

							break;
						} else {
							b = 0;

						}
					}

					if (b != 0) {
						del1.setEnabled(true);
					} else {

						del1.setEnabled(false);
					}

				}
			});

			del1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					my_sel_items = new String("");
					a = lv.getCheckedItemPositions();
					Toast.makeText(getApplicationContext(), "" + a.size() + "",
							Toast.LENGTH_SHORT).show();

					for (int i = 0; i < a.size(); i++) {
						if (a.valueAt(i) == true) {
							if (i != 0 && !my_sel_items.equals("")) {
								my_sel_items += ",";
							}
							Cursor cursor3 = (Cursor) lv.getAdapter().getItem(
									a.keyAt(i));
							if (!cursor3.getString(
									cursor3.getColumnIndex("_id")).equals("")) {
								my_sel_items += cursor3.getString(cursor3.getColumnIndex("_id"));
							}

							//cursor3.close();
						}
					}
					str=my_sel_items.split(",");
					Log.v("values", my_sel_items);
					if(type==1)
					{
									final Dialog d2 = new Dialog(c, R.style.Dialog);
									LayoutInflater lj = (LayoutInflater) c
											.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									View v2 = lj.inflate(R.layout.confirmbox, null, false);
									d2.setContentView(v2);
									d2.setCancelable(true);
									//final CheckBox cb1 = (CheckBox) d2.findViewById(R.id.checkBox1);
									TextView txt1=(TextView)d2.findViewById(R.id.confirmtext);
									TextView txt2=(TextView)d2.findViewById(R.id.title2);
									txt2.setText("Confirm Archieve");
									txt1.setText("All read notifications will be archieved!");
									Button bt1 = (Button) d2.findViewById(R.id.del1);
									bt1.setText("Archieve");
									Button bt2 = (Button) d2.findViewById(R.id.can1);
									//cb1.setVisibility(View.VISIBLE);
									bt1.setOnClickListener(new OnClickListener() {
				
										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											d2.cancel();
//											Cursor cursor1 = db
//													.rawQuery(
//															"select _id,subject,sent_time from notebox where _id in ("
//																	+ my_sel_items
//																	+ ")",
//															null);
											String mid;
											if (str.length != 0) {
													//if (cursor1.moveToFirst()) {
														//do {
												for(int i=0;i<str.length;i++){
															mid = str[i];
															db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("
																	+ mid
																	+ ",3,datetime('now','localtime'),1,0)");
												}
												//		} while (cursor1.moveToNext());
												//	}
													db.execSQL("delete from notebox where _id in("
															+ my_sel_items + ") and status=0");
													//cursor1.close();
													onBackPressed();
											}
										}
									});
									bt2.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {
											d2.cancel();
										}});d2.show();
				}
					else
					{
						final Dialog d2 = new Dialog(c, R.style.Dialog);
						LayoutInflater lj = (LayoutInflater) c
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View v2 = lj.inflate(R.layout.confirmbox, null, false);
						d2.setContentView(v2);
						d2.setCancelable(true);
						//final CheckBox cb1 = (CheckBox) d2.findViewById(R.id.checkBox1);
	
						Button bt1 = (Button) d2.findViewById(R.id.del1);
						Button bt2 = (Button) d2.findViewById(R.id.can1);
						//cb1.setVisibility(View.VISIBLE);
						bt1.setOnClickListener(new OnClickListener() {
	
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								d2.cancel();
//								Cursor cursor1 = db
//										.rawQuery(
//												"select _id,subject,sent_time from notebox where _id in ("
//														+ my_sel_items
//														+ ")",
//												null);
								String mid;
								if (str.length != 0) {
										//if (cursor1.moveToFirst()) {
											//do {
									for(int i=0;i<str.length;i++){
												mid = str[i];
												db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("
														+ mid
														+ ",5,datetime('now','localtime'),1,0)");
									}
									//		} while (cursor1.moveToNext());
									//	}
										db.execSQL("delete from notebox where _id in("
												+ my_sel_items + ") and status=0");
										//cursor1.close();
										onBackPressed();
								}
							}
						});
						bt2.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								d2.cancel();
							}});d2.show();
					}
				
				
				}});
			mark1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (mark1.getText().equals("Mark All")) {
						for (int i = 0; i < cursor.getCount(); i++) {
							lv.setItemChecked(i, true);
						}
						del1.setEnabled(true);
						mark1.setText("Unmark All");
					} else {
						for (int i = 0; i < cursor.getCount(); i++) {
							lv.setItemChecked(i, false);
						}
						del1.setEnabled(false);
						mark1.setText("Mark All");
					}}});
	        	db = (new DatabaseHelper(this)).getWritableDatabase();
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
	        adapter = new SimpleCursorAdapter(this, R.layout.multiungroup, cursor, new String[] {"sender","msg", "sent_time"},new int[] {R.id.sender1,R.id.textmsg, R.id.stime2});
	        lv.setAdapter(adapter);
	        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        //cursor.close();
	    }
	    public void onDestroy()
	    {db.close();cursor.close();super.onDestroy(); }
	  
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			cursor.close();
			db.close();
			Intent intent = new Intent(c, UnGroupView.class);
			intent.putExtra("subject",ttid);
			intent.putExtra("unreadcount","");
			intent.putExtra("mtype", mtype);
			startActivity(intent);
	
		}

}