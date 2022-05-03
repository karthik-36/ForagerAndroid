package com.example.forager.ui.orders;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.fragment.app.Fragment;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.forager.R;
import com.example.forager.databinding.FragmentOrderBinding;
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

public class OrdersFragment extends Fragment implements OnMapReadyCallback , RoutingListener, GoogleMap.OnMarkerClickListener , GoogleMap.InfoWindowAdapter {

private FragmentOrderBinding binding;

    ListView listview;
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
    TextView titleStatus;

    int position;
    Boolean isNew = true;
    EditText editText;
    int selectedList = -1;
    RelativeLayout previousRl;


    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;


    //polyline object
    private List<Polyline> polylines = null;


    public static OrdersFragment newInstance() {
        OrdersFragment fragment = new OrdersFragment();
        return fragment;
    }

    String[] donorName;
    String[] descriptions;
    String[] expiry;
    String[] quantity;
    int[] images;
    String[] phone = {"+13129004250"};


    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("item", " Newwww Createeeeeeeeeee");

        View view = inflater.inflate(R.layout.fragment_order, container, false);


        Bundle bundle = this.getArguments();
        String itemName = bundle.getString("itemName", " ");
        boolean comingFrom = bundle.getBoolean("fromTimeline", false);


        ImageButton msg = view.findViewById(R.id.imageButton);
        Button placeOrder = view.findViewById(R.id.orderButton);
        Button cancel = view.findViewById(R.id.cancel);
        Button reception = view.findViewById(R.id.reception);
        Button doneButton = view.findViewById(R.id.doneButton);



        ImageView imgView = (ImageView) view.findViewById(R.id.MainImage);
        TextView titleText = (TextView) view.findViewById(R.id.titleText);
        TextView titleQty = (TextView) view.findViewById(R.id.titleQty);
        TextView titleExpiry = (TextView) view.findViewById(R.id.titleExpiry4);
        titleStatus = (TextView) view.findViewById(R.id.titleStatus);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView contact = (TextView) view.findViewById(R.id.contact);

