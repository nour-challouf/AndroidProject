package com.example.androidproject;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class MenuItemConverter {
    @TypeConverter
    public static List<MenuItem> fromString(String value) {
        Type listType = new TypeToken<List<MenuItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<MenuItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}