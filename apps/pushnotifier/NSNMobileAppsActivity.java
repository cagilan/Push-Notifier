package nsn.mobile.apps.nsnnotifier;

import org.json.JSONException;
import org.json.JSONObject;

import nsn.mobile.apps.nsnnotifier.R;
import nsn.mobile.apps.nsnnotifier.communication.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NSNMobileAppsActivity extends Activity {
	SQLiteDatabase db;
	String TAG;
	CheckBox chkBoxCycling;
	final Context context = this;

	@Override
	public void onBackPressed() {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		chkBoxCycling = (CheckBox) findViewById(R.id.CheckBoxResponse);

		TextView t3 = (TextView) findViewById(R.id.textView2);

		t3.setText(Html
				.fromHtml("<b>I accept the </b>"
						+ "<a href=\"http://www.nokiasiemensnetworks.com/about-us/terms-use\">  Terms of use </a> <b> | </b>"
						+ "<a href=\"http://www.nokiasiemensnetworks.com/about-us/privacy-policy\">  Privacy Policy</a> <b>of Nokia Siemens Networks.</b>"));
		t3.setMovementMethod(LinkMovementMethod.getInstance());

		final Context con = this;
		final SharedPreferences prex1 = this.getSharedPreferences("MyPrefs",
				MODE_PRIVATE);

		Button next = (Button) findViewById(R.id.button1);
		next.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("null")
			public void onClick(View view) {

				final EditText empid = (EditText) findViewById(R.id.empid);
				final EditText mobile = (EditText) findViewById(R.id.mobile);
				final EditText email = (EditText) findViewById(R.id.email);

				final String empValue = empid.getText().toString();
				final String mobileValue = mobile.getText().toString();
				final String emailValue = email.getText().toString();

				final String sSimSerial = getMyPhoneNumber();
				final String Psimserial = "Psimserial";
				final String PemailValue = "PemailValue";
				final String PmobileValue = "PmobileValue";
				final String PempValue = "PempValue";
				final String Pacenable = "Pacenable";

				if (empValue.isEmpty() || mobileValue.isEmpty() 
						|| emailValue.isEmpty() || empValue.length() < 8
						|| mobileValue.length() < 10
						|| !emailValue.contains("@")
						|| emailValue.startsWith("@") || emailValue.startsWith(" @")) {
					if (empValue.isEmpty()) {
						Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Enter a Valid NSN Employee Id ", 2000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
					} else if (mobileValue.isEmpty()) {
						Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Enter a Valid Mobile Number ", 2000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
				    } else if (emailValue.isEmpty()) {
				    	Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Enter a Valid Email Id ", 2000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
				   } else if (empValue.length() < 8) {
					    Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Enter a Valid NSN Employee Id ", 2000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
				   } else if (mobileValue.length() < 10) {
					   Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Enter a Valid Mobile Number ", 2000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
				   } else if (!emailValue.contains("@")
							|| !emailValue.contains(".com") || emailValue.startsWith(" @") || emailValue.contains("@.com") || emailValue.contains("@ .com")
							|| emailValue.startsWith("@")) {
					    Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Enter a Valid Email Id ", 2000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
				   }

				}

				else if (chkBoxCycling.isChecked()) {

					final Dialog progressDialog = null;
					
					try {
						String result1 = NSNConnect.getContent(context, "insertuser/"
								+ empValue + "/" + mobileValue + "/"
								+ sSimSerial + "/" + emailValue + "");
						JSONObject myAwway = new JSONObject(result1);
						result1=myAwway.getString("rsts");
						
											
						if (result1.equals("true")) {
							ProgressDialog.show(NSNMobileAppsActivity.this,
									"Please Wait", "Activation in progress...");
							
							SharedPreferences.Editor prefEditor = prex1.edit();
							prefEditor.putString(Psimserial,
									sSimSerial.toString());
							prefEditor.putString(PemailValue,
									emailValue.toString());
							prefEditor.putString(PmobileValue,
									mobileValue.toString());
							prefEditor.putString(PempValue, empValue.toString());
							prefEditor.putString(Pacenable, "0");
							prefEditor.commit();
							
							Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Your Activation Request has been Accepted ", 3000);
							toast.setGravity(Gravity.TOP, 10, 120);
							toast.show();


							int i = Login();

							if (i == 0) {
								startActivity(new Intent("mobile.apps.homepage"));
								finish();
							} else {
								Intent intent = new Intent(con,
										newactivation.class);
								startActivity(intent);
								finish();
							}

						} else if (result1.equals("false")) {
							Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "Your Activation has been already registered. Please Contact Support Team. ", 3000);
							toast.setGravity(Gravity.TOP, 10, 120);
							toast.show();
							empid.setText("");
							mobile.setText("");
							email.setText("");
						}		
						else if (result1.equals("ERROR")) {
														
							runOnUiThread(new Runnable() {
								public void run() {
									Dialog myDialog;
									myDialog = new Dialog(
											NSNMobileAppsActivity.this);
									myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									myDialog.setContentView(R.layout.serverdialog1);
									myDialog.setCancelable(true);

									Button button1 = (Button) myDialog
											.findViewById(R.id.button1);
									button1.setOnClickListener(new View.OnClickListener() {
										public void onClick(
												View v) {
											finish();
										}
									});

						
									myDialog.show();
								}
							});
						}

						//progressDialog.dismiss();

					} catch (NSNConnectException t) {
						runOnUiThread(new Runnable() {
							public void run() {
								Dialog myDialog;
								myDialog = new Dialog(
										NSNMobileAppsActivity.this);
								myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								myDialog.setContentView(R.layout.serverdialog1);
								myDialog.setCancelable(true);

								Button button1 = (Button) myDialog
										.findViewById(R.id.button1);
								button1.setOnClickListener(new View.OnClickListener() {
									public void onClick(
											View v) {
										finish();
									}
								});
							myDialog.show();
							}
						});
						
					} catch(JSONException t){
						Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "You need to accept the NSN Terms of Use and Privacy Policy Conditions...", 3000);
						toast.setGravity(Gravity.TOP, 10, 120);
						toast.show();
					}
					
				} else {
					Toast toast=Toast.makeText(NSNMobileAppsActivity.this, "You need to accept the NSN Terms of Use and Privacy Policy Conditions...", 3000);
					toast.setGravity(Gravity.TOP, 10, 120);
					toast.show();
			    }
			}

			private String getMyPhoneNumber() {
				TelephonyManager mTelephonyMgr;
				mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				// return mTelephonyMgr.getLine1Number();
				return mTelephonyMgr.getSimSerialNumber();
			}

			public int Login() {
				String Psim = prex1.getString("Psimserial", null);
				String Pac = prex1.getString("Pacenable", null);
				int pac1 = Integer.parseInt(Pac);
				String sSimSerial = getMyPhoneNumber();

				if (sSimSerial.equals(Psim) && pac1 == 0) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}
}
