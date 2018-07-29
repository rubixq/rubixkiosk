package com.rubixq.rubixkiosk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Queue {
    private String id;
    private String name;
    private String title;
    private String description;

    public Queue(String id, String name, String title, String description) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
