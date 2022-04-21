package com.example.forager.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.forager.R;
import com.example.forager.databinding.FragmentHomeBinding;
import com.example.forager.ui.confirm.ConfirmFragment;
import com.example.forager.ui.settings.SettingsFragment;
import com.example.forager.ui.settings.SettingsViewModel;

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

        String[] donorName = {"karthik" , "aditya" , "peter" , "erick" , "Nate" , "paul" , "karthik" , "amrit"};

        int[] images = {R.drawable.milk , R.drawable.eggs , R.drawable.pb , R.drawable.cottage_cheese , R.drawable.bananas , R.drawable.apple , R.drawable.basmati , R.drawable.cashew };

        ListView listview = (ListView) root.findViewById(R.id.mainMenu);

        kAdapter adapter = new kAdapter(getContext() , menuItems , images , donorName);

//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.single_row,
//                R.id.itemName,
//                menuItems
//        );

        listview.setAdapter(adapter);



        // Create new fragment and transaction
//        Fragment newFragment = new SettingsFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.navigation_home, newFragment);
//        transaction.addToBackStack(null);
//
//        transaction.commit();

        ImageButton button = (ImageButton) root.findViewById(R.id.itemPlus);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ConfirmFragment llf = new ConfirmFragment();
                ft.replace(R.id.nav_host_fragment_activity_main, llf);
                ft.commit();

            }
        });






        return root;
    }

//    public void onClickNewItem(View v)
//    {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ConfirmFragment llf = new ConfirmFragment();
//        ft.replace(R.id.nav_host_fragment_activity_main, llf);
//        ft.commit();
//    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


class kAdapter extends ArrayAdapter<String>
{
    Context context;
    int[] images;

    String[] menuItems;

    String[] donorName;

    String[] expiryDate = {"2 days"};

    kAdapter(Context c , String[] mI , int imgs[] , String[] dN ){
        super(c, R.layout.single_row , R.id.itemName , mI );
        this.context = c;
        this.images = imgs;
        this.menuItems = mI;
        this.donorName = dN;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row , parent , false);

        ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
        TextView itemName = (TextView) row.findViewById(R.id.itemName);
        TextView expiryText = (TextView) row.findViewById(R.id.expiryText);
        TextView donorText = (TextView) row.findViewById(R.id.donorText);

        myImage.setImageResource(images[position]);
        itemName.setText(menuItems[position]);
        expiryText.setText(expiryDate[0]);
        donorText.setText(donorName[position]);

        return row;
    }
}







//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);