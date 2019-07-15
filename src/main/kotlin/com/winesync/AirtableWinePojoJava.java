package com.winesync;

import com.google.gson.annotations.SerializedName;

public class AirtableWinePojoJava {

    @SerializedName("bottles in cellar")
    private Integer noBottles;

    @SerializedName("country")
    private String country;

    @SerializedName("flasker uten plassering")
    private Integer noUnplacedBottles;

    @SerializedName("id")
    private String id;

    @SerializedName("plasserte flasker")
    private Integer noPlacedBottles;

    @SerializedName("region")
    private String region;

    @SerializedName("regional wine style")
    private String wineStyle;

    @SerializedName("vintage")
    private String vintage;

    @SerializedName("wine name")
    private String name;

    @SerializedName("wine type")
    private String wineType;

    @SerializedName("winery")
    private String winery;


    public Integer getNoBottles() {
        return noBottles;
    }

    public void setNoBottles(Integer noBottles) {
        this.noBottles = noBottles;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getNoUnplacedBottles() {
        return noUnplacedBottles;
    }

    public void setNoUnplacedBottles(Integer noUnplacedBottles) {
        this.noUnplacedBottles = noUnplacedBottles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNoPlacedBottles() {
        return noPlacedBottles;
    }

    public void setNoPlacedBottles(Integer noPlacedBottles) {
        this.noPlacedBottles = noPlacedBottles;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWineStyle() {
        return wineStyle;
    }

    public void setWineStyle(String wineStyle) {
        this.wineStyle = wineStyle;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWineType() {
        return wineType;
    }

    public void setWineType(String wineType) {
        this.wineType = wineType;
    }

    public String getWinery() {
        return winery;
    }

    public void setWinery(String winery) {
        this.winery = winery;
    }
}
