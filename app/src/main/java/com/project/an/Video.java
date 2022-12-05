package com.project.an;
import com.google.gson.annotations.SerializedName;

// Video model for HTTP requests
public class Video {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("_id")
    private String _id;

    public Video(String _id,String title, String description) {
        this.title = title;
        this.description = description;
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Object getID() {return _id;}
}
