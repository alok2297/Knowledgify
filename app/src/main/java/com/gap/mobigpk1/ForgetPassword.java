package com.gap.mobigpk1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText etEmailForgotPassword;
    Button btReset;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        getSupportActionBar().hide();

        etEmailForgotPassword = findViewById(R.id.etEmailForgotPassword);
        btReset = findViewById(R.id.btReset);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");
        progressDialog.setCancelable(false);

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(!etEmailForgotPassword.getText().toString().trim().isEmpty()) {
                    auth.sendPasswordResetEmail(etEmailForgotPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                                        builder.setCancelable(false);
                                        builder.setTitle("Reset Password");
                                        builder.setMessage("An e-mail regarding resetting password has been sent.\nCheck your e-mail");
                                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        }).create();

                                        builder.show();
                                    }
                                    else{
                                        Toast.makeText(ForgetPassword.this, "Please enter registered email!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
                else{
                    progressDialog.dismiss();
                    etEmailForgotPassword.setError("Empty!");
                    etEmailForgotPassword.requestFocus();
                }
            }
        });
    }
}