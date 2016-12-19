package com.example.soka.loginme;

import android.os.AsyncTask;

/**
 * Created by soka on 18/12/16.
 */

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class APIClient extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "APIClient";

    public APIClient() {
        super(APIClient.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String mode = intent.getStringExtra("mode");
        String url = intent.getStringExtra("url");
        String email = intent.getStringExtra("email");
        String password = sha1(intent.getStringExtra("password")).toLowerCase();

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            if (mode.equals("login")) {
                try {
                    String results = login(url, email, password);
                    bundle.putString("resp", results);
                    receiver.send(STATUS_FINISHED, bundle);
                } catch (Exception e) {

                    /* Sending error message back to activity */
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_ERROR, bundle);
                }
            }
            else {
                try {
                    ArrayList<JSONObject> users = get_users(url);
                    if (users != null)
                        bundle.putString("resp", users.toString());
                    else
                        bundle.putString("resp", "error");
                }
                catch (Exception e) {
                    /* Sending error message back to activity */
                    bundle.putString(Intent.EXTRA_TEXT, e.toString());
                    receiver.send(STATUS_ERROR, bundle);
                }
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String login(String requestUrl, String email, String password) throws IOException, DownloadException {
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        URL url = new URL(requestUrl+"/api/generate-token/"+email+"/"+password);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(3000);



        try {
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            Log.d("APIClient", "Code: " + Integer.toString(statusCode));
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                String response = convertInputStreamToString(inputStream);


                Log.d("APIClient", "Rcv: " + response);
                try {
                    JSONObject resp = new JSONObject(response);
                    Boolean loginResult = resp.getBoolean("result");
                    if (loginResult) {
                        return "ok";
                    }
                    Log.d("APICLIENT", "Not ok: " + response);
                    return "INVALID_CRED";
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                return "Error with request";
            } else {
                throw new DownloadException("Incorrect Resp Code !");
            }
        }
        catch (UnknownHostException e) {
            return "NO_INTERNET";
        }


    }

    private ArrayList<JSONObject> get_users(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        URL url = new URL(requestUrl + "/api/show-users");

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(3000);

        urlConnection.connect();
        int statusCode = urlConnection.getResponseCode();
        Log.d("APIClient", "Code: " + Integer.toString(statusCode));
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response = convertInputStreamToString(inputStream);


            Log.d("APIClient", "Rcv: " + response);
            try {
                JSONObject users = new JSONObject(response);
                JSONObject user1 = users.getJSONObject("1");
                JSONObject user2 = users.getJSONObject("2");
                ArrayList<JSONObject> ret = new ArrayList<JSONObject>();
                ret.add(user1);
                ret.add(user2);
                return ret;

            }catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new DownloadException("Incorrect Resp Code !");
        }
        return null;



    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }



    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String sha1(String str) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte result[] = digest.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();

            for (byte b : result) {
                sb.append(String.format("%02X", b));
            }

            return sb.toString();

        }catch (NoSuchAlgorithmException e) {
            Log.e("digest", Log.getStackTraceString(e));
        }
        catch (UnsupportedEncodingException e) {
            Log.e("encoding", Log.getStackTraceString(e));
        }

        return "";
    }
}