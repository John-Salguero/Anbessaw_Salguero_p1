package com.salanb.webapp.utilities;

import com.google.gson.Gson;

public class GsonSingleton {

    private static GsonSingleton instance;
    private final Gson gson;

    public static GsonSingleton getInstance(){
        if(instance == null)
            instance = new GsonSingleton();
        return instance;
    }

    public Gson getGson(){
        return gson;
    }

    private GsonSingleton() {
        gson = new Gson();
    }

}
