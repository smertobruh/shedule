package com.example.shedule.model;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PairedLesson implements Lesson{
    SingleLesson[] singleLessons;
    public PairedLesson(Element el) {
        singleLessons = new SingleLesson[2];
        Elements elements = el.select("div.schedule__lesson");
        for(int i = 0; i < 2; i++) {
            singleLessons[i] = new SingleLesson(elements.get(i));
        }
    }
    public PairedLesson(Object o)  {
        singleLessons = new SingleLesson[2];
        JSONObject j = (JSONObject) o;
        JSONObject tmp = (JSONObject) j.get("subgroup0");
        singleLessons[0] = new SingleLesson(tmp.get("singleLesson"));
        tmp = (JSONObject) j.get("subgroup1");
        singleLessons[1] = new SingleLesson(tmp.get("singleLesson"));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PairedLesson").append("{").append("\n");
        sb.append(singleLessons[0]).append("\n");
        sb.append(singleLessons[1]).append("\n");
        sb.append("}");
        return sb.toString();
    }
    public JSONObject toJSON() {
        JSONObject json =  new JSONObject();
        json.put("subgroup0", singleLessons[0].toJSON());
        json.put("subgroup1", singleLessons[1].toJSON());
        JSONObject pLesson = new JSONObject();
        pLesson.put("pairedlesson", json);
        //Files.write(Paths.get("fiqwle.json"), sLesson.toJSONString().getBytes());
        return pLesson;
    }

    @Override
    public String getDiscipline() {
        return singleLessons[0].getDiscipline()+" "+ singleLessons[1].getDiscipline();

    }

    @Override
    public String getPlace() {
        return singleLessons[0].getPlace()+" "+ singleLessons[1].getPlace();
    }

    @Override
    public String getTeacher() {
        return singleLessons[0].getTeacher()+" "+ singleLessons[1].getTeacher();
    }
    public  SingleLesson getSubgroup(int i){
        return singleLessons[i];
    }
}
