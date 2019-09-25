package nsn.mobile.apps.nsnnotifier;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class welcome extends Activity  {
	/** Called when the activity is first created. */
	public TextView fade,fade1;
	public LinearLayout lv,lay2,lay3;
	public Animation controller,name,weltext,line2;
	LayoutAnimationController feature;
	public Context c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcomepage);
		c=this;
		 lv=(LinearLayout)findViewById(R.id.header);
        controller  = AnimationUtils.loadAnimation( this, R.anim.catlist);
        controller.setDuration(2000);
        
        fade = (TextView) findViewById(R.id.textnew1);
		name = AnimationUtils.loadAnimation(this, R.anim.catlist);
		fade1 = (TextView) findViewById(R.id.textnew2);
		weltext=AnimationUtils.loadAnimation(this,R.anim.catlist);
		lay2 = (LinearLayout) findViewById(R.id.linearLayout3);
		line2=AnimationUtils.loadAnimation(this,R.anim.catlist);
		lay3=(LinearLayout)findViewById(R.id.linearLayout5);
		feature=AnimationUtils.loadLayoutAnimation(this,R.anim.catanimation);

        controller.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {

			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				//fade.setAnimation(name);
				
				fade.startAnimation(name);
			}
		});
        
        lv.setAnimation(controller);
        lv.startAnimation(controller);

		name.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				fade.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				//fade1.setAnimation(weltext);
				fade1.startAnimation(weltext);
			}
		});
		
		
		

		weltext.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
			fade1.setVisibility(View.VISIBLE);	
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				lay2.startAnimation(line2);
				
			}
		});
		
		

		line2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				lay2.setVisibility(View.VISIBLE);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				lay3.setLayoutAnimation(feature);
				lay3.setVisibility(View.VISIBLE);
				lay3.startLayoutAnimation();
				
			}
		});
		lay3.setLayoutAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				//lay3.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(c,"completed ", Toast.LENGTH_LONG)	.show();
			
			}
		});
//		Animation a = AnimationUtils.loadAnimation(this, R.anim.translatealpha);
//	    a.reset();
//	    //fade.clearAnimation();
//	    fade.startAnimation(a);
//	    
//	    Animation b = AnimationUtils.loadAnimation(this, R.anim.fouranimation);
//	    b.reset();
//	   // fade1.clearAnimation();
//	    fade1.startAnimation(b);
//	    
//	    Animation c = AnimationUtils.loadAnimation(this, R.anim.fouranimation);
//	    c.reset();
//	    fade2.clearAnimation();
//	    fade2.startAnimation(b);
//	        
//	    Paint paint = new Paint();
//		TextView animatedView1 = (TextView) findViewById(R.id.text1);
//		TextView animatedView2 = (TextView) findViewById(R.id.text2);
//		TextView animatedView3 = (TextView) findViewById(R.id.text3);
//		
//		
//		float measureTextCenter = paint.measureText(animatedView1.getText()
//				.toString());
//		
//		Animation animation1 = new TranslateAnimation(-measureTextCenter, 100, 0.0f,0.0f );
//		animation1.setDuration(4000);
//		animation1.setFillAfter(true);   
//		animation1.setInterpolator(new AnticipateInterpolator());
//		animatedView1.startAnimation(animation1);
//		
//		
//		Animation animation2 = new TranslateAnimation(-measureTextCenter, 100, 0.0f,0.0f );
//		animation2.setDuration(5000);
//		animation2.setFillAfter(true);   
//		animation2.setInterpolator(new AnticipateInterpolator());
//		animatedView2.startAnimation(animation2);
//		
//		Animation animation3 = new TranslateAnimation(-measureTextCenter, 100, 0.0f,0.0f );
//		animation3.setDuration(6000);
//		animation3.setFillAfter(true);   
//		animation3.setInterpolator(new AnticipateInterpolator());
//		animatedView3.startAnimation(animation3);
//	    
//		

	}


//	@Override
//	public void onAnimationStart(Animation animation) {
//
//	}
//
//	@Override
//	public void onAnimationEnd(Animation animation) {
//		
//		
//		
//		fadein = (TextView) findViewById(R.id.textnew);
//		   // fadein.setText("We hope to enjoy NSN Mobile Application Features such as");
//		    float from = 1.0f;
//			float to = 0.0f;
//			
//			if (fadein.getVisibility() == View.INVISIBLE) {
//				from = to;
//				to = 1.0f;
//			}
//		
//			Animation animationnew = new AlphaAnimation(from, to);
//			animationnew.setDuration(100);
//			animationnew.setAnimationListener(this);
//			fadein.startAnimation(animationnew);
//			
//
//		if (fadein.getVisibility() == View.VISIBLE) {
//			fadein.setVisibility(View.VISIBLE);
//			fadein.setTextColor(Color.BLACK); 
//			fadein.setTypeface(Typeface.SERIF);
//			
//			
//			
//		} else {
//			fadein.setVisibility(View.VISIBLE);
//			fadein.setTextColor(0xFF000000); 
//		}
//		
//		
//	}
//
//	@Override
//	public void onAnimationRepeat(Animation animation) {
//
//
//	}
}


