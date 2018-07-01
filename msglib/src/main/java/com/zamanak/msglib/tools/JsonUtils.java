package com.zamanak.msglib.tools;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by PirFazel on 2/4/2017.
 */

public abstract class JsonUtils {
    public static JSONObject createAnswer(double longitude, double latitude, double radius, String activityType) {
        JSONObject answer = new JSONObject();
        try {
            answer.put("Longitude", longitude);
            answer.put("Latitude", latitude);
            answer.put("Radius", radius);
            answer.put("ActivityType", activityType);
            Log.v("answer", answer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answer;
    }


    public static JsonElement convertStringToJsonObj(String jsonAsString) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonAsString);
    }

    public static String trimMessage(String json, String key) {
        String trimmedString;
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            trimmedString = obj.getJSONObject("error").getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
