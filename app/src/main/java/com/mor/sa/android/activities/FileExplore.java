package com.mor.sa.android.activities;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.checker.sa.android.adapter.Item;
import com.checker.sa.android.adapter.explorerPreviewAdapter;
import com.checker.sa.android.helper.Constants;
import com.mor.sa.android.activities.R;

public class FileExplore extends Activity {

	// S++tores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;

	private static final String TAG = "F_PATH";

	private Item[] fileList;

	private File path = new File(CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
			+ "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;

	explorerPreviewAdapter adapter;
	SharedPreferences myPrefs;

	protected File currentFile;

	private boolean sortByDate = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		sortByDate = myPrefs.getBoolean(Constants.FILE_DATE_WISE, false);
		String sPath = myPrefs.getString(Constants.FILE_EXPLORE_PATH,
				CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
						+ "");

		path = new File(sPath);

		sPath = sPath.replace(CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
						+ "",
				"");

		String[] strArr = sPath.split("/");
		for (int i = 0; i < strArr.length; i++) {
			if (!strArr[i].equals("")) {
				firstLvl = false;
				str.add(strArr[i]);
			}
		}
		//
		// imgLoader = new ImageLoader(FileExplore.this);
		String pathh = getFilesDir().getAbsolutePath() + "";

		loadFileList();

		createDialog(DIALOG_LOAD_FILE);
		Log.d(TAG, path.getAbsolutePath());

	}

	private static final int FILE_SELECT_CODE = 11123;

