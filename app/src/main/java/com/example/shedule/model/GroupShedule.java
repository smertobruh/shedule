package com.example.shedule.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class GroupShedule {
    private String url;
    private Week[] weeks;
    public GroupShedule(String url) throws IOException {
            this.url = url;
            weeks = new Week[27];
            // нулевая неделя на сайте = текущей неделе
            for(int i = 0; i < 27; i++) {
                Week week = new Week(i, url);
                weeks[i] = week;
            }
        }
    public GroupShedule(Object o) throws IOException, ParseException {


        JSONObject j = (JSONObject) o;

        JSONArray jsonWeeks = (JSONArray)j.get("weeks");
        weeks = new Week[27];
        for(int i = 0; i < 26; i++) {
            weeks[i] = new Week(jsonWeeks.get(i));
        }
    }

    public Week getWeek(int number) {
        if(number < 0 || number >= weeks.length || this == null) return null;
        else return weeks[number];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GroupShedule ").append("{").append("\n");
        for (int i = 0; i < 27; i++) {
            if(weeks[i] == null) sb.append("None").append("\n");
            else sb.append(i + " " + weeks[i]).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }


    public JSONObject toJSON() throws IOException {
        JSONObject json = new JSONObject();
        json.put("url", url);
        JSONArray jsonWeeks = new JSONArray();
        for(int i = 0; i < 27; i++) {
            jsonWeeks.add(weeks[i].toJSON());
        }
        json.put("weeks", jsonWeeks);
        return json;
    }
}

