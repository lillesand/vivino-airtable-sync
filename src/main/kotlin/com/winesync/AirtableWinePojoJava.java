package com.winesync;

import com.google.gson.annotations.SerializedName;

public class AirtableWinePojoJava {

    public AirtableWinePojoJava() { }

    public AirtableWinePojoJava(String winery, String name, String vintage, String country, String region, String wineStyle, String wineType, Integer noBottles, Double averageRating) {
        this.noBottles = noBottles;
        this.country = country;
        this.region = region;
        this.wineStyle = wineStyle;
        this.vintage = vintage;
        this.name = name;
        this.wineType = wineType;
        this.winery = winery;
        this.averageRating = averageRating.toString();
    }

    @SerializedName("Bottles in cellar")
    private Integer noBottles;

    @SerializedName("Country")
    private String country;

    @SerializedName("Flasker uten plassering")
    private Integer noUnplacedBottles;

    @SerializedName("id")
    private String id;

    @SerializedName("Plasserte flasker")
    private Integer noPlacedBottles;

    @SerializedName("Region")
    private String region;

    @SerializedName("Average rating")
    private String averageRating;

    @SerializedName("Regional wine style")
    private String wineStyle;

    @SerializedName("Vintage")
    private String vintage;

    @SerializedName("Wine name")
    private String name;

    @SerializedName("Wine type")
    private String wineType;

    @SerializedName("Winery")
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

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }
}
