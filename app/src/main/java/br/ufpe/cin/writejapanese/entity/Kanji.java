package br.ufpe.cin.writejapanese.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Tomer Simis on 26/06/2015.
 */
public class Kanji {

    private String id;

    private String name;

    private int rating;

    private String level;

    public Kanji(){

    }

    public Kanji(String id, String name, String level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage(Context context) {
        return context.getResources().getDrawable(context.getResources().getIdentifier(id, "drawable", context.getPackageName()));
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
