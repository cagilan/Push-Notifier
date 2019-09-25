package nsn.mobile.apps.nsnnotifier;



import nsn.mobile.apps.adapter.CategoryAdapter;
import nsn.mobile.apps.nsnnotifier.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainCategory extends Activity {
	String[] category = { "BSS", "NSS", "OSS","TRN","OTHERS" };
	protected SQLiteDatabase db;
	public Cursor cursor;
	protected ListAdapter adapter;
	public Integer press = 0;
	
	
	public Context c;
	protected String s;
	protected String s2;
	protected ListView lv;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        c=this;
	        db = (new DatabaseHelper(this)).getWritableDatabase();
	        setContentView(R.layout.main);
	        lv = (ListView) findViewById(R.id.inboxlist);
//	        LayoutAnimationController controller  = AnimationUtils.loadLayoutAnimation(
//	          this, R.anim.catanimation);
//	        lv.setLayoutAnimation(controller);
	        lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					//lastpress = arg2;
					Intent intent = new Intent(c, GroupView.class);
					Cursor cursor2 = (Cursor) adapter.getItem(arg2);
					intent.putExtra("mtype", cursor2.getInt(cursor2.getColumnIndex("_id")));
					intent.putExtra("title", cursor2.getString(cursor2.getColumnIndex("mtype")));
					intent.putExtra("unreadcount", cursor2.getString(cursor2.getColumnIndex("gcount")));
//					db.close();
//					cursor2.close();
					startActivity(intent);
				}
			});
	 }
	 
	 @Override
	 public void onResume() {
			super.onResume();
			
			cursor = this.db.rawQuery("select catname as mtype,_id,gcount from category c left join (select mtype,sum(status) as gcount from notebox where status=1 group by mtype) r on r.mtype=c._id",null);
			adapter = new CategoryAdapter(this, R.layout.category, cursor, new String[] { "mtype", "gcount"}, new int[] { R.id.cattext, R.id.unread});
			lv.setAdapter(adapter);
			
		}
	    
	 
	 @Override
		protected void onDestroy() {
			super.onDestroy();
			
			
				cursor.close();
				db.close();
				
			
		}
		
	 
	 
}
