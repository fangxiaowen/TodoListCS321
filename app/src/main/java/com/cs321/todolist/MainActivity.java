package com.cs321.todolist;

import com.cs321.todolist.db.TaskContract;
import com.cs321.todolist.db.TaskDbHelper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import java.util.ArrayList;
import android.database.Cursor;


public class MainActivity extends AppCompatActivity {
	public final static String intentExtra = "com.cs321.todolist.info";

	private static final String TAG = "MainActivity";
	private TaskDbHelper mHelper;
	private ListView mTaskListView;
	private MyArrayAdapter mAdapter;
	public static ArrayList<Integer> priority;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHelper = new TaskDbHelper(this);
		mTaskListView = (ListView) findViewById(R.id.list_todo);
		updateUI();
	}

	public void setReminder(String priority, String text){
		final String p = priority;
		final String t = text;
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Would you like to add a reminder?:")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						Intent openDateTime = new Intent(MainActivity.this, ReminderActivity.class);
						openDateTime.putExtra(intentExtra, new String[]{p, t});
						startActivity(openDateTime);
						//createNotification(p, t);
					}
				})
				.setNegativeButton("No", null)
				.create();
		dialog.show();
	}

	/*public void createNotification(String priority, String text){

		NotificationCompat.Builder nBuilder = (android.support.v7.app.NotificationCompat.Builder)
				new NotificationCompat.Builder(this)
				.setContentTitle("Complete" + priority + "priority task!")
				.setContentText(text)
				.setTicker("Reminder: Item requires attention!")
				.setSmallIcon(R.drawable.alert);

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(nID, nBuilder.build());
		isNActive = true;
	}*/

	//edit created items. Has bugs in database stuff
	//need to add method for updating priority
	public void editTask(View view){
		View parent = (View) view.getParent();
		TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
		final String task = String.valueOf(taskTextView.getText());
		final EditText taskEditText = new EditText(this);
		taskEditText.setText(task, TextView.BufferType.EDITABLE);
		final CharSequence[] items = {" High "," Medium "," Low "," None "}; // Added the ability to have no priority.

		//create a alert dialog for user to change info of this clicked task
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Edit task:")
				.setView(taskEditText)
				//Let user choose priority for this task
				.setSingleChoiceItems(items, 0, null)
				.setPositiveButton("Add", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String editedTask = String.valueOf(taskEditText.getText());
						ListView lw = ((AlertDialog)dialog).getListView();
						String checkedPriority = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
						int priorityNum;
						//3 for High, 2 for Medium, 1 for Low
						if (checkedPriority.equals(" High "))
							priorityNum = 3;
						else if (checkedPriority.equals(" Medium "))
							priorityNum = 2;
						else if (checkedPriority.equals(" Low "))
							priorityNum = 1;
						// 0 was added for None
						else
							priorityNum = 0;
						//get a database to update new data of this task
						SQLiteDatabase db = mHelper.getWritableDatabase();
						ContentValues values = new ContentValues();
						values.put(TaskContract.TaskEntry.COL_TASK_TITLE, editedTask);
						values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, priorityNum);
						//this is the update method. We can use a better way to do this. Will do it later.
						db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE + "='" + task + "'", null);
						db.close();

						updateUI();
					}
				})
				.setNegativeButton("Cancel", null)
				.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    		getMenuInflater().inflate(R.menu.main_menu, menu);
    		return super.onCreateOptionsMenu(menu);
	}

	@Override
	//create a new task when "+" button is clicked. Pretty the same as method editTask().
	public boolean onOptionsItemSelected(MenuItem item) {
    		switch (item.getItemId()) {
        		case R.id.action_add_task:
					final EditText taskEditText = new EditText(this);
					final CharSequence[] items = {" High "," Medium "," Low ", " None "}; // Added None priority

					AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add a new task:")
					.setView(taskEditText)
					//Let user choose priority for this task
					.setSingleChoiceItems(items, 0, null)
					.setPositiveButton("Add", new DialogInterface.OnClickListener(){
				    	@Override
					 	public void onClick(DialogInterface dialog, int which) {
							String task = String.valueOf(taskEditText.getText());
							if (task.equals("")) return; // Ensures that you cannot create an empty task.
							ListView lw = ((AlertDialog)dialog).getListView();
							String checkedPriority = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
							int priorityNum;
							Log.v("Pri ", checkedPriority);
							if (checkedPriority.equals(" High "))
								priorityNum = 3;
							else if (checkedPriority.equals(" Medium "))
								priorityNum = 2;
							else if (checkedPriority.equals(" Low "))
								priorityNum = 1;
							// 0 for None
							else
								priorityNum = 0;
					   		SQLiteDatabase db = mHelper.getWritableDatabase();
					   		ContentValues values = new ContentValues();
							values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
							values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, priorityNum);
					   		db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
						   	null,
						   	values,
						   	SQLiteDatabase.CONFLICT_REPLACE);
							db.close();
							updateUI();
							setReminder(checkedPriority.toLowerCase(), task);
							//createNotification(checkedPriority.toLowerCase(), task);
						}
				    })
		 			.setNegativeButton("Cancel", null)
					.create();

					dialog.show();
				return true;

        		default:
            			return super.onOptionsItemSelected(item);
    		}
	}

	public void deleteTask(View view) {
		View parent = (View) view.getParent();
		TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
		String task = String.valueOf(taskTextView.getText());
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.delete(TaskContract.TaskEntry.TABLE,
				TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
				new String[]{task});
		db.close();
		updateUI();
	}

	public void updateUI() {
		ArrayList<String> taskList = new ArrayList<>();
		ArrayList<Integer> priorityList = new ArrayList<>();
		SQLiteDatabase db = mHelper.getReadableDatabase();
		//Get a query which sorts the database by task priority in descending order
		Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
				new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_PRIORITY},
				null, null, null, null, TaskContract.TaskEntry.COL_TASK_PRIORITY + " DESC");
		//get the task title and task priority and store them in taskList and priorityList
		while(cursor.moveToNext()) {
			int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
			int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_PRIORITY);
			taskList.add(cursor.getString(idx1));
			priorityList.add(cursor.getInt(idx2));
		}
		priority = priorityList;
		Log.v("Priority ", priorityList.toString());
		//Use adapter to create the whole view for our app
		if (mAdapter == null) {
			mAdapter = new MyArrayAdapter(this,
					R.layout.item_todo,
					R.id.task_title,
					taskList);
			mTaskListView.setAdapter(mAdapter);
		} else {
			mAdapter.clear();
			mAdapter.addAll(taskList);
			mAdapter.notifyDataSetChanged();
		}

		cursor.close();
		db.close();


	}

}
