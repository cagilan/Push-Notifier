package nsn.mobile.apps.nsnnotifier;

import nsn.mobile.apps.nsnnotifier.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.TextView;

public class CustomSelection extends Activity {

	protected SQLiteDatabase db;
	public Cursor cursor,cursor1;
	private int mtype;
	private int type;
	protected ListAdapter adapter;
	public Integer press = 0;
	public Integer lastpress = 0;
	protected ListView lv;
	public Context c;
	protected String s;
	private String my_sel_items;
	private String str[];
	protected Button del1;
	protected Button mark1;
	protected String prior_condi;
	//protected String s2;
	String[] longpress = { "Delete", "Edit", "Copy" };

	SparseBooleanArray a;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiinbox);
        mtype = getIntent().getIntExtra("mtype",0);
        type = getIntent().getIntExtra("type",0);
		c = this;
		TextView title1=(TextView) findViewById(R.id.textinbox);
		lv = (ListView) findViewById(R.id.inboxlist);
		del1 = (Button) findViewById(R.id.del1);
		if(type==1)
		{
			del1.setText("Archieve");
			title1.setText("Multi Archieve");
		}
		else
		{
			del1.setText("Delete");
			title1.setText("Multi Delete");
		}
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
		
				my_sel_items = new String("");
				a = lv.getCheckedItemPositions();
				//Toast.makeText(getApplicationContext(), "" + a.size() + "", Toast.LENGTH_SHORT).show();

				for (int i = 0; i < a.size(); i++) {
					if (a.valueAt(i) == true) {
						if (i != 0 && !my_sel_items.equals("")) {
							my_sel_items += ",";
						}
						Cursor cursor3 = (Cursor) lv.getAdapter().getItem(
								a.keyAt(i));
						if (!cursor3.getString(
								cursor3.getColumnIndex("subject")).equals("")) {
							my_sel_items += "'"
									+ cursor3.getString(cursor3
											.getColumnIndex("subject")) + "'";
						}

						//cursor3.close();
					}
				}
				str=my_sel_items.split(",");
				
				Log.v("values", ""+str.length);
				
				cursor1 = db.rawQuery("select _id,subject,sent_time from(select _id,subject,sent_time from notebox where subject in ("
						+ my_sel_items + ") and status=0 and mtype="+mtype+" order by sent_time desc) group by subject",null);
				
				if(type==1)
				{
							final Dialog d2 = new Dialog(c, R.style.Dialog);
							LayoutInflater lj = (LayoutInflater) c
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
							cb1.setVisibility(View.GONE);
							bt1.setOnClickListener(new OnClickListener() {
			
								@Override
								public void onClick(View arg0) {
									
									d2.cancel();
									
									String mid;
			
									if (str.length != 0) 
									{
											if (cursor1.moveToFirst()) {
												do{
												//for(int i=0;i<str.length;i++){
													mid = cursor1.getString(cursor1.getColumnIndex("_id"));
													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("
															+ mid
															+ ",3,datetime('now','localtime'),1,0)");
												
												}while(cursor1.moveToNext());
												
											}
											db.execSQL("delete from notebox where subject in("
													+ my_sel_items + ") and status=0 and mtype="+mtype);
			
											//cursor1.close();
											onBackPressed();
									}
			
									
			
								}
							});
							bt2.setOnClickListener(new OnClickListener() {
			
								@Override
								public void onClick(View arg0) {
												d2.cancel();
								}
							});
			
							d2.show();

				}
				//---------------------------------------------------------------
				else
				{
							final Dialog d2 = new Dialog(c, R.style.Dialog);
							LayoutInflater lj = (LayoutInflater) c
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View v2 = lj.inflate(R.layout.confirmbox, null, false);
							d2.setContentView(v2);
							d2.setCancelable(true);
							final CheckBox cb1 = (CheckBox) d2.findViewById(R.id.checkBox1);
							
							Button bt1 = (Button) d2.findViewById(R.id.del1);
							
							Button bt2 = (Button) d2.findViewById(R.id.can1);
							cb1.setVisibility(View.VISIBLE);
							bt1.setOnClickListener(new OnClickListener() {
		
								@Override
								public void onClick(View arg0) {
									
									d2.cancel();
									
									String mid;
		
									if (str.length != 0) {
		
										if (cb1.isChecked()) {
											cursor1 = db.rawQuery("select _id,subject,sent_time from(select _id,subject,sent_time from notebox where subject in ("
													+ my_sel_items + ") and mtype="+mtype+" order by sent_time desc) group by subject",null);
											
											if (cursor1.moveToFirst()) {
												do {
													//for(int i=0;i<str.length;i++){
													mid = cursor1.getString(cursor1.getColumnIndex("_id"));
													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("
															+ mid
															+ ",5,datetime('now','localtime'),1,1)");
												
													//}
													} while (cursor1.moveToNext());
		
											}
											db.execSQL("delete from notebox where subject in("
													+ my_sel_items + ") and mtype="+mtype);
		
											//cursor1.close();
											onBackPressed();
										} else {
											
											if (cursor1.moveToFirst()) {
												
											do{
												//for(int i=0;i<str.length;i++){
													mid = cursor1.getString(cursor1.getColumnIndex("_id"));
													db.execSQL("insert into tbox(msgid,status,read_time,delinfo,readinfo) values("
															+ mid
															+ ",5,datetime('now','localtime'),1,0)");
												//}
												} while (cursor1.moveToNext());
		
											}
											db.execSQL("delete from notebox where subject in("
													+ my_sel_items + ") and status=0 and mtype="+mtype);
		
										//	cursor1.close();
											onBackPressed();
										
		
									}
		
								}
							}});
							bt2.setOnClickListener(new OnClickListener() {
		
								@Override
								public void onClick(View arg0) {
												d2.cancel();
								}
							});
		
							d2.show();
							
				}
		}
			
			//iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
		}

		);

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
				}

			}

		});

	}

	public void onResume() {
		super.onResume();
		db = (new DatabaseHelper(this)).getWritableDatabase();

		//Toast.makeText(getApplicationContext(), "List View was clicked", Toast.LENGTH_SHORT).show();

		cursor = this.db
				.rawQuery(
						
						"select _id,subject,unread||', '||read as msg from (select a._id as _id,a.subject as subject," +
						"case when unread<>0 then 'Unread: '||unread" +
						" else 'Unread: '||aread " +
						"end as unread," +
						" case when read<>0 then 'Read:'||read " +
						"else 'Read: '||aread " +
						"end as read from" +
						"(select _id as _id,subject ,sent_time,0 as aread from notebox where mtype=? group by subject order by sent_time desc) a " +
						"left join" +
						"(select _id,subject, count(*) as unread from notebox where status=? and mtype=? group by subject) b on a.subject=b.subject" +
						" left join	(select _id,subject, count(*) as read from notebox where status=? and mtype=? group by subject) c on a.subject=c.subject)"
						,new String[] { ""+mtype,"1",""+mtype,"0",""+mtype });
		adapter = new SimpleCursorAdapter(this, R.layout.sss, cursor,new String[] { "subject", "msg" }, new int[] { R.id.sub,R.id.count });
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

	}

//	@Override
//	public void onBackPressed() {
//		
//		super.onBackPressed();
//		cursor.close();
//		db.close();
//
//	}

	public void onDestroy() {
	
		super.onDestroy();
		
		cursor.close();
		db.close();
	}

}