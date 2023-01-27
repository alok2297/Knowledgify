package com.gap.mobigpk1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Books#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Books extends Fragment implements ValueEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView slide;

    LinearLayoutManager layoutManager;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference();
    DatabaseReference  top=reference.child("topic");
    DatabaseReference  stop=reference.child("subtopic");
    DatabaseReference  link=reference.child("link");
    String s;
    private CardView btn;
    private TextView topic;
    private  TextView subTop;
    private Dialog progressDialog;


    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_book, container, false);

        FirebaseMessaging.getInstance().subscribeToTopic("Notification");

        ImageSlider img=v.findViewById(R.id.slide);
        List<SlideModel> ImgSlider=new ArrayList<>();
//        ImgSlider.add(new SlideModel(R.drawable.slide1,ScaleTypes.FIT));
//        ImgSlider.add(new SlideModel(R.drawable.slide2, ScaleTypes.FIT));
//        ImgSlider.add(new SlideModel(R.drawable.slide3,ScaleTypes.FIT));
//        ImgSlider.add(new SlideModel(R.drawable.slide4,ScaleTypes.FIT));

        img.setImageList(ImgSlider);
        if(!isConnected())
        {
            progressDialog=new Dialog(getActivity());
            progressDialog.setContentView(R.layout.dialog_layout);
            progressDialog.setCancelable(false);
            Button btn=progressDialog.findViewById(R.id.retry);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isConnected())
                        progressDialog.dismiss();
                    else
                        Toast.makeText(getActivity(),"No Internet Access",Toast.LENGTH_SHORT).show();
                }
            });
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            progressDialog.show();
        }

        topic=v.findViewById(R.id.topic);
        subTop=v.findViewById(R.id.subtopic);
        btn=v.findViewById(R.id.wholebtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                startActivity(intent);
            }
        });

        Button button3 =v.findViewById(R.id.btn3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Main4Activity.class);
                intent.putExtra("title","Civil Engg.");
                Yodo1Mas.getInstance().showRewardedAd(getActivity());
                Yodo1Mas.getInstance().setRewardListener(new Yodo1Mas.RewardListener() {
                    @Override
                    public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

                    }

                    @Override
                    public void onAdvertRewardEarned(@NonNull Yodo1MasAdEvent event) {

                    }

                    @Override
                    public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                    }

                    @Override
                    public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
                        startActivity(intent);

                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.getValue()!=null){
            String key= snapshot.getKey();
            if(key.equals("topic")){
                topic.setText(snapshot.getValue(String.class));
            }
            if(key.equals("subtopic")){
                subTop.setText(snapshot.getValue(String.class));
            }
            if(key.equals("link")){
                 s=snapshot.getValue(String.class);

            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onStart() {
        super.onStart();
        top.addValueEventListener(this);
        stop.addValueEventListener(this);
        link.addValueEventListener(this);

    }
}