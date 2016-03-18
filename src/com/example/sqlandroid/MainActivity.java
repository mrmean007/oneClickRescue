package com.example.sqlandroid;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.GINGERBREAD) 

public class MainActivity extends Activity {

	TextView resultView;
	String result = "";
	InputStream isr = null;
	JSONObject json;
	JSONArray jArray;
	ConnectionDetector connectionDtector;
	ProgressDialog progressDialog;
	
	EditText nameEt, passwordEt;
	Button checkinBtn, createBtn;
	TextView text;
	RetrieveData retRecords;
	boolean isInternet;
	Intent intent;
	
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	 
	StrictMode.enableDefaults(); 
	GPSTracker gps = new GPSTracker(MainActivity.this);
	
	progressDialog = new ProgressDialog(this);
	progressDialog.setMessage("Please Wait ...");
	progressDialog.setCanceledOnTouchOutside(false);
	progressDialog.setCancelable(false);
	
	nameEt = (EditText)findViewById(R.id.usernameEt);
	passwordEt = (EditText)findViewById(R.id.passwordEt);
	checkinBtn = (Button)findViewById(R.id.loginbtn);
	createBtn = (Button)findViewById(R.id.signupbtn);
	intent = new Intent(getApplicationContext(),UserHomeActivity.class);
	//text = (TextView)findViewById(R.id.textView3);	 
	retRecords = new RetrieveData();
	sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
	
	String name = sharedpreferences.getString("name", "");
	
	if(!(name.equals(""))){
		//String name = sharedpreferences.getString("name", "");
		Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this,UserHomeActivity.class);
		startActivity(intent);
		finish();
		
		
		/*SharedPreferences.Editor edit = sharedpreferences.edit();
		edit.clear();
		edit.commit();*/
	}
	
	connectionDtector = new ConnectionDetector(getApplicationContext());
	isInternet = false;
	/*Intent gIntent = getIntent();
	
	String n = gIntent.getStringExtra("name");
	String p = gIntent.getStringExtra("password");
	
	nameEt.setText(n);
	passwordEt.setText(p);
	*/
	}
	
	public void login(View v){
		///////////////////////////////////// thread is added here
	//	final Context context = getApplicationContext();
		
	//	final ProgressBar pr = (ProgressBar)findViewById(R.id.progressBar1);
		//pr.setVisibility(1);
		progressDialog.show();
		
	class asynkTask extends AsyncTask<Void, Void, String>{

		String result = "";
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				isInternet = connectionDtector.isConnectingToInternet();
				if(!isInternet){
					GPSTracker tracker = new GPSTracker(getApplicationContext());
					tracker.showSettingsAlert();
					//Toast.makeText(this, "Enable you internet connection", Toast.LENGTH_LONG).show();
				}
				else {
				String username = nameEt.getText().toString();
				String password = passwordEt.getText().toString();
				boolean searchResult = false;
				
				//Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show();
				retRecords.getData();	
				searchResult = retRecords.search(username, password);
				
				
				if( searchResult ){
					Intent i = new Intent(getApplicationContext(),UserHomeActivity.class);
				
					if(!(sharedpreferences.contains(MyPREFERENCES))){
					SharedPreferences.Editor sharedEditor = sharedpreferences.edit();
					sharedEditor.putString("name", username);
					//sharedEditor.putString("password", password);
					sharedEditor.putString("Ephone",retRecords.getEmergencyPhone());
					
					sharedEditor.commit();
				
					}
					username="";
					password="";
					result =  "hit";
				}
				else
					result =  "miss";
					//Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
				}
				}catch(Exception e){
					//Toast.makeText(getApplicationContext(), "something is wrong",Toast.LENGTH_SHORT).show();
				}
			//Toast.makeText(MainActivity.this, "loging in", Toast.LENGTH_SHORT).show();
		//	TextView txt = (TextView)findViewById(R.id.textView3);
		//	txt.setText("logging in ");
			Log.i("login button click","login button is pressed");
			return result;
		}
		
		 protected void onPostExecute(String result) {
			// pr.setVisibility(-1);
			 super.onPostExecute(result);
			 progressDialog.dismiss();
            
             if(result.equals("hit"))
             {
            	 startActivity(intent);
            	 finish();
             }else
            	 Toast.makeText(getApplicationContext(), "invalid username or password", Toast.LENGTH_SHORT).show();
             
         }
		}
	asynkTask processLogin = new asynkTask();
	processLogin.execute();
			
	
		
	}
	
	public void createAccount(View v){
		Intent intent = new Intent(this,CreateAccountActivity.class);
		startActivity(intent);
//		finish();
	}
	 
	
	
}