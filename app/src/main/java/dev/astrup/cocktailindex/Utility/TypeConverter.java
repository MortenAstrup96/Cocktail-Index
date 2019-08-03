package dev.astrup.cocktailindex.Utility;

import java.util.ArrayList;

public class TypeConverter  {
    @androidx.room.TypeConverter
    public String fromArray(ArrayList<String> strings) {
        if(strings != null && !strings.isEmpty()) {
            String string = "";
            for(String s : strings) string += (s + ",");

            return string;
        }
        return null;

    }

    @androidx.room.TypeConverter
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
