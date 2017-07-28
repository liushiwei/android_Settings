package com.android.settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {
	private static final String TAG = "CarSettings";
	private static final String CAR_SETTINGS = "car_settings";
	private static final String REVERSE_SETTINGS = "reverse_settings";
	private static final String SHUTDOWN_SETTINGS = "shutdown_settings";
	private static final String KEY_BRIGHTNESS = "brightness";
	private PreferenceScreen mCarSettings;
	private Preference mOTGSettings;
	private Preference mMCURESETSettings;
	private PreferenceScreen mReverseSettings;
	private PreferenceScreen mShutdownSettings;
	private ListPreference mTP_change ;
	private int mTipCount = 2;
	private Preference mBrightnessPreference;
	
	private final static String BOARDCAST_COMMAND = "com.george.canbox.intent.command";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.car_settings);
		final PreferenceScreen screen = getPreferenceScreen();
		mCarSettings = (PreferenceScreen) screen.findPreference(CAR_SETTINGS);
		mCarSettings.setOnPreferenceClickListener(mClickListener);
		mReverseSettings = (PreferenceScreen) screen.findPreference(REVERSE_SETTINGS);
		mReverseSettings.setOnPreferenceClickListener(mClickListener);
		mTP_change = (ListPreference) findPreference("tp_type");
		mShutdownSettings= (PreferenceScreen) screen.findPreference(SHUTDOWN_SETTINGS);
		// mShutdownSettings.setOnPreferenceClickListener(mClickListener);
		mBrightnessPreference = findPreference(KEY_BRIGHTNESS);
        mBrightnessPreference.setOnPreferenceClickListener(mClickListener);
	}
	
	

	@Override
	public void onResume() {
		
		ListView listview = (ListView) getView().findViewById(android.R.id.list);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mTipCount--;
				if(mTipCount==0){
					Log.e(TAG, "add mOTGSettings");
					final PreferenceScreen screen = getPreferenceScreen();
					
					mOTGSettings = new Preference(getActivity());
					mOTGSettings.setTitle("OTG Enable");
					mOTGSettings.setOnPreferenceClickListener(mClickListener);
					screen.addPreference(mOTGSettings);
					mMCURESETSettings = new Preference(getActivity());
					mMCURESETSettings.setTitle("MCU Reset");
					mMCURESETSettings.setOnPreferenceClickListener(mClickListener);
					screen.addPreference(mMCURESETSettings);
				}
				if(mTP_change==null){
					final PreferenceScreen screen = getPreferenceScreen();
					mTP_change=new ListPreference(getActivity());//构建一个子Preference，待添加到容器中
					mTP_change.setTitle(R.string.tp_setting);//设置title
					mTP_change.setEntries(R.array.tp_type);
					mTP_change.setEntryValues(R.array.tp_type_values);;
					screen.addPreference(mTP_change);//添加到容器中
					mTP_change.setOnPreferenceChangeListener(CarSettings.this);
				}
				
				return false;
			}
			
		});
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Log.e(TAG, "list item onclick");
//				mTipCount--;
//				if(mTipCount<0){
//					mTP_change.setEnabled(true);
//					mTP_change.setOnPreferenceChangeListener(CarSettings.this);
//				}
//			}
//			
//		});
		super.onResume();
	}





	private OnPreferenceClickListener mClickListener = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {

			if (preference == mCarSettings) {
				// getActivity().sendBroadcast(new
				// Intent("com.george.settings.car_settings"));
				getActivity().startActivity(new Intent("com.george.canbus.intent.action.OptionsActivity"));
			}
			if (preference == mReverseSettings) {
				getActivity().sendBroadcast(new Intent("com.george.settings.reverse_settings"));
			}
			if (mShutdownSettings == preference) {
				Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);

				intent.putExtra(Intent.EXTRA_KEY_CONFIRM,false);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
			
			if (mOTGSettings == preference) {
				Intent intent= new Intent(BOARDCAST_COMMAND);
				intent.putExtra("command", 1);
				getActivity().sendBroadcast(intent);
			}
			
			if (mMCURESETSettings == preference) {
				Intent intent= new Intent(BOARDCAST_COMMAND);
				intent.putExtra("command", 2);
				getActivity().sendBroadcast(intent);
			}
			if(preference == mBrightnessPreference){
	        	getActivity().sendBroadcast(new Intent("com.george.settings.back_light"));
	        }
			return false;
		}

	};

	private static final String TP_FILE = "/data/system/tp_cfg";

	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		String path = (String) arg1;
		File ko = new File(path);
		if (ko.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(new File(TP_FILE));
				out.write(path.getBytes());
				out.write('\n');
				out.close();
				SystemProperties.set("ctl.start", "change_tp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			if(path.startsWith("/storage")){
				ko = new File("/storage/sdcard0/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write(("/storage/sdcard0/goodix.ko"+'\n').getBytes());
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				ko = new File("/storage/sdcard1/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write(("/storage/sdcard1/goodix.ko"+'\n').getBytes());
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				ko = new File("/storage/usbdisk2/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write(("/storage/usbdisk2/goodix.ko"+'\n').getBytes());
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				ko = new File("/storage/usbdisk3/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write("/storage/usbdisk3/goodix.ko".getBytes());
						out.write('\n');
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				ko = new File("/storage/usbdisk4/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write("/storage/usbdisk4/goodix.ko".getBytes());
						out.write('\n');
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				ko = new File("/storage/usbdisk5/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write("/storage/usbdisk5/goodix.ko".getBytes());
						out.write('\n');
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				ko = new File("/storage/usbdisk1/goodix.ko");
				if (ko.exists()) {
					try {
						FileOutputStream out = new FileOutputStream(new File(TP_FILE));
						out.write("/storage/usbdisk1/goodix.ko".getBytes());
						out.write('\n');
						out.close();
						SystemProperties.set("ctl.start", "change_tp");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				Toast.makeText(getActivity(), R.string.file_not_exist, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), R.string.file_not_exist, Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}

}
