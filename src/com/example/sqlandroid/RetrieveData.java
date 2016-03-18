package com.example.sqlandroid;
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

import android.util.Log;
import android.widget.TextView;

public class RetrieveData {
	
		
		// all made static
		TextView resultView;
		String result = "";
		static InputStream isr = null;
		static JSONObject json;
		static JSONArray jArray;
		static String username;
		static String EmergencyContact;
		
		void test(){
			Log.i("retrive record","its working");
		}
		public void getData(){
			try{
			 
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost( "http://innovatepp.com/rescue_app/newphp.php"); 
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			 
			isr = entity.getContent();
			//Toast.makeText(getApplicationContext(), "ip is picked", Toast.LENGTH_LONG).show(); 
			Log.i("getData()","ip is picked");
			}catch(Exception e){
			 
			Log.e("log_tag", "Error in http connection "+e.toString());
			resultView.setText("Couldnt connect to database, axception thrown");
			}

			//convert response to string
			 
			try{
			 
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
			 
			StringBuilder sb = new StringBuilder();
			 
			String line = null;
			 
			while ((line = reader.readLine()) != null) {
			 
			sb.append(line + "\n");
			 
			}
			 
			isr.close();
			 
			result=sb.toString();
			 
			}
			 
			catch(Exception e){
			 
			Log.e("log_tag", "Error  converting result "+e.toString());
			 
			}
			 
			//parse json data
			 
			try {
			 
			String s = "";
			 
			jArray = new JSONArray(result);
			
			} catch (Exception e) {
			Log.e("log_tag", "Error Parsing Data "+e.toString());
			}
			}////// end of get data function
			
public boolean search(String name, String pass){
				boolean boolResult = false;
				try{
				for(int i=0; i<jArray.length();i++){
					json = jArray.getJSONObject(i);
					
					if( name.equals(json.getString("name")) ){
						Log.i("search-log","name is found"); 
						
						if( pass.equals(json.getString("password")) ){
							Log.i("search-log","password match");
							boolResult = true; 
							EmergencyContact = json.getString("emergencyPhone");
							Log.i("ePhone: ",EmergencyContact);
							username = json.getString("name");
							Log.i("username:" ,username);
							break; 
													
						}
							else
								Log.i("search-log","password not found");
						
					}// end of outer if
					else{
						Log.i("search-log","name is not found");	}//end of else
					
				}// end of for
				}catch(Exception e){ e.printStackTrace(); }
				return boolResult;
			}
			String getname(){
				return username;
			}
			String getEmergencyPhone(){
				return EmergencyContact;
			}

	}


