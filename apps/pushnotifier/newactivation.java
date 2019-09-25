package nsn.mobile.apps.nsnnotifier;

import nsn.mobile.apps.nsnnotifier.R;
import nsn.mobile.apps.nsnnotifier.communication.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import org.json.JSONObject;

public class newactivation extends Activity {

	final Context context = this;
	private ProgressDialog progressBar;
	
	@Override
	public void onBackPressed() {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.onetime);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Intent intent = new Intent(this, newactivation.class);
		final SharedPreferences prex1 = this.getSharedPreferences("MyPrefs",
				MODE_PRIVATE);
		final String TAG = null;

		
		String emailacc = prex1.getString("PemailValue", null);
		TextView t3 = (TextView) findViewById(R.id.text2);

		t3.setText(Html
				.fromHtml("Your request for Activation has been accepted. You will recieve a 8-digit Activation Key to (<b>"
						+ emailacc + "</b>) in a couple of days based on request and accept policy."));

		Button next = (Button) findViewById(R.id.button1);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
								
				final EditText actid = (EditText) findViewById(R.id.actid);
				if(actid.length() < 8)
				{
					Toast.makeText(newactivation.this,
							"Please Enter your 8-Digit Activation Key", Toast.LENGTH_LONG)
							.show();
				}
				else
				{
				String emp_id = prex1.getString("PempValue", null);
				String sSimSerial = getMyPhoneNumber();
				String key = null;
				String otp = actid.getText().toString();

				try {
					String result1 = NSNConnect.getContent(context,"validatekey/"+ sSimSerial + "/" + otp + "");

					JSONObject myAwway = new JSONObject(result1);
					result1=myAwway.getString("rsts");
					
				if ((result1.equals("true"))) {
					try {
						
						ProgressDialog.show(newactivation.this,
								"Please wait for few secs...", "Processing is going on...");
						String result2 = NSNConnect.getContent(context, "updateuser/"
								+ emp_id);
						
						JSONObject myAwway1 = new JSONObject(result2);
						result2=myAwway1.getString("rsts1");
						
						 					
						if ((result2.equals("true"))) {
							
							String Psim = prex1.getString("Psimserial", null);
							String sSimSerial1 = getMyPhoneNumber();

							SharedPreferences.Editor prefEditor = prex1.edit();
							if (sSimSerial1.equals(Psim)) {
								
								prefEditor.putString("Pacenable", "1");
							} else {
								
								prefEditor.putString("Pacenable", "0");
							}

							prefEditor.commit();
							
													
							Toast.makeText(newactivation.this,
									"Your Activation Key is Accepted", Toast.LENGTH_LONG)
									.show();
						
							int i = Login();
							
							if (i == 0) {
								Intent intent = new Intent(context, newactivation.class);
								startActivity(intent);
								finish();
							} else {
								Intent intent = new Intent(context, GroupView.class);
								startActivity(intent);
								finish();
						}
						}
						else
						{
							Toast.makeText(newactivation.this,
									"Cannot Connect to the Server",
									Toast.LENGTH_LONG).show();
						}
			} catch (Throwable t) {
				Toast.makeText(newactivation.this,
						"Cannot Connect to the Server",
						Toast.LENGTH_LONG).show();
					}
				}
				else {
					Toast toast=Toast.makeText(newactivation.this, "Invalid Activation Key", 3000);
					toast.setGravity(Gravity.TOP, 10, 120);
					toast.show();
				}
				} catch (Throwable t) {
					Toast.makeText(newactivation.this,
							"Cannot Connect to the Server",
							Toast.LENGTH_LONG).show();
				}
			}
				
			}

			private String getMyPhoneNumber() {
				TelephonyManager mTelephonyMgr;
				mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				return mTelephonyMgr.getSimSerialNumber();
			}

			public int Login() {
				String Psim = prex1.getString("Psimserial", null);
				String Pac = prex1.getString("Pacenable", null);
				String sSimSerial = getMyPhoneNumber();
				int pac1 = Integer.parseInt(Pac);

				if (sSimSerial.equals(Psim) && pac1 == 1) {
					return 1;
				} else {
					return 0;
				}
			}
		});

	}

}
