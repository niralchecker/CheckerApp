package com.checker.sa.android.data;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.checker.sa.android.adapter.posPreviewAdapter;
import com.checker.sa.android.helper.POS_Toggles.EnumToggleButton;

import java.io.Serializable;
import java.util.ArrayList;

public class EditorNote implements Serializable{
	private String NoteFromEditor;
	private String DataLink;
	private String UserName;

	public String getNoteFromEditor() {
		return NoteFromEditor;
	}

	public void setNoteFromEditor(String noteFromEditor) {
		NoteFromEditor = noteFromEditor;
	}

	public String getDataLink() {
		return DataLink;
	}

	public void setDataLink(String dataLink) {
		DataLink = dataLink;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}
}
