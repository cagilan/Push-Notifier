package nsn.mobile.apps.nsnnotifier;

import nsn.mobile.apps.nsnnotifier.R;
import nsn.mobile.apps.nsnnotifier.communication.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import org.json.JSONArray;
import org.json.JSONObject;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

/** Called when the activity is first created. */
public class splash extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 1000;
	Object result;
	String TAG;
	final Context context = this;
	Cursor c = null;
	final String Psimserial = "Psimserial";
	final String PemailValue = "PemailValue";
	final String PmobileValue = "PmobileValue";
	final String PempValue = "PempValue";
	final String Pacenable = "Pacenable";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		final SharedPreferences prex1 = this.getSharedPreferences("MyPrefs",MODE_PRIVATE);
		Thread splashTread = new Thread() {

			@Override
			public void run() {

				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					
					/** check for sim id availability. */
					int i = FirstLogin();
					if (i == 0) {
						try {
							
							/** check for sim id availability in server. */
							String sSimSerial = getMyPhoneNumber();
							System.out.println("chandran"+sSimSerial);
							String emp_id, mobile_no, email_id, sim_no, ac_enable;
							emp_id = mobile_no = email_id = sim_no = ac_enable = null;
							
							/** check for network availability. */
							
							boolean b = isNetworkAvailable();
							if (b == true) {
								String result1 = NSNConnect
										.getContent(context, "checkuser/" + sSimSerial);
								if (!(result1.equals(""))) {
									JSONObject myAwway = new JSONObject(result1);
									JSONArray suggestions = myAwway
											.getJSONArray("data");

									JSONObject tmp = new JSONObject(
											suggestions.getString(i));

									emp_id = tmp.getString("eid");
									mobile_no = tmp.getString("mno");
									email_id = tmp.getString("emid");
									sim_no = tmp.getString("sno");
									ac_enable = tmp.getString("acen");

									SharedPreferences.Editor prefEditor = prex1
											.edit();
									prefEditor.putString(Psimserial,
											sim_no.toString());
									prefEditor.putString(PemailValue,
											email_id.toString());
									prefEditor.putString(PmobileValue,
											mobile_no.toString());
									prefEditor.putString(PempValue,
											emp_id.toString());
									prefEditor.putString(Pacenable,
											ac_enable.toString());
									prefEditor.commit();

									int j1 = LoginForTrue();
									int j2 = LoginForFalse();
									int j3 = FirstLogin();

									if (j3 == 0) {
										Intent intent = new Intent(context,
												NSNMobileAppsActivity.class);
										startActivity(intent);
										finish();
									} else if (j3 >= 1 && j1 >= 1) {
										Intent intent = new Intent(context,
												GroupView.class);
										startActivity(intent);
										finish();
									} else if (j3 >= 1 && j2 >= 1) {
										Intent intent = new Intent(context,
												newactivation.class);
										startActivity(intent);
										finish();
									}
								} else {
									Intent intent = new Intent(context,
											NSNMobileAppsActivity.class);
									startActivity(intent);
									finish();
								}
							}

							else {
								/** Called when the network not available. */
								
									runOnUiThread(new Runnable() {
									public void run() {
										Dialog myDialog;
										myDialog = new Dialog(splash.this);
										myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
										myDialog.setContentView(R.layout.dialog);
										myDialog.setCancelable(true);

										Button button1 = (Button) myDialog
												.findViewById(R.id.button1);
										button1.setOnClickListener(new View.OnClickListener() {
											public void onClick(View v) {
												startActivity(new Intent(
														Settings.ACTION_WIRELESS_SETTINGS));

											}
										});

										/** Called when the network not available and check for condition again. */
										Button button2 = (Button) myDialog
												.findViewById(R.id.button2);
										button2.setOnClickListener(new View.OnClickListener() {
											public void onClick(View v) {
												boolean b = isNetworkAvailable();
												if (b == true) {
													int i = FirstLogin();
													
													if (i == 0) {
														try {
															String sSimSerial = getMyPhoneNumber();
															String emp_id, mobile_no, email_id, sim_no, ac_enable;
															emp_id = mobile_no = email_id = sim_no = ac_enable = null;

															boolean b1 = isNetworkAvailable();
															if (b1 == true) {
																String result1 = NSNConnect
																		.getContent(context, "checkuser/"
																				+ sSimSerial);
																if (!(result1
																		.equals(""))) {
																	JSONObject myAwway = new JSONObject(
																			result1);
																	JSONArray suggestions = myAwway
																			.getJSONArray("data");

																	JSONObject tmp = new JSONObject(
																			suggestions
																					.getString(i));

																	emp_id = tmp
																			.getString("emp_id");
																	mobile_no = tmp
																			.getString("mobile_no");
																	email_id = tmp
																			.getString("email_id");
																	sim_no = tmp
																			.getString("sim_no");
																	ac_enable = tmp
																			.getString("ac_enable");

																	SharedPreferences.Editor prefEditor = prex1
																			.edit();
																	prefEditor
																			.putString(
																					Psimserial,
																					sim_no.toString());
																	prefEditor
																			.putString(
																					PemailValue,
																					email_id.toString());
																	prefEditor
																			.putString(
																					PmobileValue,
																					mobile_no
																							.toString());
																	prefEditor
																			.putString(
																					PempValue,
																					emp_id.toString());
																	prefEditor
																			.putString(
																					Pacenable,
																					ac_enable
																							.toString());
																	prefEditor
																			.commit();

																	int j1 = LoginForTrue();
																	int j2 = LoginForFalse();
																	int j3 = FirstLogin();

																	if (j3 == 0) {
																		Intent intent = new Intent(
																				context,
																				NSNMobileAppsActivity.class);
																		startActivity(intent);
																		finish();
																	} else if (j3 >= 1
																			&& j1 >= 1) {
																		Intent intent = new Intent(
																				context,
																				GroupView.class);
																		startActivity(intent);
																		finish();
																	} else if (j3 >= 1
																			&& j2 >= 1) {
																		Intent intent = new Intent(
																				context,
																				newactivation.class);
																		startActivity(intent);
																		finish();
																	}
																} else {
																	Intent intent = new Intent(
																			context,
																			NSNMobileAppsActivity.class);
																	startActivity(intent);
																	finish();
																}
															}
															else {
																runOnUiThread(new Runnable() {
																	public void run() {
																		Dialog myDialog;
																		myDialog = new Dialog(
																				splash.this);
																		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
																		myDialog.setContentView(R.layout.dialog);
																		myDialog.setCancelable(true);

																		Button button1 = (Button) myDialog
																				.findViewById(R.id.button1);
																		button1.setOnClickListener(new View.OnClickListener() {
																			public void onClick(
																					View v) {
																				startActivity(new Intent(
																						Settings.ACTION_WIRELESS_SETTINGS));
																			}
																		});

																		Button button2 = (Button) myDialog
																				.findViewById(R.id.button2);
																		button2.setOnClickListener(new View.OnClickListener() {
																			public void onClick(
																					View v) {
																				finish();
																			}
																		});
																		myDialog.show();
																	}
																});
															}
														}

														catch (Throwable t) {

															runOnUiThread(new Runnable() {
																public void run() {
																	Dialog myDialog;
																	myDialog = new Dialog(
																			splash.this);
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

													} else {
														int i1 = LoginForTrue();
														int i2 = LoginForFalse();
														int i3 = FirstLogin();

														if (i3 == 0) {
															Intent intent = new Intent(
																	context,
																	NSNMobileAppsActivity.class);
															startActivity(intent);
															finish();	
														} else if (i3 >= 1
																&& i1 >= 1) {
															Intent intent = new Intent(
																	context,
																	GroupView.class);
															startActivity(intent);
															finish();
														} else if (i3 >= 1
																&& i2 >= 1) {
															Intent intent = new Intent(
																	context,
																	newactivation.class);
															startActivity(intent);
															finish();
														}
													}

												} else {
													Toast.makeText(
															splash.this,
															"Cannot connect to Internet. Please check your connection setting and try again.",
															Toast.LENGTH_LONG)
															.show();
												}
											}
										});
										myDialog.show();
									}
								});
								//finish();
							}
						}

						catch (Throwable t) {
							
							runOnUiThread(new Runnable() {
								public void run() {
									Dialog myDialog;
									myDialog = new Dialog(
											splash.this);
									myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									myDialog.setContentView(R.layout.serverdialog);
									myDialog.setCancelable(true);

									Button button1 = (Button) myDialog
											.findViewById(R.id.button1);
									button1.setOnClickListener(new View.OnClickListener() {
										public void onClick(
												View v) {
											ProgressDialog.show(splash.this,
													"Please Wait", "Activation in progress...");
												/** check for sim id availability. */
												int i = FirstLogin();
												if (i == 0) {
													try {
														
														/** check for sim id availability in server. */
														String sSimSerial = getMyPhoneNumber();
														String emp_id, mobile_no, email_id, sim_no, ac_enable;
														emp_id = mobile_no = email_id = sim_no = ac_enable = null;
														
														/** check for network availability. */
														
														boolean b = isNetworkAvailable();
														if (b == true) {
															String result1 = NSNConnect
																	.getContent(context, "checkuser/" + sSimSerial);
															if (!(result1.equals(""))) {
																JSONObject myAwway = new JSONObject(result1);
																JSONArray suggestions = myAwway
																		.getJSONArray("data");

																JSONObject tmp = new JSONObject(
																		suggestions.getString(i));

																emp_id = tmp.getString("eid");
																mobile_no = tmp.getString("mno");
																email_id = tmp.getString("emid");
																sim_no = tmp.getString("sno");
																ac_enable = tmp.getString("acen");

																SharedPreferences.Editor prefEditor = prex1
																		.edit();
																prefEditor.putString(Psimserial,
																		sim_no.toString());
																prefEditor.putString(PemailValue,
																		email_id.toString());
																prefEditor.putString(PmobileValue,
																		mobile_no.toString());
																prefEditor.putString(PempValue,
																		emp_id.toString());
																prefEditor.putString(Pacenable,
																		ac_enable.toString());
																prefEditor.commit();

																int j1 = LoginForTrue();
																int j2 = LoginForFalse();
																int j3 = FirstLogin();

																if (j3 == 0) {
																	Intent intent = new Intent(context,
																			NSNMobileAppsActivity.class);
																	startActivity(intent);
																	finish();
																} else if (j3 >= 1 && j1 >= 1) {
																	Intent intent = new Intent(context,
																			GroupView.class);
																	startActivity(intent);
																	finish();
																} else if (j3 >= 1 && j2 >= 1) {
																	Intent intent = new Intent(context,
																			newactivation.class);
																	startActivity(intent);
																	finish();
																}
															} else {
																Intent intent = new Intent(context,
																		NSNMobileAppsActivity.class);
																startActivity(intent);
																finish();
															}
														}

														else {
															/** Called when the network not available. */
															
																runOnUiThread(new Runnable() {
																public void run() {
																	Dialog myDialog;
																	myDialog = new Dialog(splash.this);
																	myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
																	myDialog.setContentView(R.layout.dialog);
																	myDialog.setCancelable(true);

																	Button button1 = (Button) myDialog
																			.findViewById(R.id.button1);
																	button1.setOnClickListener(new View.OnClickListener() {
																		public void onClick(View v) {
																			startActivity(new Intent(
																					Settings.ACTION_WIRELESS_SETTINGS));

																		}
																	});

																	/** Called when the network not available and check for condition again. */
																	Button button2 = (Button) myDialog
																			.findViewById(R.id.button2);
																	button2.setOnClickListener(new View.OnClickListener() {
																		public void onClick(View v) {
																			boolean b = isNetworkAvailable();
																			if (b == true) {
																				int i = FirstLogin();
																				
																				if (i == 0) {
																					try {
																						String sSimSerial = getMyPhoneNumber();
																						String emp_id, mobile_no, email_id, sim_no, ac_enable;
																						emp_id = mobile_no = email_id = sim_no = ac_enable = null;

																						boolean b1 = isNetworkAvailable();
																						if (b1 == true) {
																							String result1 = NSNConnect
																									.getContent(context, "checkuser/"
																											+ sSimSerial);
																							if (!(result1
																									.equals(""))) {
																								JSONObject myAwway = new JSONObject(
																										result1);
																								JSONArray suggestions = myAwway
																										.getJSONArray("data");

																								JSONObject tmp = new JSONObject(
																										suggestions
																												.getString(i));

																								emp_id = tmp
																										.getString("emp_id");
																								mobile_no = tmp
																										.getString("mobile_no");
																								email_id = tmp
																										.getString("email_id");
																								sim_no = tmp
																										.getString("sim_no");
																								ac_enable = tmp
																										.getString("ac_enable");

																								SharedPreferences.Editor prefEditor = prex1
																										.edit();
																								prefEditor
																										.putString(
																												Psimserial,
																												sim_no.toString());
																								prefEditor
																										.putString(
																												PemailValue,
																												email_id.toString());
																								prefEditor
																										.putString(
																												PmobileValue,
																												mobile_no
																														.toString());
																								prefEditor
																										.putString(
																												PempValue,
																												emp_id.toString());
																								prefEditor
																										.putString(
																												Pacenable,
																												ac_enable
																														.toString());
																								prefEditor
																										.commit();

																								int j1 = LoginForTrue();
																								int j2 = LoginForFalse();
																								int j3 = FirstLogin();

																								if (j3 == 0) {
																									Intent intent = new Intent(
																											context,
																											NSNMobileAppsActivity.class);
																									startActivity(intent);
																									finish();
																								} else if (j3 >= 1
																										&& j1 >= 1) {
																									Intent intent = new Intent(
																											context,
																											GroupView.class);
																									startActivity(intent);
																									finish();
																								} else if (j3 >= 1
																										&& j2 >= 1) {
																									Intent intent = new Intent(
																											context,
																											newactivation.class);
																									startActivity(intent);
																									finish();
																								}
																							} else {
																								Intent intent = new Intent(
																										context,
																										NSNMobileAppsActivity.class);
																								startActivity(intent);
																								finish();
																							}
																						}
																						else {
																							runOnUiThread(new Runnable() {
																								public void run() {
																									Dialog myDialog;
																									myDialog = new Dialog(
																											splash.this);
																									myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
																									myDialog.setContentView(R.layout.dialog);
																									myDialog.setCancelable(true);

																									Button button1 = (Button) myDialog
																											.findViewById(R.id.button1);
																									button1.setOnClickListener(new View.OnClickListener() {
																										public void onClick(
																												View v) {
																											startActivity(new Intent(
																													Settings.ACTION_WIRELESS_SETTINGS));
																										}
																									});

																									Button button2 = (Button) myDialog
																											.findViewById(R.id.button2);
																									button2.setOnClickListener(new View.OnClickListener() {
																										public void onClick(
																												View v) {
																											finish();
																										}
																									});
																									myDialog.show();
																								}
																							});
																						}
																					}

																					catch (Throwable t) {

																						runOnUiThread(new Runnable() {
																							public void run() {
																								Dialog myDialog;
																								myDialog = new Dialog(
																										splash.this);
																								myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
																								myDialog.setContentView(R.layout.serverdialog1);
																								myDialog.setCancelable(true);

																								Button button1 = (Button) myDialog
																										.findViewById(R.id.button1);
																								button1.setOnClickListener(new View.OnClickListener() {
																									public void onClick(
																											View v) {
																										finish();
//																										startActivity(new Intent(
//																												Settings.ACTION_WIRELESS_SETTINGS));
																									}
																								});

																					
																								myDialog.show();
																							}
																						});
																					}

																				} else {
																					int i1 = LoginForTrue();
																					int i2 = LoginForFalse();
																					int i3 = FirstLogin();

																					if (i3 == 0) {
																						Intent intent = new Intent(
																								context,
																								NSNMobileAppsActivity.class);
																						startActivity(intent);
																						finish();	
																					} else if (i3 >= 1
																							&& i1 >= 1) {
																						Intent intent = new Intent(
																								context,
																								GroupView.class);
																						startActivity(intent);
																						finish();
																					} else if (i3 >= 1
																							&& i2 >= 1) {
																						Intent intent = new Intent(
																								context,
																								newactivation.class);
																						startActivity(intent);
																						finish();
																					}
																				}

																			} else {
																				Toast.makeText(
																						splash.this,
																						"Cannot connect to Internet. Please check your connection setting and try again.",
																						Toast.LENGTH_LONG)
																						.show();
																			}
																		}
																	});
																	myDialog.show();
																}
															});
															//finish();
														}
													}

													catch (Throwable t) {
														
														runOnUiThread(new Runnable() {
															public void run() {
																Dialog myDialog;
																myDialog = new Dialog(
																		splash.this);
																myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
																myDialog.setContentView(R.layout.serverdialog1);
																myDialog.setCancelable(true);

																Button button1 = (Button) myDialog
																		.findViewById(R.id.button1);
																button1.setOnClickListener(new View.OnClickListener() {
																	public void onClick(
																			View v) {
																		finish();
//																		startActivity(new Intent(
//																				Settings.ACTION_WIRELESS_SETTINGS));
																	}
																});

													
																myDialog.show();
															}
														});
								
													}

												} else {
													int i1 = LoginForTrue();
													int i2 = LoginForFalse();
													int i3 = FirstLogin();

													if (i3 == 0) {
														Intent intent = new Intent(context,
																NSNMobileAppsActivity.class);
														startActivity(intent);
														finish();
													} else if (i3 >= 1 && i1 >= 1) {
														Intent intent = new Intent(context, GroupView.class);
														startActivity(intent);
														finish();
													} else if (i3 >= 1 && i2 >= 1) {
														Intent intent = new Intent(context,
																newactivation.class);
														startActivity(intent);
														finish();
													}
												}
		
										}
									});

									Button button2 = (Button) myDialog
											.findViewById(R.id.button2);
									button2.setOnClickListener(new View.OnClickListener() {
										public void onClick(
												View v) {
											finish();
										}
									});
									myDialog.show();
								}
							});
	
						}

					} else {
						int i1 = LoginForTrue();
						int i2 = LoginForFalse();
						int i3 = FirstLogin();

						if (i3 == 0) {
							Intent intent = new Intent(context,
									NSNMobileAppsActivity.class);
							startActivity(intent);
							finish();
						} else if (i3 >= 1 && i1 >= 1) {
							Intent intent = new Intent(context, GroupView.class);
							startActivity(intent);
							finish();
						} else if (i3 >= 1 && i2 >= 1) {
							Intent intent = new Intent(context,
									newactivation.class);
							startActivity(intent);
							finish();
						}
					}
					//finish();
				}
			}

		};
		// finish();
		splashTread.start();
	}

	
	/** check for simid availability */
	public int FirstLogin() {
		final SharedPreferences prex1 = this.getSharedPreferences("MyPrefs",
				MODE_PRIVATE);

		String Psim = prex1.getString("Psimserial", null);
		if (Psim == null) {
			return 0;
		} else {
			return 1;
		}
	}

	/** check for simid availability and account enable true*/
	public int LoginForTrue() {

		final SharedPreferences prex1 = this.getSharedPreferences("MyPrefs",
				MODE_PRIVATE);

		String Psim = prex1.getString("Psimserial", null);
		String Pac = prex1.getString("Pacenable", null);
		String sSimSerial = getMyPhoneNumber();
		int pac1 = Integer.parseInt(Pac);
		if (sSimSerial.equals(Psim) && pac1 == 1) {
			System.out.println(sSimSerial+Psim);	
			return 1;
		} else {
			return 0;
		}
		
	}

	/** check for simid availability and account enable false*/
	public int LoginForFalse() {

		final SharedPreferences prex1 = this.getSharedPreferences("MyPrefs",
				MODE_PRIVATE);

		String Psim = prex1.getString("Psimserial", null);
		String Pac = prex1.getString("Pacenable", null);
		String sSimSerial = getMyPhoneNumber();
		int pac1 = Integer.parseInt(Pac);
		if (sSimSerial.equals(Psim) && pac1 == 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/** check for network availability*/
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	/** get simnumber. */
	private String getMyPhoneNumber() {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getSimSerialNumber();
	}

}
