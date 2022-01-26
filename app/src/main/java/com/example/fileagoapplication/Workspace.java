package com.example.fileagoapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
public class Workspace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private Intent intent;
    private ActionBarDrawerToggle toggle;
    BottomNavigationView bottomNavigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        drawerLayout=findViewById(R.id.drawer);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        navigationView=findViewById(R.id.navigationview);
        getSupportActionBar().setTitle("Home");
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration
                .Builder(new int[]{R.id.yourStuff,R.id.groups,R.id.chat})
                .build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);
         intent=getIntent();
        String token=intent.getStringExtra("token");
        Menu menu=navigationView.getMenu();
        MenuItem components=menu.findItem(R.id.componentname);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.yourStuff:
                        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        YourStuff yourStuff=new YourStuff();
                        components.setTitle("Your Stuff");
                        getSupportActionBar().setTitle("Your Stuff");
                        Bundle data=new Bundle();
                        data.putString("token",token);
                        yourStuff.setArguments(data);
                        fragmentTransaction.replace(R.id.nav_host_fragment,yourStuff).commit();
                        break;
                    case R.id.groups:
                       FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                       Groups groups=new Groups();
                        Bundle group=new Bundle();
                        components.setTitle("Groups");
                        getSupportActionBar().setTitle("Groups");
                        group.putString("token",token);
                        groups.setArguments(group);
                        fragmentTransaction1.replace(R.id.nav_host_fragment,groups).commit();
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
                case R.id.home_navigate:
                    intent=getIntent();
                    String token=intent.getStringExtra("token");
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    YourStuff yourStuff=new YourStuff();
                    Bundle data=new Bundle();
                    data.putString("token",token);
                    yourStuff.setArguments(data);
                    fragmentTransaction.replace(R.id.nav_host_fragment,yourStuff).commit();
                    break;
        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp(){
        return NavigationUI.navigateUp(navController,appBarConfiguration);
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


}