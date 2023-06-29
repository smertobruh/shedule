package com.example.shedule.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Day {
    private Date date;
    private Lesson[] lessons;

    public Day() {
        date = null;
        lessons = null;
    }
    public Day(Elements list) {
        date = getDateFromHTML(list.get(0));
        lessons = getLessonsFromElements(list);
    }
    public Day(Object o){
        JSONObject j = (JSONObject) o;
        date = new Date((long)j.get("date"));
        JSONArray jsonLessons = (JSONArray) j.get("lessons");
        lessons = new Lesson[8];
        for(int i = 0; i < 7; i++) {
             {
                 lessons[i] = readLessonFromJSON(jsonLessons.get(i));
            }
        }
    }

    public Lesson getLesson(int number) {
        if(number < 0 || number >= lessons.length) return null;
        else return lessons[number];
    }
    public Date getDate() {
        return date;
    }


    public Lesson[] getLessonsFromElements(Elements list) {
        Lesson[] lessons = new Lesson[8];
        for(int i = 1; i < list.size(); i++) {
            lessons[i-1] = createLesson(list.get(i));
        }
        return lessons;
    }
    private Date getDateFromHTML(Element el) {

        String[] fullDateStr = el.select("div.schedule__head-date").get(0).text().split("\\.");
        int day = Integer.parseInt(fullDateStr[0]);
        int month = Integer.parseInt(fullDateStr[1]) -1;
        int year = Integer.parseInt(fullDateStr[2]);
        Calendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }
    private Lesson createLesson(Element el) {
        if(el.select("div.schedule__lesson").size() == 0) return null;
        else if (el.select("div.schedule__lesson").size() == 1)
            return new SingleLesson(el.select("div.schedule__lesson").get(0));
        else return new PairedLesson(el);
    }
    private Lesson readLessonFromJSON(Object o) {
        if(o == null) return null; // если пары нет
        JSONObject j = (JSONObject) o;
        Object tmp = j.get("singleLesson");
        if(tmp != null) return new SingleLesson(tmp); //если пара одинарная
        else return new PairedLesson(j.get("pairedlesson")); // если пара двойная
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Day ").append(" "+date+" ").append("{").append("\n");
        for (int i = 0; i < 8; i++) {
           if(lessons[i] == null) sb.append("None").append("\n");
           else sb.append(lessons[i]).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    public JSONObject toJSON() throws IOException {
        JSONObject json =  new JSONObject();
        json.put("date", date.getTime());
        JSONArray jsonLessons = new JSONArray();
        for(int i = 0; i < 8; i++){
           if(lessons[i] != null) jsonLessons.add(lessons[i].toJSON());
           else jsonLessons.add(null);
        }
        json.put("lessons", jsonLessons);
        //Files.write(Paths.get("fiqwle.json"), sLesson.toJSONString().getBytes());
        return json;
    }
}
