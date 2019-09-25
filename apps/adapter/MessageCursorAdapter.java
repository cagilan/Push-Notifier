package nsn.mobile.apps.adapter;



import nsn.mobile.apps.nsnnotifier.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MessageCursorAdapter extends SimpleCursorAdapter {

    private int mLayout;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater; 
    public int ccount;
    
    
    private final class ViewHolder {
        public TextView sender;
        public TextView mindex;
        public TextView textmsg;
        public TextView stime;
    }
   
    public MessageCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);

        this.mLayout = layout;
        this.mCursor = c;
        this.mLayoutInflater = LayoutInflater.from(context);   
        this.ccount=c.getCount();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor.moveToPosition(position)) {

        		 ViewHolder viewHolder;
                 if (convertView == null) {
                      convertView = mLayoutInflater.inflate(mLayout, null);
                      viewHolder = new ViewHolder();
                    
                      viewHolder.sender = (TextView) convertView.findViewById(R.id.sender1);
                      viewHolder.mindex=(TextView) convertView.findViewById(R.id.mindex);
                    viewHolder.stime=(TextView) convertView.findViewById(R.id.stime2);
                    viewHolder.textmsg=(TextView) convertView.findViewById(R.id.textmsg);
                      convertView.setTag(viewHolder);
                  }
                  else {
                      viewHolder = (ViewHolder) convertView.getTag();
                  }

                  String name = mCursor.getString(mCursor.getColumnIndex("sender"));
                String mess = mCursor.getString(mCursor.getColumnIndex("msg"));
                String time = mCursor.getString(mCursor.getColumnIndex("sent_time"));
                String count =""+(ccount-position)+" of "+ccount;
                
                
                  viewHolder.sender.setText(name);
                 viewHolder.mindex.setText(count);
                  viewHolder.textmsg.setText(mess);
                  viewHolder.stime.setText(time);
        		
        }
        return convertView;
    }

}

