package com.hurdle.medcare;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.hurdle.medcare.R.*;

public class  Profilefragment extends Fragment {
    ImageView ppic;
    TextView pname;
    TextView plocation;
    LocationManager locationManager;
    ArrayList<CustomSpinner> mspinner;
    SpinnerAdapter spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(layout.profile, container, false);
        ppic=(ImageView)v.findViewById(id.profile_pic);
        pname=(TextView)v.findViewById(id.p_name);
        plocation=v.findViewById(R.id.p_location);
        Spinner spinner = v.findViewById(R.id.name_spinner);
        getlocation();
        initlist();
        spinnerAdapter=new SpinnerAdapter(getActivity(),mspinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinner clicked=(CustomSpinner) parent.getItemAtPosition(position);
                String clickname=clicked.getPatientname();
                Toast.makeText(getActivity(),clickname,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String personName = user.getDisplayName();
            String personEmail = user.getEmail();
            String personId = user.getProviderId();
            //String uip=user.getPhotoUrl().toString();
            if(personName==null){
                pname.setText(personEmail);
            }
            else {
                pname.setText(personName);
            }
            if(user.getPhotoUrl()==null){
                ppic.setImageResource(R.drawable.ic_person);
            }
            else {
                Glide.with(this).load(user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null).into(ppic);
            }
        }
        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            pname.setText(personName);
            ppic.setImageURI(null);
            ppic.setImageURI(personPhoto);
            Glide.with(this).load((acct.getPhotoUrl()).toString()).into(ppic);
        }*/
        return v;
    }

    private void initlist() {
        mspinner = new ArrayList<>();
        mspinner.add(new CustomSpinner("Harshil Patel"));
        mspinner.add(new CustomSpinner("Nihal Shubhash"));
    }

    private void getlocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                //   patientlocation.setText(provider);
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
                    String city=address.get(0).getLocality();
                    String country=address.get(0).getCountryName();
                    plocation.setText(city+", "+country);

                } catch (IOException e) {
                    // Handle IOException
                } catch (NullPointerException e) {
                    // Handle NullPointerException
                }
            }
            else {
                plocation.setText("Unknown");
            }
        }catch (Exception e){}
    }


    private void requestlocationpermission() {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
}
