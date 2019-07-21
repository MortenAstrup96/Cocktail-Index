package com.cocktail.application.cocktailindex.Utility;


import java.util.ArrayList;
import java.util.List;

public class Measurements {
    private List<String> measureSingle;
    private List<String> measurePlural;
    
    public Measurements(boolean useMetric) {
        measureSingle = new ArrayList<>();
        measurePlural = new ArrayList<>();
        measureSingle.clear();
        measurePlural.clear();

        if(useMetric) {
            measureSingle.add("mL");
            measureSingle.add("dash");
            measureSingle.add("drop");
            measureSingle.add("tsp.");
            measureSingle.add("g");
            measureSingle.add("sprig");
            measureSingle.add("leaf");
            measureSingle.add("oz.");
            measureSingle.add("cup");
            measureSingle.add(" ");

            measurePlural.add("mL");
            measurePlural.add("dashes");
            measurePlural.add("drops");
            measurePlural.add("tsp.");
            measurePlural.add("g");
            measurePlural.add("sprigs");
            measurePlural.add("leaves");
            measurePlural.add("oz.");
            measurePlural.add("cups");
            measurePlural.add(" ");

        } else {
            measureSingle.add("oz.");
            measureSingle.add("tsp.");
            measureSingle.add("dash");
            measureSingle.add("drop");
            measureSingle.add("sprig");
            measureSingle.add("leaf");
            measureSingle.add("cup");
            measureSingle.add("mL");
            measureSingle.add("g");
            measureSingle.add(" ");

            measurePlural.add("oz.");
            measurePlural.add("tsp.");
            measurePlural.add("dashes");
            measurePlural.add("drops");
            measurePlural.add("sprigs");
            measurePlural.add("leaves");
            measurePlural.add("cups");
            measurePlural.add("mL");
            measurePlural.add("g");
            measurePlural.add(" ");
        }
    }

    public List<String> getMeasurements(boolean plural) {
        if(plural) return measurePlural;
        else return measureSingle;
    }
}


