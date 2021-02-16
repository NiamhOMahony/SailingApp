package com.niamh.sailing3app.Utils;

//Adapted from Michael Gleesons lecture on 12/11/2020 gleeson.io

public class Config {

    public static final String DATABASE_NAME = "sailing_emp_db";

    //Column names of safety table
    public static final String TABLE_SAFETY= "safety_table";
    public static final String COLUMN_SAFETY_ID = "_id";
    public static final String COLUMN_SAFETY_TYPE = "type";
    public static final String COLUMN_SAFETY_DESCRIPTION = "description";
    public static final String COLUMN_SAFETY_AVAILABLE = "available";
    public static final String COLUMN_SAFETY_AVAILUSER = "availuser";
    public static final String COLUMN_SAFETY_FAULT = "fault";
    public static final String COLUMN_SAFETY_FAULTUSER = "faultuser";
    public static final String COLUMN_SAFETY_FAULTDES = "faultdes";
    public static final String COLUMN_SAFETY_IMAGE = "image";

    //others for general purpose key-value pair data
    public static final String TITLE = "safety_equipment";
    public static final String CREATE_SAFETY= "create_safety";
    public static final String UPDATE_SAFETY = "update_safety";

}

