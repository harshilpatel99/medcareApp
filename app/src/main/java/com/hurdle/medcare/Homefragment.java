package com.hurdle.medcare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Homefragment extends Fragment{
    TextView pname;
    ImageView ppic;
    TextView plocation;
    LocationManager locationManager;
    String value;
    ProgressBar progressBar1;

    LinearLayout linearLayout;
    AutoCompleteTextView s1,s2,s3;
    Button button;
    TextView dtext;
    int index1,index2,index3;           //equivalent index of selected text
    final String[] disease=new String[307];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        pname=(TextView)v.findViewById(R.id.patientname);
        ppic=(ImageView)v.findViewById(R.id.profile_pic);
        plocation = (TextView) v.findViewById(R.id.patientlocation);
        linearLayout=(LinearLayout)v.findViewById(R.id.output);
        progressBar1=(ProgressBar)v.findViewById(R.id.progressBar) ;
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();
            //Write whole code here


        } else {
            requestlocationpermission();
        }



        button=(Button)v.findViewById(R.id.go);
        dtext=(TextView)v.findViewById(R.id.disease_text);

        symptoms();

        for(int i=0;i<307;i++){
            if(disease[i].equals("name")||disease[i].equals("0"))
            {
                disease[i]=" ";
            }
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,disease);
        //   Log.d("SymptomsCheck","adapter: "+symptomsdbList.get(0).getSymptom());
        s1=(AutoCompleteTextView)v.findViewById(R.id.symptom1);
        s2=(AutoCompleteTextView)v.findViewById(R.id.symptom2);
        s3=(AutoCompleteTextView)v.findViewById(R.id.symptom3);

        s1.setThreshold(1);
        s1.setAdapter(adapter);
        s1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index1=java.util.Arrays.asList(disease).indexOf(s1.getText().toString());
            }
        });


        s2.setThreshold(1);
        s2.setAdapter(adapter);
        s2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index2=java.util.Arrays.asList(disease).indexOf(s2.getText().toString());
            }
        });

        s3.setThreshold(1);
        s3.setAdapter(adapter);
        s3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index3=java.util.Arrays.asList(disease).indexOf(s3.getText().toString());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((s1.getText().toString().isEmpty()&&s2.getText().toString().isEmpty())){
                    s1.setError("Enter atleast two symptoms for better prediction");
                }
                else if((s2.getText().toString().isEmpty()&&s3.getText().toString().isEmpty())){
                    s2.setError("Enter atleast two symptoms for better prediction");
                }
                else if((s3.getText().toString().isEmpty()&&s1.getText().toString().isEmpty())){
                    s3.setError("Enter atleast two symptoms for better prediction");
                }
                else {
                    if(s1.getText().toString().isEmpty()){
                        //s1.setText(s2.getText().toString());
                        index1=java.util.Arrays.asList(disease).indexOf(s2.getText().toString());
                    }
                    else if(s2.getText().toString().isEmpty()){
                        //s2.setText(s3.getText().toString());
                        index2=java.util.Arrays.asList(disease).indexOf(s3.getText().toString());
                    }
                    else if(s3.getText().toString().isEmpty()){
                       // s3.setText(s1.getText().toString());
                        index3=java.util.Arrays.asList(disease).indexOf(s1.getText().toString());
                    }
                        diagnose();
                }
            }
        });
        return v;
    }

    private void diagnose() {
        String show="hello" + index1 + " "+ index2+ " " + index3;

        String[] diagnose_d=new String[1538];
        String[][] back_array=new String[5569][3];
        int i1=0;
        String a1;

        InputStream inputStream1=getResources().openRawResource(R.raw.dia_tfi);
        BufferedReader reader1=new BufferedReader(new InputStreamReader(inputStream1,Charset.forName("UTF-8")));
        try{
            reader1.readLine();  //step over one
            diagnose_d[i1]="0";
            i1++;
            String line="";

            while((line=reader1.readLine())!=null){
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                if(tokenizer.hasMoreTokens()){
                    a1=tokenizer.nextToken();
                    diagnose_d[i1]=tokenizer.nextToken();
                }
                else
                {
                    Toast.makeText(getActivity(), "Ohh! Snap!", Toast.LENGTH_SHORT).show();
                }
                Log.d("Array: ", i1 + " " + diagnose_d[i1]);
                i1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////BACK ARRAY
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int i2=0,j2,a2 = 0,b2 = 0,c2 = 0;
        InputStream inputStream2=getResources().openRawResource(R.raw.diffsydiwfg);
        BufferedReader reader2=new BufferedReader(new InputStreamReader(inputStream2,Charset.forName("UTF-8")));
        try{
            reader2.readLine();         //step over head
            String line="";
            while((line=reader2.readLine())!=null){
                j2=0;
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                if(tokenizer.hasMoreTokens()){
                    back_array[i2][j2]=tokenizer.nextToken();
                    a2=j2;
                    j2++;
                    back_array[i2][j2]=tokenizer.nextToken();
                    b2=j2;
                    j2++;
                    back_array[i2][j2]=tokenizer.nextToken();
                    c2=j2;
                }
                else
                {
                    Toast.makeText(getActivity(), "Sorry", Toast.LENGTH_SHORT).show();
                }
                Log.d("Back Array: ",  " " + back_array[i2][a2] + " " + back_array[i2][b2] + " " + back_array[i2][c2]);
                i2++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       // int[] array_i1=new int[85],array_i2=new int[85],array_i3=new int[85];
        List<Integer> array_1=new ArrayList<Integer>();
        List<Integer> array_2=new ArrayList<Integer>();
        List<Integer> array_3=new ArrayList<Integer>();

        for(int i=0;i<5569;i++){
            if(Integer.toString(index1).equals(back_array[i][0])){
                array_1.add(Integer.parseInt(back_array[i][1]));
//                Log.d("display1", String.valueOf(array_1.get(i)));
               // array_i1[k]=Integer.parseInt(back_array[i][1]);

            }
            if(Integer.toString(index2).equals(back_array[i][0])){
                array_2.add(Integer.parseInt(back_array[i][1]));
            //    Log.d("display2", String.valueOf(array_2.get(i)));
            //    array_i2[l]=Integer.parseInt(back_array[i][1]);

            }
            if(Integer.toString(index3).equals(back_array[i][0])){
            //    Log.d("display3", String.valueOf(array_3.get(i)));
                array_3.add(Integer.parseInt(back_array[i][1]));
            //    array_i3[m]=Integer.parseInt(back_array[i][1]);

            }
        }




        Collections.sort(array_1);
        Collections.sort(array_2);
        Collections.sort(array_3);

        for(int i=0;i<array_1.size();i++){
            Log.d("sort1", String.valueOf(array_1.get(i)));
        }

        for(int i=0;i<array_2.size();i++){
            Log.d("sort2", String.valueOf(array_2.get(i)));
        }

        for(int i=0;i<array_3.size();i++){
            Log.d("sort3", String.valueOf(array_3.get(i)));
        }


        /*for(int i=0;i<array_1.size();i++){
            dtext.append(String.valueOf(array_1.get(i)));
            dtext.append("\n");
        }
        dtext.append("\n");
        for(int i=0;i<array_2.size();i++){
            dtext.append(String.valueOf(array_2.get(i)));
            dtext.append("\n");
        }
        dtext.append("\n");
        for(int i=0;i<array_3.size();i++){
            dtext.append(String.valueOf(array_3.get(i)));
            dtext.append("\n");
        }
        dtext.append("\n\n");*/
        Log.d("Show",show);
        dtext.setText("");
        List<Integer> one=new ArrayList<Integer>(array_1);
        one.retainAll(array_2);
        one.retainAll(array_3);
        for(int i=0;i<one.size();i++){
            Log.d("filtered"," "+one.get(i));
            dtext.append("-  "+diagnose_d[one.get(i)]+"\n");
        }
        /////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////Mapping
        ////////////////////////////////////////////////////////////////////////////////

        /*int i_map1=0,j_map2=0,k_map3= 0;

        while (i_map1 < array_1.size() && j_map2< array_2.size() && k_map3 < array_3.size())
        {
            // If x = y and y = z, print any of them and move ahead
            // in all arrays
            if (array_1.get(i_map1) == array_2.get(j_map2) && array_2.get(j_map2) == array_3.get(k_map3))
            {
                dtext.append("-  "+array_1.get(i_map1)+"\n");

                Log.d("filtered",array_1.get(i_map1)+" ");
                i_map1++;
                j_map2++;
                k_map3++;
            }

            // x < y
            else if (array_1.get(i_map1) < array_2.get(j_map2)) {
                i_map1++;
            }

                // y < z
            else if (array_2.get(j_map2) < array_3.get(k_map3)) {
                j_map2++;
            }

                // We reach here when x > y and z < y, i.e., z is smallest
            else {
                k_map3++;
            }
        }*/
        ///////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
       /* progressBar1.setVisibility(VISIBLE);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar1.setVisibility(GONE);
                linearLayout.setVisibility(VISIBLE);
            }
        },1000);*/

        linearLayout.setVisibility(VISIBLE);




    }


    public void symptoms() {

        InputStream inputStream=getResources().openRawResource(R.raw.sym_tf);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
        try {
            int i=0;
            reader.readLine();          //Step over header
            String line="";
            disease[0]="name";
            Log.d("SymptomsCheck","Line: "+disease[i]);
            i++;
            while ((line=reader.readLine())!=null){
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                if(tokenizer.hasMoreTokens()) {
                    String f = tokenizer.nextToken(), s = tokenizer.nextToken();

                    //split by ','

                    //read data
                    Log.d("check", "tokens" + f + " " + s + " ");
                    if (f.length() > 0) {
                        //    symptomsdb.setSid(f);

                    } else {
                        //  symptomsdb.setSid("0");
                    }

                    if (s.length() > 0) {
                        //   symptomsdb.setSymptom(s);
                        String input = s;
                        Log.d("check2", "input: " + i + " " + input);
                        disease[i] = input;
                        Log.d("SymptomsCheck", "Line: " + i + " " + disease[i]);
                        i++;
                    } else {
                        //  symptomsdb.setSymptom("0");
                        disease[i] = "0";
                        Log.d("SymptomsCheck", "Line: " + i + " " + disease[i]);
                        i++;
                    }

                    // symptomsdbList.add(symptomsdb);
                }
                else{
                    Toast.makeText(getActivity(), "Sorry", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void getlocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
             //   patientlocation.setText(provider);
                onLocationchanged(location);
            }
            else {
                plocation.setText("Unknown");
            }
        }catch (Exception e){}
    }

    private void onLocationchanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
         /*   int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }*/
         String city=address.get(0).getLocality();
         String country=address.get(0).getCountryName();
         plocation.setText(city+", "+country);
         value=plocation.getText().toString();
        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    private void requestlocationpermission() {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
}
