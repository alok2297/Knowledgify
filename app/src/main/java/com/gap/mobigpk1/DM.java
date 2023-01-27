package com.gap.mobigpk1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.ChatModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DM extends AppCompatActivity {

    ImageView ivBack;
    TextView tvOtherName;
    EditText etMessage;
    ImageView fab;

    RecyclerView recyclerViewMessage;

    String username, othername, uid, ouid;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    ChatAdapter adapter;
    List<ChatModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm);

        ivBack = findViewById(R.id.ivOtherProfile);
        tvOtherName = findViewById(R.id.tvOtherName);
        etMessage = findViewById(R.id.etMessage);
        fab = findViewById(R.id.fab);
        recyclerViewMessage = findViewById(R.id.userRecyclerView);

        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        username = getIntent().getStringExtra("username");
        othername = getIntent().getStringExtra("othername");
        uid = getIntent().getStringExtra("uid");
        ouid = getIntent().getStringExtra("ouid");
            getMessage();
        tvOtherName.setText(othername);



        ivBack.setOnClickListener(view -> {
            Intent intent = new Intent(DM.this, Messaging.class);
            startActivity(intent);
            finish();
        });

        fab.setOnClickListener(view -> {
            if (!etMessage.equals("")){
                sendMessage(etMessage.getText().toString());
                etMessage.setText("");
            }
        });
    }

    private void getMessage() {
        reference.child("Chats").child(uid).child(username).child(othername)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ChatModel modelClass = snapshot.getValue(ChatModel.class);
                        list.add(modelClass);
                        adapter.notifyDataSetChanged();
                        recyclerViewMessage.scrollToPosition(list.size() - 1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        adapter = new ChatAdapter(list, username);
        recyclerViewMessage.setAdapter(adapter);


    }

    void sendMessage(String message){

        final String key = reference.child("Chats").child(uid).child(username).child(othername).push().getKey();
        final Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("from", username);
        reference.child("Chats").child(uid).child(username).child(othername).child(key).setValue(messageMap)
                .addOnCompleteListener(DM.this, task -> {
                    if(task.isSuccessful()){
                        reference.child("Chats").child(ouid).child(othername).child(username).child(key).setValue(messageMap);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}