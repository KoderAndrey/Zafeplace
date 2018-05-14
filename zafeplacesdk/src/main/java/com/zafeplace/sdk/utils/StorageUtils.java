package com.zafeplace.sdk.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Iterator;

public class StorageUtils {

    public static boolean deleteFile(String filePath) {
        if(filePath!=null) {
            File file = new File(filePath);
            return file.delete();
        }
        else return false;
    }
}
