package com.gap.mobigpk1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.Note;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotesList extends AppCompatActivity {

    private String[] path;
    private DatabaseReference notesReference;
    private RecyclerView notesRV;
    private ProgressBar progressBar;
    private ArrayList<Note> list;
    private NotesListAdapter adapter;
    private DatabaseReference bookReference;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        notesRV=findViewById(R.id.notesRV);
        progressBar=findViewById(R.id.progressBar);

        Intent i=getIntent();
        path=i.getStringExtra("reference").toString().split(",");

        notesRV.setLayoutManager(new LinearLayoutManager(this));
        notesRV.setHasFixedSize(true);

        if(i.getStringExtra("directedBy").equals("Note")) {
            notesReference = FirebaseDatabase.getInstance().getReference().child("Books").child(path[0]).child("childData").child(path[1]).child("Notes");
            getNote();
        }
        else if(i.getStringExtra("directedBy").equals("pyq") || i.getStringExtra("directedBy").equals("syl")){
            if(i.getStringExtra("directedBy").equals("pyq"))
                reference = FirebaseDatabase.getInstance().getReference().child("Books").child("101").child("childData").child(path[1]).child("Notes");
            else
                reference = FirebaseDatabase.getInstance().getReference().child("Books").child("102").child("childData").child(path[1]).child("Notes");
            getExtra();

        }
        else{
            bookReference = FirebaseDatabase.getInstance().getReference().child("Books").child(path[0]).child("childData").child(path[1]).child("Book");
            getBook();
        }



    }

    private void getExtra() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                String link = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    link=dataSnapshot.child("1").child("link").getValue().toString();
                    Note data = snapshot.getValue(Note.class);
                    list.add(data);
                }

                if(list.size()==1){
                    Intent intent = new Intent(NotesList.this, WebViewNV.class);
                    intent.putExtra("url",link);
                    intent.putExtra("title", "Knowledgify");
                    startActivity(intent);
                    finish();
                }


                adapter =  new NotesListAdapter(NotesList.this, list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                notesRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getBook() {
        bookReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Note data = snapshot.getValue(Note.class);
                    list.add(data);
                }

                adapter =  new NotesListAdapter(NotesList.this, list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                notesRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getNote() {
        notesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Note data = snapshot.getValue(Note.class);
                    list.add(data);
                }

                adapter =  new NotesListAdapter(NotesList.this, list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                notesRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}