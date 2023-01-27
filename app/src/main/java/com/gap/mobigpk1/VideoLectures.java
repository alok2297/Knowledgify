package com.gap.mobigpk1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gap.mobigpk1.Model.Playlist;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VideoLectures extends YouTubeBaseActivity implements OnItemClick{

    private String vKey;

    private YouTubePlayerView youTubePlayer;
    private DatabaseReference videoReference;
    private String link;
    private ArrayList<Playlist> list;
    private VideoAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView playlistRV;
    private String[] path;
    private YouTubePlayer mYouTubePlayer;
    int index=0;
    private String vContent,vTitle;
    private TextView title,subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_yt_lectures);
        playlistRV=findViewById(R.id.playlist);
        progressBar=findViewById(R.id.progressBar);

        title=findViewById(R.id.title);

        youTubePlayer= findViewById(R.id.ytPlayer);


        Intent i=getIntent();
        if(i.hasExtra("reference")) {
            path = i.getStringExtra("reference").toString().split("/");
            videoReference = FirebaseDatabase.getInstance().getReference().child(path[1]).child(path[2]).child(path[3]).child(path[4]).child(path[5]).child(path[6]).child("Playlist");
            vTitle=i.getStringExtra("vTopic");
            title.setText(vTitle);

        }
        else{
            index=1;
            videoReference = FirebaseDatabase.getInstance().getReference().child("Nil");

        }
        link=i.getStringExtra("mainLink");

        playlistRV.setLayoutManager(new LinearLayoutManager(this));
        playlistRV.setHasFixedSize(true);

        FirebaseRecyclerOptions<Playlist> options=new FirebaseRecyclerOptions.Builder<Playlist>()
                .setQuery(videoReference,Playlist.class)
                .build();

        adapter=new VideoAdapter(options,this);
        playlistRV.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);


        youTubePlayer.initialize(getString(R.string.yt_APIKEY), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                mYouTubePlayer =youTubePlayer;
                youTubePlayer.loadVideo(link);
                youTubePlayer.play();
                if(index==1) {
                    youTubePlayer.setFullscreen(true);
                    youTubePlayer.setShowFullscreenButton(false);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onClick(String value, String values) {
        mYouTubePlayer.loadVideo(value);
        title.setText(vTitle+":"+values.substring(values.indexOf(' ')));


    }
}