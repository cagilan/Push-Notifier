package nsn.mobile.apps.adapter;

import android.content.Context;
import android.widget.ExpandableListView;

public class DebugExpandableListView extends ExpandableListView {
    public DebugExpandableListView( Context context ) {
        super( context );
    }

	public void setRows( int rows,int height ) {
		this.rows = rows;
		this.ROW_HEIGHT=height;
		
	}

    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
    	setMeasuredDimension( getMeasuredWidth(), rows*ROW_HEIGHT );
      
    } 

    protected void onLayout (boolean changed, int left, int top, int right, int bottom) {
        super.onLayout( changed, left,top,right,bottom );
      
    }
 
    private int ROW_HEIGHT;
    private int rows;
}

