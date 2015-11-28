package com.ami.model;


/**
 * Created by rajeshkhandelwal on 11/27/15.
 */
public class ProductFood {
    String item_name;
    String brand_name;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getNf_calories() {
        return nf_calories;
    }

    public void setNf_calories(String nf_calories) {
        this.nf_calories = nf_calories;
    }

    public String getNf_ingredient_statement() {
        return nf_ingredient_statement;
    }

    public void setNf_ingredient_statement(String nf_ingredient_statement) {
        this.nf_ingredient_statement = nf_ingredient_statement;
    }

    public String getNf_sugars() {
        return nf_sugars;
    }

    public void setNf_sugars(String nf_sugars) {
        this.nf_sugars = nf_sugars;
    }

    String nf_calories;
    String nf_ingredient_statement;
    String nf_sugars;
}
