package com.niamh.sailing3app.SafetyCRUD.UpdateSafety;

//Adapted from Michael Gleesons lecture on 12/11/2020 gleeson.io


import com.niamh.sailing3app.SafetyCRUD.CreateSafety.Safety;

//Listening for when update is actually clicked so it can implement code
public interface SafetyUpdateListener {

    void onSafetyInfoUpdate(Safety safety, int position);
}
