package com.gap.mobigpk1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    private final int REQUEST_CODE=11;
    public Dialog progressDialog;
     Button btn,btn2;
    SharedPreferences sharedPreferences,firsttime;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ConstraintLayout  editInfo;
    BottomNavigationView btnNav;

    public static final String FILENAME = "com.gap.mobigpk1.User_Details";

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private ReviewManager reviewManager;
    private String T_C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//          sharedPreferences = getSharedPreferences("FIRSTRUN", MODE_PRIVATE);
        firsttime = getSharedPreferences("firsttime", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);
        btnNav=findViewById(R.id.btn_nav);
        reviewManager = ReviewManagerFactory.create(MainActivity.this);

        if(getIntent().hasExtra("targetFragment") && getIntent().getStringExtra("targetFragment").equals("Notification")){
            fragment=new Notifications();
            btnNav.setSelectedItemId(R.id.notifications);
        }
        else{
            fragment=new Book();
            btnNav.setSelectedItemId(R.id.books);
        }

        reference.child("TandC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                T_C=snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.OPEN,R.string.CLOSE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Yodo1Mas.getInstance().init(this, "fBKm8gUEIO", new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {
            }
        });

        if(!isConnected())
        {
            progressDialog=new Dialog(MainActivity.this);
            progressDialog.setContentView(R.layout.dialog_layout);
            progressDialog.setCancelable(false);
            btn=progressDialog.findViewById(R.id.retry);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isConnected())
                        progressDialog.dismiss();
                    else
                        Toast.makeText(MainActivity.this,"No Internet Access",Toast.LENGTH_SHORT).show();
                }
            });
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            progressDialog.show();
        }

//        if(first()){
//            SharedPreferences.Editor edit = firsttime.edit();
//            edit.putBoolean("firstrun", false);
//            edit.commit();
//            progressDialog=new Dialog(MainActivity.this);
//            progressDialog.setContentView(R.layout.warning);
//            progressDialog.setCancelable(false);
//            btn=progressDialog.findViewById(R.id.okay);
//          //  language();
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    progressDialog.dismiss();
//                }
//            });
//            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            progressDialog.show();
//        }

        AppUpdateManager appUpdateManager= AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask= appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if(result.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,MainActivity.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
            }
        });



        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();



        btnNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.home) {
                    fragment = new Home();

                }
                if(id==R.id.books)
                    fragment = new Book();
                if(id==R.id.add_post) {
                    Intent i=new Intent(MainActivity.this,AddPost.class);
                    startActivity(i);
                    return true;
                }
                if(id==R.id.notifications)
                    fragment=new Notifications();

                if(id==R.id.settings){
                    Intent i=new Intent(MainActivity.this,Profile.class);
                    i.putExtra("userId",user.getUid());
                    startActivity(i);
                    return true;
                }


                getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
                return true;
            }

        });

        View header = navigationView.getHeaderView(0);
        editInfo = header.findViewById(R.id.editInfo);
        ImageView editProfile = header.findViewById(R.id.editProfile);
        ImageView profilePic = header.findViewById(R.id.profile);
        TextView tvName = header.findViewById(R.id.teacherName);
        TextView tvEmail = header.findViewById(R.id.email);
        reference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = user.getEmail();
                String pic=snapshot.child("profilePic").getValue().toString();
                Glide.with(MainActivity.this).load(pic).into(profilePic);
                tvEmail.setText(email);
                tvName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editInfo.setOnClickListener(view -> startActivity(new Intent(this, ProfileActivity.class)));
        editProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.nav_about) {
                    Intent i=new Intent(MainActivity.this, About.class);
                    startActivity(i);
                }

                if(id== R.id.nav_share){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String Share = "Download GURUKUL POLYTECHNIC Now :- https://play.google.com/store/apps/details?id=com.gap.mobigpk1&hl=en";
                    String sub = "GURUKUL POLYTECHNIC";
                    intent.putExtra(Intent.EXTRA_SUBJECT, sub);
                    intent.putExtra(Intent.EXTRA_TEXT, Share);
                    startActivity(Intent.createChooser(intent, "Share Using"));

                }
                if(id==R.id.nav_review){
                    showRateApp();
                }

                if(id==R.id.nav_dev){
                    Intent i=new Intent(Intent.ACTION_SENDTO);
                    i.setData(Uri.parse("mailto:"+ Uri.encode("suport.knowledgify@gmail.com")));
                    startActivity(Intent.createChooser(i,"Send Email"));
                }

                if(id==R.id.nav_tc){
                    Intent i=new Intent(MainActivity.this,WebViewNV.class);
                    i.putExtra("title","Terms & Conditions");
                    i.putExtra("url",T_C);
                    startActivity(i);
//                    Toast.makeText(MainActivity.this, "new Activity acc. to suggestion", Toast.LENGTH_SHORT).show();
                }

                if(id == R.id.logOut){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("profileCompleted", false);
                    editor.commit();
                    auth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    public void showRateApp() {
        Task <ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();

                        Task <Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                        flow.addOnCompleteListener(task1 -> {

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else{
                        Toast.makeText(this, "Cannot open", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.hamburger,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
//        if(id==R.id.dm){
//            Intent i=new Intent(this, Messaging.class);
//            startActivity(i);
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            Toast.makeText(this,"Start Download",Toast.LENGTH_SHORT).show();
            if(resultCode!=RESULT_OK){
                Log.d("num","Update Flow failed"+resultCode);
            }
        }
    }

//    private boolean language(){
//        progressDialog=new Dialog(MainActivity.this);
//        progressDialog.setContentView(R.layout.language);
//        progressDialog.setCancelable(false);
//        btn=progressDialog.findViewById(R.id.hindi);
//        btn2=progressDialog.findViewById(R.id.english);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(check()){
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("Language", "H");
//                    myEdit.commit();
//                    Toast.makeText(MainActivity.this,"Hindi saved",Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }
//        });
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(check()){
//                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                    myEdit.putString("Language", "E");
//                    myEdit.commit();
//                    Toast.makeText(MainActivity.this,"English saved",Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//
//            }
//        });
//
//        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        progressDialog.show();
//        return  true;
//    }

    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

//    private boolean check(){
//        String value = sharedPreferences.getString("Language","H");
//        return value!=null;
//    }

    private boolean first(){
        boolean value = firsttime.getBoolean("firstrun", true);
        return value;
    }
}

