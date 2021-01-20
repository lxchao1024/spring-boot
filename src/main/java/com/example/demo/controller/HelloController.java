package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.services.TheSqlSessionFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        SqlSessionFactory sqlSessionFactory = null;
        SqlSession openSession = null;
        List<User> users = new ArrayList<>();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", 0);
        jsonObject.addProperty("message", "success");

        try {
            sqlSessionFactory = TheSqlSessionFactory.getSqlSessionFactory();
            openSession = sqlSessionFactory.openSession();
            users = openSession.selectList("TB1Mapper.selectedTB1");
            JsonArray jsonArray = new JsonArray();
            JsonObject object;
            for (int i = 0;i<users.size();i++) {
                object = new JsonObject();
                object.addProperty("userId", users.get(i).getUserId());
                object.addProperty("userName", users.get(i).getUserName());
                object.addProperty("sex", users.get(i).getSex());
                object.addProperty("createdTime", users.get(i).getCreatedTime());
                jsonArray.add(object);
            }
            jsonObject.add("content", jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.addProperty("code", -1);
            jsonObject.addProperty("message", "failed");
            jsonObject.addProperty("content", new JsonArray().toString());
        } finally {
            if (null != openSession) {
                openSession.close();
            }
        }

        return jsonObject.toString();
    }
}
