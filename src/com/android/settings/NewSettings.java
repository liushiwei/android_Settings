/*
 * Copyright (C) 2008 The Android Open Source Project
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

import com.android.settings.applications.AppOpsSummary;

/**
 * Top-level Settings activity
 */
public class NewSettings extends SettingsActivity {

    /*
    * Settings subclasses for launching independently.
    */
    public static class BluetoothSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WirelessSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SimSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class TetherSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class VpnSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DateTimeSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class StorageSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WifiSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WifiP2pSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class InputMethodAndLanguageSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class KeyboardLayoutPickerActivity extends NewSettingsActivity { /* empty */ }
    public static class InputMethodAndSubtypeEnablerActivity extends NewSettingsActivity { /* empty */ }
    public static class VoiceInputSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SpellCheckersSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class LocalePickerActivity extends NewSettingsActivity { /* empty */ }
    public static class UserDictionarySettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class HomeSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DisplaySettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DeviceInfoSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ApplicationSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ManageApplicationsActivity extends NewSettingsActivity { /* empty */ }
    public static class AppOpsSummaryActivity extends NewSettingsActivity {
        @Override
        public boolean isValidFragment(String className) {
            if (AppOpsSummary.class.getName().equals(className)) {
                return true;
            }
            return super.isValidFragment(className);
            }
    }
    public static class StorageUseActivity extends NewSettingsActivity { /* empty */ }
    public static class DevelopmentSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilitySettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class CaptioningSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityInversionSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityContrastSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityDaltonizerSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SecuritySettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class UsageAccessSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class LocationSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PrivacySettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class RunningServicesActivity extends NewSettingsActivity { /* empty */ }
    public static class ManageAccountsSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PowerUsageSummaryActivity extends NewSettingsActivity { /* empty */ }
    public static class BatterySaverSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccountSyncSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccountSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccountSyncSettingsInAddAccountActivity extends NewSettingsActivity { /* empty */ }
    public static class CryptKeeperSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DeviceAdminSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DataUsageSummaryActivity extends NewSettingsActivity { /* empty */ }
    public static class AdvancedWifiSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SavedAccessPointsSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class TextToSpeechSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AndroidBeamSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WifiDisplaySettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DreamSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationStationActivity extends NewSettingsActivity { /* empty */ }
    public static class UserSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationAccessSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ConditionProviderSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class UsbSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class TrustedCredentialsSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PaymentSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PrintSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PrintJobSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ZenModeSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationAppListActivity extends NewSettingsActivity { /* empty */ }
    public static class AppNotificationSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class OtherSoundSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class QuickLaunchSettingsActivity extends NewSettingsActivity { /* empty */ }

    public static class TopLevelSettings extends NewSettingsActivity { /* empty */ }
    public static class ApnSettingsActivity extends NewSettingsActivity { /* empty */ }
}

