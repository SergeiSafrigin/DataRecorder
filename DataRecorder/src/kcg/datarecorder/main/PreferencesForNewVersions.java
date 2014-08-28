package kcg.datarecorder.main;

import java.util.List;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

public class PreferencesForNewVersions extends PreferenceActivity {
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public static class PrefsFragment extends PreferenceFragment {
		private ListPreference cameraListPreference;
		private String screenSizes[];
		private ListPreference screenSizesPreference;

		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);
			addPreferencesFromResource(R.xml.preference);
			PreferenceCategory preferencecategory = (PreferenceCategory)findPreference("general");
			screenSizesPreference = new ListPreference(getActivity());
			screenSizesPreference.setTitle("Screen Size");
			screenSizesPreference.setKey("screenSize");
			setScreenSizes();
			screenSizesPreference.setEntries(screenSizes);
			screenSizesPreference.setEntryValues(screenSizes);
			screenSizesPreference.setValueIndex(-1 + screenSizes.length);
			preferencecategory.addPreference(screenSizesPreference);
			cameraListPreference = (ListPreference)findPreference("camera");
			cameraListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {


				public boolean onPreferenceChange(Preference preference, Object object) {
                    int n;
                    if (object.equals((Object)("back"))) {
                        n = 0;
                    } else {
                        boolean bl = object.equals((Object)("front"));
                        n = 0;
                        if (bl) {
                            n = 1;
                        }
                    }
                    Camera camera = Camera.open((int)(n));
                    Config.SCREEN_SIZES = camera.getParameters().getSupportedPreviewSizes();
                    PrefsFragment.this.setScreenSizes();
                    camera.release();
                    return true;
                }

			});
		}

		public void setScreenSizes() {
			List<Size> sizes = Config.SCREEN_SIZES;
			screenSizes = new String[sizes.size()];
			for(int i = 0; i < sizes.size(); i++){
				Size size = sizes.get(i);
				screenSizes[i] = size.width+" x "+size.height;
			}
		}
	}

	public PreferencesForNewVersions() {
	}
}
