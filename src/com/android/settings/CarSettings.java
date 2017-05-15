package com.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;

public class CarSettings extends SettingsPreferenceFragment {
	private static final String TAG = "CarSettings";

	private static final String CAR_SETTINGS = "car_settings";
	
	private static final String REVERSE_SETTINGS = "reverse_settings";
	private PreferenceScreen mCarSettings;
	private PreferenceScreen mReverseSettings;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.car_settings);
		final PreferenceScreen screen = getPreferenceScreen();
		mCarSettings = (PreferenceScreen) screen.findPreference(CAR_SETTINGS);
		mCarSettings.setOnPreferenceClickListener(mClickListener);
		mReverseSettings  = (PreferenceScreen) screen.findPreference(REVERSE_SETTINGS);
		mReverseSettings.setOnPreferenceClickListener(mClickListener);
	}

	private OnPreferenceClickListener mClickListener = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {

			if (preference == mCarSettings) {
//				getActivity().sendBroadcast(new Intent("com.george.settings.car_settings"));
				getActivity().startActivity(new Intent("com.george.canbus.intent.action.OptionsActivity"));
			}
			if (preference == mReverseSettings) {
				getActivity().sendBroadcast(new Intent("com.george.settings.reverse_settings"));
			}
			return false;
		}

	};

}
