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
    public static class BluetoothNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WirelessNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SimNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class TetherNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class VpnNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DateTimeNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class StorageNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WifiNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WifiP2pNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class InputMethodAndLanguageNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class KeyboardLayoutPickerActivity extends NewSettingsActivity { /* empty */ }
    public static class InputMethodAndSubtypeEnablerActivity extends NewSettingsActivity { /* empty */ }
    public static class VoiceInputNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SpellCheckersNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class LocalePickerActivity extends NewSettingsActivity { /* empty */ }
    public static class UserDictionaryNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class HomeNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DisplayNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DeviceInfoNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ApplicationNewSettingsActivity extends NewSettingsActivity { /* empty */ }
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
    public static class DevelopmentNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class CaptioningNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityInversionNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityContrastNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccessibilityDaltonizerNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SecurityNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class UsageAccessNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class LocationNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PrivacyNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class RunningServicesActivity extends NewSettingsActivity { /* empty */ }
    public static class ManageAccountsNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PowerUsageSummaryActivity extends NewSettingsActivity { /* empty */ }
    public static class BatterySaverNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccountSyncNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccountNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AccountSyncSettingsInAddAccountActivity extends NewSettingsActivity { /* empty */ }
    public static class CryptKeeperNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DeviceAdminNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DataUsageSummaryActivity extends NewSettingsActivity { /* empty */ }
    public static class AdvancedWifiNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class SavedAccessPointsNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class TextToSpeechNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class AndroidBeamNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class WifiDisplayNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class DreamNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationStationActivity extends NewSettingsActivity { /* empty */ }
    public static class UserNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationAccessNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ConditionProviderNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class UsbNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class TrustedCredentialsNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PaymentNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PrintNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class PrintJobNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class ZenModeNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class NotificationAppListActivity extends NewSettingsActivity { /* empty */ }
    public static class AppNotificationNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class OtherSoundNewSettingsActivity extends NewSettingsActivity { /* empty */ }
    public static class QuickLaunchNewSettingsActivity extends NewSettingsActivity { /* empty */ }

    public static class TopLevelSettings extends NewSettingsActivity { /* empty */ }
    public static class ApnNewSettingsActivity extends NewSettingsActivity { /* empty */ }
}

