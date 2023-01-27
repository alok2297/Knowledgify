package com.gap.mobigpk1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class nNotification extends AppCompatActivity {

    public static final String CHANNEL_ID = "Knowledgify";
    public static final String CHANNEL_NAME = "Knowledgify Coding";
    public static final String CHANNEL_DESC = "Knowledgify Coding Notifications";

    private RequestQueue mRequestQueue;
    private String URL="https://fcm.googleapis.com/fcm/send";
    private String token,body,postKey;
    private String image="https://firebasestorage.googleapis.com/v0/b/gurukul-9d4c9.appspot.com/o/ic_launcher.png?alt=media&token=3e713402-b25f-4f29-ac75-53de733fbf41";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nnotification);

        FirebaseMessaging.getInstance().subscribeToTopic("updating");
        Button btn=findViewById(R.id.btn);
        mRequestQueue= Volley.newRequestQueue(this);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        body = intent.getStringExtra("body");
        postKey=intent.getStringExtra("postKey");
        if(getIntent().hasExtra("image"))
            image = intent.getStringExtra("image");


        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        sendNotification(token,body,postKey,image);



    }

    private void sendNotification(String token,String body,String postKey,String image) {
        JSONObject json=new JSONObject();
//        Toast.makeText(this, image, Toast.LENGTH_SHORT).show();
        try {
            json.put("to",token);
            JSONObject notificationObj=new JSONObject();
            notificationObj.put("title","Knowledgify");
            notificationObj.put("body",body);
            notificationObj.put("image", Uri.parse(image));
            
            JSONObject extraData=new JSONObject();
            extraData.put("category","Notification");
//            extraData.put("target",postKey);
            
            json.put("notification",notificationObj);
            json.put("data",extraData);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization",getString(R.string.notification));
                    return header;
                }
            };
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }


}