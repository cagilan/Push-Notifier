package nsn.mobile.apps.adapter;



import nsn.mobile.apps.nsnnotifier.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CategoryAdapter extends SimpleCursorAdapter {

    private int mLayout;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater; 
   
    private final class ViewHolder {
        public TextView subject;
        public TextView gcount;
        
    }
    private final class ViewHolder2 {
        public TextView subject;
   
        
    }
    public CategoryAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.mLayout = layout;
        this.mCursor = c;
        this.mLayoutInflater = LayoutInflater.from(context);   
        
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor.moveToPosition(position)) {
        	
        	if( !(""+mCursor.getString(mCursor.getColumnIndex("gcount"))).equals("null"))
        	{
        		 ViewHolder viewHolder;
                      convertView = mLayoutInflater.inflate(mLayout, null);
                      viewHolder = new ViewHolder();
                      viewHolder.subject = (TextView) convertView.findViewById(R.id.cattext);
                      viewHolder.gcount=(TextView) convertView.findViewById(R.id.unread);
                 
                  String name = mCursor.getString(mCursor.getColumnIndex("mtype"));
                String count = (mCursor.getString(mCursor.getColumnIndex("gcount"))).trim();
                  viewHolder.subject.setText(name);
                 viewHolder.gcount.setText(count);
    		
        	}
        	else
        	{
        		 ViewHolder2 viewHolder;
                      convertView = mLayoutInflater.inflate(R.layout.category2, null);
                      viewHolder = new ViewHolder2();
                      viewHolder.subject = (TextView) convertView.findViewById(R.id.cattext);
                  String name = mCursor.getString(mCursor.getColumnIndex("mtype"));
               viewHolder.subject.setText(name);
        	}
        	
        	
           
        }
        return convertView;
    }

}

