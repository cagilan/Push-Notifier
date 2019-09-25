package nsn.mobile.apps.nsnnotifier;
 

import nsn.mobile.apps.layout.ListPref;
import nsn.mobile.apps.nsnnotifier.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.ListView;
 
public class PreferenceSettings extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	private static final String KEY_ARCHIEVETIME = "archievetime";
	private static final String KEY_SERVERTIME = "servertime";
	@Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.prefscreen);
                ListView lv = (ListView)findViewById(android.R.id.list);
                lv.setBackgroundColor(Color.WHITE);
                lv.setScrollingCacheEnabled(false);
                lv.setSelector(R.drawable.selector);
                updatePreference(KEY_ARCHIEVETIME);
                updatePreference(KEY_SERVERTIME);
        }
        @Override
	    protected void onResume(){
	        super.onResume();
	        // Set up a listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	        //updatePreference(KEY_EDIT_TEXT_PREFERENCE);
	    }
	    @Override
	    protected void onPause() {
	        super.onPause();
	        // Unregister the listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	    }
        @Override
	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	            String key) {
        	 Preference preference = findPreference(key);
        	if (key.equals(KEY_ARCHIEVETIME))
	        {
	            if (preference instanceof ListPref){
	            	ListPref autoarchieve =  (ListPref)preference;
	                if(!autoarchieve.getEntry().equals("5 Minutes(Default)")){
	                autoarchieve.setSummary(autoarchieve.getEntry());
	                }
	                else
	                {
	                	autoarchieve.setSummary("Adjust time interval to archieve");
	                }
	            }
	        }
        	else if(key.equals(KEY_SERVERTIME))
	        {
	            if (preference instanceof ListPref){
	            	ListPref autoarchieve =  (ListPref)preference;
	            	if(!autoarchieve.getEntry().equals("5 Minutes(Default)")){
		                autoarchieve.setSummary(autoarchieve.getEntry());
		                }
		                else
		                {
		                	autoarchieve.setSummary("Adjust time interval to check server");
		                }
	            }
	        }
	    }
	 
	    private void updatePreference(String key){
	        if (key.equals(KEY_ARCHIEVETIME))
	        {
	            Preference preference = findPreference(key);
	            if (preference instanceof ListPref){
	            	ListPref autoarchieve =  (ListPref)preference;
	            	if(!autoarchieve.getEntry().equals("5 Minutes(Default)")){
		                autoarchieve.setSummary(autoarchieve.getEntry());
		                }
		                else
		                {
		                	autoarchieve.setSummary("Adjust time interval to archieve");
		                }
	            }
	        }
	        else if(key.equals(KEY_SERVERTIME))
	        {
	        	Preference preference = findPreference(key);
	            if (preference instanceof ListPref){
	            	ListPref autoarchieve =  (ListPref)preference;
	            	if(!autoarchieve.getEntry().equals("5 Minutes(Default)")){
		                autoarchieve.setSummary(autoarchieve.getEntry());
		                }
		                else
		                {
		                	autoarchieve.setSummary("Adjust time interval to check server");
		                }
	            }
	        }
	    }
}