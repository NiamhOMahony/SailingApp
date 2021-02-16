package com.niamh.sailing3app.SafetyCRUD.ShowSafetyList;

//Adapted from Michael Gleesons lecture on 12/11/2020 gleeson.io

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.niamh.sailing3app.Database.DatabaseQueryClass;
import com.niamh.sailing3app.MainActivity;
import com.niamh.sailing3app.R;
import com.niamh.sailing3app.SafetyCRUD.CreateSafety.Safety;
import com.niamh.sailing3app.SafetyCRUD.CreateSafety.SafetyCreateDialogFragment;
import com.niamh.sailing3app.SafetyCRUD.CreateSafety.SafetyCreateListener;
import com.niamh.sailing3app.SettingsActivity;
import com.niamh.sailing3app.Utils.Config;

import java.util.ArrayList;
import java.util.List;

public class SafetyListActivity extends AppCompatActivity implements SafetyCreateListener {



    private final DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    EditText inputSearch;
    ImageView killcord;
    ImageView grabbag;
    ImageView vhf;
    ImageView firstaid;
    ImageView dinghy;
    ImageView anchor;
    ImageView bouy;
    TextView all;
    CharSequence search = "";

    CharSequence KillCord = "killcord";
    CharSequence GrabBag = "grabbag";
    CharSequence VHF = "vhf";
    CharSequence FirstAid = "firstaid";
    CharSequence SafetyBoat = "safetyboat";
    CharSequence Anchor = "anchor";
    CharSequence Bouy = "bouy";


    private List<Safety> safetyList = new ArrayList<>();

    //Passing information to the main content activity
    private TextView safetyListEmptyTextView;
    private SafetyListRecyclerViewAdapter safetyListRecyclerViewAdapter;

    //When class is created the folowing attributes get their values passed to them
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        safetyListEmptyTextView = findViewById(R.id.emptyListTextView);

        killcord = findViewById(R.id.killcord);
        grabbag = findViewById(R.id.grabbag);
        vhf = findViewById(R.id.vhf);
        firstaid = findViewById(R.id.firstaid);
        dinghy = findViewById(R.id.dinghy);
        anchor = findViewById(R.id.anchor);
        bouy = findViewById(R.id.bouy);
        all = findViewById(R.id.all);

        inputSearch = findViewById(R.id.inputSearch);
        inputSearch.setHint("Search Safety Equipment Descriptions");
        //loadSearchList();

        safetyList.addAll(databaseQueryClass.getAllSafety());

        // making the recycler split in two rows
        //adapted from https://www.youtube.com/watch?v=BrLnsDkoba0
        safetyListRecyclerViewAdapter = new SafetyListRecyclerViewAdapter(this, safetyList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(safetyListRecyclerViewAdapter);

        viewVisibility();

        // when bin view selected were are given the option to delete all
        ImageView deleteImageView = findViewById(R.id.deleteImageView);
        deleteImageView.setOnClickListener(v -> onDeleteAllSelected());

        //float action button brings us to the create fragment
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openSafetyCreateDialog());

          /*Code adapted from How to Implement Bottom Navigation With Activities in Android Studio
         | BottomNav | Android Coding https://www.youtube.com/watch?v=JjfSjMs0ImQ*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.menuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menuSettings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menuHome:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menuUser:
                    return true;
            }
            return false;
        });

        //Have to go back to all after each click must fix
        //When all is clicked all items in list show up
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);            }
        });
        //When Killcord image is clicked all items in list that are type killcord show up
        killcord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    safetyListRecyclerViewAdapter.getFilter().filter(search);
                    safetyListRecyclerViewAdapter.getFilter().filter(KillCord);
            }
        });
        //When GrabBag image is clicked all items in list that are type GrabBag show up
        grabbag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);
                safetyListRecyclerViewAdapter.getFilter().filter(GrabBag);
            }
        });
        //When VHF image is clicked all items in list that are type VHF show up
        vhf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);
                safetyListRecyclerViewAdapter.getFilter().filter(VHF);
            }
        });
        //When FirstAid image is clicked all items in list that are type FirstAid show up
        firstaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);
                safetyListRecyclerViewAdapter.getFilter().filter(FirstAid);
            }
        });
        //When dinghy image is clicked all items in list that are type dinghy show up
        dinghy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);
                safetyListRecyclerViewAdapter.getFilter().filter(SafetyBoat);
            }
        });
        //When anchor image is clicked all items in list that are type anchor show up
        anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);
                safetyListRecyclerViewAdapter.getFilter().filter(Anchor);

            }
        });
        //When bouy image is clicked all items in list that are type bouy show up
        bouy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safetyListRecyclerViewAdapter.getFilter().filter(search);
                safetyListRecyclerViewAdapter.getFilter().filter(Bouy);
            }
        });
        //When text is typed into the search bar it has an automatic response by changing the recycler View
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                safetyListRecyclerViewAdapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //delete all fragment
    private void onDeleteAllSelected() {
        //When delete menu action is clicked the program asks if youre sure
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to delete all Safety Equipment?");
        //If you select yes after the warning the table clears
        alertDialogBuilder.setPositiveButton("Yes",
                (arg0, arg1) -> {
                    boolean isAllDeleted = databaseQueryClass.deleteAllSafety();
                    if(isAllDeleted){
                        safetyList.clear();
                        safetyListRecyclerViewAdapter.notifyDataSetChanged();
                        viewVisibility();
                    }
                });

        //If you select no you're brought back to the "Homepage"
        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void viewVisibility() {
        if(safetyList.isEmpty())
            safetyListEmptyTextView.setVisibility(View.VISIBLE);
        else
            safetyListEmptyTextView.setVisibility(View.GONE);
    }

    private void openSafetyCreateDialog() {
        SafetyCreateDialogFragment safetyCreateDialogFragment = SafetyCreateDialogFragment.newInstance(this);
        safetyCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_SAFETY);
    }

    //when new data is added update main recycler view
    @Override
    public void onSafetyCreated(Safety safety) {
        safetyList.add(safety);
        safetyListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Log.d("***NIAMH_FYP***","Create Safety Equip: "+safety.getType());
    }

}