package com.example.fileagoapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class GroupDataOnce extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String groupuuid;
    private FloatingActionButton extendedFloatingActionButton;
    private FloatingActionButton fab1,fab2;
    private RecyclerView dataview;
    private ArrayList<data> dataArrayList;
    private GroupDataAdapter dataAdapter;
    private String token;
    private Intent i;
    private String actionbartittle;
    private Boolean isfalse;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_your_stuff);
        fab1=findViewById(R.id.fileupload);
        fab2=findViewById(R.id.createfolder);
        fab1.hide();
        fab2.hide();
        extendedFloatingActionButton=findViewById(R.id.actions);
        isfalse=false;
        extendedFloatingActionButton.show();
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isfalse) {
                    fab1.show();
                    fab2.show();
                    isfalse = true;
                }
                else{
                    fab1.hide();
                    fab2.hide();
                    isfalse=false;
                }
            }
        });
        token=getIntent().getStringExtra("token");
        final String ss=token;
        groupuuid=getIntent().getStringExtra("uuid");
        dataview=findViewById(R.id.folderslist);
        dataArrayList=new ArrayList<>();
        actionbartittle=getIntent().getStringExtra("groupname");
        getSupportActionBar().setTitle(actionbartittle);
        navigationView=findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu=navigationView.getMenu();
        MenuItem components=menu.findItem(R.id.componentname);
        components.setTitle(actionbartittle);
        dataAdapter=new GroupDataAdapter(dataArrayList,GroupDataOnce.this,token,actionbartittle,groupuuid);
        dataview.setLayoutManager(new LinearLayoutManager(this));
        dataview.setAdapter(dataAdapter);
        getfolders(ss,groupuuid);
        dataAdapter.notifyDataSetChanged();
    }
    private void getfolders(String token, String uuid) {
        retrofit2.Call<PersonalWorkspace> call=RetrofitClient.getApiInterface().getallgroupsdata("Bearer "+token,uuid);
        call.enqueue(new Callback<PersonalWorkspace>() {
            @Override
            public void onResponse(retrofit2.Call<PersonalWorkspace> call, Response<PersonalWorkspace> response) {
                if(response.isSuccessful()){
                    Toast.makeText(GroupDataOnce.this, "Succesfull", Toast.LENGTH_SHORT).show();
                    PersonalWorkspace workSpc= response.body();
                    ArrayList<data> folders=workSpc.getData();
                    String status=response.body().getStatus();
                    System.out.println(status);
                    for(int i=0;i<folders.size();i++){
                        dataArrayList.add(new data(folders.get(i).getName(),folders.get(i).getUuid()));
                    }
                    dataAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(GroupDataOnce.this, uuid, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PersonalWorkspace> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.private_navigate:
                Toast.makeText(GroupDataOnce.this, "Private ShARES CLICKED", Toast.LENGTH_SHORT).show();
                 i=new Intent(GroupDataOnce.this,PrivateShares.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.public_navigate:
                Toast.makeText(GroupDataOnce.this, "Private ShARES CLICKED", Toast.LENGTH_SHORT).show();
                 i=new Intent(GroupDataOnce.this,PublicShares.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.fav_navigate:
                Toast.makeText(GroupDataOnce.this, "Private ShARES CLICKED", Toast.LENGTH_SHORT).show();
                 i=new Intent(GroupDataOnce.this,Favorites.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.shared_navigate:
                Toast.makeText(GroupDataOnce.this, "Private ShARES CLICKED", Toast.LENGTH_SHORT).show();
                i=new Intent(GroupDataOnce.this,SharedWithYou.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.trash_navigate:
                Toast.makeText(GroupDataOnce.this, "Private ShARES CLICKED", Toast.LENGTH_SHORT).show();
                i=new Intent(GroupDataOnce.this,Trash.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.incoming_navigate:
                Toast.makeText(GroupDataOnce.this, "Private ShARES CLICKED", Toast.LENGTH_SHORT).show();
                i=new Intent(GroupDataOnce.this,Incoming.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
        }
        return true;
    }
}