package com.example.shedule.model;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Main{
    public static void main(String[] args) throws IOException, ParseException {
//       GroupShedule gs = new GroupShedule("https://ssau.ru/rasp?groupId=799359428");
//       gs.toJSON();
//       Files.write(Paths.get("fiqwle.json"), gs.toJSON().toJSONString().getBytes());
       Object o = new JSONParser().parse(new FileReader("fiqwle.json"));
       GroupShedule s = new GroupShedule(o);
       System.out.println(s);
       }
}