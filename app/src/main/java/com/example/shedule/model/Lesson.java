package com.example.shedule.model;

import org.json.simple.JSONObject;

public interface Lesson {
    @Override
    public String toString();
    public JSONObject toJSON();
    public String getDiscipline();
    public String getPlace();
    public String getTeacher();
}
