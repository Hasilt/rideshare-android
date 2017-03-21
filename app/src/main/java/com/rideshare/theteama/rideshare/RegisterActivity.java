package com.rideshare.theteama.rideshare;

import android.content.Intent;
import android.net.Uri;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;

//import org.json.JSONException;
import org.json.JSONException;
import org.json.JSONObject;


import com.facebook.FacebookSdk;

import java.io.BufferedWriter;
//import java.io.IOException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import io.socket.client.Url;


public class RegisterActivity extends AppCompatActivity {


/*    public class RegisterAsync extends AsyncTask<Void, Void, Integer> {
        String phno, pwd, name, email;

        protected void onPreExecute() {
            phno = phNo_EditText.getText().toString();
            pwd = password_EditText.getText().toString();
            name = name_EditText.getText().toString();
            email = email_EditText.getText().toString();
        }



        protected Integer doInBackground(Void... params) {
            try {

                URL url = new URL(Rideshare.SERVER_URL + "/user/signup");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);


                Uri.Builder _data = new Uri.Builder().appendQueryParameter("displayName", name).appendQueryParameter("phone", phno).appendQueryParameter("email", email).
                        appendQueryParameter("password", pwd);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                writer.write(_data.build().getEncodedQuery());
                writer.flush();
                writer.close();
                String line;
                String res = "";
                String result = null;
                InputStreamReader in = new InputStreamReader(connection.getInputStream());


                StringBuilder jsonResults1 = new StringBuilder();
                ArrayList<String> resultList = null;

// Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults1.append(buff, 0, read);
                }
                connection.disconnect();
                JSONObject jsonObj = new JSONObject(jsonResults1.toString());
                System.out.print(jsonObj.toString());
                String results = jsonObj.getString("status");
                Log.d("results", results);
                if (results.matches("loggedIn")) {
                    Intent intent = new Intent(RegisterActivity.this, SOSActivity.class);
                    startActivity(intent);
                }
//                Intent intent=new Intent(LoginActivity.this,SOSActivity.class);
//                startActivity(intent);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 1;
        }

        protected void onPostExecute(Integer result) {

        }

  */

    Button register_Button, clear_Button;
    EditText phNo_EditText, name_EditText, password_EditText, rePassword_EditText, email_EditText;
   // String emailtxt,passwordtxt;
    //List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_Button = (Button) findViewById(R.id.regstr_register_Button);
        clear_Button = (Button) findViewById(R.id.clear_register_Button);
        phNo_EditText = (EditText) findViewById(R.id.phNo_register_EditText);
        name_EditText = (EditText) findViewById(R.id.name_register_EditText);
        email_EditText = (EditText) findViewById(R.id.email_register_EditText);
        password_EditText = (EditText) findViewById(R.id.password_register_EditText);
        rePassword_EditText = (EditText) findViewById(R.id.rePassword_register_EditText);

        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

              // RegisterAsync registerAsync = new RegisterAsync();

               //new RegisterAsync().execute();

                String phno, pwd, name, email;
                phno = phNo_EditText.getText().toString();
                pwd = password_EditText.getText().toString();
                name = name_EditText.getText().toString();
                email = email_EditText.getText().toString();

try {
    URL url = new URL(Rideshare.SERVER_URL + "/user/signup");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestMethod("POST");


    Uri.Builder _data = new Uri.Builder().appendQueryParameter("displayName", name).appendQueryParameter("phone", phno).appendQueryParameter("email", email).
            appendQueryParameter("password", pwd);
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
    writer.write(_data.build().getEncodedQuery());
    writer.flush();
    writer.close();
    String line;
    String res = "";
    String result = null;
    InputStreamReader in = new InputStreamReader(connection.getInputStream());


    StringBuilder jsonResults1 = new StringBuilder();
    ArrayList<String> resultList = null;

// Load the results into a StringBuilder
    int read;
    char[] buff = new char[1024];
    while ((read = in.read(buff)) != -1) {
        jsonResults1.append(buff, 0, read);
    }
    connection.disconnect();
    JSONObject jsonObj = new JSONObject(jsonResults1.toString());
    System.out.print(jsonObj.toString());
    String results = jsonObj.getString("status");
    Log.d("results", results);
    if (results.matches("loggedIn")) {
        Intent intent = new Intent(RegisterActivity.this, SOSActivity.class);
        startActivity(intent);
    }
}catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();

    } catch (JSONException e) {
        e.printStackTrace();
    }

            }
        });

    }

    }



