package com.niamh.sailing3app.SafetyCRUD.CreateSafety;

/*
 * Adapted from Michael Gleesons lecture on 12/11/2020 gleeson.io
 */

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.niamh.sailing3app.Database.DatabaseQueryClass;
import com.niamh.sailing3app.R;
import com.niamh.sailing3app.Utils.Config;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class SafetyCreateDialogFragment extends DialogFragment {
    //Creating a new Safety Equipment

    //adding in external listener
    private static SafetyCreateListener safetyCreateListener;

    //declaring variables
    private Spinner typeSpinner;
    private EditText descriptionEditText;
    private Spinner availableSpinner;
    private Spinner faultSpinner;
    private Spinner signOutSpinner;
    private Spinner signOutFSpinner;
    private EditText faultEditText;
    private ImageView safetyImageView;
    private TextView availableTextView;
    private TextView faultTextView;
    private View typeIndicatorView;

    private Bitmap imgToStore;

    //giving variables values
    private String typeString = "";
    private String descriptionString = "";
    private String availableString = "";
    private String availuserString = "";
    private String faultString = "";
    private String faultUserString = "";
    private String faultdescString = "";

   // public String safetyColor; //need to give value in SQLtable
    private String selectedSafetyColor;

    private  static final  int PICK_IMAGE_REQUEST = 104;

    public SafetyCreateDialogFragment() {
        // Required empty public constructor
    }

    //when a new instance of the create fragment is opened it processes the below
    public static SafetyCreateDialogFragment newInstance(SafetyCreateListener listener){
        safetyCreateListener = listener;
        SafetyCreateDialogFragment safetyCreateDialogFragment = new SafetyCreateDialogFragment();
        Bundle args = new Bundle();
        safetyCreateDialogFragment.setArguments(args);

        return safetyCreateDialogFragment;
    }


    @Override
    public View onCreateView(@org.jetbrains.annotations.NotNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
    /*when the create button is clicked whatever has been written into the edit text boxes is gotten and declared
     as a new row in the database*/
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_safety_create_dialog, container, false);

        typeSpinner = view.findViewById(R.id.typeEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        availableSpinner = view.findViewById(R.id.availableSpinner);
        signOutSpinner = view.findViewById(R.id.signOutSpinner);
        faultSpinner = view.findViewById(R.id.faultSpinner);
        signOutFSpinner = view.findViewById(R.id.signOutFSpinner);
        faultEditText = view.findViewById(R.id.faultEditText);
        safetyImageView = view.findViewById(R.id.safetyImageView);
        availableTextView = view.findViewById(R.id.signedOutTextView);
        faultTextView = view.findViewById(R.id.signedOutFTextView);
        typeIndicatorView = view.findViewById(R.id.typeIndicatorView);

        ImageView createImageView = view.findViewById(R.id.createImageView);
        ImageView backImageView = view.findViewById(R.id.backImageView);

        String title = Objects.requireNonNull(getArguments()).getString(Config.TITLE);
        Objects.requireNonNull(getDialog()).setTitle(title);

        selectedSafetyColor = "#EDEDED";
        setTypeIndicatorColor();

        //How to use Spinner and its setOnItemClickListener() event https://www.youtube.com/watch?v=LU3XyZWO8Z0
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Changing backgroud color inspo | Android Development | Notes App | Tutorial #5 | Note Color | Android Studio https://www.youtube.com/watch?v=Xpd9E4CD84Q&t=1096s
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        selectedSafetyColor = "#FEA3AA";
                        setTypeIndicatorColor();
                        break;
                    case 1:
                        selectedSafetyColor = "#F8B88B";
                        setTypeIndicatorColor();
                        break;
                    case 2:
                        selectedSafetyColor = "#FAF88A";
                        setTypeIndicatorColor();
                        break;
                    case 3:
                        selectedSafetyColor = "#BAED91";
                        setTypeIndicatorColor();
                        break;
                    case 4:
                        selectedSafetyColor = "#B2CEFE";
                        setTypeIndicatorColor();
                        break;
                    case 5:
                        selectedSafetyColor = "#F2A2E8";
                        setTypeIndicatorColor();
                        break;
                    case 6:
                        selectedSafetyColor = "#FFDAC1";
                        setTypeIndicatorColor();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSafetyColor = "#A4A4A4";
                setTypeIndicatorColor();
            }
        });

        availableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        signOutSpinner.setVisibility(View.GONE);
                        availableTextView.setVisibility(View.GONE);
                        break;
                    case 1:
                        signOutSpinner.setVisibility(View.VISIBLE);
                        availableTextView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                signOutSpinner.setVisibility(View.GONE);
                availableTextView.setVisibility(View.GONE);
            }
        });

        faultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                switch (i){
                    case 0:
                        signOutFSpinner.setVisibility(View.GONE);
                        faultEditText.setVisibility(View.GONE);
                        faultTextView.setVisibility(View.GONE);
                        break;
                    case 1:
                        signOutFSpinner.setVisibility(View.VISIBLE);
                        faultEditText.setVisibility(View.VISIBLE);
                        faultTextView.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                signOutFSpinner.setVisibility(View.GONE);
                faultEditText.setVisibility(View.GONE);
                faultTextView.setVisibility(View.GONE);
            }
        });

        createImageView.setOnClickListener(v -> {

            if(!typeSpinner.getSelectedItem().toString().isEmpty() &&
                    !descriptionEditText.getText().toString().isEmpty() &&
                    !availableSpinner.getSelectedItem().toString().isEmpty() &&
                    !faultSpinner.getSelectedItem().toString().isEmpty() &&
                    safetyImageView.getDrawable()!= null &&
                    imgToStore!= null){

                typeString = typeSpinner.getSelectedItem().toString();
                descriptionString = descriptionEditText.getText().toString();
                availableString = availableSpinner.getSelectedItem().toString();
                availuserString = signOutSpinner.getSelectedItem().toString();
                faultString = faultSpinner.getSelectedItem().toString();
                faultUserString = signOutFSpinner.getSelectedItem().toString();
                faultdescString = faultEditText.getText().toString();
                //safetyColor = selectedSafetyColor; //need to ad to sql

                Safety safety = new Safety(-1, typeString, descriptionString, availableString, availuserString,
                        faultString, faultUserString, faultdescString, imgToStore); //safety color

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertSafety(safety);

                if (id > 0) {
                    safety.setId(id);
                    safetyCreateListener.onSafetyCreated(safety);
                    Objects.requireNonNull(getDialog()).dismiss();
                }


            }else {
                Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_SHORT).show();
            }
        });

        safetyImageView.setOnClickListener(v -> {
        Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
        });


        backImageView.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!= null){
            Uri imgUri = data.getData();
            try {
                ContentResolver resolver = Objects.requireNonNull(getActivity()).getContentResolver();
                imgToStore = MediaStore.Images.Media.getBitmap(resolver, imgUri);
                safetyImageView.setImageBitmap(imgToStore);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void setTypeIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) typeIndicatorView.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedSafetyColor));
    }
}
