package dev.astrup.cocktailindex.Objects;

import android.graphics.Bitmap;

public class Image {
    private int id;
    private Bitmap image;

    public Image(Bitmap image, int id) {
        this.image = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
