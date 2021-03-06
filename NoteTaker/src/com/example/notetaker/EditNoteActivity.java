package com.example.notetaker;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNoteActivity extends Activity {
	public static final int RESULT_DELETE = -500;
	private boolean isInEditMode = true;
	private boolean isAddingNote = true;
	private EditText titleEditText;
	private EditText noteEditText;
	private String titleOldState;
	private String noteOldState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	
		titleEditText = (EditText)findViewById(R.id.titleEditText);
		noteEditText = (EditText)findViewById(R.id.noteEditText);
		final TextView dateEditText = (TextView)findViewById(R.id.dateEditText);
		final TextView timeEditText = (TextView)findViewById(R.id.timeEditText);

		Serializable extra = getIntent().getSerializableExtra("Note");
		if (extra != null)
		{
			Note note = (Note)extra;
			titleEditText.setText(note.getTitle());
			noteEditText.setText(note.getNote());
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String date = dateFormat.format(note.getDate());
			dateEditText.setText(date);

			dateFormat = new SimpleDateFormat("HH:mm:ss");
			date = dateFormat.format(note.getDate());
			timeEditText.setText(date);

			if (savedInstanceState != null)
			{
				isInEditMode = savedInstanceState.getBoolean("isInEditMode");
			}
			else
			{
				isInEditMode = false;
			}
			titleEditText.setEnabled(isInEditMode);
			noteEditText.setEnabled(isInEditMode);
			isAddingNote = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_action_bar, menu);
		if (isAddingNote && isInEditMode)
		{
			menu.removeItem(R.id.deleteNoteItem);
			menu.removeItem(R.id.editNoteItem);
			menu.removeItem(R.id.cancelItem);
			menu.removeItem(R.id.shareNoteItem);
		}
		else if(isAddingNote == false && isInEditMode == false)
		{
			menu.removeItem(R.id.saveNoteItem);
			menu.removeItem(R.id.cancelItem);
		}
		else if (isAddingNote == false && isInEditMode)
		{
			menu.removeItem(R.id.deleteNoteItem);
			menu.removeItem(R.id.editNoteItem);
			menu.removeItem(R.id.shareNoteItem);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.deleteNoteItem:
			deleteAlertDialog();
			break;
		case R.id.cancelItem:
			cancelActionButton();
			break;
		case R.id.saveNoteItem:
			saveNote();
			break;
		case R.id.editNoteItem:
			editNote();
			break;
		case android.R.id.home:
//			NavUtils.navigateUpFromSameTask(this);
			if (isAddingNote || isInEditMode)
			{
				noteNotSavedAlertDialog();
			}
			setResult(RESULT_CANCELED, new Intent());
			finish();
			break;
		case R.id.shareNoteItem:
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, titleEditText.getText().toString());
			emailIntent.putExtra(Intent.EXTRA_TEXT, noteEditText.getText().toString());
			String title = getResources().getString(R.string.share_this_note_via);
			Intent chooser = Intent.createChooser(emailIntent, title);
			if (emailIntent.resolveActivity(getPackageManager()) != null) 
			{
			    startActivity(chooser);
			}
			
		}
		return true;
	}

	public void cancelActionButton() {
		if (!titleOldState.equals(titleEditText.getText().toString()) || 
				!noteOldState.equals(noteEditText.getText().toString()))
		{
			noteNotSavedAlertDialog();
		}
		isInEditMode = false;
		titleEditText.setEnabled(isInEditMode);
		noteEditText.setEnabled(isInEditMode);
		invalidateOptionsMenu();
	}

	public void editNote() {
		titleOldState = titleEditText.getText().toString();
		noteOldState = noteEditText.getText().toString();
		isInEditMode = true;
		titleEditText.setEnabled(isInEditMode);
		noteEditText.setEnabled(isInEditMode);
		invalidateOptionsMenu();
	}

	public void deleteAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_confirmation_text);
		builder.setTitle(R.string.delete_confirm);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent();
				setResult(RESULT_DELETE, returnIntent);
				finish();
			}
		});
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		builder.create().show();
	}
	
	public void emptyTitleAlertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.enter_a_title_please_);
		builder.setTitle(R.string.empty_title);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		builder.create().show();
	}

	public void noteNotSavedAlertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to save changes before canceling?");
		builder.setTitle("Save Note");
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				saveNote();
			}
		});
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				titleEditText.setText(titleOldState);
				noteEditText.setText(noteOldState);
			}
		});
		
		builder.create().show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("isInEditMode", isInEditMode);
		super.onSaveInstanceState(outState);
	}

	public void saveNote(){
		if (titleEditText.getText().toString().equals(""))
		{
			emptyTitleAlertDialog();
			return;
		}
		Note note = new Note(titleEditText.getText().toString(), 
				noteEditText.getText().toString(), 
				Calendar.getInstance().getTime());
		Intent returnIntent = new Intent();
		returnIntent.putExtra("Note", note);
		setResult(RESULT_OK, returnIntent);
		isInEditMode = false;
		invalidateOptionsMenu();
		finish();
	}

}
