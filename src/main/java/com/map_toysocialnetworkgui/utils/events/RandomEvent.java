package com.map_toysocialnetworkgui.utils.events;



public class RandomEvent implements Event {
    private ChangeEventType type;
    private String data;
    private String oldData;

    public RandomEvent(ChangeEventType type, String data) {
        this.type = type;
        this.data = data;
    }
    public RandomEvent(ChangeEventType type, String data, String oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String getOldData() {
        return oldData;
    }
}