package com.example.forager.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.example.forager.ui.confirm.ConfirmFragment;
import com.example.forager.ui.orders.OrdersFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback, RoutingListener , GoogleMap.OnMarkerClickListener , GoogleMap.InfoWindowAdapter {

//    private FragmentHomeBinding binding;

    ListView listview;
    kAdapter adapter;
    GoogleMap map;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    LatLng[] destinations;
    String[] menuItems;
    List<String> filteredMenu = new ArrayList<String>();
    List<Integer> filteredImages = new ArrayList<Integer>();
    List<LatLng> filteredLatLong = new ArrayList<LatLng>();
    List<String> filteredDonors = new ArrayList<String>();
    List<String> filteredDistance = new ArrayList<>();
    List<String> filteredExpiryDate = new ArrayList<>();

    Boolean isNew = true;
    EditText editText;
    int selectedList = -1;
    RelativeLayout previousRl;

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


        menuItems = new String[]{"500ml Milk", "2 dozen Eggs", "Peanut butter jar", "cottage cheese", "bananas", "apples", "basmati rice",
                "cashews"};
        String[] donorName = {"karthik", "aditya", "peter", "erick", "Nate", "paul", "karthik", "amrit"};

        String[] distance = {"420 m", "734 m", "1.1 km", "1.2 km", "1.4 km", "2.1 km", "2.1 km", "2.2 km"};

        String[] expiryDate = {"11-02-2022", "21-04-2020", "14-05-2022", "14-11-2021", "01-10-2021", "09-06-2021", "01-01-2020", "17-08-2022"};






        int[] images = {R.drawable.milk, R.drawable.eggs, R.drawable.pb, R.drawable.cottage_cheese, R.drawable.bananas, R.drawable.apple, R.drawable.basmati, R.drawable.cashew};
        destinations = new LatLng[]{new LatLng(41.86970636792965, -87.65487239804796) ,  new LatLng(41.867667097915096, -87.64239594283593) ,  new LatLng(41.87352215752626, -87.64807617785777)  ,  new LatLng(41.863998807911955, -87.6475392074577) ,  new LatLng(41.868376823816185, -87.65775309712812) ,  new LatLng(41.872802617151144, -87.66131585779513) ,  new LatLng(41.87689351773915, -87.65290400086626) ,  new LatLng(41.87262728007855, -87.65644437727795) };




        for( int i = 0 ; i < menuItems.length; i++){
                filteredMenu.add(menuItems[i]);
                filteredImages.add(images[i]);
                filteredDonors.add(donorName[i]);
                filteredLatLong.add(destinations[i]);
                filteredDistance.add(distance[i]);
                filteredExpiryDate.add(expiryDate[i]);
        }


        listview = (ListView) view.findViewById(R.id.mainMenu);
        adapter = new kAdapter(getContext(), filteredMenu, filteredImages, filteredDonors , filteredDistance , filteredExpiryDate);
        listview.setAdapter(adapter);


        ImageButton button = (ImageButton) view.findViewById(R.id.itemPlus);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ConfirmFragment llf = new ConfirmFragment();

                Bundle bundle = new Bundle();
                bundle.putBoolean("fromTimeline", false);
                llf.setArguments(bundle);

                ft.replace(R.id.nav_host_fragment_activity_main, llf).addToBackStack( "tag" );
                ft.commit();

            }
        });


        editText = (EditText) view.findViewById(R.id.editTextTextPersonName);
        editText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
