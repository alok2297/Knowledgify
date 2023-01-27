package com.gap.mobigpk1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.Poll;
import com.gap.mobigpk1.Model.Posts;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/*
college while pushing
document handling
if postType is document/poll then pass in comment and create new layout for it
 */
public class HomeFragAdapter extends RecyclerView.Adapter<HomeFragAdapter.HomeFragViewAdapter> {

    private Context context;

    private ArrayList<Posts> list;
    private String directed;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users");

    String postType = null;
    int photo = 0;
    int document = 1;
    int poll = 2;
    int text=3;
    String likeCount;

    public HomeFragAdapter(Context context, ArrayList<Posts> list, String directed) {
        this.context = context;
        this.list = list;
        this.directed=directed;
    }

    @NonNull
    @Override
    public HomeFragViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == photo){
            view = LayoutInflater.from(context).inflate(R.layout.sv_firebase_post,parent,false);
        }
        else if(viewType == document){
            view = LayoutInflater.from(context).inflate(R.layout.documentpost_layout,parent,false);
        }
        else if(viewType == poll){
            view = LayoutInflater.from(context).inflate(R.layout.pollpost_layout,parent,false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.text_post_layout,parent,false);
        }
        return new HomeFragViewAdapter(view);
    }

    public void getUserChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("What next?");
        builder.setNeutralButton("Restrict this user", (new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }));
        builder.setPositiveButton((CharSequence) "Block this user", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        builder.show();
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        Posts currentItem=list.get(holder.getAdapterPosition());
        holder.Name.setText(currentItem.getName());
        holder.College.setText(currentItem.getCollege());
        holder.comment.setText(currentItem.getCommentCount());
        if(currentItem.getCaption().length()>=200)
            holder.caption.setText(currentItem.getCaption().substring(0,200)+"...\nRead More");
        else
            holder.caption.setText(currentItem.getCaption());

        holder.caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.caption.setText(currentItem.getCaption());
            }
        });

        userReference.child(currentItem.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(context).load(snapshot.child("profilePic").getValue()).into(holder.Profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final int[] likes = new int[1];
        reference.child(currentItem.getKeyPost()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.comment.setText(snapshot.child("commentCount").getValue()+"");
                holder.like.setText(snapshot.child("likeCount").getValue()+"");
                likes[0] =Integer.parseInt(snapshot.child("likeCount").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        holder.posterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(directed.equals("fromHome")) {
//                    holder.Name.setClickable(false);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Intent i = new Intent(activity, Profile.class);
                    i.putExtra("userId", currentItem.getUserId());
                    i.putExtra("homePost", "Yes");
                    activity.startActivity(i);
                }
            }
        });




        String postKey = list.get(position).getKeyPost();
        likeCount=currentItem.getLikeCount();
        holder.like.setText(likeCount);

        userReference.child(user.getUid()).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(dataSnapshot.hasChild(currentItem.getKeyPost())){
                            if (dataSnapshot.child(currentItem.getKeyPost()).getValue().toString().equals("false")) {
                                holder.like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.like, 0, 0);
                                int like = likes[0];
                                holder.like.setText(String.valueOf(like+1));
                                like++;
                                currentItem.setLikeCount(String.valueOf(like));
                                reference.child(currentItem.getKeyPost()).child("likeCount").setValue(like + "");
                                userReference.child(user.getUid()).child("Likes").child(currentItem.getKeyPost()).setValue("true");
                            } else {

                                holder.like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.like_empty, 0, 0);
                                int like = likes[0];
                                if(like-1>=0)
                                    holder.like.setText(String.valueOf(like-1));
                                else
                                    holder.like.setText(0);
                                like--;
                                currentItem.setLikeCount(String.valueOf(like));
                                reference.child(currentItem.getKeyPost()).child("likeCount").setValue(like + "");
                                userReference.child(user.getUid()).child("Likes").child(currentItem.getKeyPost()).setValue("false");
                            }
                        }else{
                            holder.like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.like, 0, 0);
                            int like = likes[0];
                            holder.like.setText(String.valueOf(like+1));
                            like++;
                            currentItem.setLikeCount(String.valueOf(like));
                            reference.child(currentItem.getKeyPost()).child("likeCount").setValue(like + "");
                            userReference.child(user.getUid()).child("Likes").child(currentItem.getKeyPost()).setValue("true");
                        }
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(directed.equals("myPost") && user.getUid().equals(currentItem.getUserId())){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete!");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            reference.child(currentItem.getKeyPost()).child("delete").setValue("true");

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

        }

        holder.tvSpamReport.setOnClickListener(view -> {

            reference.child(postKey).child("spamReportsList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.hasChild(user.getUid())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setCancelable(true);

                        builder.setTitle("Report spam?");

                        builder.setMessage("Do you want to report this as spam?");


                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int spamCount = Integer.parseInt(currentItem.getSpamReports());
                                spamCount++;
                                list.remove(position);

                                holder.tvSpamReport.setClickable(false);
                                currentItem.setSpamReports(String.valueOf(spamCount));
                                reference.child(postKey).child("spamReports").setValue(spamCount);
                                reference.child(postKey).child("spamReportsList").child(user.getUid()).setValue(true);
                                getUserChoice();
                            }
                        }).create();

                        builder.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });









        userReference.child(user.getUid()).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(currentItem.getKeyPost()))
                    if(dataSnapshot.child(currentItem.getKeyPost()).getValue().toString().equals("true")){
                        holder.like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.like, 0, 0);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(firebase_model_all.getLink()));

                Intent intent=new Intent(activity,Comment.class);
                intent.putExtra("key",currentItem.getKeyPost());
                intent.putExtra("userId",currentItem.getUserId());
                intent.putExtra("postType",currentItem.getPostType());
                    activity.startActivity(intent);
            }
        });


        if(postType.equals("photo")){

            final String[] img = {""};
            reference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("imgUrl")) {
                        img[0] = snapshot.child("imgUrl").getValue().toString();
                        Glide.with(context).load(snapshot.child("imgUrl").getValue().toString()).into(holder.Display);
                    }
                    else{
                        img[0]="";
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.Display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity=(AppCompatActivity) v.getContext();
                    Intent intent=new Intent(activity,ImageViewFull.class);
                    intent.putExtra("fullPic",img[0]);
                    activity.startActivity(intent);
                }
            });
        }

        else if(postType.equals("document")){
            final String[] docUrl = new String[1];
            reference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("docUrl")) {
                        new RetrivePDFfromUrl(holder).execute(snapshot.child("docUrl").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.ivDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AppCompatActivity activity=(AppCompatActivity) v.getContext();
                            Intent i=new Intent(activity,PDFViewer.class);
                            i.putExtra("url", snapshot.child("docUrl").getValue().toString());
                            i.putExtra("title","Document");
                            activity.startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });




        }

        else if(postType.equals("poll")){
//            DatabaseReference pollUser=FirebaseDatabase.getInstance().getReference().child("Posts").child(currentItem.getKeyPost()).child("PollUsers");
            reference.child(postKey).child("Options").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    ArrayList<Poll> listOptions = new ArrayList<>();
                    int count = Integer.parseInt(snapshot.child("optionsCount").getValue().toString());
                    int totalOption_Votes=0;
                    for (int i = 1; i <= count; i++) {
                        listOptions.add(new Poll(postKey, snapshot.child("option" + i).child("description").getValue().toString(), Integer.parseInt(snapshot.child("option" + i).child("totalVotes").getValue().toString())));
                        totalOption_Votes+=Integer.parseInt(snapshot.child("option" + i).child("totalVotes").getValue().toString());
                    }
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerView.setHasFixedSize(true);
                    int finalTotalOption_Votes = totalOption_Votes;


                    userReference.child(user.getUid()).child("poll").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(postKey))
                                holder.recyclerView.setAdapter(new PollPostAdapter(listOptions, context,"result", finalTotalOption_Votes));
                            else
                                holder.recyclerView.setAdapter(new PollPostAdapter(listOptions, context,"option",0));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getPostType().equals("photo")){
            postType = "photo";
            return photo;
        }
        else if(list.get(position).getPostType().equals("document")){
            postType = "document";
            return document;
        }
        else if(list.get(position).getPostType().equals("text")){
            postType = "text";
            return text;
        }
        else {
            postType = "poll";
            return poll;
        }
    }

    public class HomeFragViewAdapter extends RecyclerView.ViewHolder {
        private final TextView tvSpamReport;
        private ImageButton delete;
        private ConstraintLayout posterInfo;
        private TextView caption;
        private TextView comment;
        private TextView like;
        private TextView Name;
        private TextView College;
        private ImageView Profile,Display;
        private RecyclerView recyclerView, resultRecyclerView;
        private LinearLayout optionsLayout, resultsLayout;
        private ConstraintLayout fullPost;
        private PDFView ivDocument;

        public HomeFragViewAdapter(@NonNull View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.userName);
            College=itemView.findViewById(R.id.College);
            Profile=itemView.findViewById(R.id.profile_image);
            fullPost=itemView.findViewById(R.id.poll_post);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            caption=itemView.findViewById(R.id.caption);
            posterInfo=itemView.findViewById(R.id.posterInfo);
            delete=itemView.findViewById(R.id.delete);
            tvSpamReport = itemView.findViewById(R.id.tvSpamReport);

            if(postType.equals("photo")){
                Display=itemView.findViewById(R.id.addStoryImg);
            }

            else if(postType.equals("poll")){
//                tvPollDes = itemView.findViewById(R.id.tvPollDes);
                recyclerView = itemView.findViewById(R.id.optionsRecyclerView);
            }

            else if(postType.equals("document")){
                ivDocument = itemView.findViewById(R.id.ivDocument);
            }
        }
    }
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        private HomeFragViewAdapter holder;

        public RetrivePDFfromUrl(HomeFragViewAdapter holder){
            this.holder = holder;
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            holder.ivDocument.fromStream(inputStream).load();
        }
    }
}
