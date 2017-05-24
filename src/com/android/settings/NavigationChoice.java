package com.android.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.FileUtils;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NavigationChoice extends Preference {
	private ListView mAppList;
	private List<String> mItems = null;
	private IconicAdapter mAppListAdapter;
	private PackageManager mPackageManager;
	private List<ResolveInfo> mApps;
	private List<View> mViews;
	private AlertDialog mAlertDialog;
	private static final String TAG = "CaritNavigationChoice";
	private static final String PROPERTIESFILE = "/data/system/.properties_file";
	private String mPackageName = "";
	private String mClassName = "";
	public NavigationChoice(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPackageManager = context.getPackageManager();
		mItems = new ArrayList<String>();
		mViews = new ArrayList<View>();
		//List<ApplicationInfo> installedAppList = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		getNavConfig();
		final List<ResolveInfo> apps = mPackageManager.queryIntentActivities(mainIntent, 0);
		mApps = new ArrayList<ResolveInfo>();
		for (ResolveInfo appInfo : apps) {
			boolean flag = false;
			if ((appInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				// Updated system app
				flag = true;
			} else if ((appInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// Non-system app
				flag = true;
			}
			if("com.autonavi.amapauto".equals(appInfo.activityInfo.packageName)){
				flag = true;
			}
			if (flag) {
				mApps.add(appInfo);
			}
		}
		LayoutInflater inflater = LayoutInflater.from(this.getContext());
		for (ResolveInfo app : mApps) {
			mItems.add(app.loadLabel(mPackageManager).toString());
			
			View row = inflater.inflate(R.layout.app_list_view, null, false);
			ViewWrapper wrapper = new ViewWrapper(row);
			row.setTag(wrapper);
			mViews.add(row);
		}
		
		
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
	}

	@Override
	protected void onClick() {
		LinearLayout appLayout = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(R.layout.app_list, null);
		mAppList = (ListView) appLayout.findViewById(R.id.appList);
		mAppListAdapter = new IconicAdapter(R.layout.app_list_view, mItems);
		mAppList.setAdapter(mAppListAdapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext()).setIcon(android.R.drawable.ic_dialog_map)
				.setTitle(getContext().getString(R.string.navigation_choice_title)).setView(appLayout);
		// .setPositiveButton(R.string.navigation_choice_sure,
		// null).setView(appLayout);
		mAlertDialog = builder.create();
		mAlertDialog.show();
		mAppList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.e(TAG, "onItemClick");
				mAlertDialog.dismiss();
				ViewWrapper tag = (ViewWrapper) view.getTag();
				String temp = "";
				try {
					File file = new File(PROPERTIESFILE);
					if (!file.exists()) {
						file.createNewFile();
						FileUtils.setPermissions(PROPERTIESFILE, FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IRWXO, -1, -1);
					}
					FileInputStream fis = new FileInputStream(file);
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader br = new BufferedReader(isr);
					StringBuffer buf = new StringBuffer();

					do {
						temp = br.readLine();
						if (temp!=null&&temp.indexOf("nav_app_class_name") < 0&&temp.indexOf("nav_app_package_name") < 0) {
							buf = buf.append(temp);
							buf = buf.append("\n");
						}
					} while (temp != null);

					br.close();
					buf.append("nav_app_class_name=" + tag.getClassName() + "\n");
					buf.append("nav_app_package_name=" + tag.getPackageName() + "\n");
					temp = buf.toString();
					Log.e(TAG,temp);
					FileOutputStream fos = new FileOutputStream(file);
					PrintWriter pw = new PrintWriter(fos);
					pw.write(temp);
					pw.flush();
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				NavigationChoice.this.getContext().sendBroadcast(new Intent("android.intent.action.NAVI_CHANGED"));
				getNavConfig();
			}

		});
		super.onClick();
	}

	class IconicAdapter extends ArrayAdapter<String> {
		private List<String> listItems;
		private int layout;

		IconicAdapter(int layout, List<String> listItems) {
			super(NavigationChoice.this.getContext(), layout, listItems);
			this.listItems = listItems;
			this.layout = layout;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			/*View row = convertView;
			ViewWrapper wrapper = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(this.getContext());

				row = inflater.inflate(layout, parent, false);
				wrapper = new ViewWrapper(row);
				row.setTag(wrapper);
			} else {
				wrapper = (ViewWrapper) row.getTag();
			}*/
			View row = mViews.get(position);
			ViewWrapper wrapper = (ViewWrapper)row.getTag();
			// if (BluetoothApplication.getConnectType()>0 && wrapper.getIcon()
			// != null && position ==
			// BluetoothApplication.getConnectedDevice()-1) {
			// wrapper.getIcon().setVisibility(ImageView.VISIBLE);
			// }else{
			// wrapper.getIcon().setVisibility(ImageView.GONE);
			// }
			// File file = new File(listItems.get(position));
			wrapper.getIcon().setImageDrawable(mApps.get(position).loadIcon(mPackageManager));
			String pkgName =mApps.get(position).activityInfo.applicationInfo.packageName;
			//Log.e(TAG, "position = "+position+"pkgName = "+mApps.get(position).activityInfo.applicationInfo.packageName+ "mPackageName ="+mPackageName);
			wrapper.setPackageName(pkgName);
			wrapper.setClassName(mApps.get(position).activityInfo.name);
			
			wrapper.getLabel().setText(listItems.get(position));
			if(pkgName.equals(mPackageName)){
				wrapper.getLabel().setChecked(true);
			}else{
				wrapper.getLabel().setChecked(false);
			}
			return (row);
		}
	}

	class ViewWrapper {
		View base;
		CheckedTextView label = null;
		ImageView icon = null;
		String packageName = null;
		String className = null;
		int id = -1;

		ViewWrapper(View base) {
			this.base = base;
			id = -1;
		}

		CheckedTextView getLabel() {
			if (label == null) {
				label = (CheckedTextView) base.findViewById(R.id.packageInfoName);
			}

			return (label);
		}

		ImageView getIcon() {
			if (icon == null) {
				icon = (ImageView) base.findViewById(R.id.icon);
			}

			return (icon);
		}

		int getId() {
			return id;
		}

		void setId(int deviceId) {
			id = deviceId;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
		
		

	}
	private void getNavConfig(){
		File file = new File(PROPERTIESFILE);
		Log.e(TAG, "getNavConfig()");
		if (file.exists()) {
			Log.e(TAG, "file.exists()");
			BufferedReader buf;
			String source = null;

			try {
				buf = new BufferedReader(new FileReader(file));
				do {
					source = buf.readLine();
					if (source != null && source.startsWith("nav_app_class_name=")) {
						mClassName = source.substring(source.indexOf("=") + 1);
					}
					if (source != null && source.startsWith("nav_app_package_name=")) {
						mPackageName = source.substring(source.indexOf("=") + 1);
					}
					if (source != null)
						Log.e(TAG, source);
				} while (source != null);
				buf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
