package com.example.forager.ui.confirm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.forager.R;
import com.example.forager.databinding.FragmentConfirmBinding;

public class ConfirmFragment extends Fragment {

private FragmentConfirmBinding binding;

    ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        ConfirmViewModel messagesViewModel =
                new ViewModelProvider(this).get(ConfirmViewModel.class);

        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
//
//        binding = FragmentConfirmBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        imageView = view.findViewById(R.id.addImageButton);


        if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CAMERA} , 101);

        }



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent , 101);
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,  data);
        if(requestCode == 101){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }


    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}