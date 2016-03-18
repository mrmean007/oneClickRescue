package com.example.sqlandroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CreateAccountActivity extends Activity {

	Button createBtn;
	EditText nameEt, fNameEt, phoneEt, addressEt, passwordEt, emailEt, emergencyEt;
	ValidateRecords valRecords;
	Intent intent;
	ConnectionDetector connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		createBtn = (Button)findViewById(R.id.createbutton);
		
		connection = new ConnectionDetector(this);
		valRecords = new ValidateRecords();
		nameEt = (EditText)findViewById(R.id.usernameEt);
		fNameEt = (EditText)findViewById(R.id.fathernameEt);
		phoneEt = (EditText)findViewById(R.id.phonnoEt);
		addressEt = (EditText)findViewById(R.id.addressEt);
		passwordEt = (EditText)findViewById(R.id.passwordEt);
		emailEt = (EditText)findViewById(R.id.EmailEt);
		emergencyEt = (EditText)findViewById(R.id.phon2Et);
		 
	    }

	    
	    public void insert(View view){
	    	
	    	String name = nameEt.getText().toString();
	        String fname = fNameEt.getText().toString();
	        String add = addressEt.getText().toString();
	        String email = emailEt.getText().toString();
	        String phone = phoneEt.getText().toString();
	        String password = passwordEt.getText().toString();
	        String emerPhone = emergencyEt.getText().toString();
	     	
	       
	    	if( !(valRecords.validateName(name) && valRecords.validateName(fname) &&
	    			valRecords.validatePhone(phone) && valRecords.validatePhone(emerPhone) &&
	    			valRecords.validateAddress(add) && valRecords.validateEmail(email) &&
	    			valRecords.validatePassword(password)) ){
	    		Toast.makeText(this, "All Fields Must Have Valid Values", Toast.LENGTH_LONG).show();
	    		
	    	}else{
	    	
	    	if(connection.isConnectingToInternet()){
	    		
	    	Toast.makeText(this, "Please Wait...", Toast.LENGTH_SHORT).show();	
	        insertToDatabase(name,fname,password,add,email, phone,emerPhone);
	        
	       
	        intent = new Intent(this,MainActivity.class);
	        intent.putExtra("name", name);
	        intent.putExtra("password", password);
	       
	       
	        }
	    	else
	    	{
	    		GPSTracker obj = new GPSTracker(CreateAccountActivity.this);
	    		obj.showSettingsAlert();
	    		//Toast.makeText(this, "enable your internet", Toast.LENGTH_SHORT).show();
	    	}
	    	}
	    }
	    
	    private void insertToDatabase(final String name, final String fname, final String password, final String add, final String email, final String phone, final String emerPhone){
	    	
	    	final ProgressDialog progressDialog = new ProgressDialog(this);
	    	progressDialog.setMessage("Please wait...");
	    	progressDialog.show();
	    	
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
	                nameValuePairs.add(new BasicNameValuePair("name", name));
	                nameValuePairs.add(new BasicNameValuePair("fname", fname));
	                nameValuePairs.add(new BasicNameValuePair("password", password));
	                nameValuePairs.add(new BasicNameValuePair("address", add));
	                nameValuePairs.add(new BasicNameValuePair("email", email));
	                nameValuePairs.add(new BasicNameValuePair("phone", phone));
	                nameValuePairs.add(new BasicNameValuePair("emerPhone",emerPhone));
	                try {
	                    HttpClient httpClient = new DefaultHttpClient();
	                    HttpPost httpPost = new HttpPost(
	                            "http://innovatepp.com/rescue_app/insert-db.php");
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
					
					progressDialog.dismiss();
					
	                super.onPostExecute(result);
	                
	                if(result.equals("ok"))
	                Toast.makeText(getApplicationContext(), "Your Account Has Been Created", Toast.LENGTH_LONG).show();
	                else
	                	Toast.makeText(getApplicationContext(),"Something Is Wrong With Your Connectivity.", Toast.LENGTH_SHORT).show();
	       
	                
	                	startActivity(intent);
	                	finish();
	                	//TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
	                //textViewResult.setText("Insertion ok");
	            }

	    		
	    	}
	    	sendPostReqAsyncTask SendPostReqAsyncTask = new sendPostReqAsyncTask();
	        SendPostReqAsyncTask.execute(name, fname, password, add, email, phone, emerPhone);
	    }
	    
}
