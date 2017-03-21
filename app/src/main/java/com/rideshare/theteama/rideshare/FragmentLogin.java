package com.rideshare.theteama.rideshare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.util.*;

import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.lang.*;
//import com.rideshare.theteama.rideshare.PrefUtil;
import static android.R.attr.data;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created  on 03/10/17.
 */


public class FragmentLogin extends Fragment {



    Button login_Button, register_Button;
    EditText phoneNumber_EditText, password_EditText;
    CheckBox rememberMe_CheckBox;
    LoginButton FBlogin;
    CallbackManager callbackManager;





        //fb_login = (LoginButton) view.findViewById(R.id.fb_login);

        // If using in a fragment
        //
        // Other app specific specialization




    public Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");


            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));


            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"),
                    object.getString("gender"));

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    // Callback registration

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, null, false);




    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        phoneNumber_EditText = (EditText) view.findViewById(R.id.phNo_login_editText);
        password_EditText = (EditText) view.findViewById(R.id.pwd_login_editText);
        rememberMe_CheckBox = (CheckBox) view.findViewById(R.id.remember_login_checkBox);
        login_Button = (Button) view.findViewById(R.id.login_login_button);
        register_Button = (Button) view.findViewById(R.id.rgstr_login_button);
        FBlogin = (LoginButton) view.findViewById(R.id.fb_login);
        FBlogin.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        FBlogin.setFragment(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();







        FBlogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {


                String accessToken = loginResult.getAccessToken().getToken();
                FBlogin.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(),SOSActivity.class);
                startActivity(intent);
                getActivity().finish();

                // save accessToken to SharedPreference
                prefUtil.saveAccessToken(accessToken);



                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject,
                                                    GraphResponse response) {

                                // Getting FB User Data
                                Bundle facebookData = getFacebookData(jsonObject);


                            }


                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                Log.d(TAG, "Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Log.d(TAG, "Login attempt failed.");
                //  deleteAccessToken();
            }




        });












        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, String>() {

                    int responseCode;

                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            URL url = new URL(Rideshare.SERVER_URL + "/user/login/");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.connect();

                            Uri.Builder _data = new Uri.Builder().appendQueryParameter("phone", params[0]).appendQueryParameter("password", params[1]);
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                            writer.write(_data.build().getEncodedQuery());
                            writer.flush();
                            writer.close();

                            responseCode = connection.getResponseCode();

                            //neaw

                           InputStream stream = connection.getErrorStream();




                            StringBuilder result = new StringBuilder();
                            String line;
                            if (responseCode > 199 && responseCode < 300) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }
                                reader.close();
                                return result.toString();
                            } else {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }
                                reader.close();
                                return result.toString();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {

                        if (responseCode == 200) {
                            try {
                                JSONObject jsonObj = new JSONObject(s);
                           System.out.print(jsonObj.toString());
                                String results = jsonObj.getString("status");
                                String userId= jsonObj.getString("userId");
                                String displayName= jsonObj.getString("displayName");
                                HashMap<String, String> userInfo = new HashMap<>();
                                userInfo.put("USER_ID",userId);
                                userInfo.put("USER_NAME",displayName);
                                userInfo.put("PHONE",userId);
                              Log.d("results", results);
                                if (results.matches("loggedIn"))
                                {
                                    Rideshare.saveUser(getActivity().getApplicationContext(), userInfo);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getActivity(), SOSActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }, 20);

                                }
                            } catch (Exception ex) {
                            }
                        } else {
                            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute(phoneNumber_EditText.getText().toString(), password_EditText.getText().toString());
            }
        });
        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        HashMap<String, String> current = Rideshare.getUser(getActivity().getApplicationContext());
        if(current != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), SOSActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }, 20);
        }
    }


//    private class AuthAsync extends AsyncTask<Void, Void, Integer> {
//        String phno, pwd;
//
//        protected void onPreExecute() {
//            phno = phoneNumber_EditText.getText().toString();
//            pwd = password_EditText.getText().toString();
//        }
//
//        //http://namitbehl.net/hackathon/fetch_data.php?update_rider=true&riderID=8002144009&riderSource=ndkjfhnskjfdnsjkddnksJ&riderDestination=nnsfkjdnskjndskjandksjd1s53dsaa1
//        protected Integer doInBackground(Void... params) {
//            try {
//                URL url = new URL(Rideshare.SERVER_URL + "/citizen/login");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//                connection.setRequestMethod("POST");
//
//                Uri.Builder _data = new Uri.Builder().appendQueryParameter("phone", phno).appendQueryParameter("password", pwd);
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//                writer.write(_data.build().getEncodedQuery());
//                writer.flush();
//                writer.close();
//                String line;
//                String res = "";
//                String result = null;
//                InputStreamReader in = new InputStreamReader(connection.getInputStream());
//
//
//                StringBuilder jsonResults1 = new StringBuilder();
//                ArrayList<String> resultList = null;
//// Load the results into a StringBuilder
//                int read;
//                char[] buff = new char[1024];
//                while ((read = in.read(buff)) != -1) {
//                    jsonResults1.append(buff, 0, read);
//                }
//                connection.disconnect();
//                JSONObject jsonObj = new JSONObject(jsonResults1.toString());
//                System.out.print(jsonObj.toString());
//                String results = jsonObj.getString("status");
//                Log.d("results", results);
//                if (results.matches("loggedIn"))
//
//
//                {
//                    Intent intent = new Intent(getActivity(), SOSActivity.class);
//                    startActivity(intent);
//                }
////                Intent intent=new Intent(LoginActivity.this,SOSActivity.class);
////                startActivity(intent);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return 1;
//        }
//
//        protected void onPostExecute(Integer result) {
//        }
//    }
}
