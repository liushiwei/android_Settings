/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.backup.IBackupManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.util.Log;

/**
 * Gesture lock pattern settings.
 */
public class PrivacySettings extends SettingsPreferenceFragment implements
        DialogInterface.OnClickListener, Indexable {

    // Vendor specific
    private static final String GSETTINGS_PROVIDER = "com.google.settings";
    private static final String BACKUP_CATEGORY = "backup_category";
    private static final String BACKUP_DATA = "backup_data";
    private static final String AUTO_RESTORE = "auto_restore";
    private static final String CONFIGURE_ACCOUNT = "configure_account";
    private static final String MCU_UPGRADE = "mcu_upgrade";
    private static final String OTA_UPGRADE = "ota_update";
    private static final String BACKUP_INACTIVE = "backup_inactive";
    private static final String MASTER_CLEAR_TITLE = "master_clear";
    private static final String PERSONAL_DATA_CATEGORY = "personal_data_category";
    private static final String TAG = "PrivacySettings";
    private IBackupManager mBackupManager;
    private SwitchPreference mBackup;
    private SwitchPreference mAutoRestore;
    private Dialog mConfirmDialog;
    private PreferenceScreen mConfigure;
    private PreferenceScreen mMCUUpgrade;
    private PreferenceScreen mMasterClear;
    private PreferenceScreen mOTAUpgrade;
    private boolean mEnabled;

    private static final int DIALOG_ERASE_BACKUP = 2;
    private static final int DIALOG_MASTER_CLEAR = 3;
    private int mDialogType;
    
    private static final String RECOVERY_SYSTEM = "/data/recovery_system";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Don't allow any access if this is a secondary user
        mEnabled = Process.myUserHandle().isOwner();
        if (!mEnabled) {
            return;
        }

        addPreferencesFromResource(R.xml.privacy_settings);
        final PreferenceScreen screen = getPreferenceScreen();
        mBackupManager = IBackupManager.Stub.asInterface(
                ServiceManager.getService(Context.BACKUP_SERVICE));

        mBackup = (SwitchPreference) screen.findPreference(BACKUP_DATA);
        mBackup.setOnPreferenceChangeListener(preferenceChangeListener);
        mMCUUpgrade = (PreferenceScreen)screen.findPreference(MCU_UPGRADE);
        mMCUUpgrade.setOnPreferenceClickListener(mClickListener);
        
        
        mOTAUpgrade = (PreferenceScreen)screen.findPreference(OTA_UPGRADE);
        mOTAUpgrade.setOnPreferenceClickListener(mClickListener);
        
        mMasterClear = (PreferenceScreen)screen.findPreference(MASTER_CLEAR_TITLE);
        mMasterClear.setOnPreferenceClickListener(mClickListener);
        
        mAutoRestore = (SwitchPreference) screen.findPreference(AUTO_RESTORE);
        mAutoRestore.setOnPreferenceChangeListener(preferenceChangeListener);

        mConfigure = (PreferenceScreen) screen.findPreference(CONFIGURE_ACCOUNT);
        
        

        ArrayList<String> keysToRemove = getNonVisibleKeys(getActivity());
        final int screenPreferenceCount = screen.getPreferenceCount();
        for (int i = screenPreferenceCount - 1; i >= 0; --i) {
            Preference preference = screen.getPreference(i);
            if (keysToRemove.contains(preference.getKey())) {
                screen.removePreference(preference);
            }
        }
        PreferenceCategory backupCategory = (PreferenceCategory) findPreference(BACKUP_CATEGORY);
        if (backupCategory != null) {
            final int backupCategoryPreferenceCount = backupCategory.getPreferenceCount();
            for (int i = backupCategoryPreferenceCount - 1; i >= 0; --i) {
                Preference preference = backupCategory.getPreference(i);
                if (keysToRemove.contains(preference.getKey())) {
                    backupCategory.removePreference(preference);
                }
            }
        }
        updateToggles();
    }
    
    private OnPreferenceClickListener mClickListener = new OnPreferenceClickListener(){

		@Override
		public boolean onPreferenceClick(Preference preference) {
			 if(preference == mMCUUpgrade){
		        	getActivity().sendBroadcast(new Intent("com.george.settings.mcu_update"));
		        	return false;
		        }
			 if(preference == mMasterClear){
				 mDialogType = DIALOG_MASTER_CLEAR;
				 mConfirmDialog = new AlertDialog.Builder(getActivity()).setMessage(R.string.master_clear_confirm_title)
			                .setTitle(R.string.backup_erase_dialog_title)
			                .setPositiveButton(android.R.string.ok, PrivacySettings.this)
			                .setNegativeButton(android.R.string.cancel, PrivacySettings.this)
			                .show();
				 return false;
			 }
			 if(preference == mOTAUpgrade){
				 Intent intent = new Intent("com.george.intent.action.ota");
				 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 getActivity().startActivity(intent);
		        }
			return false;
		}
    	
    };

    @Override
    public void onResume() {
        super.onResume();

        // Refresh UI
        if (mEnabled) {
            updateToggles();
        }
    }

    @Override
    public void onStop() {
        if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
            mConfirmDialog.dismiss();
        }
        mConfirmDialog = null;
        mDialogType = 0;
        super.onStop();
    }

    private OnPreferenceChangeListener preferenceChangeListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (!(preference instanceof SwitchPreference)) {
                return true;
            }
            boolean nextValue = (Boolean) newValue;
            boolean result = false;
            if (preference == mBackup) {
                if (nextValue == false) {
                    // Don't change Switch status until user makes choice in dialog
                    // so return false here.
                    showEraseBackupDialog();
                } else {
                    setBackupEnabled(true);
                    result = true;
                }
            } else if (preference == mAutoRestore) {
                try {
                    mBackupManager.setAutoRestore(nextValue);
                    result = true;
                } catch (RemoteException e) {
                    mAutoRestore.setChecked(!nextValue);
                }
            }
            return result;
        }
    };

    private void showEraseBackupDialog() {
        mDialogType = DIALOG_ERASE_BACKUP;
        CharSequence msg = getResources().getText(R.string.backup_erase_dialog_message);
        // TODO: DialogFragment?
        mConfirmDialog = new AlertDialog.Builder(getActivity()).setMessage(msg)
                .setTitle(R.string.backup_erase_dialog_title)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this)
                .show();
    }

    /*
     * Creates toggles for each available location provider
     */
    private void updateToggles() {
        ContentResolver res = getContentResolver();

        boolean backupEnabled = false;
        Intent configIntent = null;
        String configSummary = null;
        try {
            backupEnabled = mBackupManager.isBackupEnabled();
            String transport = mBackupManager.getCurrentTransport();
            configIntent = mBackupManager.getConfigurationIntent(transport);
            configSummary = mBackupManager.getDestinationString(transport);
        } catch (RemoteException e) {
            // leave it 'false' and disable the UI; there's no backup manager
            mBackup.setEnabled(false);
        }
        mBackup.setChecked(backupEnabled);

        mAutoRestore.setChecked(Settings.Secure.getInt(res,
                Settings.Secure.BACKUP_AUTO_RESTORE, 1) == 1);
        mAutoRestore.setEnabled(backupEnabled);

        final boolean configureEnabled = (configIntent != null) && backupEnabled;
        mConfigure.setEnabled(configureEnabled);
        mConfigure.setIntent(configIntent);
        setConfigureSummary(configSummary);
    }

    private void setConfigureSummary(String summary) {
        if (summary != null) {
            mConfigure.setSummary(summary);
        } else {
            mConfigure.setSummary(R.string.backup_configure_account_default_summary);
        }
    }

    private void updateConfigureSummary() {
        try {
            String transport = mBackupManager.getCurrentTransport();
            String summary = mBackupManager.getDestinationString(transport);
            setConfigureSummary(summary);
        } catch (RemoteException e) {
            // Not much we can do here
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // Dialog is triggered before Switch status change, that means marking the Switch to
        // true in showEraseBackupDialog() method will be override by following status change.
        // So we do manual switching here due to users' response.
        if (mDialogType == DIALOG_ERASE_BACKUP) {
            // Accept turning off backup
            if (which == DialogInterface.BUTTON_POSITIVE) {
                setBackupEnabled(false);
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                // Reject turning off backup
                setBackupEnabled(true);
            }
            updateConfigureSummary();
        }
		if (mDialogType == DIALOG_MASTER_CLEAR) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				File file = new File(RECOVERY_SYSTEM);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					FileUtils.setPermissions(RECOVERY_SYSTEM, FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IRWXO,
							-1, -1);
				}
				
				String []  CMD= { "/system/bin/sync" };
				try {
					runcmd(CMD,"/");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						getActivity().sendBroadcast(new Intent("com.george.intent.reboot"));
					}
				}, 1500);

			}
		}
        mDialogType = 0;
    }

	/**
	 * 执行一个shell命令，并返回字符串值
	 *
	 * @param cmd
	 *            命令名称&参数组成的数组（例如：{"/system/bin/cat", "/proc/version"}）
	 * @param workdirectory
	 *            命令执行路径（例如："system/bin/"）
	 * @return 执行结果组成的字符串
	 * @throws IOException
	 */
	public static synchronized String runcmd(String[] cmd, String workdirectory) throws IOException {
		StringBuffer result = new StringBuffer();
		try {
			// 创建操作系统进程（也可以由Runtime.exec()启动）
			// Runtime runtime = Runtime.getRuntime();
			// Process proc = runtime.exec(cmd);
			// InputStream inputstream = proc.getInputStream();
			ProcessBuilder builder = new ProcessBuilder(cmd);
			InputStream in = null;
			// 设置一个路径（绝对路径了就不一定需要）
			if (workdirectory != null) {
				// 设置工作目录（同上）
				builder.directory(new File(workdirectory));
				// 合并标准错误和标准输出
				builder.redirectErrorStream(true);
				// 启动一个新进程
				java.lang.Process process = builder.start();
				// 读取进程标准输出流
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1) {
					result = result.append(new String(re));
				}
			}
			// 关闭输入流
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}

    /**
     * Informs the BackupManager of a change in backup state - if backup is disabled,
     * the data on the server will be erased.
     * @param enable whether to enable backup
     */
    private void setBackupEnabled(boolean enable) {
        if (mBackupManager != null) {
            try {
                mBackupManager.setBackupEnabled(enable);
            } catch (RemoteException e) {
                mBackup.setChecked(!enable);
                mAutoRestore.setEnabled(!enable);
                return;
            }
        }
        mBackup.setChecked(enable);
        mAutoRestore.setEnabled(enable);
        mConfigure.setEnabled(enable);
    }

    @Override
    protected int getHelpResource() {
        return R.string.help_url_backup_reset;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new PrivacySearchIndexProvider();

    private static class PrivacySearchIndexProvider extends BaseSearchIndexProvider {

        boolean mIsPrimary;

        public PrivacySearchIndexProvider() {
            super();

            mIsPrimary = UserHandle.myUserId() == UserHandle.USER_OWNER;
        }

        @Override
        public List<SearchIndexableResource> getXmlResourcesToIndex(
                Context context, boolean enabled) {

            List<SearchIndexableResource> result = new ArrayList<SearchIndexableResource>();

            // For non-primary user, no backup or reset is available
            if (!mIsPrimary) {
                return result;
            }

            SearchIndexableResource sir = new SearchIndexableResource(context);
            sir.xmlResId = R.xml.privacy_settings;
            result.add(sir);

            return result;
        }

        @Override
        public List<String> getNonIndexableKeys(Context context) {
            return getNonVisibleKeys(context);
        }
    }

    private static ArrayList<String> getNonVisibleKeys(Context context) {
        final ArrayList<String> nonVisibleKeys = new ArrayList<String>();
        final IBackupManager backupManager = IBackupManager.Stub.asInterface(
                ServiceManager.getService(Context.BACKUP_SERVICE));
        boolean isServiceActive = false;
        try {
            isServiceActive = backupManager.isBackupServiceActive(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.w(TAG, "Failed querying backup manager service activity status. " +
                    "Assuming it is inactive.");
        }
        if (isServiceActive) {
            nonVisibleKeys.add(BACKUP_INACTIVE);
        } else {
            nonVisibleKeys.add(AUTO_RESTORE);
            nonVisibleKeys.add(CONFIGURE_ACCOUNT);
            nonVisibleKeys.add(BACKUP_DATA);
        }
        if (UserManager.get(context).hasUserRestriction(
                UserManager.DISALLOW_FACTORY_RESET)) {
            nonVisibleKeys.add(PERSONAL_DATA_CATEGORY);
        }
        // Vendor specific
        if (context.getPackageManager().
                resolveContentProvider(GSETTINGS_PROVIDER, 0) == null) {
            nonVisibleKeys.add(BACKUP_CATEGORY);
        }
        return nonVisibleKeys;
    }
}
