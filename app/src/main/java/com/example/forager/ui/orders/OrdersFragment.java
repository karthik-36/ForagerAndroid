package com.example.forager.ui.orders;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.forager.R;
import com.example.forager.databinding.FragmentOrderBinding;

public class OrdersFragment extends Fragment {

private FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);


        Bundle bundle = this.getArguments();
        int myInt = bundle.getInt("itemPosition", 1);

        Log.d("itemPosition ", " position " + myInt);

        return view;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}











//        OrdersViewModel ordersViewModel =
//                (OrdersViewModel) new ViewModelProvider(this).get(ViewModel.class);
//
//    binding = FragmentOrderBinding.inflate(inflater, container, false);
//    View root = binding.getRoot();

//        final TextView textView = binding.textMessage;
//        ordersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);