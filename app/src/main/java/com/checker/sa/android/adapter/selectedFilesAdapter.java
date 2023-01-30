package com.checker.sa.android.adapter;

import java.util.ArrayList;
import java.util.List;

import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class selectedFilesAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final QuestionnaireActivity qAct;
	private final List<String> values;
	private final ArrayList<Uri> urIs;
	private final ArrayList<filePathDataID> datasIds;

	public ArrayList<filePathDataID> getFiles() {
		return datasIds;
	}

	public ArrayList<Uri> getURIs() {
		return urIs;
	}

	public selectedFilesAdapter(QuestionnaireActivity context,
			List<String> valuess, ArrayList<Uri> uris,
			ArrayList<filePathDataID> datas, Set set) {
		super(context, R.layout.file_name_row, valuess);
		this.context = context;
		qAct = context;
		values = valuess;
		urIs = uris;
		datasIds = new ArrayList<filePathDataID>();
		for (int i = 0; i < datas.size(); i++) {
			filePathDataID newFilePath = new filePathDataID();
			newFilePath.setDataID(datas.get(i).getDataID(), false);
			newFilePath.setFilePath(datas.get(i).getFilePath());

			newFilePath.setUPLOAD_FILe_BRANCH_NAME(datas.get(i)
					.getUPLOAD_FILe_BRANCH_NAME());
			newFilePath.setUPLOAD_FILe_CLIENT_NAME(datas.get(i)
					.getUPLOAD_FILe_CLIENT_NAME());
			newFilePath.setUPLOAD_FILe_DATAID(datas.get(i)
					.getUPLOAD_FILe_DATAID());
			newFilePath.setUPLOAD_FILe_DATE(datas.get(i).getUPLOAD_FILe_DATE());
			newFilePath.setUPLOAD_FILe_MEDIAFILE(datas.get(i)
					.getUPLOAD_FILe_MEDIAFILE());
			newFilePath.setUPLOAD_FILe_ORDERID(datas.get(i)
					.getUPLOAD_FILe_ORDERID());
			newFilePath.setUPLOAD_FILe_SET_NAME(datas.get(i)
					.getUPLOAD_FILe_SET_NAME());
			newFilePath.setQuestion_name(getQuestionFromSet(datas.get(i)
					.getDataID(), set));
			newFilePath.setUPLOAD_FILe_Sample_size(datas.get(i)
					.getUPLOAD_FILe_Sample_size());
			newFilePath.setUPLOAD_FILe_LOCATIONID(datas.get(i)
					.getUPLOAD_FILe_LOCATIONID());
			newFilePath.setUPLOAD_FILe_PRODUCTID(datas.get(i)
					.getUPLOAD_FILe_PRODUCTID());

			datasIds.add(newFilePath);
		}
	}

	private String getQuestionFromSet(String dataid, Set set) {
		if (set == null || set.getListObjects() == null
				|| set.getListObjects().size() == 0 || dataid == null
				|| dataid.equals(""))
			return null;

		for (int i = 0; i < set.getListObjects().size(); i++) {
			if (set.getListObjects().get(i) != null
					&& set.getListObjects().get(i).getDataID() != null
					&& set.getListObjects().get(i).getDataID().equals(dataid)) {
				return set.getListObjects().get(i).getQuestion();
			}
		}
		return null;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.file_name_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.nameText);
		TextView qtextView = (TextView) rowView.findViewById(R.id.qnameText);

		View qtextlabel = (View) rowView.findViewById(R.id.qnamelabel);
		if (datasIds.get(position).getQuestion_name() == null) {
			qtextView.setVisibility(RelativeLayout.GONE);
			qtextlabel.setVisibility(RelativeLayout.GONE);
		} else {
			qtextView.setVisibility(RelativeLayout.VISIBLE);
			qtextlabel.setVisibility(RelativeLayout.VISIBLE);
			qtextView.setText(Helper.makeHtmlString(datasIds.get(position)
					.getQuestion_name()));
		}
		textView.setTextSize(UIHelper.getFontSize(context,
				textView.getTextSize()));
		ImageView btnClose = (ImageView) rowView.findViewById(R.id.btnClose);
		textView.setText(values.get(position) + "-"
				+ datasIds.get(position).getDataID());
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (values != null && values.size() > position)
					values.remove(position);
				if (urIs != null && urIs.size() > position)
					urIs.remove(position);
				if (datasIds != null && datasIds.size() > position)
					datasIds.remove(position);
				qAct.removeThisAttachment(position);
				notifyDataSetChanged();
			}
		});

		return rowView;
	}
}
