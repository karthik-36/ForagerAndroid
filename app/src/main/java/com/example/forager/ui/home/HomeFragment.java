package com.example.forager.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.forager.R;
import com.example.forager.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home , container , false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);



        String[] menuItems = {"Milk" , "Eggs" , "Peanut butter" , "cottage cheese" , "bananas" , "apples" , "basmati rice" ,
                "cashews"};

        String[] donorName = {"karthik" , "aditya" , "peter" , "erick" , "Nate" , "paul" , "karthik" ,
                "amrit"};

        int[] images = {R.drawable.milk , R.drawable.eggs , R.drawable.pb , R.drawable.cottage_cheese , R.drawable.bananas , R.drawable.apple , R.drawable.basmati };

        ListView listview = (ListView) root.findViewById(R.id.mainMenu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.single_row,
                R.id.itemName,
                menuItems
        );


        listview.setAdapter(listViewAdapter);


        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}










//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);