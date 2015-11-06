package com.agible.ofb.events;

/**
 * Created by seth on 5/12/15.
 */
public class EventItem {
    public static final int EVENT_LOCKED = 0x0;
    public static final int EVENT_UNLOCKED = 0x1;

    private String name;
    private int category;
    private int status;

    public EventItem(String name, int category, int status){
        this.category = category;
        this.name = name;
        this.status = status;
    }

    public String getName(){return name;}
    public int getCategory(){return category;}
    public int getStatus(){return status;}



}
