package com.example.application.cocktailindex.Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TypeConverter  {
    @android.arch.persistence.room.TypeConverter
    public String fromArray(ArrayList<String> strings) {
        String string = "";
        for(String s : strings) string += (s + ",");

        return string;
    }

    @android.arch.persistence.room.TypeConverter
    public ArrayList<String> toArray(String concatenatedStrings) {
        ArrayList<String> myStrings = new ArrayList<>();

        for(String s : concatenatedStrings.split(",")) {
            myStrings.add(s);
        }

        return myStrings;
    }


}
