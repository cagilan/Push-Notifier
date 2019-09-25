package nsn.mobile.apps.adapter;



import nsn.mobile.apps.nsnnotifier.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class InboxCursorAdapter extends SimpleCursorAdapter {

    private int mLayout;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater; 
    private final class ViewHolder {
        public TextView subject;
        public TextView gcount;
        public TextView msg;
        public TextView stime;
    }
    private final class ViewHolder2 {
        public TextView subject;
   
        public TextView msg;
        public TextView stime;
    }
    public InboxCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);

        this.mLayout = layout;
        this.mCursor = c;
        this.mLayoutInflater = LayoutInflater.from(context);   
        
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor.moveToPosition(position)) {
        	if(!mCursor.getString(mCursor.getColumnIndex("gcount")).equals(""))
        	{
        		 ViewHolder viewHolder;
               //  if (convertView == null) {
                      convertView = mLayoutInflater.inflate(mLayout, null);
                      viewHolder = new ViewHolder();
                    
                      viewHolder.subject = (TextView) convertView.findViewById(R.id.subject);
                      viewHolder.gcount=(TextView) convertView.findViewById(R.id.gcount);
                    viewHolder.stime=(TextView) convertView.findViewById(R.id.stime);
                    viewHolder.msg=(TextView) convertView.findViewById(R.id.msg);
                      convertView.setTag(viewHolder);
//                  }
//                  else {
//                      viewHolder = (ViewHolder) convertView.getTag();
//                  }

                  String name = mCursor.getString(mCursor.getColumnIndex("subject"));
                String mess = mCursor.getString(mCursor.getColumnIndex("msg"));
                String time = mCursor.getString(mCursor.getColumnIndex("sent_time"));
                String count = (mCursor.getString(mCursor.getColumnIndex("gcount"))).trim();
                  viewHolder.subject.setText(name);
                 viewHolder.gcount.setText(count);
                  viewHolder.msg.setText(mess);
                  viewHolder.stime.setText(time);
        		
        	}
        	else
        	{
        		 ViewHolder2 viewHolder;
               //  if (convertView == null) {
                      convertView = mLayoutInflater.inflate(R.layout.list_item2, null);
                      viewHolder = new ViewHolder2();
                    
                      viewHolder.subject = (TextView) convertView.findViewById(R.id.subject);
                      //viewHolder.gcount=(TextView) convertView.findViewById(R.id.gcount);
                    viewHolder.stime=(TextView) convertView.findViewById(R.id.stime);
                    viewHolder.msg=(TextView) convertView.findViewById(R.id.msg);
                      convertView.setTag(viewHolder);
               //   }
//                  else {
//                      viewHolder = (ViewHolder2) convertView.getTag();
//                  }

                  String name = mCursor.getString(mCursor.getColumnIndex("subject"));
                String mess = mCursor.getString(mCursor.getColumnIndex("msg"));
                String time = mCursor.getString(mCursor.getColumnIndex("sent_time"));
                //String count = (mCursor.getString(mCursor.getColumnIndex("gcount"))).trim();
                
                viewHolder.subject.setText(name);
                
                  viewHolder.msg.setText(mess);
                  viewHolder.stime.setText(time);
        		
        		
        	}
        	
        	
           
        }
        return convertView;
    }

}

