package com.example.notetaker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListNotesActivity extends Activity{

	private static final String FILE_NAME = "ListNoteState";
	private List<Note> notes;
	private ListView notesListView;
	private int editingNoteId = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_notes);
		
		notes = new ArrayList<Note>();
		loadListNoteState();
		notesListView = (ListView)findViewById(R.id.notesListView);
		notesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int itemNumber,
					long id) {
				// TODO Auto-generated method stub
				Intent editNoteIntent = new Intent(view.getContext(), 
						EditNoteActivity.class);
				editNoteIntent.putExtra("Note", notes.get(itemNumber));
				editingNoteId = itemNumber;
				startActivityForResult(editNoteIntent, 1);
			}
		});
		
		populateList();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent editNoteIntent = new Intent(this, EditNoteActivity.class);
		startActivityForResult(editNoteIntent, 1);
		return true;
	}

	private void populateList() {
		List<String> values = new ArrayList<String>();
		for(Note note : notes)
		{
			values.add(note.getTitle());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,values);	
		notesListView.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode)
		{
		case RESULT_CANCELED:
			editingNoteId = -1;
			break;
		case EditNoteActivity.RESULT_DELETE:
			notes.remove(editingNoteId);
			editingNoteId = -1;
			saveListNoteState();
			populateList();
			break;
		case RESULT_OK:
			Serializable extra = data.getSerializableExtra("Note");
			if (extra != null)
			{
				Note newNote = (Note)extra;
				if (editingNoteId > -1)
				{
					notes.set(editingNoteId, newNote);
					editingNoteId = -1;
				}
				else
				{
					notes.add(newNote);
				}
				saveListNoteState();
				populateList();
			}
		}
		
	}

	public void saveListNoteState() {
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(notes);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadListNoteState() {
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			notes = (List<Note>)ois.readObject();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
