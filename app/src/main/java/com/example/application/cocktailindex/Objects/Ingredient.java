package com.example.application.cocktailindex.Objects;

public class Ingredient {

    private String ingredient;
    private String amount;
    private String measurement;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Ingredient(String ingredient, String amount, String measurement) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.measurement = measurement;
    }


}
