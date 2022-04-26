package com.example.forager.ui.confirm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.example.forager.R;
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

import java.util.ArrayList;
import java.util.List;

public class ConfirmFragment extends Fragment implements OnMapReadyCallback, RoutingListener, GoogleMap.OnMarkerClickListener , GoogleMap.InfoWindowAdapter {



    ImageView imageView;



    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    protected LatLng start = null;
    Bitmap bitmap;


    boolean fromTimeLine;
    Boolean isNew = true;


    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        ConfirmViewModel messagesViewModel =
                new ViewModelProvider(this).get(ConfirmViewModel.class);

        View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        fromTimeLine = false;
//
//        binding = FragmentConfirmBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        Bundle bundle = this.getArguments();

        boolean comingFrom = bundle.getBoolean("fromTimeline", false);


        EditText foodName = (EditText) view.findViewById(R.id.foodName);
        EditText foodDescription = (EditText) view.findViewById(R.id.foodDescription);
        EditText quantityText = (EditText) view.findViewById(R.id.quantityText);
        EditText editTextDate2 =  (EditText) view.findViewById(R.id.editTextDate2);
        imageView = view.findViewById(R.id.addImageButton);
        Button btn = (Button) view.findViewById(R.id.button7);

        if(comingFrom){


            String[] descriptions = new String[]{"Fresh Avocados. good for health, I did not want to waste. Bought on 04/20/2022. Best before 3 weeks", "Dozen Mandarin Oranges. Slightly sour taste. And oh yeah the color is orange too!. Bought on 04/20/2022. Best before 2 weeks of expiry", "1 pack of Ruffles of Cheddar and Sour Cream. was a bit too spicy for me. Bought on 03/24/2022. Best before 7 months of manufacturing date", "Fresh Spinach. Wanna get muscles like popeye? better eat this. Bought on 04/15/2022. Best before 04/30/2022", "Fresh Okra. I have no idea what this fruit is. Bought on 04/16/2022. Best before 04/28/2022"};

            String[] quantity = new String[]{"6 x 1 count", "12 x 1 count", "1 count", "1 count", "10 oz", "12 oz"};

            fromTimeLine = true;
            //bundle.putBoolean("fromTimeline", true);

            foodName.setText(bundle.getString("foodName", ""));
            foodDescription.setText(descriptions[bundle.getInt("position" , 0)]);
            quantityText.setText(quantity[bundle.getInt("position" , 0)]);
            imageView.setImageResource(bundle.getInt("images", 0));
            editTextDate2.setText(bundle.getString("expiryDate", ""));

            btn.setText("Edit & Re - Submit");
            btn.setBackgroundTintList(AppCompatResources.getColorStateList(getContext(), R.color.green));

        }






        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub


                if(bitmap == null  && !
                        fromTimeLine){
                    Toast toast = Toast.makeText(getContext(), Html.fromHtml("<font color='#ff0000' background-color = '#ff0000' ><b>" + "Need atleast 1 image" + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }

                if(foodName.getText().length() == 0){
                    Toast toast = Toast.makeText(getContext(), Html.fromHtml("<font color='#ff0000' background-color = '#ff0000' ><b>" + "Food Name cannot be empty!" + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }

                if(foodDescription.getText().length() == 0){
                    Toast toast = Toast.makeText(getContext(), Html.fromHtml("<font color='#ff0000' background-color = '#ff0000' ><b>" + "Food Description cannot be empty!" + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }


                if(quantityText.getText().length() == 0){
                    Toast toast = Toast.makeText(getContext(), Html.fromHtml("<font color='#ff0000' background-color = '#ff0000' ><b>" + "Quantity cannot be empty!" + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }

                if(editTextDate2.getText().length() == 0){
                    Toast toast = Toast.makeText(getContext(), Html.fromHtml("<font color='#ff0000' background-color = '#ff0000' ><b>" + "Expiry date is required." + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }

                Toast.makeText(getActivity(), "Order Submitted", Toast.LENGTH_SHORT).show();
                btn.setText("Edit & Re - Submit");
                btn.setBackgroundTintList(AppCompatResources.getColorStateList(getContext(), R.color.green));
//                myButton.backgroundTintList = AppCompatResources.getColorStateList(getContext(), R.color.green);
//
//
//                btn.setTint

            }
        });



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


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView2);
        mapFragment.getMapAsync(this);



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,  data);
        if(requestCode == 101){
            bitmap = (Bitmap) data.getExtras().get("data");

            bitmap = rotateBitmap(bitmap , 90);

            imageView.setImageBitmap(bitmap);
        }


    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
        original.recycle();
        return rotatedBitmap;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

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

//        for(int i = 0 ; i < destinations.length ; i++){
//            mMap.addMarker(new MarkerOptions().position(destinations[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(menuItems[i]));
//        }
        //0...Findroutes(curr, curr2);

        //getMyLocation();


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
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(ltlng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("pick up location"));

                    isNew = false;
                }


            }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("pick up location"));

                Log.d("here1" , "trigger");

            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub

            }
        });

    }



}