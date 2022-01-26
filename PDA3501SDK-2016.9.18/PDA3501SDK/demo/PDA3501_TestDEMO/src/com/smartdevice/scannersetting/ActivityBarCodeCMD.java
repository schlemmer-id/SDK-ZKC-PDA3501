/** 
 *  
 * @author	xuxl
 * @version  
 *     1.0 2016年7月21日 下午5:04:09 
 */ 
package com.smartdevice.scannersetting;

import com.smartdevice.testd.ScannerActivity;
import com.smartdevice.testd3501.R;
import com.smartdevicesdk.utils.StringUtility;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/** 
 * This class is used for : 
 *  
 * @author	xuxl
 * @version  
 *     1.0 2016年7月21日 下午5:04:09 
 */
public class ActivityBarCodeCMD extends Activity {
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_cmd);
		
		textView=(TextView)findViewById(R.id.textView_info);
		
		Button button_send=(Button)findViewById(R.id.button_send);
		button_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText editText_cmd=(EditText)findViewById(R.id.editText_cmd);
				
				String str=editText_cmd.getText().toString();
				byte[] bt=StringUtility.StringToByteArray(str);
				if(bt!=null&&bt.length>0){
					byte[] btRec= ScannerActivity.scanner.sendCommand(bt);
					String hexString=StringUtility.ByteArrayToString(btRec, btRec.length);
					String textString=new String(btRec);
					
					textView.setText("HEX:"+hexString+"\r\nTEXT:"+textString);
				}
			}
		});
	}
}
