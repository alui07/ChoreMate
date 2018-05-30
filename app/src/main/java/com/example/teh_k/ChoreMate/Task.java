package com.example.teh_k.ChoreMate;
// used to keep track of the time of tasks
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
// used to create a list of users
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The object class for the tasks.
 * Implements Parcelable to be able to be passed between activities.
 */
public class Task implements Parcelable {
    //database access id. for delete task;
    private String key;

    //name of task name and details, set by the user
    private String task_name;
    private String task_detail;

    // Avatar of the housemate.
    private Uri housemateAvatar;

    //whether the task is "triggered" by another user or occurs at set times
    // 0 for no trigger, 1 for trigger
    private boolean trigger;

    //whether the task is standby or in progress
    // 0 for standby, 1 for in progress
    private boolean status;

    //list of users that the task will be accomplished by
    private List<String> user_list;

    // Date that the task is to occur
    // NOTE: MONTH STARTS FROM 0. 0 - January, 1 - February, etc.
    private String time;

    // whether or not the task recurs
    // 0 for non recurring, 1 for recurring
    private boolean recur;

    private int amountOfTime;
    private String unitOfTime;
    private String uid;
    private String household;
    private int index;

    //TODO: how should we handle when the task recurs, for daily, weekly, monthly, yearly

    public Task() {
        // Default empty constructor.
    }

    //getter and setter for task name and details
    public String getKey() { return key; }
    public void setKey(String task_name) { this.key = key; }

    public String getTask_name() { return task_name; }
    public void setTask_name(String task_name) { this.task_name = task_name; }

    public String getTask_detail() { return task_detail; }
    public void setTask_detail(String task_detail) { this.task_detail = task_detail; }

    public Uri getHousemateAvatar() {
        return housemateAvatar;
    }
    public void setHousemateAvatar(Uri avatar) {
        housemateAvatar = avatar;
    }

    //getter and setter for trigger
    public boolean isTrigger() { return trigger; }
    public void setTrigger(boolean trigger) { this.trigger = trigger; }

    //getter and setter for status
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    //getter and setter for the list of user
    public List<String> getUser_list() { return user_list; }
    public void setUser_list(ArrayList<String> user_list) { this.user_list = user_list; }

    //getters and setters for the task deadline
    /*
    public Calendar getTime() {
        Calendar calendarTime = Calendar.getInstance();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            calendarTime.setTime(formatter.parse(this.time));
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return calendarTime;
    }
        public void setTime(Calendar time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        this.time = formatter.format(time.getTime());
    }
    */
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    //getter and setter for whether the task recurs
    public boolean isRecur() { return recur; }
    public void setRecur(boolean recur) { this.recur = recur; }

    //getter and setter for amount of time
    public int getAmountOfTime() { return amountOfTime; }
    public void setAmountOfTime(int amountOfTime) { this.amountOfTime = amountOfTime; }

    //getter and setter for unit of time
    public String getUnitOfTime() { return unitOfTime; }
    public void setUnitOfTime(String unitOfTime) { this.unitOfTime = unitOfTime;
    }
    //getter and setter for uid
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    //getter and setter for unit of time
    public String getHousehold() { return household; }
    public void setHousehold(String household) { this.household = household;
    }
    //getter and setter for unit of time
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index;
    }

    // Code generated from www.parcelabler.com
    // Following section of code used for implementing Parcelable.
    /**
     * Constructor method for when passing object through activities.
     */
    protected Task(Parcel in) {
        key = in.readString();
        task_name = in.readString();
        task_detail = in.readString();
        housemateAvatar = (Uri) in.readValue(Uri.class.getClassLoader());
        trigger = in.readByte() != 0x00;
        status = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            user_list = new ArrayList<String>();
            in.readList(user_list, String.class.getClassLoader());
        } else {
            user_list = null;
        }
        time = in.readString();
        recur = in.readByte() != 0x00;
        amountOfTime = in.readInt();
        unitOfTime = in.readString();
        uid = in.readString();
        household = in.readString();
        index = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens the task object to a Parcelable.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(task_name);
        dest.writeString(task_detail);
        dest.writeValue(housemateAvatar);
        dest.writeByte((byte) (trigger ? 0x01 : 0x00));
        dest.writeByte((byte) (status ? 0x01 : 0x00));
        if (user_list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(user_list);
        }
        dest.writeString(time);
        dest.writeByte((byte) (recur ? 0x01 : 0x00));
        dest.writeInt(amountOfTime);
        dest.writeString(unitOfTime);
        dest.writeString(uid);
        dest.writeString(household);
        dest.writeInt(index);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of your Parcelable class from a Parcel.
     */
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("task_name", task_name);
        result.put("task_detail", task_detail);
        result.put("trigger", trigger);
        result.put("status", status);
        result.put("housemateAvatar", housemateAvatar);
        result.put("user_list", user_list);
        result.put("time", time);
        result.put("recur", recur);
        result.put("amountOfTime",amountOfTime);
        result.put("unitOfTime", unitOfTime);
        result.put("uid", uid);
        result.put("household",household);
        result.put("index", index);

        return result;
    }
}