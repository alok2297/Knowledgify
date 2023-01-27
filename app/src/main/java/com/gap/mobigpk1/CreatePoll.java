package com.gap.mobigpk1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.Options;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CreatePoll extends AppCompatActivity   {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("posts").child(user.getUid()).child("posts_created").child("polls").push();

    Button btAddOption;
    EditText etOption,etQuestion;
    FloatingActionButton post;
    TextView optLimit;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter myAdapter;
    ArrayList<Options> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        btAddOption = findViewById(R.id.btAddOption);
        etOption = findViewById(R.id.etOption);
        etQuestion = findViewById(R.id.etQuestion);
        recyclerView = findViewById(R.id.recyclerView);
        post = findViewById(R.id.fab);
        options = new ArrayList<>();
        optLimit = findViewById(R.id.optLim);

        post.setVisibility(View.INVISIBLE);

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(40);
        etOption.setFilters(inputFilters);


        etOption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                optLimit.setText(charSequence.length()+"/40");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btAddOption.setOnClickListener(view -> {
            if(!etOption.getText().toString().isEmpty()) {
                String opDes = etOption.getText().toString();
                etOption.getText().clear();

                if(options.size()>=4){
                    Toast.makeText(this, "Max Option limit reached", Toast.LENGTH_SHORT).show();
                }
                else {
                    options.add(new Options(opDes));
                    myAdapter.notifyDataSetChanged();
                }
                if(options.size() == 2){
                    post.setVisibility(View.VISIBLE);
//                    post.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6200EE")));
                }

            }
            else {
                etOption.requestFocus();
                etOption.setError("Please enter an option!");
            }
        });



        layoutManager = new LinearLayoutManager(this);
        myAdapter = new OptionsAdapter(options, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);

        post.setOnClickListener(view -> {
            if(options.size() >= 2) {
                if (etQuestion.getText().toString().isEmpty()) {
                    etQuestion.requestFocus();
                    etQuestion.setError("Please poll question");

                } else {
                    Intent intent = new Intent(CreatePoll.this, AddPost.class);
                    String arrayAsString = new Gson().toJson(options);
                    intent.putExtra("question", etQuestion.getText().toString().trim());
                    intent.putExtra("options", arrayAsString);
                    intent.putExtra("pollCreated", true);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Toast.makeText(CreatePoll.this, "Create more than two options!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}