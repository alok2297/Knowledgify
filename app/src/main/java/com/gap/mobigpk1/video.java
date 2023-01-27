//package com.gap.mobigpk1;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.gap.mobigpk1.Model.Category;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import java.util.Locale;
//import java.util.concurrent.TimeUnit;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link video#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class video extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    Context context;
//    ImageView nointernet;
//    RecyclerView recyclerView;
//    VideoAdapter adapter;
//    FirebaseDatabase database;
//    DatabaseReference reference;
//    private Dialog progressDialog;
//    SearchView search;
//
//    public video() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment video.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static video newInstance(String param1, String param2) {
//        video fragment = new video();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//    private boolean isConnected()
//    {
//        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v=inflater.inflate(R.layout.fragment_video, container, false);
//        nointernet = v.findViewById(R.id.nointernt);
//        search=v.findViewById(R.id.searching);
//
//        FirebaseMessaging.getInstance().subscribeToTopic("Video");
//
//        if(!isConnected())
//        {
//            progressDialog=new Dialog(getActivity());
//            progressDialog.setContentView(R.layout.dialog_layout);
//            progressDialog.setCancelable(false);
//            Button btn=progressDialog.findViewById(R.id.retry);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(isConnected())
//                        progressDialog.dismiss();
//                    else
//                        Toast.makeText(getActivity(),"No Internet Access",Toast.LENGTH_SHORT).show();
//                }
//            });
//            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            progressDialog.show();
//            //    Toast.makeText(getActivity(),"No Internet Access",Toast.LENGTH_LONG).show();
//        }
//
//        long duration= TimeUnit.SECONDS.toMillis(2);
//        new CountDownTimer(duration, 1000) {
//            @Override
//            public void onTick(long l) {
//                String sDuration=String.format(Locale.ENGLISH,"%02d"
//                        , TimeUnit.MILLISECONDS.toSeconds(l));
//                //Toast.makeText(getActivity(),"Please wait! Connecting to Internet",Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFinish() {
//                ProgressBar progress=v.findViewById(R.id.progress_icon);
//                progress.setVisibility(View.INVISIBLE);
//            }
//        }.start();
//
//
//        database= FirebaseDatabase.getInstance();
//        reference=database.getReference("Catagory");
//
//        recyclerView=v.findViewById(R.id.recyclerView);
//        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
//        recyclerView.setLayoutManager(layoutManager);
//
//
//        FirebaseRecyclerOptions<Category> options=new FirebaseRecyclerOptions.Builder<Category>()
//                .setQuery(reference,Category.class)
//                .build();
//
//        adapter=new VideoAdapter(options);
//        recyclerView.setAdapter(adapter);
//
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                txtSearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                txtSearch(newText);
//                return false;
//            }
//        });
//
//
//        return v;
//    }
//
//    private void txtSearch(String str) {
//
//        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
//                .setQuery(reference.orderByChild("categoryName").startAt(str).endAt(str + "~"), Category.class)
//                .build();
//
//        adapter = new VideoAdapter(options);
//        adapter.startListening();
//        recyclerView.setAdapter(adapter);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//
//
//
//}