	private void loadFileList() {

		ArrayList<String> listOFExternalMounts = new ArrayList<String>();
		if (firstLvl) {
			// listOFExternalMounts = getExternalMounts();
		}
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists()) {

			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();

				}
			};

			String[] fList = path.list(filter);
			int totalLength = fList.length;
			if (listOFExternalMounts.size() > 0) {
				totalLength += listOFExternalMounts.size();
			}
			fileList = new Item[totalLength];

			for (int j = 0; j < listOFExternalMounts.size(); j++) {
				fileList[j] = new Item(listOFExternalMounts.get(j),
						R.drawable.file_icon);

				// Convert into file path
				File sel = new File(fileList[j].file);

				// Set drawables
				if (sel.isDirectory()) {
					fileList[j].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList[j].file);
				} else {
					Log.d("FILE", fileList[j].file);
				}

			}
			for (int i = 0 + listOFExternalMounts.size(); i < fList.length; i++) {
				fileList[i] = new Item(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList[i].file);
				} else {

					fileList[i].lastModified = sel.lastModified();
					Log.d("FILE", fileList[i].file);
				}
			}

		} else {
			Log.e(TAG, "path does not exist");
		}

		if (txtNoFileFound != null && fileList != null && fileList.length <= 0) {
			txtNoFileFound.setVisibility(RelativeLayout.VISIBLE);
		} else if (txtNoFileFound != null) {
			txtNoFileFound.setVisibility(RelativeLayout.GONE);
		}
		adapter = new explorerPreviewAdapter(FileExplore.this, fileList, path,
				this.sortByDate);
		//
		// new ArrayAdapter<Item>(this,
		// android.R.layout.select_dialog_item, android.R.id.text1,
		// fileList) {
		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// // creates view
		// View view = super.getView(position, convertView, parent);
		// TextView textView = (TextView) view
		// .findViewById(android.R.id.text1);
		//
		// if (fileList[position].file.equals("Up")) {
		// textView.setCompoundDrawablesWithIntrinsicBounds(
		// fileList[position].icon, 0, 0, 0);
		// } else {
		// // put the image on the text view
		// File sel = new File(path, fileList[position].file);
		// textView.setCompoundDrawablesWithIntrinsicBounds(
		// fileList[position].icon, 0, 0, 0);
		//
		// if (!sel.isDirectory()) {
		// try {
		// imgLoader.DisplayImage(sel.getAbsolutePath(),
		// textView);
		// } catch (Exception ex) {
		//
		// }
		//
		// }
		// }
		//
		// // add margin between image and text (support various screen
		// // densities)
		// int dp5 = (int) (5 * getResources().getDisplayMetrics().density +
		// 0.5f);
		// textView.setCompoundDrawablePadding(dp5);
		//
		// return view;
		// }
		// };

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			showAlert();
		}

	}

	public static ArrayList<String> getExternalMounts() {
		final ArrayList<String> out = new ArrayList<String>();
		String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
		String s = "";
		try {
			final Process process = new ProcessBuilder().command("mount")
					.redirectErrorStream(true).start();
			process.waitFor();
			final InputStream is = process.getInputStream();
			final byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				s = s + new String(buffer);
			}
			is.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// parse output
		final String[] lines = s.split("\n");
		for (String line : lines) {
			if (!line.toLowerCase(Locale.US).contains("asec")) {
				if (line.matches(reg)) {
					String[] parts = line.split(" ");
					for (String part : parts) {
						if (part.startsWith("/"))
							if (!part.toLowerCase(Locale.US).contains("vold"))
								out.add(part);
					}
				}
			}
		}
		return out;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// Toast.makeText(FileExplore.this, "Back pressed", Toast.LENGTH_SHORT)
		// .show();
		finish();
	}

	ImageView btnUp = null;

	private ListView listView;

	private View txtNoFileFound;

	private boolean isSelfCreated;

	public Button btnDismiss;

	private ToggleButton tglselect_unselect;

	private TextView tglText;

	public void makeDialog(Context context, ListAdapter adapter2) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_file_explorer);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		txtNoFileFound = (View) dialog.findViewById(R.id.txt_extra);
		if (fileList != null && fileList.length <= 0) {
			txtNoFileFound.setVisibility(RelativeLayout.VISIBLE);
		} else {
			txtNoFileFound.setVisibility(RelativeLayout.GONE);
		}
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				if (isSelfRemoved) {
					isSelfRemoved = false;
				} else {
					if (path.getAbsolutePath().equals(
							new File(CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
									+ "").getAbsolutePath())) {
						finish();
					} else {

						loadUpFolder();
						createDialog(1);
					}
				}
			}
		});
		tglselect_unselect = (ToggleButton) dialog
				.findViewById(R.id.btn_select_unselect);
		tglText = (TextView) dialog.findViewById(R.id.nameText);
		tglselect_unselect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tglselect_unselect.isChecked()) {
					setAllTgl(true);
				} else {
					setAllTgl(false);
				}
			}
		});
		setAllTgl(false);
		listView = (ListView) dialog.findViewById(R.id.list_preview);
		final Spinner btnSort = (Spinner) dialog.findViewById(R.id.btn_sort);
		isSelfCreated = true;
		if (sortByDate)
			btnSort.setSelection(1);
		else
			btnSort.setSelection(0);
		btnSort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					sortByDate = false;
					SharedPreferences.Editor prefsEditor = myPrefs.edit();
					prefsEditor.putBoolean(Constants.FILE_DATE_WISE, false);
					prefsEditor.commit();

				} else {
					sortByDate = true;
					SharedPreferences.Editor prefsEditor = myPrefs.edit();
					prefsEditor.putBoolean(Constants.FILE_DATE_WISE, true);
					prefsEditor.commit();

				}
				if (isSelfCreated == true) {
					isSelfCreated = false;
				} else {
					loadFileList();

					isSelfRemoved = true;
					dialog.dismiss();
					createDialog(DIALOG_LOAD_FILE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		btnDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
		btnDismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.putExtra(Constants.SELECT_FILE_PATH_ARRAY,
						getStringArrayOfAllIds());
				setResult(RESULT_OK, intent);
				dialog.dismiss();
				finish();
			}
		});
		btnDismiss.setVisibility(RelativeLayout.GONE);
		btnUp = (ImageView) dialog.findViewById(R.id.btn_up);
		if (firstLvl) {
			btnUp.setVisibility(RelativeLayout.INVISIBLE);
		} else
			btnUp.setVisibility(RelativeLayout.VISIBLE);
		btnUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadUpFolder();
			}
		});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int which, long id) {
			}
		});

		dialog.setTitle("");
	}

	public void selectThisIndex(int which) {
		chosenFile = fileList[which].file;
		if (!chosenFile.contains("/")) {
			currentFile = new File(path + "/" + chosenFile);
		} else
			currentFile = new File(chosenFile);

		final File sel = currentFile;

		if (sel.isDirectory()) {

			SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.putString(Constants.FILE_EXPLORE_PATH,
					sel.getAbsolutePath());
			prefsEditor.commit();

			firstLvl = false;

			// Adds chosen directory to list
			str.add(chosenFile);
			fileList = null;
			path = new File(sel + "");

			loadFileList();
			if (firstLvl) {
				btnUp.setVisibility(RelativeLayout.INVISIBLE);
			} else
				btnUp.setVisibility(RelativeLayout.VISIBLE);
			listView.setAdapter(adapter);

			Log.d(TAG, path.getAbsolutePath());

		}

		// Checks if 'up' was clicked
		else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {
			loadUpFolder();
		}
		// File picked
		else {
			Intent intent = new Intent();
			intent.putExtra(Constants.SELECT_FILE_PATH,
					currentFile.getAbsolutePath());
			setResult(RESULT_OK, intent);
			finish();
			dialog.dismiss();
			// showAlert();
		}

	}

	protected String[] getStringArrayOfAllIds() {
		if (allIds != null) {
			String[] ids = new String[allIds.size()];
			for (int i = 0; i < allIds.size(); i++) {
				ids[i] = allIds.get(i).file;
			}
			return ids;
		} else
			return null;
	}

	private void setAllTgl(boolean b) {

		if (b && btnDismiss != null) {
			adapter.setIsAllChecked(1);
			tglText.setText("Un Select All");

			btnDismiss.setVisibility(RelativeLayout.VISIBLE);
		} else if (btnDismiss != null) {
			adapter.setIsAllChecked(0);
			tglText.setText("Select All");
			btnDismiss.setVisibility(RelativeLayout.GONE);
		}
	}

	private void showAlert() {
		if (currentFile == null || currentFile.length() <= 0)
			return;
		isSelfRemoved = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(FileExplore.this);
		builder.setMessage(
				getResources().getString(
						R.string.questionnaire_select_file_alert))
				.setTitle(getResources().getString(R.string._alert_title))
				.setCancelable(false)
				.setNeutralButton(
						getResources().getString(R.string.button_show_file),
						new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						})
				.setPositiveButton(
						getResources().getString(
								R.string.questionnaire_exit_delete_alert_yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

								// Reply back

								Intent intent = new Intent();
								intent.putExtra(Constants.SELECT_FILE_PATH,
										currentFile.getAbsolutePath());
								setResult(RESULT_OK, intent);
								finish();
								dialog.dismiss();

							}
						})
				.setNegativeButton(
						getResources().getString(
								R.string.questionnaire_exit_delete_alert_no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								isSelfRemoved = true;
								loadUpFolder();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	Dialog dialog = null;

	private boolean isSelfRemoved = false;

	public ArrayList<Item> allIds = null;

	public void createDialog(int id) {

		AlertDialog.Builder builder = new Builder(this);

		if (fileList == null) {
			Log.e(TAG, "No files loaded");
			makeDialog(FileExplore.this, null);

		} else
			makeDialog(FileExplore.this, adapter);

		dialog.show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadFileList();

		isSelfRemoved = true;
		dialog.dismiss();
		createDialog(DIALOG_LOAD_FILE);

	}

	private void loadUpFolder() {

		// present directory removed from list
		String s = str.remove(str.size() - 1);

		// path modified to exclude present directory
		path = new File(path.toString().substring(0,
				path.toString().lastIndexOf(s)));
		fileList = null;

		// if there are no more directories in the list, then
		// its the first level
		if (str.isEmpty()) {
			firstLvl = true;
			btnUp.setVisibility(RelativeLayout.INVISIBLE);

		}
		loadFileList();
		listView.setAdapter(adapter);

		// isSelfRemoved = true;
		// dialog.dismiss();
		// createDialog(DIALOG_LOAD_FILE);

		Log.d(TAG, path.getAbsolutePath());

	}

}