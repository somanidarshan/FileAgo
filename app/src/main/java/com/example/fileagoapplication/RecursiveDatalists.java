package com.example.fileagoapplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;
public class RecursiveDatalists extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String uuid;
    private AlertDialog.Builder dialogBuilder;
    private String  actionbartittle;
    private EditText foldername;
    private String fileaccesskey;
    private Button createfolder,cancel;
    private FloatingActionButton extendedFloatingActionButton;
    private FloatingActionButton fab1,fab2;
    private RecyclerView dataview;
    private ArrayList<data> dataArrayList;
    private DataAdapter dataAdapter;
    private String token;
    private ArrayList<String> copieduuids=new ArrayList<>();
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
        components.setTitle("Your Stuff");
        fileaccesskey=getIntent().getStringExtra("filekey");
        actionbartittle=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(actionbartittle);

        token=getIntent().getStringExtra("token");
        final String ss=token;
        uuid=getIntent().getStringExtra("uuid");

        //Fetching Data
        dataview=findViewById(R.id.folderslist);
        foldername=findViewById(R.id.foldername);
        createfolder=findViewById(R.id.createbutton);
        Call<MyAccessWorkspace> call=RetrofitClient.getApiInterface().myaccess("Bearer "+token,uuid);
        call.enqueue(new Callback<MyAccessWorkspace>() {
            @Override
            public void onResponse(Call<MyAccessWorkspace> call, Response<MyAccessWorkspace> response) {
                if(response.isSuccessful()){
                    MyAccessWorkspace myAccessWorkspace=response.body();
                    String[] access=myAccessWorkspace.getData();
                    Set<String> ss=new HashSet();
                    Log.e("MYAccess",access[1]);
                    for(int i=0;i<access.length;i++){
                        ss.add(access[i]);
                    }
                    if(!ss.contains("write")){
                            extendedFloatingActionButton.hide();
                    }
                    else{
                        extendedFloatingActionButton.show();

                        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!isfalse) {
                                    fab1.show();
                                    fab2.show();
                                    isfalse = true;
                                }
                                else {
                                    fab1.hide();
                                    fab2.hide();
                                    isfalse = false;
                                }

                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<MyAccessWorkspace> call, Throwable t) {
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewfolderfunction(token,uuid);
                }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RecursiveDatalists.this,UploadFilestoServer.class);
                i.putExtra("token",token);
                i.putExtra("uuid",uuid);
                i.putExtra("name",actionbartittle);
                i.putExtra("filekey",fileaccesskey);
                startActivity(i);
                finish();
            }
        });
        dataArrayList=new ArrayList<>();
        dataAdapter=new DataAdapter(dataArrayList,RecursiveDatalists.this,token,uuid,actionbartittle,fileaccesskey);
        dataview.setLayoutManager(new LinearLayoutManager(this));
        dataview.setAdapter(dataAdapter);
        getfolders(ss,uuid);
        dataAdapter.notifyDataSetChanged();
    }

    private void createnewfolderfunction(String token, String uuid) {
        dialogBuilder=new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Enter the Name of the Folder");
        final EditText foldername=new EditText(RecursiveDatalists.this);
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
                            Toast.makeText(RecursiveDatalists.this, "Success", Toast.LENGTH_SHORT).show();
                            dataArrayList.clear();
                            fab1.hide();
                            fab2.hide();
                            Intent i=new Intent(RecursiveDatalists.this,RecursiveDatalists.class);
                            i.putExtra("token",token);
                            i.putExtra("uuid",uuid);
                            i.putExtra("name",actionbartittle);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(RecursiveDatalists.this, "Something went Wrong!!Please Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RecursiveDatalists.this, "Failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RecursiveDatalists.this, "Succesfull", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RecursiveDatalists.this, uuid, Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(RecursiveDatalists.this, PrivateShares.class);
                i.putExtra("token",token);
                startActivity(i);
                break;
            case R.id.public_navigate:
                i=new Intent(RecursiveDatalists.this,PublicShares.class);
                i.putExtra("token",token);
                startActivity(i);
                break;
            case R.id.fav_navigate:
                i=new Intent(RecursiveDatalists.this,Favorites.class);
                i.putExtra("token",token);
                startActivity(i);
                break;
            case R.id.shared_navigate:
                i=new Intent(RecursiveDatalists.this,SharedWithYou.class);
                i.putExtra("token",token);
                startActivity(i);
                break;
            case R.id.trash_navigate:
                i=new Intent(RecursiveDatalists.this,Trash.class);
                i.putExtra("token",token);
                startActivity(i);
                break;
            case R.id.incoming_navigate:
                i=new Intent(RecursiveDatalists.this,Incoming.class);
                i.putExtra("token",token);
                startActivity(i);
                break;
            case R.id.home_navigate:
                Intent intent=new Intent(RecursiveDatalists.this,Workspace.class);
                intent.putExtra("token",token);
                startActivity(intent);
                break;
            case R.id.logout_navigate:
                Intent intent2=new Intent(RecursiveDatalists.this,HomeActivity.class);
                intent2.putExtra("Logout","Logout");
                startActivity(intent2);
                finish();
                break;
        }
        return true;
    }

}


/*
 copieduuids=getIntent().getStringArrayListExtra("datacopied");
        if(copieduuids!=null) {
            if (copieduuids.size() != 0) {
                fab3.show();
                fab3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < copieduuids.size(); i++) {
                            MoveNode moveNode = new MoveNode();
                            moveNode.setMove_to(uuid);
                            Call<Void> call = RetrofitClient.getApiInterface().movenode("Bearer "+token,copieduuids.get(i),moveNode);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(RecursiveDatalists.this, "Copied Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RecursiveDatalists.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(RecursiveDatalists.this, "Failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }
 */