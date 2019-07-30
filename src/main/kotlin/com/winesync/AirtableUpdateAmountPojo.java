package com.winesync;

import com.google.gson.annotations.SerializedName;

public class AirtableUpdateAmountPojo {

    public AirtableUpdateAmountPojo() { }

    public AirtableUpdateAmountPojo(String id, Integer noBottles) {
        this.id = id;
        this.noBottles = noBottles;
    }

    @SerializedName("id")
    private String id;

    @SerializedName("Bottles in cellar")
    private Integer noBottles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNoBottles() {
        return noBottles;
    }

    public void setNoBottles(Integer noBottles) {
        this.noBottles = noBottles;
    }

}
