package com.cs321.todolist;

/**
 * Created by Administrator on 11/26/2016.
 */

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void createNewTask(){
        onView(withId(R.id.action_add_task))            // withId(R.id.my_view) is a ViewMatcher
                .perform(click());
    }

    @Test
    public void editTask(){
        onView(withText("UI"))
                .perform(click());
    }

    @Test
    public void taskCreated(){
        onView(withId(R.id.task_title))
                .check(matches(withText("UI")));
    }

    @Test
    public void hasBottomViews(){
        onData(allOf(instanceOf(ArrayList.class), is("UI")))
                .onChildView(withId(R.id.task_delete));
        onData(allOf(instanceOf(ArrayList.class), is("UI")))
                .onChildView(withId(R.id.task_finish));
    }

    @Test
    public void finishTask(){
        onData(allOf(instanceOf(ArrayList.class), is("UI")))
                .perform(click());
                //.onChildView(withId(R.id.bottom_wrapper))
                //.onChildView(withId(R.id.task_finish));
                //.perform(click());
    }





}
