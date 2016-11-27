package com.cs321.todolist;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button setReminder;
    public static String text;
    public static String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Intent received = getIntent();
        String[] items = received.getStringArrayExtra(MainActivity.intentExtra);
        text = items[1];
        priority = items[0];
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        setReminder = (Button) findViewById(R.id.setReminder);

        final Calendar currentDate = Calendar.getInstance();
        datePicker.init(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH), null);

        timePicker.setCurrentHour(currentDate.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(currentDate.get(Calendar.MINUTE));

        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar setDate = Calendar.getInstance();
                setDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                System.out.println(setDate.getTimeInMillis()-currentDate.getTimeInMillis());
                createReminder(setDate, currentDate);
                Intent goBack = new Intent(ReminderActivity.this, MainActivity.class);
                startActivity(goBack);
            }
        });
    }

    private void createReminder(Calendar setDate, Calendar currentDate) {
        Intent reminderIntent = new Intent(this, AlertReceiver.class);
        //reminderIntent.putExtra(intentExtra, new String[]{priority, text});
        AlarmManager reminderManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        reminderManager.set(AlarmManager.RTC_WAKEUP, setDate.getTimeInMillis()-currentDate.getTimeInMillis(), PendingIntent.getBroadcast(this, 1,
                reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
