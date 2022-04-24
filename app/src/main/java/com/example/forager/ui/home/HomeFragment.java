package com.example.forager.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.forager.R;
import com.example.forager.databinding.FragmentHomeBinding;
import com.example.forager.ui.confirm.ConfirmFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback, RoutingListener {

    private FragmentHomeBinding binding;


    GoogleMap map;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        String[] menuItems = {"Milk", "Eggs", "Peanut butter", "cottage cheese", "bananas", "apples", "basmati rice",
                "cashews"};
        String[] donorName = {"karthik", "aditya", "peter", "erick", "Nate", "paul", "karthik", "amrit"};
        int[] images = {R.drawable.milk, R.drawable.eggs, R.drawable.pb, R.drawable.cottage_cheese, R.drawable.bananas, R.drawable.apple, R.drawable.basmati, R.drawable.cashew};

        ListView listview = (ListView) view.findViewById(R.id.mainMenu);

        kAdapter adapter = new kAdapter(getContext(), menuItems, images, donorName);

        listview.setAdapter(adapter);


        ImageButton button = (ImageButton) view.findViewById(R.id.itemPlus);
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


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("hereeeeeeeeeee ready");
        // Add a marker in Sydney and move the camera

        LatLng curr = new LatLng(41.86908257667361, -87.64799558167456);
        mMap.addMarker(new MarkerOptions().position(curr).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));


        LatLng curr2 = new LatLng(41.86970636792965, -87.65487239804796);
        mMap.addMarker(new MarkerOptions().position(curr2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("destination location"));

        Findroutes(curr, curr2);

        //getMyLocation();


    }


    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyC3PnfK5TabONuaWT7_QeUsF8fVgNplLWY")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getActivity(), "Route Fail !", Toast.LENGTH_SHORT).show();
        Log.d("help ", "onRoutingFailure: " + e);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(getActivity(), "Finding Route...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        Toast.makeText(getActivity(), "Route Found !", Toast.LENGTH_SHORT).show();

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.purple_700));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }

        }

        System.out.println("start here");
        System.out.println(polylineStartLatLng);
        System.out.println(polylineEndLatLng);
        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    //to get user location
//    private void getMyLocation() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//
//                myLocation = location;
//                LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
//                        ltlng, 16f);
//                mMap.animateCamera(cameraUpdate);
//            }
//        });
//
//    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation=location;
                LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 16f);
                mMap.animateCamera(cameraUpdate);
            }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end=latLng;

                mMap.clear();

                start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                //start route finding
                Findroutes(start,end);
            }
        });

    }


    class kAdapter extends ArrayAdapter<String> {
        Context context;
        int[] images;

        String[] menuItems;

        String[] donorName;

        String[] expiryDate = {"2 days"};

        kAdapter(Context c, String[] mI, int imgs[], String[] dN) {
            super(c, R.layout.single_row, R.id.itemName, mI);
            this.context = c;
            this.images = imgs;
            this.menuItems = mI;
            this.donorName = dN;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.single_row, parent, false);

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
}


    ////








//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);