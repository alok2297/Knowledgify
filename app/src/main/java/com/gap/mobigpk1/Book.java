package com.gap.mobigpk1;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.gap.mobigpk1.Model.ParentItem;
import com.gap.mobigpk1.viewmodel.FirebaseViewModel;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class Book extends Fragment {
    private RecyclerView parentRecyclerView;
    private FirebaseViewModel firebaseViewModel;
    private ParentAdapter parentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_book_demo, container, false);

        ImageSlider img=v.findViewById(R.id.slide);
        List<SlideModel> ImgSlider=new ArrayList<>();
        ImgSlider.add(new SlideModel(R.drawable.s1, ScaleTypes.FIT));
        ImgSlider.add(new SlideModel(R.drawable.s2, ScaleTypes.FIT));
        ImgSlider.add(new SlideModel(R.drawable.s3,ScaleTypes.FIT));
        ImgSlider.add(new SlideModel(R.drawable.s4,ScaleTypes.FIT));

        img.setImageList(ImgSlider);
        if(!isConnected())
        {
            Dialog progressDialog = new Dialog(getActivity());
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

        parentRecyclerView = v.findViewById(R.id.parentRecyclerView);

        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        parentAdapter = new ParentAdapter();
        parentRecyclerView.setAdapter(parentAdapter);

        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);

        firebaseViewModel.getAllData();
        firebaseViewModel.getParentItemMutableLiveData().observe(requireActivity(), new Observer<List<ParentItem>>() {
            @Override
            public void onChanged(List<ParentItem> parentItemList) {
                parentAdapter.setParentItemList(getContext(),parentItemList);
                parentAdapter.notifyDataSetChanged();
            }
        });
        firebaseViewModel.getDatabaseErrorMutableLiveData().observe(requireActivity(), new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError error) {
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}