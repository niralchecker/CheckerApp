package com.checker.sa.android.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.checker.sa.android.data.POS_Shelf;
import com.checker.sa.android.data.Products;
import com.mor.sa.android.activities.R;

public class POS_Toggles {

	public POS_Toggles(Boolean isPreviewScreen) {
		this.isPreviewScreen = isPreviewScreen;
	}

	Boolean isPreviewScreen;
	onListShown onListShow;

	public interface onListShown {
		public void onDalogClose();
	}

	public void setOnListShownListener(onListShown onListShow) {
		this.onListShow = onListShow;
	}

	public ImageView imgPreview;
	public TextView txtPreview;
	public ListView listPreview;
	public POS_Shelf shelf_item;
	public TextView txtExtra;
	public Button btnDismiss;

	public ToggleButton tglCount;
	public ToggleButton tglPrice;
	public ToggleButton tglExpiration;
	public ToggleButton tglNote;
	public ToggleButton tglPicture;

	public RelativeLayout layout_none;
	public RelativeLayout layout_preview;
	public RelativeLayout layout_Count;
	public RelativeLayout layout_Price;
	public RelativeLayout layout_Expiration;
	public RelativeLayout layout_Note;
	public RelativeLayout layout_Picture;

	public EnumToggleButton currentToggle;
	private Context context;
	private String SelectedProduct;
	private Dialog dialog;

