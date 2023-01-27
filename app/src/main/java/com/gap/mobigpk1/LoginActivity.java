package com.gap.mobigpk1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btSignIn;
    SignInButton btGoogle;
    TextView tvSignUp;

    public static final String FILENAME = "com.gap.mobigpk1.User_Details";

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    SharedPreferences sharedPreferences;

    GoogleSignInClient client;
    private TextView tvForgetPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.inputEmail);
        etPassword = findViewById(R.id.InputPassword);
        btSignIn = findViewById(R.id.button_login);
        btGoogle = findViewById(R.id.signInButton);
        tvSignUp = findViewById(R.id.newUser);
        tvForgetPass=findViewById(R.id.forgotpass);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");
        progressDialog.setCancelable(false);

        /*
        google signIn
        forgotPass
         */
        getSupportActionBar().hide();


        sharedPreferences= getSharedPreferences(FILENAME, MODE_PRIVATE);

        btSignIn.setOnClickListener(view -> {

            progressDialog.show();
            if(!(etEmail.getText().toString().trim().isEmpty())) {
                if(!(etPassword.getText().toString().trim().isEmpty())){
                    auth.signInWithEmailAndPassword(etEmail.getText().toString().trim(), etPassword.getText().toString().trim())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(this, "Incorrect Credentials! Try Again!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                }
                else{
                    progressDialog.dismiss();
                    etPassword.requestFocus();
                    etPassword.setError("Enter Password!");
                }
            }else{
                progressDialog.dismiss();
                etEmail.requestFocus();
                etEmail.setError("Enter email!");
            }

        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });


        btGoogle.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            client = GoogleSignIn.getClient(LoginActivity.this, gso);

            Intent intent = client.getSignInIntent();

            startActivityForResult(intent, 1);
        });

        tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccount.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String name = account.getDisplayName();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(LoginActivity.this, task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Sign In successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", name);
                                editor.commit();
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Sign In failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user != null){
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra("fromLogin", true);
            startActivity(intent);
            finish();
        }
    }
}