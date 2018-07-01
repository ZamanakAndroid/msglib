package com.zamanak.msglib.rows;

/**
 * Created by PirFazel on 2/22/2017.
 */

public class MessageRowModel {

    public String id;
    public String title;
    public String desc;
    public String date;
    public boolean seen;
    public Object data;

    public MessageRowModel(String title, String desc, String date, boolean seen) {
        this.desc = desc;
        this.title = title;
        this.date = date;
        this.seen = seen;
    }

    public MessageRowModel(String title, String desc, String date, Object data) {
        this.desc = desc;
        this.title = title;
        this.date = date;
        this.data = data;
    }
}
