package com.example.fileagoapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Favorites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String uuid;
    private FloatingActionButton extendedFloatingActionButton;
    private FloatingActionButton fab1,fab2;
    private RecyclerView dataview;
    private ArrayList<data> dataArrayList;
    private DataAdapter dataAdapter;
    private String token;
    private String msg;
    private String fileaccesskey;
    private NavigationView navigationView;
    private Boolean isfalse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_your_stuff);
        fab1=findViewById(R.id.fileupload);
        fab2=findViewById(R.id.createfolder);
        fab1.hide();
        fab2.hide();
        extendedFloatingActionButton=findViewById(R.id.actions);
        extendedFloatingActionButton.hide();
        isfalse=false;
        navigationView=findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu=navigationView.getMenu();
        MenuItem components=menu.findItem(R.id.componentname);

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
        fileaccesskey=getIntent().getStringExtra("filekey");
        getSupportActionBar().setTitle("Favorites");
        token=getIntent().getStringExtra("token");
        final String ss=token;
        uuid=getIntent().getStringExtra("groupuuid");
        msg=getIntent().getStringExtra("group");
        dataview=findViewById(R.id.folderslist);
        dataArrayList=new ArrayList<>();
        dataAdapter=new DataAdapter(dataArrayList,Favorites.this,token,"","",fileaccesskey);
        dataview.setLayoutManager(new LinearLayoutManager(this));
        dataview.setAdapter(dataAdapter);
        if(msg!=null){
            getfolders(ss,"favorites:"+uuid);
        }
        else{
            getfolders(ss,"favorites");
        }
        dataAdapter.notifyDataSetChanged();
    }
    private void getfolders(String token, String uuid) {
        retrofit2.Call<PersonalWorkspace> call=RetrofitClient.getApiInterface().getalldata("Bearer "+token,uuid);
        call.enqueue(new Callback<PersonalWorkspace>() {
            @Override
            public void onResponse(retrofit2.Call<PersonalWorkspace> call, Response<PersonalWorkspace> response) {
                if(response.isSuccessful()){
                    Toast.makeText(Favorites.this, "Succesfull", Toast.LENGTH_SHORT).show();
                    PersonalWorkspace workSpc= response.body();
                    ArrayList<data> folders=workSpc.getData();
                    String status=response.body().getStatus();
                    System.out.println(status);
                    for(int i=0;i<folders.size();i++){
                        dataArrayList.add(new data(folders.get(i).getName(),folders.get(i).getUuid(),folders.get(i).getType(),folders.get(i).getUpdated(),folders.get(i).getSize()));
                    }
                    dataAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(Favorites.this, uuid, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PersonalWorkspace> call, Throwable t) {
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.private_navigate:
                Intent i = new Intent(Favorites.this, PrivateShares.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",uuid);
                i.putExtra("group",msg);
                startActivity(i);
                break;
            case R.id.public_navigate:
                i=new Intent(Favorites.this,PublicShares.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",uuid);
                i.putExtra("group",msg);
                startActivity(i);
                break;
            case R.id.fav_navigate:
                i=new Intent(Favorites.this,Favorites.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",uuid);
                i.putExtra("group",msg);
                startActivity(i);
                break;
            case R.id.shared_navigate:
                i=new Intent(Favorites.this,SharedWithYou.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",uuid);
                i.putExtra("group",msg);
                startActivity(i);
                break;
            case R.id.trash_navigate:
                i=new Intent(Favorites.this,Trash.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",uuid);
                i.putExtra("group",msg);
                startActivity(i);
                break;
            case R.id.incoming_navigate:
                i=new Intent(Favorites.this,Incoming.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",uuid);
                i.putExtra("group",msg);
                startActivity(i);
                break;
            case R.id.home_navigate:
                if(msg!=null) {
                    i = new Intent(Favorites.this, GroupDataOnce.class);
                    i.putExtra("token", token);
                    i.putExtra("uuid", uuid);
                    i.putExtra("name", "Home");
                    startActivity(i);
                    break;
                }
                else{
                    Intent intent=new Intent(Favorites.this,Workspace.class);
                    intent.putExtra("token",token);
                    startActivity(intent);
                    break;
                }
        }
        return true;
    }
}