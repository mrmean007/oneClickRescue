package com.example.sqlandroid;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class UserHomeActivity extends Activity {
	Button b1, b2, b3, b4, checkOutBtn, cancelHelpBtn;
	GPSTracker gps;
	RetrieveData ret;
	double longitude, latitude;
	String username;
	String emergencyPhone;
	private SharedPreferences sharedPrefrences;
	CheckBox checkbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		
		checkbox = (CheckBox)findViewById(R.id.checkBox1);
		b1 = (Button)findViewById(R.id.b1);
		b2 = (Button)findViewById(R.id.b2);
		b3 = (Button)findViewById(R.id.b3);
		b4 = (Button)findViewById(R.id.b4);
		checkOutBtn = (Button)findViewById(R.id.b6);
		cancelHelpBtn = (Button)findViewById(R.id.b5);

		gps = new GPSTracker(UserHomeActivity.this);
		ret = new RetrieveData();
		sharedPrefrences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		emergencyPhone = sharedPrefrences.getString("Ephone","");
		username = sharedPrefrences.getString("name", "");
		cancelHelpBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_LONG).show();
				onHelpCancelButtonClicked(this);
			}
		});
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	 private void insertToDatabase(final String sendname, final String longitude, final String latitude, final String rescueType){
	 
		 class sendPostReqAsyncTask extends AsyncTask<String,Void,String>{

				@Override
				protected String doInBackground(String... arg0) {
					/*String argUserName = arg0[0];
					String argAdd = arg0[1];
					String argEmail = arg0[2];
					*/
				/*	String name = nameEt.getText().toString();
					String add = addressEt.getText().toString();
					String email = emailEt.getText().toString();
					String phone = phoneEt.getText().toString();
			        String fname = fNameEt.getText().toString();
			        String password = passwordEt.getText().toString();
			        String emerPhone = emergencyEt.getText().toString();
*/
	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                nameValuePairs.add(new BasicNameValuePair("username", sendname));
	                nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
	                nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
	                nameValuePairs.add(new BasicNameValuePair("rescueType", rescueType));
	                try {
	                    HttpClient httpClient = new DefaultHttpClient();
	                    HttpPost httpPost = new HttpPost(
	                            "http://innovatepp.com/rescue_app/sendLocation.php");
	                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	                    HttpResponse response = httpClient.execute(httpPost);

	                    HttpEntity entity = response.getEntity();


	                } catch (ClientProtocolException e) {

	                } catch (IOException e) {

	                }

					
					return "your request is recieved on server";
				}
				@Override
	            protected void onPostExecute(String result) {
	                super.onPostExecute(result);

	                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
	                //TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
	                //textViewResult.setText("Insertion ok");
	            }

	    		
	    	}
		 
	    	sendPostReqAsyncTask SendPostReqAsyncTask = new sendPostReqAsyncTask();
	        SendPostReqAsyncTask.execute(sendname, longitude, latitude, rescueType);
	    																																																																																																					}
	  

	 
	
	
	//////////////////////////////////////////////////////////////////////////////////
	public void onCheckoutButtonClicked(View v){
		
		Intent intent = new Intent(this, MainActivity.class);
		//if((sharedPrefrences.contains(MainActivity.MyPREFERENCES))){
		SharedPreferences.Editor sharedEditor = sharedPrefrences.edit();
		sharedEditor.clear();
		sharedEditor.commit();
		//}
		
		startActivity(intent);
		//// changes made here 
		finish();
	}

	public void onHelpButtonClicked(View v){
		ConnectionDetector detectConnection = new ConnectionDetector(this);
		String resType = "";
		switch(v.getId()){	// code for sending user data
		case R.id.b1:
		//	Toast.makeText(getApplicationContext(), "you clicked b1", Toast.LENGTH_SHORT).show();
			resType="Medical";
			break;
		case R.id.b2:

		//	Toast.makeText(getApplicationContext(), "you clicked b2", Toast.LENGTH_SHORT).show();
			resType="Fire";
			break;
		case R.id.b3:

		//	Toast.makeText(getApplicationContext(), "you clicked b3", Toast.LENGTH_SHORT).show();
			resType="Terrorist";
			break;
		case R.id.b4:

		//	Toast.makeText(getApplicationContext(), "you clicked b4", Toast.LENGTH_SHORT).show();
			resType="Police";
			break;
		}
		// write code here for sending location to server 
		
	
		
		if( gps.canGetLocation()){// && detectConnection.isConnectingToInternet()){
			  gps.getLocation();
			  latitude = gps.getLatitude();
			  longitude = gps.getLongitude();
			//double altitude = gps.getAltitude();
			
			 Toast.makeText(getApplicationContext(), "long:" + longitude + " lat: " + latitude, Toast.LENGTH_LONG).show();
			String longStr = Double.toString(longitude);
			String latStr = Double.toString(latitude);
			
			final SmsManager smsMgr = SmsManager.getDefault();
			String sep = "<sep>";
			
			if(latitude != 0 && longitude != 0){
				if(checkbox.isChecked()){
					smsMgr.sendTextMessage("03318422476", null,username + sep + latStr + sep + longStr + sep + resType,null,null);
				}else if(detectConnection.isConnectingToInternet())
					insertToDatabase(username,longStr,latStr,resType );
				else
				{
					gps.showSettingsAlert();
					//Toast.makeText(getApplicationContext(), "not working",Toast.LENGTH_LONG).show();
					//Toast.makeText(getApplicationContext(),"Enable Your Location",Toast.LENGTH_SHORT).show();
				}
			//Toast.makeText(getApplicationContext(), emergencyPhone, Toast.LENGTH_SHORT).show();
			
			smsMgr.sendTextMessage(emergencyPhone, null, "innovatepp.com/usermap.php?name=" + username + "&lat="+latStr
					+"&lon="+longStr+"&restype="+resType, null, null);
			}else
			{
				Toast.makeText(getApplicationContext(), "Wait for a while and try again", Toast.LENGTH_SHORT).show();
			}
		}
		
		
		
		Log.i("username in homeact", username);
		
		//Toast.makeText(getApplicationContext(), emergencyPhone, Toast.LENGTH_LONG).show();
		
		/*	String longStr = Double.toString(longitude);
			String latStr = Double.toString(latitude);
			
			insertToDatabase(username,longStr,latStr,resType );
			
			//Toast.makeText(getApplicationContext(), emergencyPhone, Toast.LENGTH_SHORT).show();
			SmsManager smsMgr = SmsManager.getDefault();
			smsMgr.sendTextMessage(emergencyPhone, null, "innovatepp.com/usermap.php?name=" + username + "&lat="+latStr
					+"&lon="+longStr+"&restype="+resType, null, null);*/
			
		//Toast.makeText(getApplicationContext(), "asking for help", Toast.LENGTH_SHORT).show();
	}
	
	public void onHelpCancelButtonClicked(OnClickListener onClickListener){
		//final String name = sharedPrefrences.getString("name", "");
		
		Toast.makeText(this, username + "! Please Wait...", Toast.LENGTH_SHORT).show();
	class cancelHelp extends AsyncTask<Void, Void, String>{

			
			@Override
			protected String doInBackground(Void... params) {
				  try {
	                    HttpClient httpClient = new DefaultHttpClient();
	                    HttpPost httpPost = new HttpPost(
	                            "http://innovatepp.com/rescue_app/update.php");
	                    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	                    nameValuePairs.add(new BasicNameValuePair("username", username));
	                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	                    HttpResponse response = httpClient.execute(httpPost);

	                    HttpEntity entity = response.getEntity();


	                } catch (ClientProtocolException e) {

	                } catch (IOException e) {

	                }
				return "ok";
			}
			@Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                
                if(result.equals("ok"))
                Toast.makeText(getApplicationContext(), "Request is canceled", Toast.LENGTH_LONG).show();
                
                //TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Insertion OK");
            }
		}
		cancelHelp cancel = new cancelHelp();
		cancel.execute();
		//Toast.makeText(this, "cancel button", Toast.LENGTH_SHORT).show();
	}
	
	
}
