package com.gap.mobigpk1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccount extends AppCompatActivity {

    EditText etEmail, etPassword, etConfirmPass;
    Button btCreate;
    TextView tvWarn;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    private String T_C;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        TextView checkboxTxt = findViewById(R.id.textView6);
        

        CheckBox check=findViewById(R.id.checkBox);
        etEmail = findViewById(R.id.etEmailCreate);
        etPassword = findViewById(R.id.etPasswordCreate);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        btCreate = findViewById(R.id.btCreate);
        TextView Already_account = findViewById(R.id.Already_account);
        tvWarn = findViewById(R.id.tvWarn);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        tvWarn.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();
        /*
        handle if details are not filled in prfileactivity and erased from bg..then crashes
         */

        reference.child("TandC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T_C=snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkboxTxt.setText(Html.fromHtml(String.format(getString(R.string.terms))));
        checkboxTxt.setOnClickListener(view -> {
            Intent i=new Intent(this,WebViewNV.class);
            i.putExtra("title","Terms & Conditions");
            i.putExtra("url",T_C);
            startActivity(i);
        });

        Already_account.setOnClickListener(v -> finish());
        btCreate.setOnClickListener(view -> {
            etEmail.clearFocus();
            etPassword.clearFocus();
            etConfirmPass.clearFocus();
            progressDialog.show();
            if(!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() && !etConfirmPass.getText().toString().isEmpty()){
                if(check.isChecked()) {
                    if (etPassword.getText().toString().equals(etConfirmPass.getText().toString())) {
                        tvWarn.setVisibility(View.INVISIBLE);
                        auth.createUserWithEmailAndPassword(etEmail.getText().toString().trim(), etPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {

                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                                Toast.makeText(CreateAccount.this, "Account already exist!", Toast.LENGTH_SHORT).show();
                                            } catch (FirebaseAuthWeakPasswordException authWeakPasswordException) {
                                                Toast.makeText(CreateAccount.this, "Weak Password!", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Toast.makeText(CreateAccount.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                            progressDialog.dismiss();
                                        } else {
                                            Toast.makeText(CreateAccount.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CreateAccount.this, ProfileActivity.class);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    }
                                });
//                            .addOnSuccessListener(authResult -> {
//
//                                Toast.makeText(CreateAccount.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(CreateAccount.this, ProfileActivity.class);
//                                startActivity(intent);
//                                finish();
//                            });
                    } else {
                        progressDialog.dismiss();
                        tvWarn.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(this, "Please accept Terms and Conditions!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(CreateAccount.this, "Please fill every field!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}