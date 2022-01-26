package com.smartdevice.testd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.smartdevice.testd3501.R;
import com.smartdevicesdk.device.DeviceInfo;
import com.smartdevicesdk.device.DeviceManage;
import com.smartdevicesdk.ui.SystemControl;

public class MainActivity extends Activity {
	private ListView listView;
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

	// PC700 PDA3501 
	public static DeviceInfo devInfo = DeviceManage.getDevInfo("PDA3501");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getOverflowMenu();
		listView = (ListView) findViewById(R.id.listView1);
		if (devInfo != null) {
			// 生成SimpleAdapter适配器对象
			SimpleAdapter mySimpleAdapter = new SimpleAdapter(this,
					devInfo.getFunctionList(), R.layout.adapter_listview,
					new String[] { "id", "name" }, new int[] {
							R.id.textview_itemid, R.id.textview_itemname });
			listView.setAdapter(mySimpleAdapter);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
				String titleStr = map.get("id");
				if (titleStr.equals("scanner")){
					intent.setClass(MainActivity.this, ScannerActivity.class);
					startActivity(intent);
				} else if (titleStr.equals("camerascanner")){
					intent.setClass(MainActivity.this, CameraScannerActivity.class);
					startActivity(intent);
				} else if (titleStr.equals("psam")) {
					intent.setClass(MainActivity.this, PSAMActivity.class);
					startActivity(intent);
				}else if (titleStr.equals("psamv2")) {
					intent.setClass(MainActivity.this, PSAMActivityV2.class);
					startActivity(intent);
				}
			}
		});
		devInfo.openModel();
	}
	
	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		devInfo.closeModel();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_about) {
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, DeviceInfoActivity.class);
			startActivity(intent);
		}else if(id==R.id.action_keypress){
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, KeyPressActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		SystemControl.disableNotificationBar(this);
	}

}
