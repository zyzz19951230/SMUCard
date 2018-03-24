package com.jsutech.zyzz.smucard.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zyzz on 3/23/18.
 * 程序配置项读写模块
 */

public class Config {
    private SharedPreferences preferences;
    private boolean listenConfigChangeEvent;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public Config(Context context, String configFileName, boolean listenConfigChangeEvent){
        preferences = context.getSharedPreferences(configFileName, Context.MODE_PRIVATE);
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Config.this.onConfigChanged(s);
            }
        };
        listenConfigChangeEvent(listenConfigChangeEvent);
    }

    public Config(Context context, String configFileName){
        this(context, configFileName, false);
    }


    public boolean isConfigChangeEventListened(){
        return this.listenConfigChangeEvent;
    }

    public void listenConfigChangeEvent(boolean listenConfigChange){
        this.listenConfigChangeEvent = listenConfigChange;
        if (this.listenConfigChangeEvent){
            preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        } else {
            preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        }
    }

    public void put(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void put(String key, int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void put(String key, float value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void put(String key, long value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void put(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void put(String key, Set<String> value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public void put(Map<String, Object> configMap){
        // 自动略过configMap中不符合数据类型要求的数据
        SharedPreferences.Editor editor = preferences.edit();
        for (Map.Entry<String, Object> entry : configMap.entrySet()){
            Object v = entry.getValue();
            if (v instanceof String)
                editor.putString(entry.getKey(), (String) v);
            else if (v instanceof Boolean)
                editor.putBoolean(entry.getKey(), (Boolean) v);
            else if (v instanceof Float)
                editor.putFloat(entry.getKey(), (Float) v);
            else if (v instanceof Long)
                editor.putLong(entry.getKey(), (Long) v);
            else if (v instanceof Integer)
                editor.putInt(entry.getKey(), (Integer) v);

        }
        editor.apply();
    }
    public String get(String key, String defaultValue){
        return preferences.getString(key, defaultValue);

    }
    public int get(String key, int defaultValue){
        return preferences.getInt(key, defaultValue);

    }
    public long get(String key, long defaultValue){
        return preferences.getLong(key, defaultValue);
    }
    public boolean get(String key, boolean defaultValue){
        return preferences.getBoolean(key, defaultValue);
    }
    public float get(String key, float defaultValue){
        return preferences.getFloat(key, defaultValue);
    }
    public Set<String> get(String key, Set<String> defaultValue){
        return preferences.getStringSet(key, defaultValue);
    }

    public void remove(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void remove(String... keys){
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : keys){
            editor.remove(key);
        }
        editor.apply();
    }

    public void clear(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean contains(String key){
        return preferences.contains(key);
    }

    public Map<String, ?> getConfigMap(){
        return preferences.getAll();
    }

    public void onConfigChanged(String changedKey){

    }

}