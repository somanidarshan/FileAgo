package com.example.fileagoapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RecursiveGroupDatalist extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String uuid;
    private String groupuuid;
    private FloatingActionButton extendedFloatingActionButton;
    private FloatingActionButton fab1,fab2;
    private RecyclerView dataview;
    private ArrayList<data> dataArrayList;
    private GroupDataAdapter dataAdapter;
    private String token;
    private String actionbartittle;
    private String component;
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
        isfalse=false;
        navigationView=findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu=navigationView.getMenu();
        MenuItem components=menu.findItem(R.id.componentname);
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
          actionbartittle=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(actionbartittle);
        groupuuid=getIntent().getStringExtra("groupuuid");
        token=getIntent().getStringExtra("token");
        final String ss=token;
        uuid=getIntent().getStringExtra("uuid");
        dataview=findViewById(R.id.folderslist);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewfolderfunction(token,uuid);
            }
        });
        dataArrayList=new ArrayList<>();
        component=getIntent().getStringExtra("components");
        components.setTitle(component);
        dataAdapter=new GroupDataAdapter(dataArrayList,RecursiveGroupDatalist.this,token,component,groupuuid);
        dataview.setLayoutManager(new LinearLayoutManager(this));
        dataview.setAdapter(dataAdapter);
        getfolders(ss,uuid);
        dataAdapter.notifyDataSetChanged();
    }
    private void createnewfolderfunction(String token, String uuid) {
      AlertDialog.Builder  dialogBuilder=new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Enter the Name of the Folder");
        final EditText foldername=new EditText(RecursiveGroupDatalist.this);
        foldername.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setView(foldername);
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DIRNAME dirname = new DIRNAME();
                dirname.setFoldername(foldername.getText().toString());
                Call<Void> call=RetrofitClient.getApiInterface().createfolder("Bearer "+token,uuid,dirname);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(RecursiveGroupDatalist.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RecursiveGroupDatalist.this, "Something went Wrong!!Please Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RecursiveGroupDatalist.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialogBuilder.show();
    }
    private void getfolders(String token, String uuid) {
        retrofit2.Call<PersonalWorkspace> call=RetrofitClient.getApiInterface().getalldata("Bearer "+token,uuid);
        call.enqueue(new Callback<PersonalWorkspace>() {
            @Override
            public void onResponse(retrofit2.Call<PersonalWorkspace> call, Response<PersonalWorkspace> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RecursiveGroupDatalist.this, "Succesfull", Toast.LENGTH_SHORT).show();
                    PersonalWorkspace workSpc= response.body();
                    ArrayList<data> folders=workSpc.getData();
                    String status=response.body().getStatus();
                    System.out.println(status);
                    for(int i=0;i<folders.size();i++){
                        dataArrayList.add(new data(folders.get(i).getName(),folders.get(i).getUuid(),folders.get(i).getType()));
                    }
                    dataAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(RecursiveGroupDatalist.this, uuid, Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(RecursiveGroupDatalist.this, PrivateShares.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.public_navigate:
                i=new Intent(RecursiveGroupDatalist.this,PublicShares.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.fav_navigate:
                i=new Intent(RecursiveGroupDatalist.this,Favorites.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.shared_navigate:
                i=new Intent(RecursiveGroupDatalist.this,SharedWithYou.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.trash_navigate:
                i=new Intent(RecursiveGroupDatalist.this,Trash.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.incoming_navigate:
                i=new Intent(RecursiveGroupDatalist.this,Incoming.class);
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                startActivity(i);
                break;
            case R.id.home_navigate:
                i=new Intent(RecursiveGroupDatalist.this,GroupDataOnce.class);
                i.putExtra("token",token);
                i.putExtra("uuid",groupuuid);
                i.putExtra("name",actionbartittle);
                startActivity(i);
        }
        return true;
    }
}