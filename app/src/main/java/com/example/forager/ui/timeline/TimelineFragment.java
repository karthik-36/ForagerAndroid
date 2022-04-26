package com.example.forager.ui.timeline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.forager.R;
import com.example.forager.databinding.FragmentTimelineBinding;
import com.example.forager.ui.confirm.ConfirmFragment;
import com.example.forager.ui.orders.OrdersFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimelineFragment extends Fragment {

private FragmentTimelineBinding binding;


    ListView listview;
    tAdapter adapter;



    List<String> filteredDate = new ArrayList<String>();
    List<String> filteredExpDate = new ArrayList<String>();

    List<String> filteredFoodName = new ArrayList<String>();
    List<Integer> filteredImages = new ArrayList<Integer>();

    List<String> filteredDonorsTakers = new ArrayList<String>();


    List<Integer> filteredStatus = new ArrayList<Integer>();



    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

//        com.example.forager.ui.settings.SettingsViewModel SettingsViewModel =
//                new ViewModelProvider(this).get(com.example.forager.ui.settings.SettingsViewModel.class);
//        binding = FragmentTimelineBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

//        final TextView textView = binding.textTimeline;
//        SettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);



        filteredDate = Arrays.asList("NOW", "07-19-2020", "05-15-2020", "01-01-2020", "11-16-2019", "04-26-2019");
        filteredExpDate = Arrays.asList("11-26-2022", "10-10-2020", "09-20-2022", "09-19-2020",  "01-14-2020", "05-05-2019");
        filteredFoodName = Arrays.asList("Avocados", "Mandarin Oranges", "Ruffles Cheddar and Sour Cream", "Takis Fuego", "Spinach", "Okra");
        filteredImages = Arrays.asList(R.drawable.avocados , R.drawable.mandarins , R.drawable.ruffles , R.drawable.takis , R.drawable.spinach , R.drawable.okra);
        filteredDonorsTakers = Arrays.asList("James Franco", "Zelanskyy V", "Johnny Depp", "Bruce wayne", "Sania Newal", "Joe Biden");
        filteredStatus = Arrays.asList(0,1,2,2,1,2);


        listview = (ListView) view.findViewById(R.id.history);
        adapter = new TimelineFragment.tAdapter(getContext(), filteredDate, filteredImages , filteredDonorsTakers , filteredExpDate , filteredFoodName , filteredStatus);
        listview.setAdapter(adapter);

        return view;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    class tAdapter extends ArrayAdapter<String> {
        Context context;
        List<Integer> images;

        List<String> filteredDate;

        List<String> donorName;

        List<String> expiryDate;

        List<String>  foodName;

        List<Integer> status;


      //  filteredDate, filteredImages , filteredDonorsTakers , filteredExpDate , filteredFoodName

        tAdapter(Context c, List<String> filteredDate, List<Integer> imgs, List<String> dN , List<String> exp , List<String> fn , List<Integer> fS) {
            super(c, R.layout.timeline, R.id.itemName, filteredDate);
            // super(c, R.layout.timeline, R.id.itemName, filteredDate);
            this.context = c;
            this.images = imgs;
            this.filteredDate = filteredDate;
            this.donorName = dN;
            this.expiryDate = exp;
            this.foodName = fn;
            this.status = fS;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.timeline, parent, false);


            ImageView foodImage = (ImageView) row.findViewById(R.id.foodImage);
            TextView date= (TextView) row.findViewById(R.id.date);
            TextView foodNameText = (TextView) row.findViewById(R.id.foodName);
            TextView expiryText = (TextView) row.findViewById(R.id.expiryText);
         // TextView donorText = (TextView) row.findViewById(R.id.donorText);
            RelativeLayout rl = (RelativeLayout) row.findViewById(R.id.wholeRow);
            TextView donorTextV = (TextView) row.findViewById(R.id.donorName);
            TextView warning = (TextView) row.findViewById(R.id.danger);


            ImageView statusImage = (ImageView) row.findViewById(R.id.incomingOutgoing);
            ImageView backgroundImage = (ImageView) row.findViewById(R.id.backgroundHolder);

                    if(status.get(position) == 1){
                        statusImage.setImageResource(R.drawable.down_arrow);
                        backgroundImage.setImageResource(R.drawable.greenbtn);
                    }
                    else if(status.get(position) == 2) {
                        statusImage.setImageResource(R.drawable.up_arrow);
                        backgroundImage.setImageResource(R.drawable.orangebtn);
                    }
                    else if(status.get(position) == 0){
                        backgroundImage.setImageResource(R.drawable.bluebtn);
                    }

             LocalDate date1 = LocalDate.now();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
             System.out.println(date1.format(formatter));


            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

            LocalDate ExpiryDateReal = LocalDate.parse(expiryDate.get(position), dateTimeFormatter);  //String to LocalDate

            String dateStr = ExpiryDateReal.format(dateTimeFormatter);



             if(date1.isAfter(ExpiryDateReal)){
                 warning.setTextColor(Color.parseColor("#FF2222"));
                warning.setText("Expired");
             }else{
                 warning.setTextColor(Color.parseColor("#22FF22"));
                 warning.setText("Safe");

            }


            foodImage.setImageResource(images.get(position));
            foodNameText.setText(foodName.get(position));
            expiryText.setText(expiryDate.get(position));
            donorTextV.setText(donorName.get(position));
            date.setText(filteredDate.get(position));


            rl.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    System.out.println("clickPosition " + position + " " + filteredDate.get(position));
                    if (!filteredDate.get(position).toLowerCase().equals("now")) {

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    OrdersFragment llf = new OrdersFragment();


                    Bundle bundle = new Bundle();
                    bundle.putString("itemName", foodName.get(position));
                    bundle.putBoolean("fromTimeline", true);
                    llf.setArguments(bundle);


                    ft.replace(R.id.nav_host_fragment_activity_main, llf);
                    ft.commit();

                }else{

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ConfirmFragment llf = new ConfirmFragment();


                        Bundle bundle = new Bundle();

                        bundle.putBoolean("fromTimeline", true);
                        bundle.putInt("position" , position);
                        bundle.putInt("images", images.get(position));
                        bundle.putString("foodName", foodName.get(position));
                        bundle.putString("expiryDate", expiryDate.get(position));
                        bundle.putString("donorName", donorName.get(position));
                        bundle.putString("filteredDate", filteredDate.get(position));

                        llf.setArguments(bundle);

                        ft.replace(R.id.nav_host_fragment_activity_main, llf);
                        ft.commit();

                    }

                }
            });

            return row;
        }


        private void itemClick(int position , View v){

//            adapter.notifyDataSetChanged();
//            if(previousRl != null) {
//                previousRl.setBackgroundResource(android.R.color.transparent);
//            }
//            v.setBackgroundResource(R.color.purple_700);
//            mMap.clear();
//            for(int i = 0 ; i < destinations.length ; i++){
//                mMap.addMarker(new MarkerOptions().position(destinations[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(menuItems.get(i)));
//            }
//            selectedList = position;
//            mMap.getUiSettings().setMapToolbarEnabled(true);
//            Findroutes(start, destinations[position]);
//
//            return;
        }

    }
}