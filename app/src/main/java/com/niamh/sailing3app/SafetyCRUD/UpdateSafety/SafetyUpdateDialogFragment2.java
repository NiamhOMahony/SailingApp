package com.niamh.sailing3app.SafetyCRUD.UpdateSafety;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.niamh.sailing3app.Database.DatabaseQueryClass;
import com.niamh.sailing3app.R;
import com.niamh.sailing3app.SafetyCRUD.CreateSafety.Safety;
import com.niamh.sailing3app.Utils.Config;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class SafetyUpdateDialogFragment2 extends DialogFragment {
    //Update Existing Note

    //Declaring Variables
    private static SafetyUpdateListener safetyUpdateListener;
    private static long safetyId;
    private static int position;

    private Safety mSafety;

    //Declaring Variables
    private EditText typeEditText;
    private EditText descriptionEditText;
    private Spinner availableSpinner;
    private Spinner faultSpinner;
    private Spinner signOutSpinner;
    private Spinner signOutFSpinner;
    private EditText faultEditText;
    private ImageView safetyImageView;
    private View typeIndicatorView;

    private TextView availableTextView;
    private TextView faultTextView;

    ImageView backImageView;
    ImageView updateImageView;
    ImageView binImageView;

    Uri imgUri;
    private Bitmap imgToStore;

    private  static final  int PICK_IMAGE_REQUEST = 100;

    // public String safetyColor; //need to give value in SQLtable

    private String selectedSafetyColor;


    private DatabaseQueryClass databaseQueryClass;

    public SafetyUpdateDialogFragment2() {
        // Required empty public constructor
    }

    //when a new instance of the create fragment is opened it processes the below
    public static SafetyUpdateDialogFragment2 newInstance(long subId, int pos, SafetyUpdateListener listener) {
        //added difference of declaring 3 new variables
        safetyId = subId;
        position = pos;
        safetyUpdateListener = listener;

        SafetyUpdateDialogFragment2 safetyUpdateDialogFragment2 = new SafetyUpdateDialogFragment2();
        Bundle args = new Bundle();
        safetyUpdateDialogFragment2.setArguments(args);
        return safetyUpdateDialogFragment2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety_update_dialog2, container, false);

        /*when the update button is clicked whatever has been written into the edit text boxes is gotten and declared
     as a new row in the database*/
        typeEditText = view.findViewById(R.id.typeEditText);
        typeEditText.setEnabled(false);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        descriptionEditText.setEnabled(false);
        availableSpinner = view.findViewById(R.id.availableSpinner);
        signOutSpinner = view.findViewById(R.id.signOutSpinner);
        faultSpinner = view.findViewById(R.id.faultSpinner);
        signOutFSpinner = view.findViewById(R.id.signOutFSpinner);
        faultEditText = view.findViewById(R.id.faultEditText);
        safetyImageView = view.findViewById(R.id.safetyImageView);
        typeIndicatorView = view.findViewById(R.id.typeIndicatorView);

        availableTextView = view.findViewById(R.id.signedOutTextView);
        faultTextView = view.findViewById(R.id.signedOutFTextView);

        updateImageView = view.findViewById(R.id.updateImageView);
        backImageView = view.findViewById(R.id.backImageView);
        binImageView = view.findViewById(R.id.binImageView);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        assert getArguments() != null;
        String title = getArguments().getString(Config.TITLE);
        Objects.requireNonNull(getDialog()).setTitle(title);

        mSafety = databaseQueryClass.getSafetyById(safetyId);

        //instead of getting the values like in create were setting the values to our updated
        typeEditText.setText(mSafety.getType());
        descriptionEditText.setText(mSafety.getDescription());
        faultEditText.setText(mSafety.getFaultdes());
        safetyImageView.setImageBitmap(mSafety.getImage());

        selectedSafetyColor = "#EDEDED";
        setTypeIndicatorColor();

        //How to use Spinner and its setOnItemClickListener() event https://www.youtube.com/watch?v=LU3XyZWO8Z0

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


        updateImageView.setOnClickListener(v -> updateSafety());


        safetyImageView.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE_REQUEST);
        });

        //if the cancel button is pressed we return to the view
        backImageView.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());

        return view;
    }

    //On start up of the Update fragment section of the application these ruels are followed
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri=data.getData();
            try {
                ContentResolver resolver = Objects.requireNonNull(getActivity()).getContentResolver();
                imgToStore = MediaStore.Images.Media.getBitmap(resolver, imgUri);
                safetyImageView.setImageBitmap(imgToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSafety() {
        if ( !availableSpinner.getSelectedItem().toString().isEmpty() &&
                !faultSpinner.getSelectedItem().toString().isEmpty() &&
                safetyImageView.getDrawable() != null &&
                imgToStore != null) {

            String typeString = typeEditText.getText().toString();
            String descriptionString = descriptionEditText.getText().toString();
            String availableString = availableSpinner.getSelectedItem().toString();
            String availuserString = signOutSpinner.getSelectedItem().toString();
            String faultString = faultSpinner.getSelectedItem().toString();
            String faultUserString = signOutFSpinner.getSelectedItem().toString();
            String faultdescString = faultEditText.getText().toString();
            //not used because causing app to crash ?
            Bitmap imageBitmap = safetyImageView.getDrawingCache();

            mSafety.setType(typeString);
            mSafety.setDescription(descriptionString);
            mSafety.setAvailable(availableString);
            mSafety.setAvailuser(availuserString);
            mSafety.setFault(faultString);
            mSafety.setFaultuser(faultUserString);
            mSafety.setFaultdes(faultdescString);
            mSafety.setImage(imgToStore);

            long id = databaseQueryClass.updateSafety(mSafety);

            if (id > 0) {
                safetyUpdateListener.onSafetyInfoUpdate(mSafety, position);
                Objects.requireNonNull(getDialog()).dismiss();
            }

        }
    }

    private void setTypeIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) typeIndicatorView.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedSafetyColor));
    }
}