package com.app.tuskan.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yakup on 14.7.2018.
 */
public class Recipe implements Parcelable {

    public int id;
    public String name;
    public String ingredients;

    public Recipe(int id, String name, String ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Recipe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ingredients);
    }

    public Recipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
