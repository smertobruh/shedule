package com.example.shedule.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Week {
    private int number;
    private Day[] days;

    public Week(int number, String url) {
        this.number =number;
        Document doc = null;
        int tries = 3;
        while (tries-- != 0) {
            try {
                doc = Jsoup.connect(url+"&selectedWeek=" + (this.number+1))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.124")
                        .referrer("http://www.google.com")
                        .get();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.days = getDaysFromHtml(doc);
    }
    public Week(Object o) throws IOException, ParseException {
        JSONObject j = (JSONObject) o;
        JSONArray jsonDays = (JSONArray) j.get("days");
        days = new Day[6];
        for(int i = 0; i < 6; i++) {
            days[i] =  new Day(jsonDays.get(i));
        }
    }

    private Day[] getDaysFromHtml(Document doc) {
        Elements list = doc.select("div.schedule__item");
        Elements[] daysList = divideByLists(list);
        days = new Day[6];
        for (int i =0; i < 6; i ++) {
            days[i] = new Day(daysList[i]);
        }
        return days;
    }

    // Получаем список всех пар на неделе и разбиваем их на дни
    // нулевой элемент в каждом дне будет дата
    private Elements[] divideByLists(Elements list) {
        Elements[] daysList = new Elements[6];
        for (int i = 0; i < 6; i++) daysList[i] = new Elements();
        // на нулевом элементе лежит класс который содержит только строку "Время"
        for (int i = 1; i < list.size(); i++) {
            switch (i % 6) {
                // понедельник
                case 1:
                    daysList[0].add(list.get(i));
                    break;
                // вторник
                case 2:
                    daysList[1].add(list.get(i));
                    break;
                    // среда
                case 3:
                    daysList[2].add(list.get(i));
                    break;
                // четверг
                case 4:
                    daysList[3].add(list.get(i));
                    break;
                    // пятница
                case 5:
                    daysList[4].add(list.get(i));
                    break;
                    // суббота
                case 0:
                    daysList[5].add(list.get(i));
                    break;
            }
        }
        return daysList;
    }

    public Day getDay(int number) {
        if(number < 0 || number >= days.length) return null;
        else return days[number];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Week ").append("{").append("\n");
        for (int i = 0; i < 6; i++) {
            if(days[i] == null) sb.append("None").append("\n");
            else sb.append(days[i]).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    public JSONObject toJSON() throws IOException {
        JSONObject json =  new JSONObject();
        json.put("number", number);
        JSONArray jsonDays = new JSONArray();
        for(int i = 0; i < 6; i++){
            jsonDays.add(days[i].toJSON());
        }
        json.put("days", jsonDays);
        return json;
    }
}
