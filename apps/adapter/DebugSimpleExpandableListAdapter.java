package nsn.mobile.apps.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DebugSimpleExpandableListAdapter extends SimpleExpandableListAdapter {
//public ArrayList groupdata=new ArrayList();
public List<? extends List<? extends Map<String, ?>>> childdata,groupdata;
public Context c;

    public DebugSimpleExpandableListAdapter(Context context,
            List<? extends Map<String, ?>> groupData, int groupLayout,
            String[] groupFrom, int[] groupTo,
            List<? extends List<? extends Map<String, ?>>> childData,
            int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData,
                childLayout, childFrom, childTo);
        
        this.groupdata=(ArrayList) groupData;
        this.childdata=childData;    
        this.c=context;
        
        
        
        
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = super.getChildView( groupPosition, childPosition, isLastChild, convertView, parent );
        Log.d( LOG_TAG, "getChildView: groupPosition: "+groupPosition+"; childPosition: "+childPosition+"; v: "+v );
        return v;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = super.getGroupView( groupPosition, isExpanded, convertView, parent );
        Log.d( LOG_TAG, "getGroupView: groupPosition: "+groupPosition+"; isExpanded: "+isExpanded+"; v: "+v );
        return v;
//    	 View v;// = convertView;
//         //if (v == null) {
// //sender is activity from where you call this adapter. Set it with constructor.
//             LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
//             v = vi.inflate( android.R.layout.simple_expandable_list_item_1, null);
//        // }
////children = arraylists of Child 
////             Map<String,?> hm= (Map<String, ?>) groupdata.get(groupPosition);
////             hm.
////             	String a=(String) hm.
////             	Toast.makeText(c,a+"", Toast.LENGTH_LONG).show();
////         if (c != null) {
////                 TextView tt = (TextView) v.findViewById(android.R.id.text1);
////                 
////                 if (tt != null) {
////                       tt.setText(c.);                            }
////                 if(bt != null){
////                       bt.setText(c.text2);
////                 }
////                 if (icon != null) 
////                 {
////                     icon.setImageResource(R.drawable.rowicon);
////                 }
////         }
//         return v;

    	
    	
    	
    }

    private static final String LOG_TAG = "DebugSimpleExpandableListAdapter";
}

