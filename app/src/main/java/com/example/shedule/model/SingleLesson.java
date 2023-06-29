package com.example.shedule.model;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;

public class SingleLesson implements Lesson{
    private String discipline;
    private String place;
    private String teacher;
    private LessonType lessonType;
    private String subGroup;

    public SingleLesson(Element el)  {
            getType(el);
            discipline = el.select("div.schedule__discipline").text();
            if(el.select("div.schedule__place").size() != 0)
                place = el.select("div.schedule__place").text();
            if(el.select("div.schedule__teacher").size() != 0)
                teacher = el.select("div.schedule__teacher").text();
            if(el.select("div.schedule__groups").size()!= 0)
                subGroup = el.select("div.schedule__groups").text();

    }
    public SingleLesson(Object o)  {
        JSONObject j = (JSONObject) o;
        discipline = (String) j.get("discipline");
        place = (String) j.get("place");
        teacher = (String) j.get("teacher");
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SingleLesson ").append("{").append("\n");
        sb.append("Discipline: ").append(discipline).append("\n");
        if(place != null) sb.append("Place: ").append(place).append("\n");
        if(place != null) sb.append("Teacher: ").append(teacher).append("\n");
        if(place != null) sb.append("SubGroup: ").append(subGroup).append("\n");
        sb.append("}");
        return sb.toString();
    }
    private LessonType getType(Element el){
        // is проверяет соответствует ли элемент данному селектору
        if(el.is("div.lesson-border-type-1")) lessonType = LessonType.Lecture;
        if(el.is("div.lesson-border-type-2")) lessonType = LessonType.Lab;
        if(el.is("div.lesson-border-type-3")) lessonType = LessonType.Practice;
        if(el.is("div.lesson-border-type-4")) lessonType = LessonType.Military;
        return null;
    }
    public JSONObject toJSON() {
        JSONObject json =  new JSONObject();
        json.put("discipline",discipline);
        json.put("place",place);
        json.put("teacher",teacher);
        //json.put("subGroup",subGroup);
        //json.put("lessonType",lessonType);
        JSONObject sLesson = new JSONObject();
        sLesson.put("singleLesson", json);
        //Files.write(Paths.get("fiqwle.json"), sLesson.toJSONString().getBytes());
        return sLesson;
    }

    @Override
    public String getDiscipline() {

        return discipline;
    }

    @Override
    public String getPlace() {
        return place;
    }

    @Override
    public String getTeacher() {
        return teacher;
    }

}
