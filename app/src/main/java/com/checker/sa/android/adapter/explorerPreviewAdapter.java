package com.checker.sa.android.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.checker.sa.android.helper.ImageLoader;
import com.mor.sa.android.activities.FileExplore;
import com.mor.sa.android.activities.R;

public class explorerPreviewAdapter extends ArrayAdapter<Item> {
	private final FileExplore context;
	Item[] fileList = null;
	ImageLoader imgLoader;
	private File path;
	protected FileExplore act;
	private boolean sortByDate;

	public void setIsAllChecked(int isAllChecked) {
		if (this.sortByDate) {
			fileList = sortDate(fileList);
		} else {
			fileList = sortByName(fileList);
		}
		if (isAllChecked > 0) {

			for (int i = 0; i < fileList.length; i++) {
				if (act.allIds == null)
					act.allIds = new ArrayList<Item>();

				act.allIds.add(fileList[i]);
			}
		} else {
			if (act.allIds != null)
				act.allIds.clear();

		}
		notifyDataSetChanged();
	}

	public explorerPreviewAdapter(FileExplore context, Item[] valItems,
			File currentPath, boolean sortByDate) {
		super(context, R.layout.file_name_row, valItems);
		this.act = this.context = context;
		this.fileList = valItems;
		imgLoader = new ImageLoader(context);
		this.path = currentPath;
		this.sortByDate = sortByDate;
	}

	public Item[] sortByName(Item[] fileList) {
		Arrays.sort(fileList, new Comparator<Item>() {

			@Override
			public int compare(Item lhs, Item rhs) {
				if (rhs != null && lhs != null && rhs.file != null
						&& lhs.file != null)
					return (lhs.file).compareTo(rhs.file);
				else
					return 1;
			}
		});

		return fileList;

	}

	public Item[] sortDate(Item[] fileList) {
		Arrays.sort(fileList, new Comparator<Item>() {

			@Override
			public int compare(Item lhs, Item rhs) {
				try {
					return Long.valueOf(rhs.lastModified).compareTo(
							lhs.lastModified);
				} catch (Exception ex) {
				}
				return 1;
			}
		});

		return fileList;

	}

	@Override
	public int getCount() {

		int l = fileList.length;
		if (this.sortByDate) {
			fileList = sortDate(fileList);
		} else {
			fileList = sortByName(fileList);
		}
		return l;
		// return super.getCount();
	}

	public void OnDelClick(int pos) {
	}

	public void openFile(FileExplore context, File url) throws IOException {
		// Create URI
		File file = url;
		Uri uri = Uri.fromFile(file);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		// Check what kind of file you are trying to open, by comparing the url
		// with extensions.
		// When the if condition is matched, plugin sets the correct intent
		// (mime) type,
		// so Android knew what application to use to open the file
		if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
			// Word document
			intent.setDataAndType(uri, "application/msword");
		} else if (url.toString().contains(".pdf")) {
			// PDF file
			intent.setDataAndType(uri, "application/pdf");
		} else if (url.toString().contains(".ppt")
				|| url.toString().contains(".pptx")) {
			// Powerpoint file
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		} else if (url.toString().contains(".xls")
				|| url.toString().contains(".xlsx")) {
			// Excel file
			intent.setDataAndType(uri, "application/vnd.ms-excel");
		} else if (url.toString().contains(".zip")
				|| url.toString().contains(".rar")) {
			// Zip file
			intent.setDataAndType(uri, "application/zip");
		} else if (url.toString().contains(".rtf")) {
			// RTF file
			intent.setDataAndType(uri, "application/rtf");
		} else if (url.toString().contains(".wav")
				|| url.toString().contains(".mp3")) {
			// WAV audio file
			intent.setDataAndType(uri, "audio/x-wav");
		} else if (url.toString().contains(".gif")) {
			// GIF file
			intent.setDataAndType(uri, "image/gif");
		} else if (url.toString().contains(".jpg")
				|| url.toString().contains(".jpeg")
				|| url.toString().contains(".png")) {
			// JPG file
			intent.setDataAndType(uri, "image/jpeg");
		} else if (url.toString().contains(".txt")) {
			// Text file
			intent.setDataAndType(uri, "text/plain");
		} else if (url.toString().contains(".3gp")
				|| url.toString().contains(".mpg")
				|| url.toString().contains(".mpeg")
				|| url.toString().contains(".mpe")
				|| url.toString().contains(".mp4")
				|| url.toString().contains(".avi")) {
			// Video files
			intent.setDataAndType(uri, "video/*");
		} else {
			// if you want you can also define the intent type for any other
			// file

			// additionally use else clause below, to manage other unknown
			// extensions
			// in this case, Android will show all applications installed on the
			// device
			// so you can choose which application to use
			intent.setDataAndType(uri, "*/*");
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivityForResult(intent, 12);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		// rowView = super.getView(position, convertView, parent);

		rowView = inflater.inflate(R.layout.file_row, parent, false);

		final ToggleButton tgl_selector = (ToggleButton) rowView
				.findViewById(R.id.btn_select_unselect);
		View view = rowView.findViewById(R.id.nameTextView);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (act.allIds != null && act.allIds.size() > 0) {
					act.allIds.add(fileList[position]);
					tgl_selector.setChecked(true);
				} else
					act.selectThisIndex(position);

			}
		});
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);
		ImageView imgZoom = (ImageView) rowView.findViewById(R.id.btn_zoom);

		tgl_selector.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToggleButton thisTglBtn = (ToggleButton) v;
				if (thisTglBtn.isChecked()) {
					if (act.allIds == null) {
						act.allIds = new ArrayList<Item>();
					}
					act.allIds.add(fileList[position]);
					thisTglBtn.setChecked(true);
					act.btnDismiss.setVisibility(RelativeLayout.VISIBLE);
				} else {
					thisTglBtn.setChecked(false);
					ArrayList<Item> allIdss = act.allIds;
					if (act.allIds != null) {
						for (int i = 0; i < act.allIds.size(); i++) {
							if (act.allIds.get(i).file
									.equals(fileList[position].file)) {
								act.allIds.remove(i);
								i = i - 1;
							}
						}
					}
					if (act.allIds == null || act.allIds.size() == 0) {
						act.btnDismiss.setVisibility(RelativeLayout.GONE);
					}
				}

			}
		});
		imgZoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					String chosenFile = fileList[position].file;
					final File sel = new File(path + "/" + chosenFile);
					openFile(act, sel);
				} catch (IOException e) {
					Toast.makeText(context, "unable to open file",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		if (fileList[position] != null && fileList[position].file != null) {
			textView.setText(fileList[position].file);
			if (fileList[position].file.equals("Up")) {
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);

			} else {
				// put the image on the text view
				File sel = new File(path, fileList[position].file);
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);

				if (!sel.isDirectory()) {
					try {
						imgLoader.DisplayImage(sel.getAbsolutePath(), textView);
					} catch (Exception ex) {

					}

					if (act.allIds != null) {
						for (int i = 0; i < act.allIds.size(); i++) {
							if (act.allIds.get(i).file
									.equals(fileList[position].file)) {
								tgl_selector.setChecked(true);
							}
						}
					}

				} else {

					tgl_selector.setVisibility(RelativeLayout.GONE);
					imgZoom.setVisibility(RelativeLayout.GONE);
				}

			}
		} else {
			rowView.setVisibility(RelativeLayout.GONE);
		}
		// add margin between image and text (support various screen
		// densities)
		int dp5 = (int) (5 * context.getResources().getDisplayMetrics().density + 0.5f);
		textView.setCompoundDrawablePadding(dp5);

		return rowView;
	}
}
