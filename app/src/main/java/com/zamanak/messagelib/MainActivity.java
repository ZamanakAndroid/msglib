package com.zamanak.messagelib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zamanak.msglib.MsgSdk;

public class MainActivity extends AppCompatActivity {

    private String TOKEN="";

    /*API Keys*/
    public String API_KEY_BASE = "";
    public String API_KEY = "";

    /*URL*/
    public String URL_BASE = "http://avasdp.shamimsalamat.ir/api/v2";
    public String URL_MESSAGES = "/pharmacy-ex1/inbox-list";
    public String URL_SEEN = "/inbox/seen";

    /*Header*/
    public String HEADER_API_KEY = "X-Zamanak-Api-Key";
    public String HEADER_TOKEN = "X-Zamanak-Session-Token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MsgSdk.Builder()
                .setContext(this)
                .setBaseApiKey(API_KEY_BASE)
                .setHeaderApiKey(HEADER_API_KEY)
                .setApiKey(API_KEY)
                .setBaseUrl(URL_BASE)
                .setMessagesUrl(URL_MESSAGES)
                .setSeenUrl(URL_SEEN)
                .setToken(TOKEN)
                .setHeaderToken(HEADER_TOKEN)
                .setPrimaryColor(Color.parseColor("#06e186"))
                .setPrimaryColorDark(Color.parseColor("#05c173"))
                .setAccentColor(Color.parseColor("#00A3D9"))
                .build();
    }
}
