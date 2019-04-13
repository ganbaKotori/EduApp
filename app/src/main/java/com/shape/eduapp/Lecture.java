package com.shape.eduapp;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;

public class Lecture {
    private ArrayList<Topic> topics = new ArrayList<>();
    private String date;

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return this.date;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Topic> topics){
        this.topics = topics;
    }


}
