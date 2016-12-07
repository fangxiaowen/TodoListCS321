package com.cs321.todolist;

/**
 * Created by Administrator on 11/24/2016.
 */
import com.cs321.todolist.db.TaskContract;
import com.cs321.todolist.db.TaskDbHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;


import org.junit.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
//@RunWith(Theories.class)
public class DatabaseTest {

    /*@DataPoints
    public static String[] s = {"", "Do laundry", "Do something that is a big string", "123456",
            "Final Report", "PaY ThE BiLlS", "FEED THE DOG"};
    @DataPoints
    public static int[] x = {-1,0,1,2,3};
    */

    private TaskDbHelper myHelper = new TaskDbHelper(InstrumentationRegistry.getTargetContext());
    private SQLiteDatabase myDatabase = myHelper.getWritableDatabase();

    @Before //delete all items in the database before test
    public void cleanDatabse(){
        myDatabase.delete(TaskContract.TaskEntry.TABLE, null, null);
    }

    @After
    public void finish() {
        myDatabase.close();
    }

    @Test   //can we create a new databse?
    public void testPreConditions() {
        assertNotNull(myDatabase);
    }

    @Test   //can we add tasks?
    public void addTasks(){
        String s = "Landry";
        int priority = 1;
        assumeTrue(s != null);

        //add data to myDatabase
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, s);
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, priority);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        //Retriving data from myDatabase. String s2 = myDatabase.retrieve()
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<Integer> priorityList = new ArrayList<>();

        Cursor cursor = myDatabase.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_PRIORITY},
                null, null, null, null, TaskContract.TaskEntry.COL_TASK_PRIORITY );
        //get the task title and task priority and store them in taskList and priorityList
        while(cursor.moveToNext()) {
            int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_PRIORITY);
            taskList.add(cursor.getString(idx1));
            priorityList.add(cursor.getInt(idx2));
        }

        String s2 = taskList.get(0);
        assertEquals(s2, s);
        int priority2 = priorityList.get(0);
        assertTrue(priority2 == priority);

        myDatabase.delete(TaskContract.TaskEntry.TABLE, null, null);
    }

    @Test   //Ordered by priority??
    public void priorityOrder(){
        String tasks[] = {"Landry", "Final Report", "Pay my bill", "CS321 Test", "Gym", "Sleep"};
        int priorities[] = {0, 3, 1, 2, 3, 1};
        ContentValues values = new ContentValues();

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Landry");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, 0);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Final Report");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, 3);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Pay my bill");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, 1);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Sleep");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, 1);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "CS321 Test");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, 2);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Gym");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, 3);
        myDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);



        //Retriving data from myDatabase. String s2 = myDatabase.retrieve()
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<Integer> priorityList = new ArrayList<>();

        Cursor cursor = myDatabase.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_PRIORITY},
                null, null, null, null, TaskContract.TaskEntry.COL_TASK_PRIORITY + " DESC");
        //get the task title and task priority and store them in taskList and priorityList
        while(cursor.moveToNext()) {
            int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_PRIORITY);
            taskList.add(cursor.getString(idx1));
            priorityList.add(cursor.getInt(idx2));
        }

        String s1 = taskList.get(0);
        assertEquals("Final Report", s1);
        int priority1 = priorityList.get(0);
        assertEquals(3, priority1);

        String s2 = taskList.get(1);
        int priority2 = priorityList.get(1);
        assertEquals("Gym", s2);
        assertEquals(3, priority2);

        String s3 = taskList.get(2);
        int priority3 = priorityList.get(2);
        assertEquals("CS321 Test", s3);
        assertEquals(2, priority3);

        String s4 = taskList.get(3);
        int priority4 = priorityList.get(3);
        assertEquals("Pay my bill", s4);
        assertEquals(1, priority4);

        String s5 = taskList.get(4);
        int priority5 = priorityList.get(4);
        assertEquals("Sleep", s5);
        assertEquals(1, priority5);

        String s6 = taskList.get(5);
        int priority6 = priorityList.get(5);
        assertEquals("Landry", s6);
        assertEquals(0, priority6);
    }

}
