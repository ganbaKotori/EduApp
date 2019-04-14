package com.shape.eduapp;

import java.util.ArrayList;

public class Course {
    private String title;
    private String description;
    private ArrayList<String> lectures = new ArrayList<>();

    public Course(){

    }

    public Course(String title,String description){
        this.title = title;
        this.description = description;

    }

    public Course(String title,String description, ArrayList<String> lectures){
        this.title = title;
        this.description = description;
        this.lectures = lectures;
    }

    public String getTitle(){
        return title;
    }


    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public ArrayList<String> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<String> lectures){
        this.lectures = lectures;
    }
}
