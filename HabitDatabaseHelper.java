package com.example.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class HabitDatabaseHelper {
    private SQLiteDatabase myDb;

    public HabitDatabaseHelper(Context context) {
        myDb = context.openOrCreateDatabase("HabitsTracker", Context.MODE_PRIVATE, null);
        myDb.execSQL("CREATE TABLE IF NOT EXISTS habits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "streak INTEGER DEFAULT 0, " +
                "completed INTEGER DEFAULT 0, " +
                "last_updated TEXT)");
    }


    public void addHabit(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("streak", 0);
        values.put("completed", 0);
        values.put("last_updated", "");
        myDb.insert("habits", null, values);
    }

    // cursor gets all habits so i can display them in the listview
    public ArrayList<String> getAllHabits() {
        ArrayList<String> habits = new ArrayList<>();
        Cursor cursor = myDb.rawQuery("SELECT name FROM habits", null);
        if (cursor.moveToFirst()) {
            do {
                habits.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return habits;
    }

    // Mark a habit as completed
//    public void markHabitCompleted(String name, String date) {
//        Cursor cursor = myDb.rawQuery("SELECT streak, last_updated FROM habits WHERE name = ?", new String[]{name});
//        if (cursor.moveToFirst()) {
//            int streak = cursor.getInt(0);
//            String lastUpdated = cursor.getString(1);
//
//            if (!date.equals(lastUpdated)) {
//                streak++;
//                ContentValues values = new ContentValues();
//                values.put("completed", 1);
//                values.put("streak", streak);
//                values.put("last_updated", date);
//                myDb.update("habits", values, "name = ?", new String[]{name});
//            }
//        }
//        cursor.close();
//    }

    // Reset daily completion
//    public void resetDailyCompletion() {
//        myDb.execSQL("UPDATE habits SET completed = 0");
//    }


    public void deleteHabit(String name) {
        myDb.delete("habits", "name = ?", new String[]{name});
    }

    public void clearHabits() {
        myDb.execSQL("DELETE FROM habits");
    }



//when i click view habit details it select * from habits
    public String viewHabitDetails(String habitName) {
        Cursor cursor = myDb.rawQuery("SELECT * FROM habits WHERE name = ?", new String[]{habitName});
        String details = "Habit not found";

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int streakIndex = cursor.getColumnIndex("streak");
            int completedIndex = cursor.getColumnIndex("completed");
            int lastUpdatedIndex = cursor.getColumnIndex("last_updated");

            // index error might be zero, i had to make sure it was greater than 0
            if (idIndex >= 0 && nameIndex >= 0 && streakIndex >= 0 && completedIndex >= 0 && lastUpdatedIndex >= 0) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int streak = cursor.getInt(streakIndex);
                int completed = cursor.getInt(completedIndex);
                String lastUpdated = cursor.getString(lastUpdatedIndex);

                details = "ID: " + id +
                        "\nName: " + name +
                        "\nStreak: " + streak +
                        "\nCompleted: " + completed +
                        "\nLast Updated: " + lastUpdated;
            }
        }
        cursor.close();
        return details;
    }

}