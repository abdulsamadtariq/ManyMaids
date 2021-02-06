package com.manymaidsinprovo.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daimajia.androidanimations.library.attention.TadaAnimator;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.Model.Month;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.Model.UserM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskHelper extends SQLiteOpenHelper {

    private static int Version = 1;

    public TaskHelper(Context context) {
        super(context, "task_db", null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE task(taskId INTEGER PRIMARY KEY AUTOINCREMENT,taskName TEXT,note TEXT,startTime TEXT,howOftenQty INTEGER,howOftenDays TEXT,endTime TEXT,taskIndicator INTEGER,howoften TEXT,completedDate TEXT,taskStatus INTEGER,userName TEXT,areaId INTEGER)";

        sqLiteDatabase.execSQL(query);
        String query2 = "CREATE TABLE season(monthId INTEGER PRIMARY KEY AUTOINCREMENT,monthName TEXT,taskId INTEGER)";

        sqLiteDatabase.execSQL(query2);
        String query3 = "CREATE TABLE user_m(userId INTEGER PRIMARY KEY AUTOINCREMENT,userName TEXT,status INTEGER)";

        sqLiteDatabase.execSQL(query3);

        String query4 = "CREATE TABLE area(id INTEGER PRIMARY KEY AUTOINCREMENT,areaName TEXT,indicatorValue INTEGER)";
        sqLiteDatabase.execSQL(query4);
        String query5 = "CREATE TABLE taskHistory(taskHistoryId INTEGER PRIMARY KEY AUTOINCREMENT,taskName TEXT,note TEXT,startTime TEXT,howOftenQty INTEGER,howOftenDays TEXT,endTime TEXT,taskIndicator INTEGER,howoften TEXT,completedDate TEXT,taskStatus INTEGER,userName TEXT,areaId INTEGER,taskId INTEGER)";

        sqLiteDatabase.execSQL(query5);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS task");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS season");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user_m");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS area");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS taskHistory");
        onCreate(sqLiteDatabase);
    }


    public long addToTaskHistory(Task task,int status,String completedDate) {
        ContentValues cv = new ContentValues();
        cv.put("taskId", task.getTaskId());
        cv.put("taskName", task.getTaskName());
        cv.put("startTime", task.getStartTime());
        cv.put("endTime", task.getEndTime());
        cv.put("userName", task.getUserName());
        cv.put("areaId", task.getAreaId());
        cv.put("howOftenDays", task.getHowOftenDays());
        cv.put("howOftenQty", task.getHowOftenQty());
        cv.put("taskStatus", status);
        cv.put("completedDate", completedDate);
        cv.put("taskIndicator", task.getTaskIndicator());

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("taskHistory", null, cv);
    }

    public long addTask(String taskName, String startTime, String endTime, int areaId, String userName, int howOftenQty, String howOftenDays, int taskStatus, int taskIndicator) {
        ContentValues cv = new ContentValues();
        cv.put("taskName", taskName);
        cv.put("startTime", startTime);
        cv.put("endTime", endTime);
        cv.put("userName", userName);
        cv.put("areaId", areaId);
        cv.put("howOftenDays", howOftenDays);
        cv.put("howOftenQty", howOftenQty);
        cv.put("taskStatus", taskStatus);
        cv.put("taskIndicator", taskIndicator);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("task", null, cv);
    }

    public long updateTask(int taskId, String taskName, String startTime, String endTime, String userName, int howOftenQty, String howOftenDays, int taskStatus, int taskIndicator) {
        ContentValues cv = new ContentValues();
        cv.put("taskName", taskName);
        cv.put("startTime", startTime);
        cv.put("endTime", endTime);
        cv.put("userName", userName);
        cv.put("howOftenDays", howOftenDays);
        cv.put("howOftenQty", howOftenQty);
        cv.put("taskStatus", taskStatus);
        cv.put("taskIndicator", taskIndicator);

        SQLiteDatabase db = getWritableDatabase();
        return db.update("task", cv, "taskId=" + taskId, null);
    }

    public long updateJustDidItTask(int taskId, String startTime, String endTime, int taskStatus, String completedDate, int indicator) {
        ContentValues cv = new ContentValues();
        cv.put("startTime", startTime);
        cv.put("endTime", endTime);
        cv.put("taskStatus", taskStatus);
        cv.put("completedDate", completedDate);
        cv.put("taskIndicator", indicator);

        SQLiteDatabase db = getWritableDatabase();

        return db.update("task", cv, "taskId=" + taskId, null);
    }

    public long addToSeasonTask(String monthName, long taskId) {
        ContentValues cv = new ContentValues();
        cv.put("monthName", monthName);
        cv.put("taskId", taskId);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("season", null, cv);
    }

    public long addToUserM(String userName, int status) {
        ContentValues cv = new ContentValues();
        cv.put("userName", userName);
        cv.put("status", status);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert("user_m", null, cv);
    }

    public boolean isTaskAlready(String taskName) {
        String query = "SELECT * FROM task WHERE taskName='" + taskName + "' ";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c.getCount() > 0;
    }

    public int updateTaskNote(int taskId, String taskNote) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note", taskNote);
        return db.update("task", cv, "taskId=" + taskId, null);
    }

    public boolean isUserAlready(String userName) {
        String query = "SELECT * FROM user_m WHERE userName='" + userName + "' AND status != 0";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c.getCount() > 0;
    }

    public ArrayList<UserM> getUser() {

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM user_m WHERE status=1 ORDER BY userId DESC ";
        Cursor c = db.rawQuery(query, null);

        ArrayList<UserM> userMArrayList = new ArrayList<>();
        UserM userAll = new UserM(-1, "All", 1);
        userMArrayList.add(userAll);
        while (c.moveToNext()) {

            String userName = c.getString(c.getColumnIndex("userName"));
            int userId = c.getInt(c.getColumnIndex("userId"));
            int status = c.getInt(c.getColumnIndex("status"));

            UserM userM = new UserM(userId, userName, status);
            userMArrayList.add(userM);
        }
        return userMArrayList;
    }

    public int deleteUser(int userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", 0);
        return db.update("user_m", cv, "userId=" + userId, null);
    }

    public ArrayList<Task> getTask(int areasId) {

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM Task WHERE areaId=" + areasId;
        Cursor c = db.rawQuery(query, null);

        ArrayList<Task> taskArrayList = new ArrayList<>();
        while (c.moveToNext()) {

            int taskId = c.getInt(c.getColumnIndex("taskId"));
            String taskName = c.getString(c.getColumnIndex("taskName"));
            String note = c.getString(c.getColumnIndex("note"));
            String startTime = c.getString(c.getColumnIndex("startTime"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String howOftenDays = c.getString(c.getColumnIndex("howOftenDays"));
            int howOftenQty = c.getInt(c.getColumnIndex("howOftenQty"));
            String completedDate = c.getString(c.getColumnIndex("completedDate"));
            String userName = c.getString(c.getColumnIndex("userName"));
            int areaId = c.getInt(c.getColumnIndex("areaId"));
            int taskIndicator = c.getInt(c.getColumnIndex("taskIndicator"));
            int taskStatus = c.getInt(c.getColumnIndex("taskStatus"));

            Task task = new Task(taskId, taskName, note, startTime, endTime, areaId, taskStatus, taskIndicator, howOftenQty, howOftenDays, completedDate, userName);
            taskArrayList.add(task);
        }
        return taskArrayList;
    }

    public Task getTaskWithId(int givenTaskId) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id  WHERE taskId=" + givenTaskId;

        Cursor c = db.rawQuery(query, null);
        Task task = new Task();
        if (c.moveToFirst()) {

            do {
                int taskId = c.getInt(c.getColumnIndex("taskId"));
                String taskName = c.getString(c.getColumnIndex("taskName"));
                String note = c.getString(c.getColumnIndex("note"));
                String startTime = c.getString(c.getColumnIndex("startTime"));
                String endTime = c.getString(c.getColumnIndex("endTime"));
                String howOftenDays = c.getString(c.getColumnIndex("howOftenDays"));
                int howOftenQty = c.getInt(c.getColumnIndex("howOftenQty"));
                String completedDate = c.getString(c.getColumnIndex("completedDate"));
                String areaName = c.getString(c.getColumnIndex("areaName"));
                String userName = c.getString(c.getColumnIndex("userName"));
                int areaId = c.getInt(c.getColumnIndex("areaId"));
                int taskIndicator = c.getInt(c.getColumnIndex("taskIndicator"));
                int taskStatus = c.getInt(c.getColumnIndex("taskStatus"));

                task = new Task(taskId, taskName, note, startTime, endTime, areaId, taskStatus, taskIndicator, howOftenQty, howOftenDays, completedDate, userName, areaName);
            } while (c.moveToNext());
        }
        return task;
    }

    public ArrayList<Task> getTaskHistoryWithId(int givenTaskId) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT task.* , area.areaName FROM taskHistory task LEFT JOIN area ON task.areaId=area.Id  WHERE taskId=" + givenTaskId;

        Cursor c = db.rawQuery(query, null);
        Task task = new Task();
        ArrayList<Task> taskArrayList = new ArrayList<>();
        while (c.moveToNext()){

                int taskId = c.getInt(c.getColumnIndex("taskId"));
                int taskHistoryId = c.getInt(c.getColumnIndex("taskHistoryId"));
                String taskName = c.getString(c.getColumnIndex("taskName"));
                String note = c.getString(c.getColumnIndex("note"));
                String startTime = c.getString(c.getColumnIndex("startTime"));
                String endTime = c.getString(c.getColumnIndex("endTime"));
                String howOftenDays = c.getString(c.getColumnIndex("howOftenDays"));
                int howOftenQty = c.getInt(c.getColumnIndex("howOftenQty"));
                String completedDate = c.getString(c.getColumnIndex("completedDate"));
                String areaName = c.getString(c.getColumnIndex("areaName"));
                String userName = c.getString(c.getColumnIndex("userName"));
                int areaId = c.getInt(c.getColumnIndex("areaId"));
                int taskIndicator = c.getInt(c.getColumnIndex("taskIndicator"));
                int taskStatus = c.getInt(c.getColumnIndex("taskStatus"));

                task = new Task(taskId, taskHistoryId,taskName, note, startTime, endTime, areaId, taskStatus, taskIndicator, howOftenQty, howOftenDays, completedDate, userName, areaName);
        taskArrayList.add(task);
        }

        return taskArrayList;
    }

    public ArrayList<Task> getToDoTask(int dueWithin, String groupBy, String assignTo) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT task.* , area.areaName FROM taskHistory task LEFT JOIN area ON task.areaId=area.Id  WHERE task.taskStatus=0 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";

        if (dueWithin != 0 && groupBy.equals("None") && assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM  task LEFT JOIN area ON task.areaId=area.Id  WHERE task.taskStatus=0 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        } else if (dueWithin != 0 && groupBy.equals("Area") && assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id  WHERE task.taskStatus=0 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY area.Id";
        } else if (dueWithin != 0 && groupBy.equals("Date") && assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id WHERE task.taskStatus=0 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        } else if (dueWithin != 0 && groupBy.equals("Date")) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id WHERE task.taskStatus=0 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        } else if (dueWithin == 0 && !assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id  WHERE task.userName='" + assignTo + "' AND task.taskStatus=1 ORDER BY task.endTime";
        } else if (dueWithin == 0) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id WHERE task.taskStatus=0 ORDER BY task.endTime";
        }
        if (!assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id  WHERE task.userName='" + assignTo + "' AND task.taskStatus=0 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        }

        Cursor c = db.rawQuery(query, null);

        ArrayList<Task> taskArrayList = new ArrayList<>();
        while (c.moveToNext()) {

            int taskId = c.getInt(c.getColumnIndex("taskId"));
            String taskName = c.getString(c.getColumnIndex("taskName"));
            String note = c.getString(c.getColumnIndex("note"));
            String startTime = c.getString(c.getColumnIndex("startTime"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String howOftenDays = c.getString(c.getColumnIndex("howOftenDays"));
            int howOftenQty = c.getInt(c.getColumnIndex("howOftenQty"));
            String completedDate = c.getString(c.getColumnIndex("completedDate"));
            String areaName = c.getString(c.getColumnIndex("areaName"));
            String userName = c.getString(c.getColumnIndex("userName"));
            int areaId = c.getInt(c.getColumnIndex("areaId"));
            int taskIndicator = c.getInt(c.getColumnIndex("taskIndicator"));
            int taskStatus = c.getInt(c.getColumnIndex("taskStatus"));

            Task task = new Task(taskId, taskName, note, startTime, endTime, areaId, taskStatus, taskIndicator, howOftenQty, howOftenDays, completedDate, userName, areaName);
            taskArrayList.add(task);
        }
        return taskArrayList;
    }


    public ArrayList<Task> getCompletedTask(int dueWithin, String groupBy, String assignTo) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id  WHERE task.taskStatus=1 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";

        if (dueWithin != 0 && groupBy.equals("None") && assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id  WHERE task.taskStatus=1 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        } else if (dueWithin != 0 && groupBy.equals("Area") && assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id  WHERE task.taskStatus=1 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY area.Id";
        } else if (dueWithin != 0 && groupBy.equals("Date") && assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id WHERE task.taskStatus=1 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        } else if (dueWithin != 0 && groupBy.equals("Date")) {
            query = "SELECT task.* , area.areaName FROM task LEFT JOIN area ON task.areaId=area.Id WHERE task.taskStatus=1 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        } else if (dueWithin == 0 && !assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id  WHERE task.userName='" + assignTo + "' AND task.taskStatus=1 ORDER BY task.endTime";
        } else if (dueWithin == 0) {
            query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id WHERE task.taskStatus=1 ORDER BY task.endTime";
        }
        if (!assignTo.equals("All")) {
            query = "SELECT task.* , area.areaName FROM taskHistory  task LEFT JOIN area ON task.areaId=area.Id  WHERE task.userName='" + assignTo + "' AND task.taskStatus=1 AND  julianday(task.endTime)-julianday('now') <= " + dueWithin + " ORDER BY task.endTime";
        }

        Cursor c = db.rawQuery(query, null);

        ArrayList<Task> taskArrayList = new ArrayList<>();
        while (c.moveToNext()) {

            int taskId = c.getInt(c.getColumnIndex("taskId"));
            int taskHistoryId = c.getInt(c.getColumnIndex("taskHistoryId"));
            String taskName = c.getString(c.getColumnIndex("taskName"));
            String note = c.getString(c.getColumnIndex("note"));
            String startTime = c.getString(c.getColumnIndex("startTime"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String howOftenDays = c.getString(c.getColumnIndex("howOftenDays"));
            int howOftenQty = c.getInt(c.getColumnIndex("howOftenQty"));
            String completedDate = c.getString(c.getColumnIndex("completedDate"));
            String areaName = c.getString(c.getColumnIndex("areaName"));
            String userName = c.getString(c.getColumnIndex("userName"));
            int areaId = c.getInt(c.getColumnIndex("areaId"));
            int taskIndicator = c.getInt(c.getColumnIndex("taskIndicator"));
            int taskStatus = c.getInt(c.getColumnIndex("taskStatus"));

            Task task = new Task(taskId,taskHistoryId, taskName, note, startTime, endTime, areaId, taskStatus, taskIndicator, howOftenQty, howOftenDays, completedDate, userName, areaName);
            taskArrayList.add(task);
        }
        return taskArrayList;
    }

    public int getNoOfTaskRemaining() {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM task WHERE taskStatus=0";
        Cursor cursor = db.rawQuery(query, null);

        return cursor.getCount();
    }

    public ArrayList<Month> getSeason(int tasksId) {

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM season WHERE taskId=" + tasksId;
        Cursor c = db.rawQuery(query, null);

        ArrayList<Month> monthsArrayList = new ArrayList<>();
        while (c.moveToNext()) {

            int taskId = c.getInt(c.getColumnIndex("taskId"));
            String monthName = c.getString(c.getColumnIndex("monthName"));
            int seasonId = c.getInt(c.getColumnIndex("seasonId"));

            Month month = new Month(seasonId, monthName, taskId);
            monthsArrayList.add(month);
        }
        return monthsArrayList;
    }


    public int deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("task", "taskId=" + taskId, null);
    }

    public int deleteSeason(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("season", "taskId=" + taskId, null);
    }

    public int updateTaskIndicator(int indicatorVal, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("taskIndicator", indicatorVal);
        return db.update("task", cv, "taskId=" + id, null);
    }


    ///area functions
    public long addArea(String areaName, int indicatorValue) {
        ContentValues cv = new ContentValues();
        cv.put("areaName", areaName);
        cv.put("indicatorValue", indicatorValue);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("area", null, cv);
    }

    public boolean isAreaAlready(String areaName) {
        String query = "SELECT * FROM area WHERE areaName='" + areaName + "' ";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c.getCount() > 0;
    }

    public ArrayList<Area> getArea() {

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM area";
        Cursor c = db.rawQuery(query, null);

        ArrayList<Area> areaArrayList = new ArrayList<>();
        while (c.moveToNext()) {
            String areaName = c.getString(c.getColumnIndex("areaName"));
            int id = c.getInt(c.getColumnIndex("id"));
            int indicatorValue = c.getInt(c.getColumnIndex("indicatorValue"));

            Area area = new Area(id, areaName, indicatorValue);
            areaArrayList.add(area);
        }
        return areaArrayList;
    }


    public int deleteArea(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("area", "id=" + id, null);
    }

    public int updateAreaIndicator(int indicatorValue, int areaId) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("indicatorValue", indicatorValue);
        return db.update("area", cv, "id=" + areaId, null);

    }


}
