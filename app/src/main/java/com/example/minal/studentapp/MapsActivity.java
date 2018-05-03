package com.example.minal.studentapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static String Destination=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // mbna "20000" e3dady 30.027191, 31.212042
        // mbna "3000" 3mara 30.026055, 31.211835
        // mbna "18000" el bsen 30.024859, 31.213143
        // mbna "14000" mechanica design el gded 30.024676, 31.212431
        // mbna "13000" wr4a 30.025006, 31.212397
        // mbna "19000" mechanica syarat 30.025017, 31.211969
        // mbna "17000" mechanica power el gede 30.024948, 31.211773
        // mbna "16000" mbna khrba 30.024983, 31.211549
        // mbna "9000" el mktba 30.025305, 31.211501
        // mbna "11000" mechanica thermo 30.025467, 31.212434
        // mbna "10000" mbna mechanica 30.025468, 31.212134
        // mbna "12000" nady el kolya 30.025538, 31.212868
        // mbna "4000" mbna madany 30.026288, 31.212302
        // mbna "2000" mbna el 2dara 30.026471, 31.211669
        // mbna "1000" mbna hndsa 2n4a2ya 30.026942, 31.211739
        // mbna "8000" mbna 2tslat 30.025731, 31.211181
        // mbna "7000" modrag el sawy 30.025683, 31.211562
        // mbna "40000" tyaran 30.024303, 31.211393
        // mbna "33000" mbna chemya 30.024553, 31.210561
        // mbna "32000" mbna mnagem we petrol 30.024599, 31.209837
        // mbna "31000" mbna ray 30.025318, 31.209821
        // mbna "34000" mbna 5was el mwad / m3mal 2b7as mechanica el torba wel 2sasat 30.025347, 31.210588
        // mbna "35000" mbna 5rsana 30.025693, 31.210431
        // mbna "30000" m3mal 5was el mwad 30.025445, 31.210309
        // mbna "36000" m3mal 2b7as mechanica el torba wel 2sasat el gded 30.025635, 31.210086
        // Add a marker in Sydney and move the camera
        LatLng Structure = new LatLng(30.026942, 31.211739); // 1000
        mMap.addMarker(new MarkerOptions().position(Structure).title(" [1] مبنى الهندسة الانشائية"));
        LatLng Management = new LatLng(30.026471, 31.211669); // 2000
        mMap.addMarker(new MarkerOptions().position(Management).title(" [2] المبنى الاداري"));
        LatLng Architecture = new LatLng(30.026055, 31.211835); // 3000
        mMap.addMarker(new MarkerOptions().position(Architecture).title(" [3] مبنى الهندسة المعمارية والاشغال العامة والهندسة الحيوية الطبية والمنظومات وهندسة الحاسبات"));
        LatLng Civil = new LatLng(30.026288, 31.212302); // 4000
        mMap.addMarker(new MarkerOptions().position(Civil).title(" [4] مبنى الهندسة المدنية"));
        LatLng ElSway = new LatLng(30.025683, 31.211562); // 7000
        mMap.addMarker(new MarkerOptions().position(ElSway).title(" [7] مدرج الصاوي"));
        LatLng Communication = new LatLng(30.025731, 31.211181); // 8000
        mMap.addMarker(new MarkerOptions().position(Communication).title(" [8] مبنى هندسة الالكترونيات ولالتصالات الكهربية"));
        LatLng Library = new LatLng(30.025305, 31.211501); // 9000
        mMap.addMarker(new MarkerOptions().position(Library).title(" [9] المكتبة"));
        LatLng MechanicalDesign = new LatLng(30.025468, 31.212134); // 10000
        mMap.addMarker(new MarkerOptions().position(MechanicalDesign).title(" [10] مبنى التصميم الميكانيكي والانتاج"));
        LatLng Thermo = new LatLng(30.025467, 31.212434); // 11000
        mMap.addMarker(new MarkerOptions().position(Thermo).title(" [11] مبنى هندسة القوى الميكانيكية (مبنى الحرارة)"));
        LatLng Club = new LatLng(30.025538, 31.212868); // 12000
        mMap.addMarker(new MarkerOptions().position(Club).title(" [12] مبنى نادي الكلية"));
        LatLng Garage = new LatLng(30.025006, 31.212397); // 13000
        mMap.addMarker(new MarkerOptions().position(Garage).title(" [13] ورش الكلية"));
        LatLng NewMechanicalDesign = new LatLng(30.024676, 31.212431); // 14000
        mMap.addMarker(new MarkerOptions().position(NewMechanicalDesign).title(" [14] مبنى التصميم الميكانيكي الجديد"));
        LatLng Electricity = new LatLng(30.024983, 31.211549); // 16000
        mMap.addMarker(new MarkerOptions().position(Electricity).title(" [16] مبنى هندسة القوى والالات الكهربية"));
        LatLng NewMechanicalPower = new LatLng(30.024948, 31.211773); // 17000
        mMap.addMarker(new MarkerOptions().position(NewMechanicalPower).title(" [17] مبنى هندسة القوى الميكانيكية الجديدة"));
        LatLng Pool = new LatLng(30.024859, 31.213143); // 18000
        mMap.addMarker(new MarkerOptions().position(Pool).title(" [18] مبنى الخدامات الطلابية"));
        LatLng Vehicles = new LatLng(30.025017, 31.211969); // 19000
        mMap.addMarker(new MarkerOptions().position(Vehicles).title(" [19] مبنى هندسة القوى الميكانيكية (سيارات)"));
        LatLng Prep = new LatLng(30.027191, 31.212042); // 20000
        mMap.addMarker(new MarkerOptions().position(Prep).title(" [20] مبنى الرياضيات والفيزيقا الهندسية"));
        LatLng MaterialsLab = new LatLng(30.025445, 31.210309); // 30000
        mMap.addMarker(new MarkerOptions().position(MaterialsLab).title(" [30] معمل خواص المواد"));
        LatLng Hydro = new LatLng(30.025318, 31.209821); // 31000
        mMap.addMarker(new MarkerOptions().position(Hydro).title(" [31] مبنى الري والهايدروليكا"));
        LatLng Petrol = new LatLng(30.024599, 31.209837); // 32000
        mMap.addMarker(new MarkerOptions().position(Petrol).title(" [32] مبنى هندسة المناجم والبترول والفلزات"));
        LatLng Chem = new LatLng(30.024553, 31.210561); // 33000
        mMap.addMarker(new MarkerOptions().position(Chem).title(" [33] مبنى الهندسة الكيميائية"));
        LatLng Materials = new LatLng(30.025347, 31.210588); // 34000
        mMap.addMarker(new MarkerOptions().position(Materials).title(" [34] مبنى خواص المواد"));
        LatLng Concrete = new LatLng(30.025693, 31.210431); // 35000
        mMap.addMarker(new MarkerOptions().position(Concrete).title(" [35] مبنى خرسانة"));
        LatLng ResearchLab = new LatLng(30.025635, 31.210086); // 36000
        mMap.addMarker(new MarkerOptions().position(ResearchLab).title(" [36] معمل ابحاث ميكانيكا التربة والاساسات الجديد"));
        LatLng Aero = new LatLng(30.024303, 31.211393); // 40000
        mMap.addMarker(new MarkerOptions().position(Aero).title(" [40] مبنى هندسة الطيران"));
        //LatLng sydney = new LatLng(30.026288, 31.211728);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.026288, 31.211728), 18.0f));
    }
}
