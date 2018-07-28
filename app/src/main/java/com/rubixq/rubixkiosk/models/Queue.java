package com.rubixq.rubixkiosk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Queue {
    private String id;
    private String label;
    private String title;
    private String description;

    private Queue(String id, String label, String title, String description) {
        this.id = id;
        this.label = label;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static Queue newInstance(String id, String label, String title, String description){
        return new Queue(id,label,title,description);
    }

    public static Queue fromJSON(JSONObject json) throws JSONException{
        String id = (json.has("id")? json.getString("id") : "");
        String label = (json.has("name")? json.getString("name") : "");
        String title = (json.has("title")? json.getString("title") : "");
        String description = (json.has("description")? json.getString("description") : "");

        return Queue.newInstance(id,label,title,description);
    }
}
