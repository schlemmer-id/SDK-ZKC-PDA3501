package com.smartdevice.testd;

import com.smartdevice.testd3501.R;
import com.smartdevicesdk.io.EmGpio;
import com.smartdevicesdk.psam.PSAMhelperV2;
import com.smartdevicesdk.utils.StringUtility;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PSAMActivityV2 extends Activity {

	protected static final String TAG = "MainActivity";
	Button btn_init, btn_random, button_send, button_power_on,
			button_power_off;
	EditText editText1, editText_cmd;
	RadioGroup radioGroupCard,radioGroupPower;

	int cardLocation = 1;
	int powerValue = 2;

	long  fd =0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_psamv2);

		editText1 = (EditText) findViewById(R.id.editText1);
		editText_cmd = (EditText) findViewById(R.id.editText_cmd);

		button_power_on = (Button) findViewById(R.id.button_power_on);
		button_power_on.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int s = PSAMhelperV2.OpenCard(fd, cardLocation);
				Log.i(TAG, "psam fd=" + fd);
				if (s != -1) {
					Log.i(TAG, "open success!");
					editText1.setText(s+"");
				}
			}
		});

		button_power_off = (Button) findViewById(R.id.button_power_off);
		button_power_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int s = PSAMhelperV2.CloseCard(fd);
				if (s != -1) {
					Log.i(TAG, "close success!");
					editText1.setText(s+"");
				}
			}
		});

		btn_init = (Button) findViewById(R.id.button_init);
		btn_init.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fd > 0) {
					byte[] dataBuf = PSAMhelperV2.ResetCard(fd,cardLocation, powerValue);
					if (dataBuf != null) {
						editText1.setText(StringUtility.ByteArrayToString(dataBuf,
								dataBuf.length));
					}
				}
			}
		});

		btn_random = (Button) findViewById(R.id.btn_random);
		btn_random.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fd > 0) {
					byte[] btRandom = new byte[] { (byte) 0x00, (byte) 0x84,
							(byte) 0x00, (byte) 0x00, (byte) 0x08 };
					byte[] dataBuf = PSAMhelperV2.CardApdu(fd, btRandom, btRandom.length);
					if (dataBuf != null) {
						editText1.setText(StringUtility.ByteArrayToString(dataBuf,
								dataBuf.length));
					}
				}
			}
		});

		button_send = (Button) findViewById(R.id.button_send);
		button_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fd > 0) {
					String hexString = editText_cmd.getText().toString();
					byte[] bt = StringUtility.StringToByteArray(hexString);
					byte[] dataBuf = PSAMhelperV2.CardApdu(fd, bt, bt.length);
					if (dataBuf != null) {
						editText1.setText(StringUtility.ByteArrayToString(dataBuf,
								dataBuf.length));
					}
				}
			}
		});

		radioGroupCard = (RadioGroup) findViewById(R.id.radioGroupCard);
		radioGroupCard
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton radiobutton = (RadioButton) findViewById(group
								.getCheckedRadioButtonId());
						cardLocation = Integer.parseInt(radiobutton
								.getTag().toString());
					}
				});
		radioGroupPower = (RadioGroup) findViewById(R.id.radioGroupPower);
		radioGroupPower.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton power = (RadioButton) findViewById(group.getCheckedRadioButtonId());
				powerValue = Integer.parseInt(power.getTag().toString());
			}
			
		});
	}

	@Override
	protected void onResume() {
		EmGpio.setGPIO(24, 1);
		//打开PSAM电源
		fd=PSAMhelperV2.Open();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		PSAMhelperV2.Close(fd);
		//关闭PSAM电源
		EmGpio.setGPIO(24, 0);
		super.onDestroy();
	}

	
}
