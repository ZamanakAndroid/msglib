package com.zamanak.msglib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zamanak.msglib.ui.MsgActivity;

/**
 * Created by PIRI on 6/26/2018.
 */

public class MsgSdk {

    private static final String TAG = "MsgSdk";

    /*Keys*/
    public static String KEY_MESSAGE = "drug";
    public static String KEY_INBOX_ID = "inboxId";

    public static String TOKEN;

    @SuppressLint("StaticFieldLeak")
    private static Context CONTEXT;

    /*API Keys*/
    public static String API_KEY_BASE;
    public static String API_KEY;

    /*URL*/
    public static String URL_BASE;
    public static String URL_MESSAGES;
    public static String URL_SEEN;

    /*Header*/
    public static String HEADER_API_KEY;
    public static String HEADER_TOKEN;

    /*Colors*/
    public static int PRIMARY_COLOR;
    public static int PRIMARY_COLOR_DARK;
    public static int ACCENT_COLOR;

    MsgSdk(Builder builder) {

        TOKEN = builder.TOKEN;
        API_KEY_BASE = builder.API_KEY_BASE;
        API_KEY = builder.API_KEY;
        URL_BASE = builder.URL_BASE;
        URL_MESSAGES = builder.URL_MESSAGES;
        URL_SEEN = builder.URL_SEEN;
        HEADER_API_KEY = builder.HEADER_API_KEY;
        HEADER_TOKEN = builder.HEADER_TOKEN;
        PRIMARY_COLOR = builder.primaryColor;
        PRIMARY_COLOR_DARK = builder.primaryColorDark;
        ACCENT_COLOR = builder.accentColor;
        CONTEXT = builder.context;
        startMsgActivity((Activity) CONTEXT);
    }

    private void startMsgActivity(Activity context) {
        Intent intent = new Intent(context, MsgActivity.class);
        context.startActivity(intent);
    }

    public static class Builder {

        private String TOKEN;
        private String API_KEY_BASE;
        private String API_KEY;
        private String URL_BASE;
        private String URL_MESSAGES;
        private String URL_SEEN;
        private String HEADER_API_KEY;
        private String HEADER_TOKEN;
        private int primaryColor;
        private int primaryColorDark;
        private int accentColor;
        private Context context;

        public int getPrimaryColor() {
            return primaryColor;
        }

        public Builder setPrimaryColor(int primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }

        public int getPrimaryColorDark() {
            return primaryColorDark;
        }

        public Builder setPrimaryColorDark(int primaryColorDark) {
            this.primaryColorDark = primaryColorDark;
            return this;
        }

        public int getAccentColor() {
            return accentColor;
        }

        public Builder setAccentColor(int accentColor) {
            this.accentColor = accentColor;
            return this;
        }

        public Context getContext() {
            return context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setBaseApiKey(String apiKeyBase) {
            API_KEY_BASE = apiKeyBase;
            return this;
        }

        public Builder setApiKey(String apiKey) {
            API_KEY = apiKey;
            return this;
        }


        public Builder setMessagesUrl(String urlMessages) {
            URL_MESSAGES = urlMessages;
            return this;
        }

        public Builder setSeenUrl(String urlSeen) {
            URL_SEEN = urlSeen;
            return this;
        }

        public Builder setHeaderApiKey(String headerApiKey) {
            HEADER_API_KEY = headerApiKey;
            return this;
        }

        public Builder setHeaderToken(String headerToken) {
            HEADER_TOKEN = headerToken;
            return this;
        }

        public Builder setToken(String TOKEN) {
            this.TOKEN = TOKEN;
            return this;
        }

        public Builder setBaseUrl(String BASE_URL) {
            URL_BASE = BASE_URL;
            return this;
        }

        public void build() {
            new MsgSdk(this);
        }
    }
}
