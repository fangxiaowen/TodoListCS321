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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    @Test  //can we create new database?
    public void testPreConditions() {
        assertNotNull(myDatabase);
    }

    @Test   //can we add new task?
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
                null, null, null, null, TaskContract.TaskEntry.COL_TASK_PRIORITY + " DESC");
        //get the task title and task priority and store them in taskList and priorityList
        while(cursor.moveToNext()) {
            int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_PRIORITY);
            taskList.add(cursor.getString(idx1));
            priorityList.add(cursor.getInt(idx2));
        }

        String s2 = taskList.get(0);
        Log.i("S2 isiiiiiiiiiiiiii ", s2);
        assertEquals(s2, s);
        int priority2 = priorityList.get(0);
        assertTrue(priority2 == 1);

        myDatabase.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{s});
    }

}
