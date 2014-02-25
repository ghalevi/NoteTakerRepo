package com.example.notetaker;

import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewNoteFragment extends Fragment {
	private EditText titleEditText;
	private EditText noteEditText;
	private OnActionButtonSelectedListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.activity_edit_note_fragment, container, false);
		titleEditText = (EditText)view.findViewById(R.id.titleEditText);
		noteEditText = (EditText)view.findViewById(R.id.noteEditText);
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.edit_action_bar, menu);
		menu.removeItem(R.id.deleteNoteItem);
		menu.removeItem(R.id.editNoteItem);
		menu.removeItem(R.id.shareNoteItem);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.saveNoteItem:
			saveNote();
			break;
		case android.R.id.home:
			
		}
		return true;
	}

	public void saveNote() {
		Note note = new Note(titleEditText.getText().toString(), 
				noteEditText.getText().toString(), 
				Calendar.getInstance().getTime());
		listener.onActionButtonSelected(Activity.RESULT_OK, note);
	}

	public interface OnActionButtonSelectedListener{
		public void onActionButtonSelected(int result, Note note);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof OnActionButtonSelectedListener)
		{
			listener = (OnActionButtonSelectedListener)activity;
		}
		else
		{
			throw new ClassCastException(activity.toString() + 
					" must implemenet NewNoteFragment.OnActionButtonSelectedListener");
		}
	}
}
