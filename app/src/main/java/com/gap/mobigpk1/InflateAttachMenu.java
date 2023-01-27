package com.gap.mobigpk1;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class InflateAttachMenu extends Fragment {
    private View attach,addPhoto,addFile,close;
    private Uri pdfData;
    private String pdfname;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_inflate_attach_menu, container, false);
        attach=v.findViewById(R.id.attach);
        addPhoto=v.findViewById(R.id.addPhoto);
        addFile=v.findViewById(R.id.addDoc);
        close=v.findViewById(R.id.close);


        close.setOnClickListener(view ->getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE));
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count-1; ++i) {
            fm.popBackStack();
        }

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();

                ((ViewManager)v.getParent()).removeView(v);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhoto();
                ((ViewManager)v.getParent()).removeView(v);

            }
        });

        addFile.setOnClickListener(view -> {
            openFileManager();
            ((ViewManager)v.getParent()).removeView(v);
        });


        return v;
    }

    private void AddPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    private void openCamera() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.CAMERA}, 3);
        }
        else {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 1);
        }
    }

    private void openFileManager() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Document"),4);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 3 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==4 && resultCode==RESULT_OK) {
            pdfData=data.getData();

            if(pdfData.toString().startsWith("content://")){
                Cursor cursor=null;
                try {
                    cursor=requireActivity().getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        pdfname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            if(pdfData.toString().startsWith("file://")){
                pdfname=new File(pdfData.toString()).getName();
            }
            Toast.makeText(requireActivity(), pdfname, Toast.LENGTH_SHORT).show();

        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras()
                        .get("data");
                uri = data.getData();
                Toast.makeText(requireActivity(), uri+"", Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == 2) {
                uri = data.getData();
                Toast.makeText(requireActivity(), uri+"", Toast.LENGTH_SHORT).show();

            }
        }
    }
}