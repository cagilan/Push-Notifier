package nsn.mobile.apps.nsnnotifier;
import nsn.mobile.apps.nsnnotifier.service.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences spref = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if(Integer.parseInt(spref.getString("Pacenable", null))==1 && spref.getString("Psimserial", null).equals(mTelephonyMgr.getSimSerialNumber())){
			Intent serviceIntent = new Intent(NotificationCollectorService.class.getName());
			context.startService(serviceIntent);
		}
	}
}
