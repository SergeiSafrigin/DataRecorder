// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package kcg.datarecorder.main;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

public class Preferences extends PreferenceActivity
    implements android.content.SharedPreferences.OnSharedPreferenceChangeListener
{

    private String screenSizes[];
    private ListPreference screenSizesPreference;

    public Preferences()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setRequestedOrientation(1);
        addPreferencesFromResource(0x7f050000);
        EditTextPreference edittextpreference = (EditTextPreference)findPreference("name");
        edittextpreference.setSummary(edittextpreference.getText());
        PreferenceCategory preferencecategory = (PreferenceCategory)findPreference("general");
        screenSizesPreference = new ListPreference(this);
        screenSizesPreference.setTitle("Screen Size");
        screenSizesPreference.setKey("screenSize");
        setScreenSizes();
        screenSizesPreference.setEntries(screenSizes);
        screenSizesPreference.setEntryValues(screenSizes);
        screenSizesPreference.setValueIndex(-1 + screenSizes.length);
        preferencecategory.addPreference(screenSizesPreference);
    }

    protected void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    protected void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String s)
    {
    }

    public void setScreenSizes()
    {
        List list = Config.SCREEN_SIZES;
        screenSizes = new String[list.size()];
        int i = 0;
        do
        {
            if (i >= list.size())
            {
                return;
            }
            android.hardware.Camera.Size size = (android.hardware.Camera.Size)list.get(i);
            screenSizes[i] = (new StringBuilder(String.valueOf(size.width))).append(" x ").append(size.height).toString();
            i++;
        } while (true);
    }
}
