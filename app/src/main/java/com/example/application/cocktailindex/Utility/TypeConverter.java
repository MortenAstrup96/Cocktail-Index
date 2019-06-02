package com.example.application.cocktailindex.Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TypeConverter  {
    @android.arch.persistence.room.TypeConverter
    public String fromArray(ArrayList<String> strings) {
        if(!strings.isEmpty()) {
            String string = "";
            for(String s : strings) string += (s + ",");

            return string;
        }
        return null;

    }

    @android.arch.persistence.room.TypeConverter
    public ArrayList<String> toArray(String concatenatedStrings) {
        ArrayList<String> myStrings = new ArrayList<>();

        if(concatenatedStrings != null) {
            for(String s : concatenatedStrings.split(",")) {
                myStrings.add(s);
            }
            return myStrings;
        }
        return null;



    }


}
