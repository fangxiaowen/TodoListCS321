package com.cs321.todolist;

/**
 * Created by Administrator on 11/24/2016.
 */
import com.cs321.todolist.db.TaskContract;
import com.cs321.todolist.db.TaskDbHelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private TaskDbHelper myHelper = new TaskDbHelper(InstrumentationRegistry.getTargetContext());
    private SQLiteDatabase myDatabase = myHelper.getWritableDatabase();

    @Before
    public void setUp(){

    }

    @After
    public void finish() {
        myDatabase.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(myDatabase);
    }








}
