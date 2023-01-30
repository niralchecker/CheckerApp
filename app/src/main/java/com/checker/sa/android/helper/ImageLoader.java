package com.checker.sa.android.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.TextView;

import com.mor.sa.android.activities.R;

public class ImageLoader {

	// Initialize MemoryCache
	MemoryCache memoryCache = new MemoryCache();

	FileCache fileCache;

	// Create Map (collection) to store image and image url in key value pair
	private Map<TextView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<TextView, String>());
	ExecutorService executorService;

	// handler to display images in UI thread
	Handler handler = new Handler();

	public ImageLoader(Context context) {

		fileCache = new FileCache(context);

		// Creates a thread pool that reuses a fixed number of
		// threads operating off a shared unbounded queue.
		executorService = Executors.newFixedThreadPool(5);

	}

	// default image show in list (Before online image download)
	final int stub_id = R.drawable.file_icon;

	public void DisplayImage(String url, TextView imageView) {
		// Store image and url in Map
		imageViews.put(imageView, url);

		// Check image is stored in MemoryCache Map or not (see
		// MemoryCache.java)
		Bitmap bitmap = memoryCache.get(url);
		if (url.equals("Up")) {
			imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.next,
					0, 0, 0);
		} else if (bitmap != null) {
			// if image is stored in MemoryCache Map then
			// Show image in listview row
			Drawable d = new BitmapDrawable(bitmap);
			imageView.setCompoundDrawablesWithIntrinsicBounds(d, null, null,
					null);
			// imageView.setImageBitmap(bitmap);
		} else {
			// queue Photo to download from url
			queuePhoto(url, imageView);

			// Before downloading image show default image
			imageView.setCompoundDrawablesWithIntrinsicBounds(stub_id, 0, 0, 0);
		}
	}

	private void queuePhoto(String url, TextView imageView) {
		// Store image and url in PhotoToLoad object
		PhotoToLoad p = new PhotoToLoad(url, imageView);

		// pass PhotoToLoad object to PhotosLoader runnable class
		// and submit PhotosLoader runnable to executers to run runnable
		// Submits a PhotosLoader runnable task for execution

		executorService.submit(new PhotosLoader(p));
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public TextView imageView;

		public PhotoToLoad(String u, TextView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				// Check if image already downloaded
				if (imageViewReused(photoToLoad))
					return;
				// download image from web url
				Bitmap bmp = getBitmap(photoToLoad.url);

				// set image data in Memory Cache
				memoryCache.put(photoToLoad.url, bmp);

				if (imageViewReused(photoToLoad))
					return;

				// Get bitmap to display
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);

				// Causes the Runnable bd (BitmapDisplayer) to be added to the
				// message queue.
				// The runnable will be run on the thread to which this handler
				// is attached.
				// BitmapDisplayer run method will call
				handler.post(bd);

			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		// CHECK : if trying to decode file which not exist in cache return null
		Bitmap b = decodeFile(url);

		if (b != null)
			return b;
		else
			return decodeFileLatest(url);
		// Download image file from web
		// try {
		//
		// Bitmap bitmap = null;
		// URL imageUrl = new URL(url);
		// HttpURLConnection conn = (HttpURLConnection) imageUrl
		// .openConnection();
		// conn.setConnectTimeout(30000);
		// conn.setReadTimeout(30000);
		// conn.setInstanceFollowRedirects(true);
		// InputStream is = conn.getInputStream();
		//
		// // Constructs a new FileOutputStream that writes to file
		// // if file not exist then it will create file
		// OutputStream os = new FileOutputStream(f);
		//
		// // See Utils class CopyStream method
		// // It will each pixel from input stream and
		// // write pixels to output stream (file)
		// Utils.CopyStream(is, os);
		//
		// os.close();
		// conn.disconnect();
		//
		// // Now file created and going to resize file with defined height
		// // Decodes image and scales it to reduce memory consumption
		// // bitmap = decodeFile(f);
		//
		// return bitmap;
		//
		// } catch (Throwable ex) {
		// ex.printStackTrace();
		// if (ex instanceof OutOfMemoryError)
		// memoryCache.clear();
		// return null;
		// }
	}

	private Bitmap decodeFileLatest(String path) {
		String filepath = path;
		File imagefile = new File(filepath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(imagefile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Bitmap bm = BitmapFactory.decodeStream(fis);
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

		return bm.createScaledBitmap(bm, 120, 120, true);
	}

	// Decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(String url) {

		try {
			Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
					BitmapFactory.decodeFile(url), 120, 120);
			if (thumbImage != null)
				return thumbImage;
		} catch (Exception ex) {

		}

		try {
			Bitmap bMap = ThumbnailUtils.createVideoThumbnail(url,
					MediaStore.Video.Thumbnails.MICRO_KIND);
			return bMap;
		} catch (Exception ex) {

		}
		return null;
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {

		String tag = imageViews.get(photoToLoad.imageView);
		// Check url is already exist in imageViews MAP
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;

			// Show bitmap on UI
			if (bitmap != null) {
				Drawable d = new BitmapDrawable(bitmap);
				photoToLoad.imageView.setCompoundDrawablesWithIntrinsicBounds(
						d, null, null, null);
			} else
				photoToLoad.imageView.setCompoundDrawablesWithIntrinsicBounds(
						stub_id, 0, 0, 0);
		}
	}

	public void clearCache() {
		// Clear cache directory downloaded images and stored data in maps
		memoryCache.clear();
		fileCache.clear();
	}

}
