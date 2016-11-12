package com.cs321.todolist;

import com.cs321.todolist.db.TaskContract;
import com.cs321.todolist.db.TaskDbHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.widget.ListView;
import java.util.ArrayList;
import android.database.Cursor;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends AppCompatActivity {
	 private static final String TAG = "MainActivity";
	 private TaskDbHelper mHelper;
	 private ListView mTaskListView;
	 private ArrayAdapter<String> mAdapter;



	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHelper = new TaskDbHelper(this);
		mTaskListView = (ListView) findViewById(R.id.list_todo);

		updateUI();
	}

	//edit created items. Has bugs in database stuff
	public void editTask(View view){
		View parent = (View) view.getParent();
		TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
		final String task = String.valueOf(taskTextView.getText());

		final EditText taskEditText = new EditText(this);
		taskEditText.setText(task, TextView.BufferType.EDITABLE);
		final CharSequence[] items = {" High "," Medium "," Low "};

		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Edit task")
				.setView(taskEditText)
				//Let user choose priority for this task
				.setSingleChoiceItems(items, 0, null)
				.setPositiveButton("Add", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String editedTask = String.valueOf(taskEditText.getText());
						SQLiteDatabase db = mHelper.getWritableDatabase();
						ContentValues values = new ContentValues();
						values.put(TaskContract.TaskEntry.COL_TASK_TITLE, editedTask);
						db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE + "=" + task, null);

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
	public boolean onOptionsItemSelected(MenuItem item) {
    		switch (item.getItemId()) {
        		case R.id.action_add_task:
					final EditText taskEditText = new EditText(this);
					final CharSequence[] items = {" High "," Medium "," Low "};
					final ArrayList seletedItems=new ArrayList();


					AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add a new task")
					.setView(taskEditText)
					//Let user choose priority for this task
					.setSingleChoiceItems(items, 0, null)
					.setPositiveButton("Add", new DialogInterface.OnClickListener(){
				    	@Override
					 	public void onClick(DialogInterface dialog, int which) {
							String task = String.valueOf(taskEditText.getText());
					   		SQLiteDatabase db = mHelper.getWritableDatabase();
					   		ContentValues values = new ContentValues();
					   		values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
					   		db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
						   	null,
						   	values,
						   	SQLiteDatabase.CONFLICT_REPLACE);
						   	db.close();
							updateUI();
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

	private void updateUI() {
		ArrayList<String> taskList = new ArrayList<>();
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
				new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
				null, null, null, null, null);
		while(cursor.moveToNext()) {
			int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
			taskList.add(cursor.getString(idx));
		}

		if (mAdapter == null) {
			mAdapter = new ArrayAdapter<>(this,
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