        if(comingFrom){

            menuItems = new String[]{"Avocados", "Mandarin Oranges", "Ruffles Cheddar and Sour Cream", "Takis Fuego", "Spinach", "Okra"};
            donorName = new String[]{"James Franco", "Zelanskyy V", "Johnny Depp", "Bruce wayne", "Sania Newal", "Joe Biden"};

            images = new int[]{R.drawable.avocados , R.drawable.mandarins , R.drawable.ruffles , R.drawable.takis , R.drawable.spinach , R.drawable.okra};
            destinations = new LatLng[]{new LatLng(41.8738326086213, -87.65556405509508), new LatLng(41.87261274259981, -87.6477952527539), new LatLng(41.87119271298532, -87.6535034666652), new LatLng(41.87021106333405, -87.6477952527539), new LatLng(41.8709748646697, -87.6560290799655), new LatLng(41.86799473566682, -87.64799152744173)};
            expiry = new String[]{"11-26-2022", "10-10-2020", "09-20-2022", "09-19-2020",  "01-14-2020", "05-05-2019"};
            quantity = new String[]{"6 x 1 count", "12 x 1 count", "1 count", "1 count", "10 oz", "12 oz"};

            descriptions = new String[]{"Fresh Avocados. good for health, I did not want to waste. Bought on 04/20/2022. Best before 3 weeks", "Dozen Mandarin Oranges. Slightly sour taste. And oh yeah the color is orange too!. Bought on 04/20/2022. Best before 2 weeks of expiry", "1 pack of Ruffles of Cheddar and Sour Cream. was a bit too spicy for me. Bought on 03/24/2022. Best before 7 months of manufacturing date", "1 pack of hot chilli takies. was a bit too spicy for my taste. Bought on 03/24/2022. Best before 11 months of manufacturing date" ,  "Fresh Spinach. Wanna get muscles like popeye? better eat this. Bought on 04/15/2022. Best before 04/30/2022", "Fresh Okra. I have no idea what this fruit is. Bought on 04/16/2022. Best before 04/28/2022"};

            titleStatus.setText("Status : "+"Donation Received");
            placeOrder.setVisibility(View.GONE);
            reception.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
            doneButton.setText("Donation Received");


        }else {
            menuItems = new String[]{"500ml Milk", "2 dozen Eggs", "Peanut butter jar", "cottage cheese", "bananas", "apples", "basmati rice",
                    "cashews"};
            donorName = new String[]{"Karthik R", "Aditya R", "Peter S", "Erick G", "Nate S", "Paul S", "Karthik R", "Jose V"};

            images = new int[]{R.drawable.milk, R.drawable.eggs, R.drawable.pb, R.drawable.cottage_cheese, R.drawable.bananas, R.drawable.apple, R.drawable.basmati, R.drawable.cashew};
            destinations = new LatLng[]{new LatLng(41.86970636792965, -87.65487239804796), new LatLng(41.867667097915096, -87.64239594283593), new LatLng(41.87352215752626, -87.64807617785777), new LatLng(41.863998807911955, -87.6475392074577), new LatLng(41.868376823816185, -87.65775309712812), new LatLng(41.872802617151144, -87.66131585779513), new LatLng(41.87689351773915, -87.65290400086626), new LatLng(41.87262728007855, -87.65644437727795)};

            descriptions = new String[]{"2 L of Whole Fat Milk from Whole Foods. Unopened and bought on 04/21. Expires on 04/27.", "Dozen Organic Eggs from Petes. Bought on 04/18 and best before a month. Pickup item from porch. Call for timings.", "pack of two JIF Peanut Butter 32 oz. Bought on 04/14. Best before 3 months of manufacturing date. Creamy peanut butter", "24 oz of Cottage Cheese. Best before 2 weeks of opening. Bought on 04/20.", "Dozen unripe bananas. Bought on 04/18. Best before 3 weeks", "gala apples, fresh from indonesia. you know the old saying right? apple a day keeps doctor away. will anyone even read this? lets see.", "20 lbs Royal Basmati long grain rice. Bought on 04/15. Best before 3 months of monaufacturing.", "Planters 1lb unsalted cashew nuts. Bought on 04/03. Best before 5 months of manufacturing"};
            expiry = new String[]{"11-02-2022", "21-04-2020", "14-05-2022", "14-11-2021", "01-10-2021", "09-06-2021", "01-01-2020", "17-08-2022"};
            quantity = new String[]{"128 fl oz", "12 x 2 count", "40 oz", "24 oz", "12 x 1 count", "3 lb", "20 lb", "30 oz"};

            titleStatus.setText("Status : " + "available");
            reception.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);

        }






        for(int i = 0 ; i < menuItems.length; i++){
            Log.d("item", " compare  " + i + "  "  + menuItems[i] + "   " + itemName );
            if(menuItems[i].toLowerCase().equals(itemName.toLowerCase())){
                position = i;
                break;
            }
        }



        imgView.setImageResource(images[position]);
        titleText.setText(menuItems[position]);
        titleQty.setText("QTY : " + quantity[position]);
        titleExpiry.setText("Expiry : " +expiry[position]);

        description.setText(descriptions[position]);
        contact.setText("Message Donor : " + donorName[position]);

        //titleStatus.setBackgroundColor(R.color.yellow);
        Log.d("itemName ", " position " + itemName);







        String text = "Hi " + donorName[position] + ", I wanted to enquire about " + menuItems[position] + " :";
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean installed = isAppInstalled("com.whatsapp");
                if(installed){

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phone[0]+"&text="+ text));
                    startActivity(intent);

                }
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Order Placed!",
                        Toast.LENGTH_LONG).show();

                titleStatus.setText("Status : "+"On Going");


                placeOrder.setVisibility(View.GONE);
                reception.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);

            }



        });




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Donation Request Placed!",
                        Toast.LENGTH_LONG).show();

                titleStatus.setText("Status : "+"Cancelled");


                placeOrder.setVisibility(View.GONE);
                reception.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
                doneButton.setText("Order Cancelled");

            }
        });

        reception.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Donation Request Accepted!",
                        Toast.LENGTH_LONG).show();

                titleStatus.setText("Status : "+"Donation Received");


                placeOrder.setVisibility(View.GONE);
                reception.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
                doneButton.setText("Donation Received");

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        return view;
    }

    private boolean isAppInstalled(String s) {
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("hereeeeeeeeeee ready");

        setUpMap();
        getMyLocation();


        mMap.addMarker(new MarkerOptions().position(destinations[position]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(menuItems[position]));


    }

    private void setUpMap()
    {
        mMap.setOnMarkerClickListener(this);
    }


    private void getMyLocation() {

        Log.d("here 2 " , "location is ");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

                    Findroutes(start,destinations[position]);

                    isNew = false;
                }


            }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end=latLng;

                //mMap.clear();

                // start = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                //start route finding

                // LatLng curr = new LatLng(41.86908257667361, -87.64799558167456);
//                mMap.addMarker(new MarkerOptions().position(start).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("current location"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( start, 15));

                Log.d("here1" , "trigger");

                //Findroutes(start,end);
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub

            }
        });

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
        mMap.getUiSettings().setMapToolbarEnabled(true);
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
//        MarkerOptions startMarker = new MarkerOptions();
//        startMarker.position(polylineStartLatLng);
//        startMarker.title("My Location");
//        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title(menuItems[position] + " awaits !");
        mMap.addMarker(endMarker);
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }



    @Override
    public void onRoutingCancelled() {

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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}









