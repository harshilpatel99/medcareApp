package com.hurdle.medcare;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_design,new Homefragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistner=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_explore:

                    getSupportActionBar().show();
                    getSupportActionBar().setElevation(0);

                    selectFragment=new Explorefragment();
                    break;
                case R.id.nav_appointments:

                    getSupportActionBar().show();
                    getSupportActionBar().setElevation(0);
                    selectFragment=new Appointmentfragment();
                    break;
                case R.id.nav_home:
                    getSupportActionBar().hide();
                    selectFragment = new Homefragment();
                    break;
                case R.id.nav_doctors:

                    getSupportActionBar().hide();
                    getSupportActionBar().setElevation(0);
                    selectFragment = new Doctorfragment();
                    break;
                case R.id.nav_profile:

                    getSupportActionBar().show();
                    getSupportActionBar().setElevation(0);
                    selectFragment = new Profilefragment();
                    break;
              /*  case R.id.nav_account:
                    getSupportActionBar().setElevation(0);
                    selectFragment = new Accountfragment();
                    break;*/
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_design,selectFragment).commit();

            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_head,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_signout:
                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("Confirm Signout");
                builder.setMessage("Do you really want to Sign out?");
                builder.setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                            }
                        }).show();

                break;

        }
        return true;
    }
}
