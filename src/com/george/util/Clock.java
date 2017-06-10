/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.george.util;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Digital clock for the status bar.
 */
public class Clock extends TextView {
	private boolean mAttached;
	private Calendar mCalendar;
	private String mClockFormatString;
	private SimpleDateFormat mClockFormat;
	private Locale mLocale;

	private static final int AM_PM_STYLE_NORMAL = 0;
	private static final int AM_PM_STYLE_SMALL = 1;
	private static final int AM_PM_STYLE_GONE = 2;
	private final int mAmPmStyle;

	public Clock(Context context) {
		this(context, null);
	}

	public Clock(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Clock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mAmPmStyle = AM_PM_STYLE_NORMAL;
//		Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/LiquidCrystal-Normal.otf");
//		setTypeface(customFont);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		if (!mAttached) {
			mAttached = true;
			IntentFilter filter = new IntentFilter();

			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);

			getContext().registerReceiver(mIntentReceiver, filter);
		}

		// NOTE: It's safe to do these after registering the receiver since the
		// receiver always runsgetContext().
		// in the main thread, therefore the receiver can't run before this
		// method returns.

		// The time zone may have changed while the receiver wasn't registered,
		// so update the Time
		mCalendar = Calendar.getInstance(TimeZone.getDefault());

		// Make sure we update to the current time
		updateClock();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
			getContext().unregisterReceiver(mIntentReceiver);
			mAttached = false;
		}
	}

	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				String tz = intent.getStringExtra("time-zone");
				mCalendar = Calendar.getInstance(TimeZone.getTimeZone(tz));
				if (mClockFormat != null) {
					mClockFormat.setTimeZone(mCalendar.getTimeZone());
				}
			} else if (action.equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
				final Locale newLocale = getResources().getConfiguration().locale;
				if (!newLocale.equals(mLocale)) {
					mLocale = newLocale;
					mClockFormatString = ""; // force refresh
				}
			}
			updateClock();
		}
	};

	final void updateClock() {
		if (mDemoMode)
			return;
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		setText(getSmallTime());
	}

	private final CharSequence getSmallTime() {
		Context context = getContext();
		boolean is24 = DateFormat.is24HourFormat(context);

		SimpleDateFormat sdf;
		String format = is24 ? "HH:mm" : "hh:mm";

		mClockFormat = sdf = new SimpleDateFormat(format);
		mClockFormatString = format;
		String result = sdf.format(mCalendar.getTime());

		return result;

	}

	private boolean mDemoMode;

}
