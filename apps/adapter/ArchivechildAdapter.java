package nsn.mobile.apps.adapter;


import nsn.mobile.apps.nsnnotifier.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ArchivechildAdapter extends BaseExpandableListAdapter {
	private Context context;
    private String childdata[][];
    private String groupdata;
    private LayoutInflater inflater;
    


    public ArchivechildAdapter(Context context,String groupdata,String childdata[][]) { 
        this.context = context;
        
        this.groupdata=groupdata;
        this.childdata = childdata;
        inflater = LayoutInflater.from( context );
     //   Toast.makeText(context,((childdata.length)-1)+"ggg", Toast.LENGTH_SHORT).show();
    }
    
	@Override
	 public Object getChild(int groupPosition, int childPosition) {
        return childdata[groupPosition][childPosition];
    }

	@Override
	public long getChildId(int groupPosition, int childPosition) {
        return (long)( groupPosition*1024+childPosition );  // Max 1024 children per group
    }

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    
		View v;// = null;
		
     	v = inflater.inflate(R.layout.test, null);
 //   String gt = (String)getChild( groupPosition,childPosition );
	TextView month = (TextView)v.findViewById(  R.id.loading );
	if( groupdata != null )
		month.setText( groupdata );
	//Toast.makeText(context,childPosition+"dsfsdf"+groupPosition, Toast.LENGTH_SHORT).show();
	return v;
     
    }
	@Override
	 public int getChildrenCount(int groupPosition) {
        return ((childdata.length)-1);
    }


	@Override
	public Object getGroup(int groupPosition) {
        return groupdata;
    }

	@Override
	public int getGroupCount() {
        return 1;
    }

	@Override
	public long getGroupId(int groupPosition) {
        return (long)( groupPosition*1024 );  // To be consistent with getChildId
    }

	@Override
	 public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View v;
        v = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
     		TextView month = (TextView)v.findViewById(  android.R.id.text1 );
		if( groupdata != null )
			month.setText( groupdata );
		return v;
    }

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	 public void onGroupCollapsed (int groupPosition) {} 
	    public void onGroupExpanded(int groupPosition) {}
	    
	    
	    
	    
	    
	    
//	    class Level2GroupExpandListener implements ExpandableListView.OnGroupClickListener {
//			public Level2GroupExpandListener( int level1GroupPosition ) {
//				this.level1GroupPosition = level1GroupPosition;
//			}
//
//	       	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//	       		if( parent.isGroupExpanded( groupPosition ) )
//	            	parent.collapseGroup( groupPosition );
//	        	else
//	           		parent.expandGroup( groupPosition );
//				if( parent instanceof DebugExpandableListView ) {
//					DebugExpandableListView dev = (DebugExpandableListView)parent;
//					dev.setRows( calculateRowCount( level1GroupPosition, parent ),height );
//				}
//	           	Log.d( LOG_TAG, "onGroupClick" );
//	           	topExpList.requestLayout();
//	          	return true;
//	     	}
//
//			private int level1GroupPosition;
//		}


}