//                if(!s.equals("") ) {
//                    //do your work here
//                }

                Log.d("char1", "onTextChanged: " + s.toString());

                filteredMenu.clear();
                filteredImages.clear();
                filteredDonors.clear();
                filteredLatLong.clear();
                filteredDistance.clear();
                filteredExpiryDate.clear();
                for( int i = 0 ; i < menuItems.length; i++){
                    if(menuItems[i].toLowerCase().contains(s.toString().toLowerCase())){
                        filteredMenu.add(menuItems[i]);
                        filteredImages.add(images[i]);
                        filteredLatLong.add(destinations[i]);
                        filteredDonors.add(donorName[i]);
                        filteredDistance.add(distance[i]);
                        filteredExpiryDate.add(expiryDate[i]);
                    }
                }

                if(mMap != null) {
                    mMap.clear();
                }

                for(int i = 0 ; i < filteredLatLong.size() ; i++){
                    Log.d("herem", "onMarkerClick: " + menuItems[i]);
                    mMap.addMarker(new MarkerOptions().position(filteredLatLong.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(filteredMenu.get(i)));
                }
                mMap.getUiSettings().setMapToolbarEnabled(true);


                Log.d("char2", "Filter: " + filteredMenu);
                adapter.notifyDataSetChanged();
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

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
//        binding = null;
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

//        LatLng curr = new LatLng(41.86908257667361, -87.64799558167456);
//        mMap.addMarker(new MarkerOptions().position(curr).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("current location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(curr));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));

//
//        LatLng curr2 = new LatLng(41.86970636792965, -87.65487239804796);
//        mMap.addMarker(new MarkerOptions().position(curr2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("destination location"));
//

        setUpMap();
        getMyLocation();

        for(int i = 0 ; i < destinations.length ; i++){
            mMap.addMarker(new MarkerOptions().position(destinations[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(menuItems[i]));
        }
        //0...Findroutes(curr, curr2);

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
                polyOptions.color(getResources().getColor(R.color.dkgrey));
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

        Log.d("here 2 " , "location is ");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            Log.d("here 2 " , "location is ");

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation=location;
                LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());





                    if(isNew){
                        //mMap.addMarker(new MarkerOptions().position(ltlng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("current location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(ltlng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ltlng, 15));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                ltlng, 14f);
                        mMap.animateCamera(cameraUpdate);

                        start = ltlng;

                        isNew = false;
                    }


                }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end=latLng;



                Log.d("here1" , "trigger");

               // Findroutes(start,end);
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setUpMap()
    {
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {



//        LatLng dest =new LatLng(marker.getPosition());
        marker.showInfoWindow();
        mMap.clear();

        for(int i = 0 ; i < destinations.length ; i++){
            Log.d("herem", "onMarkerClick: " + menuItems[i]);
            mMap.addMarker(new MarkerOptions().position(destinations[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(menuItems[i]));
        }

        Log.d("heremtitle", "title " + marker.getTitle());

        marker.showInfoWindow();
        Findroutes(start,marker.getPosition());
        marker.setVisible(true);
        mMap.addMarker(new MarkerOptions().position(marker.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(marker.getTitle()));
        marker.showInfoWindow();
        marker.setVisible(true);


        Toast toast1 = Toast.makeText(getContext(),marker.getTitle(), Toast.LENGTH_SHORT);

        toast1.setGravity(Gravity.CENTER, 100, 100);

        toast1.show();

        mMap.getUiSettings().setMapToolbarEnabled(true);

        for(int j = 0 ; j < destinations.length; j++){
            System.out.println("cmp1"+marker.getTitle()+":"+menuItems[j]);
            if(marker.getTitle().equals(menuItems[j])){
                selectedList = j;
                break;
            }
        }
        if(previousRl != null) {
            previousRl.setBackgroundResource(android.R.color.transparent);
        }


        adapter.notifyDataSetChanged();

        return false;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {

        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }


    class kAdapter extends ArrayAdapter<String> {
        Context context;
        List<Integer> images;

        List<String> menuItems;

        List<String> donorName;

        List<String> distance;
        List<String> expiryDate;

        kAdapter(Context c, List<String> mI, List<Integer> imgs, List<String> dN , List<String >  fD , List<String> fE ) {
            super(c, R.layout.single_row, R.id.itemName, mI);
            this.context = c;
            this.images = imgs;
            this.menuItems = mI;
            this.donorName = dN;
            this.distance  = fD;
            this.expiryDate = fE;
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
            TextView distanceText = (TextView) row.findViewById(R.id.miles);
            RelativeLayout rl = (RelativeLayout) row.findViewById(R.id.wholeRow);

            if(selectedList == position){
                rl.setBackgroundResource(R.color.dkgrey);
            }else{
                rl.setBackgroundResource(android.R.color.transparent);
            }

            myImage.setImageResource(images.get(position));
            itemName.setText(menuItems.get(position));
            expiryText.setText(expiryDate.get(position));
            donorText.setText(donorName.get(position));
            distanceText.setText(distance.get(position));

            ImageButton deleteImageView = (ImageButton) row.findViewById(R.id.details);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    OrdersFragment llf = new OrdersFragment();


                    Bundle bundle = new Bundle();
                    bundle.putString("itemName", menuItems.get(position));
                    bundle.putBoolean("fromTimeline", false);
                    llf.setArguments(bundle);


                    ft.replace(R.id.nav_host_fragment_activity_main, llf).addToBackStack( "tag" );
                    ft.commit();

                }
            });


            rl.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    itemClick(position,v);
                    previousRl = rl;
                }
            });

            return row;
        }


        private void itemClick(int position , View v){

            adapter.notifyDataSetChanged();
            if(previousRl != null) {
                previousRl.setBackgroundResource(android.R.color.transparent);
            }
            v.setBackgroundResource(R.color.dkgrey);
            mMap.clear();
            for(int i = 0 ; i < destinations.length ; i++){
                mMap.addMarker(new MarkerOptions().position(destinations[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(menuItems.get(i)));
            }
            selectedList = position;
            mMap.getUiSettings().setMapToolbarEnabled(true);
            Findroutes(start, destinations[position]);

            return;
        }

    }
}


