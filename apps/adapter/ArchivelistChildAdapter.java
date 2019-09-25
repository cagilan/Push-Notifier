package nsn.mobile.apps.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import nsn.mobile.apps.nsnnotifier.R;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ArchivelistChildAdapter extends BaseExpandableListAdapter {
	private Context context;
    private String childdata[][];
    private String groupdata[];
    private LayoutInflater inflater;
    


    public ArchivelistChildAdapter(Context context,String groupdata[],String childdata[][]) { 
        this.context = context;
        
        this.groupdata=groupdata;
        this.childdata = childdata;
        inflater = LayoutInflater.from( context );
    }
    
//    private int calculateRowCount( int level1, ExpandableListView level2view ) {
//        int level2GroupCount = listdesc[level1].length;
//        int rowCtr = 0;
//        for( int i = 0 ; i < level2GroupCount ; ++i ) {
//            ++rowCtr;       // for the group row
//			if( ( level2view != null ) && ( level2view.isGroupExpanded( i ) ) )
//				rowCtr += listdesc[level1][i].length - 1;	// then add the children too (minus the group descriptor)
//        }
//		return rowCtr;
//    }
    
//    private List<ArrayList<HashMap<String, String>>> createChildList( int level1 ) {
//	    ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
//	    for( int i = 0 ; i < listdesc[level1].length ; ++i ) {
//// Second-level lists
//	        ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
//	        for( int n = 1 ; n < listdesc[level1][i].length ; ++n ) {
//	            HashMap<String, String> child = new HashMap<String, String>();
//		        child.put( KEY_SHADENAME, listdesc[level1][i][n][0] );
//	          //  child.put( KEY_RGB, listdesc[level1][i][n][1] );
//		        secList.add( child );
//	        }
//	        result.add( secList );
//	    }
//	    return result;
//    }
//
//    private List<HashMap<String, String>> createGroupList( int level1 ) {
//        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
//	    for( int i = 0 ; i < listdesc[level1].length ; ++i ) {
//	        HashMap<String, String> m = new HashMap<String, String>();
//	        m.put( KEY_COLORNAME,listdesc[level1][i][0][1] );
//	       
//	    	result.add( m );
//	    }
//	    return result;
//    }
//    
//    
//    private String[] creategroup(int level1)
//    {
//    	String level2group[]=new String[listdesc[level1].length];
//    	 for( int i = 0 ; i < listdesc[level1].length ; ++i ) {
// 	       level2group[i]=listdesc[level1][i][0][1];
// 	    }
//    	
//    	return level2group;
//    }
//    
//    private String[][] createchild(int level1)
//    {
//    	String level2child[][]=new String[listdesc[level1].length][];
//    	 for( int i = 0 ; i < listdesc[level1].length ; ++i ) {
//    		level2child[i]=new String[1];
//    		level2child[i][0]=listdesc[level1][i][1][1];
//    		
//    		
//   	    }
//      	
//    	
//    	return level2child;
//    }
//    
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
    String gt = (String)getChild( groupPosition,childPosition );
	TextView month = (TextView)v.findViewById(  R.id.loading );
	if( gt != null )
		month.setText( gt );
	return v;
     
    }
	@Override
	 public int getChildrenCount(int groupPosition) {
        return 1;
    }


	@Override
	public Object getGroup(int groupPosition) {
        return groupdata[groupPosition];        
    }

	@Override
	public int getGroupCount() {
        return groupdata.length;
    }

	@Override
	public long getGroupId(int groupPosition) {
        return (long)( groupPosition*1024 );  // To be consistent with getChildId
    }

	@Override
	 public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View v;
         	v = inflater.inflate(R.layout.archieveyear,null);
        String gt = (String)getGroup( groupPosition );
		TextView month = (TextView)v.findViewById(  R.id.group );
		if( gt != null )
			month.setText( gt );
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
