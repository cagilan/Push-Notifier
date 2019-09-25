package nsn.mobile.apps.nsnnotifier;

import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ImageView;
import android.widget.Toast;
import nsn.mobile.apps.adapter.ArchieveListAdapter;
import nsn.mobile.apps.nsnnotifier.communication.*;

public class Archieve extends Activity {
	static final String listdesc[][][][] = 
		{
	        { 
	          {
	            { "2012", "March","21" },
	            { "Loading..." }
	          },
	          {  
	        	  { "2012", "April","21" },
		            { "Loading..." }
	          },
	          { 
	        	  { "2012", "May","21" },
		            { "Loading..." }
	          },
	          { 
	        	  { "2012", "June","21" },
		            { "Loading..." }
	          },
	          { 
	        	  { "2012", "July","21" },
		            { "Loading..." }
	          },
	          { 
	        	  { "2012", "August","21" },
		            { "Loading..." }
	          }
	          ,
	          { 
	        	  { "2012", "September","21" },
		            { "Loading..." }
	          }
	          ,
	          { 
	        	  { "2012", "October","21" },
		            { "Loading..." }
	          }
	          ,
	          { 
	        	  { "2012", "November","21" },
		            { "Loading..." }
	          }
	          ,
	          { 
	        	  { "2012", "December","21" },
		            { "Loading..." }
	          }
	        },
	        
	        
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          },
		          { 
		        	  { "2011", "April","21" },
			            { "Loading..." }
		          },
		          { 
		        	  { "2011", "May","21" },
			            { "Loading..." }
		          },
		          { 
		            { "2011", "June","21" },
		            { "Loading..." }
		          },
		          { 
		        	  { "2011", "July","21" },
			            { "Loading..." }
		          },
		          { 
		        	  { "2011", "August","21" },
			            { "Loading..." }
		          }
	        },
	        {
		          { 
		            { "2010", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
	        {
		          { 
		            { "2011", "March","21" },
		            { "Loading..." }
		          }
		         
	        },
		};
	
	

	public String firstlist[][][][];
	public final String months[]={"January","Febraury","March","April","May","June","July","August","September","October","November","December"};
	private ArchieveListAdapter colorExpListAdapter;
	public ExpandableListView elv; 
	public Context c;
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.archievelist);
			c=this;
	        elv=(ExpandableListView)findViewById(R.id.archieve);
	      elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				// TODO Auto-generated method stub
				Toast.makeText(c,"child", Toast.LENGTH_LONG).show();
				return true;
			}
		});
	        
	        
	        
	        
//	        
	        TypedValue value = new TypedValue();
			boolean b = getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, value, true);
//			String s = TypedValue.coerceToString(value.type, value.data);
			android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			float ret = value.getDimension(metrics);
			int a=(int)ret;
			Toast.makeText(this,a+"", Toast.LENGTH_LONG).show();
//	        try {
//				String archievelist=NSNConnect.getContent(this, "getarchive");
//				
//				JSONObject record = new JSONObject(archievelist);
//				
//				JSONArray recordset = record.getJSONArray("data");
//				 String firstlist[][][][]=new String[recordset.length()][][][];
//				 for (int i = 0; i < recordset.length(); i++) {
//		            	record = new JSONObject(recordset.getString(i));
//		            	String month[]=record.getString("mon").split(",");
//		            	String count[]=record.getString("ncnt").split(",");
//		            	firstlist[i]=new String[month.length][][];
//		            	for (int j=0;j<month.length;j++)
//		            	{
//		            		firstlist[i][j]=new String[2][3];
//		            				Toast.makeText(this,count[j]+"", Toast.LENGTH_LONG).show();
//		            		//firstlist[i][j][0][0]=new String{record.getString("yer"),monthof(month[j]),count[j]};
//		            		firstlist[i][j][0][0]=record.getString("yer");
//		            		firstlist[i][j][0][1]=monthof(month[j]);
//		            		firstlist[i][j][0][2]=count[j];
//
//		            		firstlist[i][j][1][0]="Loading...";
//		            		
//		            		
//		            		
//		            	}
//		            	
//		            	
//		            	
//		            	
//		            	
//		            	
//		            	
//
//		            }
//				 colorExpListAdapter =new ArchieveListAdapter(this,elv,firstlist);
//					elv.setAdapter( colorExpListAdapter );
//				Toast.makeText(this,archievelist+"", Toast.LENGTH_LONG).show();
//				
//			} catch (NSNConnectException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        catch (Throwable t){
//				t.printStackTrace();
//			}
//	        
	        
	        
	        
	        colorExpListAdapter =new ArchieveListAdapter(this,elv,listdesc,a);
	        colorExpListAdapter.notifyDataSetChanged();
			elv.setAdapter( colorExpListAdapter );
	  }
	  
	  public String monthof(String month)
	  {
		  
		  Toast.makeText(this,months[(Integer.parseInt(month))+1]+"", Toast.LENGTH_LONG).show();
		return months[(Integer.parseInt(month))+1];
		
	  }
	  
	  
}
