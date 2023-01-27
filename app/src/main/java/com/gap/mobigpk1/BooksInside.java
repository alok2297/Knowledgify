package com.gap.mobigpk1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.Video;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BooksInside extends AppCompatActivity {

    private String link,bookLink;
    private DatabaseReference notesReference;
    private DatabaseReference videosReference;
    private RecyclerView vlistRecycler;
    private ArrayList<Video> list;
    private BooksInsideAdaptor adapter;
    private ProgressBar progressBar;
    private String pathOnly;
    private int noteListCount,bookListCount;
    private DatabaseReference bookReference;
    private TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_inside);

        Intent i=getIntent();
        pathOnly=i.getStringExtra("path");
        String[] path = i.getStringExtra("path").split(",");

        videosReference= FirebaseDatabase.getInstance().getReference().child("Books").child(path[0]).child("childData").child(path[1]).child("Videos");
        notesReference= FirebaseDatabase.getInstance().getReference().child("Books").child(path[0]).child("childData").child(path[1]).child("Notes");
        bookReference= FirebaseDatabase.getInstance().getReference().child("Books").child(path[0]).child("childData").child(path[1]).child("Book");

        vlistRecycler=findViewById(R.id.video_listRV);
        CardView notes = findViewById(R.id.notes);
        CardView books = findViewById(R.id.books);
        progressBar=findViewById(R.id.progressBar);
        txt=findViewById(R.id.textView5);

        vlistRecycler.setLayoutManager(new LinearLayoutManager(this));
        vlistRecycler.setHasFixedSize(true);

        getVideo();

        getBook();
        books.setOnClickListener(view -> {


            if(bookListCount==1){
                Intent i1 =new Intent(BooksInside.this,WebViewNV.class);
                i1.putExtra("url",bookLink);
                i1.putExtra("title","Notes PDF");
                startActivity(i1);
            }else if(bookListCount==0){
                Toast.makeText(BooksInside.this, "Content will be uploaded soon!", Toast.LENGTH_SHORT).show();
            }else{
                Intent i1 =new Intent(BooksInside.this,NotesList.class);
                i1.putExtra("reference", pathOnly);
                i1.putExtra("directedBy", "Book");
                startActivity(i1);
            }
        });

        getNote();
        notes.setOnClickListener(v -> {

            if(noteListCount==1){
                Intent i12 =new Intent(BooksInside.this,WebViewNV.class);
                i12.putExtra("url",link);
                i12.putExtra("title","Notes PDF");
                startActivity(i12);
            }else if(noteListCount==0){
                Toast.makeText(BooksInside.this, "Content will be uploaded soon!", Toast.LENGTH_SHORT).show();
            }else{
                Intent i12 =new Intent(BooksInside.this,NotesList.class);
                i12.putExtra("reference", pathOnly);
                i12.putExtra("directedBy", "Note");
                startActivity(i12);
            }

        });

    }

    private void getBook() {
        bookReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookListCount=(int)snapshot.getChildrenCount();
                if(bookListCount==1){
                    bookLink=snapshot.child("1").child("link").getValue().toString();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getNote() {
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteListCount=(int)snapshot.getChildrenCount();
                if(noteListCount==1){
                    link=snapshot.child("1").child("link").getValue().toString();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getVideo() {
        videosReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Video data = snapshot.getValue(Video.class);
                    list.add(data);
                }

                if(!list.isEmpty())
                    txt.setVisibility(View.INVISIBLE);

                adapter =  new BooksInsideAdaptor(list,videosReference);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                vlistRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}