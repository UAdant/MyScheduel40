package com.example.myschedule30;

public class ScheduleItem {
    private int id; // Унікальний ідентифікатор елемента
    private String itemName;
    private String itemType;
    private String itemDate;
    private boolean isDone; // Нове поле для позначення виконання завдання

    public ScheduleItem(int id, String itemName, String itemType, String itemDate, boolean isDone) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemDate = itemDate;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
}

