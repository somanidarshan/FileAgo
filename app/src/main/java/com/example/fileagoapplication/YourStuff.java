package com.example.fileagoapplication;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class YourStuff extends Fragment {
    private String string;
    private FloatingActionButton extendedFloatingActionButton;
    private FloatingActionButton fab1,fab2;
    private RecyclerView dataview;
    private ArrayList<data> dataArrayList;
    private DataAdapter dataAdapter;
    Boolean isfalse;
    private String fileaccesskey;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_your_stuff, container, false);
        dataview=view.findViewById(R.id.folderslist);
        fab1=view.findViewById(R.id.fileupload);
        fab2=view.findViewById(R.id.createfolder);
        fab1.hide();
        fab2.hide();
        extendedFloatingActionButton=view.findViewById(R.id.actions);
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
        Bundle data=getArguments();
        if(data!=null){
            string =data.getString("token");
            fileaccesskey=data.getString("filekey");
        }
        String home="home";
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewfolderfunction(string,home);
            }
        });
        dataArrayList=new ArrayList<>();
        dataAdapter=new DataAdapter(dataArrayList,getContext(),string,"","",fileaccesskey);
        dataview.setLayoutManager(new LinearLayoutManager(getContext()));
        dataview.setAdapter(dataAdapter);
        getData(string);
        dataAdapter.notifyDataSetChanged();
        return view;
    }

    private void createnewfolderfunction(String string, String home) {
        dialogBuilder=new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Enter the Name of the Folder");
        final EditText foldername=new EditText(getContext());
        foldername.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setView(foldername);
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DIRNAME dirname = new DIRNAME();
                dirname.setFoldername(foldername.getText().toString());
                Call<Void> call=RetrofitClient.getApiInterface().createfolder("Bearer "+string,home,dirname);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            dataArrayList.clear();
                            fab1.hide();
                            fab2.hide();
                            Intent i=new Intent(getContext(),Workspace.class);
                            i.putExtra("token",string);
                            startActivity(i);
                            ((Activity)getContext()).finish();
                        }
                        else{
                            Toast.makeText(getContext(), "Something went Wrong!!Please Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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

    private void getData(String string) {
        retrofit2.Call<PersonalWorkspace> call=RetrofitClient.getApiInterface().getdirectory("Bearer "+string);
        call.enqueue(new Callback<PersonalWorkspace>() {
            @Override
            public void onResponse(retrofit2.Call<PersonalWorkspace> call, Response<PersonalWorkspace> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Succesfull", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PersonalWorkspace> call, Throwable t) {

            }
        });
    }

}