	public void makeDialog(Context context) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {

				onListShow.onDalogClose();
			}
		});

		if (Helper.getTheme(context) == 0) {
			dialog.setContentView(R.layout.pos_shelf_study_night);
		} else {
			dialog.setContentView(R.layout.pos_shelf_study);
		}
		dialog.setTitle(context.getResources().getString(R.string.pos_preview));

		setTextPreview((TextView) dialog.findViewById(R.id.txt_product));
		setTextExtra((TextView) dialog.findViewById(R.id.txt_extra));
		setListPreview((ListView) dialog.findViewById(R.id.list_preview));
		setbnDismiss((Button) dialog.findViewById(R.id.btn_dismiss));
		//
		// Button dialogButton = (Button)
		// dialog.findViewById(R.id.dialogButtonOK);
		// // if button is clicked, close the custom dialog
		// dialogButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });

		// dialog.show();
	}

	
	public void setSelectedProduct(String selectedProduct) {
		SelectedProduct = selectedProduct;
	}

	public enum EnumToggleButton {
		COUNT, PRICE, EXPIRATION, NOTE, PICTURE
	}

	public void selectToggleButton(Boolean selectNone) {
		tglCount.setChecked(false);
		tglPrice.setChecked(false);
		tglExpiration.setChecked(false);
		tglNote.setChecked(false);
		tglPicture.setChecked(false);

		layout_Count.setVisibility(View.GONE);
		layout_Expiration.setVisibility(View.GONE);
		layout_Note.setVisibility(View.GONE);
		layout_Picture.setVisibility(View.GONE);
		layout_Price.setVisibility(View.GONE);
		imgPreview.setVisibility(View.GONE);
		layout_none.setVisibility(View.VISIBLE);

		if (currentToggle == EnumToggleButton.COUNT && !selectNone) {
			tglCount.setChecked(true);
			listPreview.setVisibility(View.GONE);
			txtPreview.setVisibility(View.GONE);
			txtExtra.setVisibility(View.GONE);
			layout_none.setVisibility(View.GONE);
			imgPreview.setVisibility(View.VISIBLE);
			layout_Count.setVisibility(View.VISIBLE);

			if (shelf_item.quantity_item.getItems(SelectedProduct) > 0
					|| isPreviewScreen)
				imgPreview.setVisibility(View.VISIBLE);
			else
				imgPreview.setVisibility(View.GONE);
		}
		if (currentToggle == EnumToggleButton.EXPIRATION && !selectNone) {
			tglExpiration.setChecked(true);
			listPreview.setVisibility(View.GONE);
			txtPreview.setVisibility(View.GONE);
			txtExtra.setVisibility(View.GONE);
			layout_Expiration.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			imgPreview.setVisibility(View.VISIBLE);

			if (shelf_item.expiration_item.getItems(SelectedProduct) > 0
					|| isPreviewScreen)
				imgPreview.setVisibility(View.VISIBLE);
			else
				imgPreview.setVisibility(View.GONE);
		}
		if (currentToggle == EnumToggleButton.NOTE && !selectNone) {
			tglNote.setChecked(true);
			listPreview.setVisibility(View.GONE);
			txtPreview.setVisibility(View.GONE);
			txtExtra.setVisibility(View.GONE);
			layout_Note.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			imgPreview.setVisibility(View.VISIBLE);

			if (shelf_item.note_item.getItems(SelectedProduct) > 0
					|| isPreviewScreen)
				imgPreview.setVisibility(View.VISIBLE);
			else
				imgPreview.setVisibility(View.GONE);
		}
		if (currentToggle == EnumToggleButton.PICTURE && !selectNone) {
			tglPicture.setChecked(true);
			listPreview.setVisibility(View.GONE);
			txtPreview.setVisibility(View.GONE);
			txtExtra.setVisibility(View.GONE);
			layout_Picture.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			imgPreview.setVisibility(View.GONE);

			// if (shelf_item.picture_item.getItems(SelectedProduct)>0)
			// imgPreview.setVisibility(RelativeLayout.VISIBLE);

		}
		if (currentToggle == EnumToggleButton.PRICE && !selectNone) {
			tglPrice.setChecked(true);
			listPreview.setVisibility(View.GONE);
			txtPreview.setVisibility(View.GONE);
			txtExtra.setVisibility(View.GONE);
			layout_Price.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			imgPreview.setVisibility(View.VISIBLE);

			if (shelf_item.price_item.getItems(SelectedProduct) > 0
					|| isPreviewScreen)
				imgPreview.setVisibility(View.VISIBLE);
			else
				imgPreview.setVisibility(View.GONE);

		}
	}

	public void hideShow(Products product, Boolean isAttachmentAllowed) {
		if (product == null) {
			tglExpiration.setVisibility(View.VISIBLE);
			tglCount.setVisibility(View.VISIBLE);
			tglNote.setVisibility(View.VISIBLE);
			tglPicture.setVisibility(View.VISIBLE);
			tglPrice.setVisibility(View.VISIBLE);
			return;
		}

		layout_Count.setVisibility(View.GONE);
		layout_Expiration.setVisibility(View.GONE);
		layout_Note.setVisibility(View.GONE);
		layout_Picture.setVisibility(View.GONE);
		layout_Price.setVisibility(View.GONE);
		layout_none.setVisibility(View.VISIBLE);

		Boolean selectNone = true;

		if (product.getCheckExpiration().equals("1")) {
			tglExpiration.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			selectNone = false;
		} else {
			tglExpiration.setVisibility(View.GONE);
			layout_Expiration.setVisibility(View.GONE);
			if (currentToggle == EnumToggleButton.EXPIRATION)
				currentToggle = EnumToggleButton.COUNT;
		}

		if (product.getAddNote().equals("1")) {
			tglNote.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			selectNone = false;
		} else {
			tglNote.setVisibility(View.GONE);
			layout_Note.setVisibility(View.GONE);
			if (currentToggle == EnumToggleButton.NOTE)
				currentToggle = EnumToggleButton.COUNT;
		}

		if (product.getTakePicture().equals("1")) {
			tglPicture.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);

			selectNone = false;
		} else {
			tglPicture.setVisibility(View.GONE);
			layout_Picture.setVisibility(View.GONE);

			if (currentToggle == EnumToggleButton.PICTURE)
				currentToggle = EnumToggleButton.COUNT;
		}

		if (product.getCheckPrice().equals("1")) {
			tglPrice.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			selectNone = false;
		} else {
			tglPrice.setVisibility(View.GONE);
			layout_Price.setVisibility(View.GONE);

			if (currentToggle == EnumToggleButton.PRICE)
				currentToggle = EnumToggleButton.COUNT;
		}

		if (product.getCheckQuantity().equals("1")) {
			tglCount.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
			selectNone = false;
		} else {
			tglCount.setVisibility(View.GONE);
			layout_Count.setVisibility(View.GONE);

			if (currentToggle == EnumToggleButton.COUNT)
				currentToggle = EnumToggleButton.PRICE;
		}

		selectToggleButton(selectNone);
	}

	public void hideCurrentLayout() {
		layout_Count.setVisibility(View.GONE);
		layout_Expiration.setVisibility(View.GONE);
		layout_Note.setVisibility(View.GONE);
		layout_Picture.setVisibility(View.GONE);
		layout_Price.setVisibility(View.GONE);
		layout_none.setVisibility(View.GONE);
	}

	public void showCurrentLayout() {

		tglCount.setChecked(false);
		tglPrice.setChecked(false);
		tglExpiration.setChecked(false);
		tglNote.setChecked(false);
		tglPicture.setChecked(false);

		layout_Count.setVisibility(View.GONE);
		layout_Expiration.setVisibility(View.GONE);
		layout_Note.setVisibility(View.GONE);
		layout_Picture.setVisibility(View.GONE);
		layout_Price.setVisibility(View.GONE);
		layout_none.setVisibility(View.GONE);

		if (currentToggle == EnumToggleButton.COUNT) {
			tglCount.setChecked(true);
			layout_Count.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);
		}
		if (currentToggle == EnumToggleButton.EXPIRATION) {
			tglExpiration.setChecked(true);
			layout_Expiration.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);

		}
		if (currentToggle == EnumToggleButton.NOTE) {
			tglNote.setChecked(true);
			layout_Note.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);

		}
		if (currentToggle == EnumToggleButton.PICTURE) {
			tglPicture.setChecked(true);
			layout_Picture.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);

		}
		if (currentToggle == EnumToggleButton.PRICE) {
			tglPrice.setChecked(true);
			layout_Price.setVisibility(View.VISIBLE);
			layout_none.setVisibility(View.GONE);

		}
	}

	public ListView showList(ListView listViewPreview, Context context,
			POS_Shelf pos_shelf_item, String productId) {
		txtPreview.setText(shelf_item.getProductName(productId));
		if (currentToggle == EnumToggleButton.COUNT) {
			listViewPreview = pos_shelf_item.quantity_item
					.getListViewReadyWithItems(listViewPreview, context,
							pos_shelf_item, productId, txtExtra,
							this.isPreviewScreen);

			// txtExtra.setText(shelf_item.getProductName(productId));

		}
		if (currentToggle == EnumToggleButton.EXPIRATION) {
			listViewPreview = pos_shelf_item.expiration_item
					.getListViewReadyWithItems(listViewPreview, context,
							pos_shelf_item, productId, this.isPreviewScreen);
			txtExtra.setText("");
			txtExtra.setVisibility(View.GONE);

		}
		if (currentToggle == EnumToggleButton.NOTE) {
			listViewPreview = pos_shelf_item.note_item
					.getListViewReadyWithItems(listViewPreview, context,
							pos_shelf_item, productId, txtExtra,
							this.isPreviewScreen);
			txtExtra.setText("");
			txtExtra.setVisibility(View.GONE);

		}
		if (currentToggle == EnumToggleButton.PICTURE) {
			listViewPreview = null;

			txtExtra.setText("");
			txtExtra.setVisibility(View.GONE);

		}
		if (currentToggle == EnumToggleButton.PRICE) {
			listViewPreview = pos_shelf_item.price_item
					.getListViewReadyWithItems(listViewPreview, context,
							pos_shelf_item, productId, txtExtra,
							this.isPreviewScreen);

		}

		return listViewPreview;
	}

	public void setimgPreview(ImageView imgPrevie, POS_Shelf shelf, Context c) {
		makeDialog(c);
		this.imgPreview = imgPrevie;
		this.shelf_item = shelf;
		this.context = c;
		imgPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (listPreview.getVisibility() == RelativeLayout.GONE)
				// {
				listPreview.setVisibility(View.VISIBLE);
				txtPreview.setVisibility(View.VISIBLE);
				txtExtra.setVisibility(View.VISIBLE);
				listPreview = showList(listPreview, context, shelf_item,
						SelectedProduct);
				dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT);
				dialog.show();
				// hideCurrentLayout();
				// } else {
				// listPreview.setVisibility(RelativeLayout.GONE);
				// txtPreview.setVisibility(RelativeLayout.GONE);
				// txtExtra.setVisibility(RelativeLayout.GONE);
				// dialog.dismiss();
				// //showCurrentLayout();
				// }
			}
		});
	}

	private void setListPreview(ListView listPrevie) {
		this.listPreview = listPrevie;
	}

	public void setTextExtra(TextView findViewById) {
		txtExtra = findViewById;
		txtExtra.setText("");
	}

	public void setTextPreview(TextView findViewById) {
		txtPreview = findViewById;
		txtPreview.setText("");
	}

	public void setbnDismiss(Button findViewById) {
		btnDismiss = findViewById;
		btnDismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

}
