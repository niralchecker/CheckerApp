package com.checker.sa.android.transport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.CheckerApp;


public class Connector {
	public static OutputStream out = null;
	public static List<Cookie> cookies = null;
	static String error = "";
	private static boolean isLogin;
	private static Context context;

	public static Bitmap getImageData(String url) {
		Bitmap bmp = null;
		DefaultHttpClient client = new DefaultHttpClient();
		client = setProxy(client);
		HttpGet get = new HttpGet(url);
		HttpResponse getResponse = null;
		try {
			getResponse = client.execute(get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String entityContents = "";
		HttpEntity responseEntity = getResponse.getEntity();
		BufferedHttpEntity httpEntity = null;
		try {
			httpEntity = new BufferedHttpEntity(responseEntity);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream imageStream = null;
		try {
			imageStream = httpEntity.getContent();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bmp = BitmapFactory.decodeStream(imageStream);
		return bmp;
	}

	public static Bitmap getImageDataa(String url) {
		URLConnection conn = null;
		InputStream input = null;
		try {
			URL updateURL = new URL(url);
			conn = updateURL.openConnection();
			conn.setRequestProperty("Content-Language", "en-US");
			conn.setConnectTimeout(90000);
			conn.setReadTimeout(90000);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			input = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(input);
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			return bm;
		} catch (Exception e) {
			System.out.println("error=" + e.getMessage());
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception e) {
				System.out.println("error=" + e.getMessage());
			}
		}
		return null;
	}

	public static byte[] getImagebyteArr(String url, Revamped_Loading_Dialog this_dialog) {
		url=convertToWWW(url);
		Bitmap bmp = null;
		DefaultHttpClient client = new DefaultHttpClient();
		client = setProxy(client);
		HttpGet get = new HttpGet(url);
		HttpResponse getResponse = null;
		try {
			getResponse = client.execute(get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String entityContents = "";
		HttpEntity responseEntity = getResponse.getEntity();
		BufferedHttpEntity httpEntity = null;
		try {
			httpEntity = new BufferedHttpEntity(responseEntity);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream imageStream = null;
		try {
			if (this_dialog != null)
			this_dialog.changePercentage(20,100);
			imageStream = httpEntity.getContent();
			BufferedInputStream bis = new BufferedInputStream(imageStream);
			Bitmap image = BitmapFactory.decodeStream(bis);
			if (this_dialog != null)
			this_dialog.changePercentage(40,100);
			bis.close();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			if (this_dialog != null)
			this_dialog.changePercentage(80,100);
			return stream.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static String convertToWWW(String url) {
//		url=url.toLowerCase();
//		if (url.contains("www"))
//		{
//			//do nothing//return url;
//		}
//		else if (url.contains("http://"))
//		{
//			url=url.replace("http://","http://www.");
//		}
//		else if (url.contains("https://"))
//		{
//			url=url.replace("https://","https://www.");
//		}
		return url;
	}

	// public static byte[] getImagebyteArr(String url) {
	// URLConnection conn = null;
	// InputStream input = null;
	// try {
	// URL updateURL = new URL(url);
	// conn = updateURL.openConnection();
	// conn.setRequestProperty("Content-Language", "en-US");
	// conn.setConnectTimeout(50000);
	// conn.setReadTimeout(50000);
	// conn.setUseCaches(false);
	// conn.setDoInput(true);
	// conn.setDoOutput(true);
	// input = conn.getInputStream();
	// BufferedInputStream bis = new BufferedInputStream(input);
	// Bitmap image = BitmapFactory.decodeStream(bis);
	// bis.close();
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	// image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	// return stream.toByteArray();
	// } catch (Exception e) {
	// System.out.println("error=" + e.getMessage());
	// } finally {
	// try {
	// if (input != null)
	// input.close();
	// } catch (Exception e) {
	// System.out.println("error=" + e.getMessage());
	// }
	// }
	// return null;
	// }

	public static HttpEntity getEntity(List<NameValuePair> extraDataList) {
		try {
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(
					extraDataList, HTTP.UTF_8);
			return postEntity;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String postForm(String url, List<NameValuePair> extraDataList) {
		try {
			// InputStream input = postData(url, extraDataList);
			// if(input == null) return error;
			// byte[] data = new byte[1024];
			// int len = 0;
			// StringBuffer raw = new StringBuffer();
			//
			// while ( -1 != (len = input.read(data)))
			// {
			// raw.append(new String(data, 0, len, "utf-8"));
			// }
			// return raw.toString().trim();
			url=convertToWWW(url);
			if (url.contains("c_login")) {
				isLogin = true;
			} else
				isLogin = false;

			String result = postData(url, extraDataList);
//<?xml version="1.0"?>
//<login>1</login><CheckerID>122437</CheckerID>
// <Username>umar1</Username><Password>2abd14f47005a4adf11ff46b6d4516a653bfde61d58bbf4a7</Password>
// <Fullname>umar1</Fullname><CheckerCode></CheckerCode><CompanyLink>68</CompanyLink><Sex></Sex>
// <RegionLink>6141</RegionLink><CityLink>95292</CityLink><Address>Hyfgggyyy</Address><HouseNumber>
// </HouseNumber><Zipcode>Ffggg</Zipcode><Lat>33.684422</Lat><Long>73.047882</Long><ZipcodeAdditional>
// </ZipcodeAdditional>
// <Phone></Phone><Phone2></Phone2><Phone3></Phone3><Email></Email><BirthDate>2018-07-25</BirthDate><IdNumber></IdNumber><SSN></SSN><IsActive>1</IsActive><BlackListed>0</BlackListed><UseVAT>0</UseVAT><IsTelephonic>1</IsTelephonic><CanSearch>0</CanSearch><PhoneConstantBonus>0</PhoneConstantBonus><AltLangLink>0</AltLangLink><LanguageOverride>0</LanguageOverride><CanAdd>0</CanAdd><CanInitiateCrit>0</CanInitiateCrit><ConfirmAssignment>1</ConfirmAssignment><NotifyBySms>0</NotifyBySms><NotifyByEmail>1</NotifyByEmail><CanEditAvailability>0</CanEditAvailability><CanEditSelfInfo>1</CanEditSelfInfo><CanSeeSetsPreview>1</CanSeeSetsPreview><CanSeeCritHistory>1</CanSeeCritHistory><CanSeeRefundReport>1</CanSeeRefundReport><CanAddRefundRecords>0</CanAddRefundRecords><CanSeeBranchAddresses>1</CanSeeBranchAddresses><CanApplyForOrders>1</CanApplyForOrders><AllowedIpAddresses></AllowedIpAddresses><CheckerPriority>91</CheckerPriority><AvailabilityRadious>0</AvailabilityRadious><MonthlyCritLimit>10</MonthlyCritLimit><DailyCritLimit>10</DailyCritLimit><DailyRegionsLimit>1</DailyRegionsLimit><DailyCitiesLimit>3</DailyCitiesLimit><TotalCrits>0</TotalCrits><ApprovedCrits>19</ApprovedCrits><DisapprovedCrits>4</DisapprovedCrits><AutomaticCritApproval>0</AutomaticCritApproval><StartDate></StartDate><RankLink></RankLink><LastSuccessfulLogin>2020-02-25 12:27:02</LastSuccessfulLogin><LastFailedLogin>2020-01-03 13:03:24</LastFailedLogin><FailedLoginsCount>0</FailedLoginsCount><CheckerBalance>0.00</CheckerBalance><IsSelfRegistered>0</IsSelfRegistered><RegistrationDate>2018-07-10 16:55:45</RegistrationDate><PictureFilename></PictureFilename><W9Filename></W9Filename><CheckerComments></CheckerComments><ICQ></ICQ><Skype></Skype><Messenger></Messenger><TimeZone>244</TimeZone><CSSFileName></CSSFileName><CanEditRegions>0</CanEditRegions><NewCheckerCanEditRegions>0</NewCheckerCanEditRegions><VOIPChannel>100680003</VOIPChannel><VOIPPhone></VOIPPhone><LastLat>0.000000</LastLat><LastLong>0.000000</LastLong><LatLongUpdate>0000-00-00 00:00:00</LatLongUpdate><RegistrationStatus>0</RegistrationStatus><EmailValidated>0000-00-00 00:00:00</EmailValidated><AllRequiredAgreementsSigned>1</AllRequiredAgreementsSigned><CallCenter_SurveyorStatus>Offline</CallCenter_SurveyorStatus><CallCenter_SurveyorStatusLastChange>2020-02-19 12:01:03</CallCenter_SurveyorStatusLastChange>
			if (result == null)
				return error;
			return result;
		} catch (Exception e) {
			System.out.println("error=" + e.getMessage());
			return "error=" + e.getClass().getName() + " " + e.getMessage();
		}
	}

	public static boolean checkConnection(String url) {
		url=convertToWWW(url);
		BufferedReader in = null;
		String data = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();

			httpclient = setProxy((DefaultHttpClient) httpclient);
			HttpGet request = new HttpGet();
			URI website = new URI(url);
			request.setURI(website);
			HttpResponse response = httpclient.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String line = in.readLine();
			if (line != null && line.contains("imalive"))
				return true;

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static String getData(String url) {
		url=convertToWWW(url);
		BufferedReader in = null;
		String data = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();

			httpclient = setProxy((DefaultHttpClient) httpclient);
			HttpGet request = new HttpGet();
			URI website = new URI(url);
			request.setURI(website);
			HttpResponse response = httpclient.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String line = in.readLine();
			String str = "";
			while (line != null) {
				str += line;
				line = in.readLine();
			}
			return str;

		} catch (Exception e) {
			return null;
		}

	}

	private static String postData(String url, List<NameValuePair> extraDataList) {
		// if (url.contains("https")) {
		// url = url.replace("https", "http");
		// }
		url=convertToWWW(url);
		HttpParams params = new BasicHttpParams();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// httpClient.setRedirectHandler(new DefaultRedirectHandler() {
		// @Override
		// public boolean isRedirectRequested(HttpResponse response,
		// HttpContext context) {
		// boolean isRedirect = super.isRedirectRequested(response,
		// context);
		// if (!isRedirect) {
		// int responseCode = response.getStatusLine().getStatusCode();
		// if (responseCode == 301 || responseCode == 302) {
		// isRedirect=true;
		// }
		// }
		// String redirectURL = response.getFirstHeader("Location").getValue();
		// if (redirectURL!=null && !redirectURL.equals(""))
		// {
		// //postData(redirectURL, extraDataList);
		// }
		// return isRedirect;
		// }
		// });
		if (!isLogin)
			httpClient = setCookie(httpClient);
		httpClient = setProxy(httpClient);
		// Setup the HTTP POST request
		HttpPost request = new HttpPost(url);
		request.setHeader("Accept-Encoding", "gzip");
		HttpEntity ht = getEntity(extraDataList);
		request.setEntity(ht);

		HttpConnectionParams.setConnectionTimeout(params, 90000);
		HttpConnectionParams.setSoTimeout(params, 90000);
		request.setParams(params);

		HttpResponse response;
		try {
			response = httpClient.execute(request);
			Header[] headers = response.getAllHeaders();
			HttpEntity entity = response.getEntity();
			Header contentEncodingHeader = entity.getContentEncoding();

			if (contentEncodingHeader != null) {
				HeaderElement[] encodings = contentEncodingHeader.getElements();
				for (int i = 0; i < encodings.length; i++) {
					if (encodings[i].getName().equalsIgnoreCase("gzip")) {
						entity = new GzipDecompressingEntity(entity);
						break;
					}
				}
			}
			// HttpEntity he = response.getEntity();
			String xml = EntityUtils.toString(entity, HTTP.UTF_8);
			// InputStream input = he.getContent();
			getCookie(httpClient);
			return xml;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			error = "error=" + e.getClass().getName() + " " + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			error = "error=" + e.getClass().getName() + " " + e.getMessage();
		}
		return null;
	}

	public static String saveFiletoServer(boolean isTime_stamp, String uri,
			String url, String orderid, String data_id, String unix,
			String sampleSize,String pid,String lid) {
		url=convertToWWW(url);
		String str = null;
		try {
			String origanalURI = null;
			if (sampleSize != null && !sampleSize.equals("")
					&& !sampleSize.equals("0")) {
				origanalURI = new File(uri).getName();
				uri = resizeFile(uri, sampleSize);

			}

			String extension = FilenameUtils.getExtension(new File(uri)
					.getName());
			if (isTime_stamp
					&& (extension.toLowerCase().equals("png")
							|| extension.toLowerCase().equals("jpg") || extension
							.toLowerCase().equals("jpeg"))) {
				File root = CheckerApp.localFilesDir;//android.os.Environment
						//.getExternalStorageDirectory();

				String path = root.getAbsolutePath()
						+ "/mnt/sdcard/Checker_binaries/";
				File folder = new File(path);
				if (!folder.isFile() && !folder.exists()) {
					folder.mkdir();
				}
				if (folder.exists()) {
					if (origanalURI == null)
						origanalURI = new File(uri).getName();
					File dest = new File(path, "time_uploading_file.jpg");
					File src = new File(uri);

					try {
						uri = timeStampIt(src, dest);
					} catch (Exception ex) {
					}
				}

			}

			File file = new File(uri);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient = setCookie(httpClient);
			httpClient = setProxy(httpClient);
			HttpPost httppost = new HttpPost(url);

			MultipartEntity postEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			if (pid == null || pid.equals("")) {
			} else
				postEntity.addPart("ProductID", new StringBody(pid));
			if (lid == null || lid.equals("")) {
			} else
				postEntity.addPart("LocationID", new StringBody(lid));
			if (data_id == null || data_id.equals("")) {
			} else
				postEntity.addPart("DataID", new StringBody(data_id));
			postEntity.addPart("OrderID", new StringBody(orderid));
			postEntity.addPart(Constants.POST_FIELD_QUES_UNIX, new StringBody(
					unix));
			if (origanalURI != null)
				postEntity.addPart("mediaFile", new FileBody(file, origanalURI,
						"IMAGE/JPEG", null));
			else
				postEntity.addPart("mediaFile", new FileBody(file));

			httppost.setEntity(postEntity);
			HttpResponse response = httpClient.execute(httppost);

			HttpEntity he = response.getEntity();
			InputStream input = he.getContent();
			byte[] data = new byte[1024];
			int len = 0;
			StringBuffer raw = new StringBuffer();
			while (-1 != (len = input.read(data))) {
				raw.append(new String(data, 0, len, "UTF-8"));
			}
			str = raw.toString().trim();
			str = str.toString();
			// Do something with response...

		} catch (Exception e) {
			// show error
			e.printStackTrace();
		}
		return str;
	}

	// public static File setExifVar(File exifVar, String critStartLat,
	// String critStartLong) throws IOException {
	// String mString = "Generic Text..";
	// ExifInterface exif = new ExifInterface(exifVar.getAbsolutePath());
	// exif.setAttribute("UserComment", mString);
	//
	// if (Helper.critStartLat.equals("")) {
	// Helper.critStartLat = "0.0";
	// Helper.critStartLat = "0.0";
	// }
	// exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
	// String.valueOf(critStartLat));
	//
	// exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
	// String.valueOf(critStartLong));
	//
	// exif.saveAttributes();
	//
	// return exifVar;
	// }

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

		Matrix matrix = new Matrix();
		switch (orientation) {
		case ExifInterface.ORIENTATION_NORMAL:
			return bitmap;
		case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
			matrix.setScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.setRotate(180);
			break;
		case ExifInterface.ORIENTATION_FLIP_VERTICAL:
			matrix.setRotate(180);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_TRANSPOSE:
			matrix.setRotate(90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.setRotate(90);
			break;
		case ExifInterface.ORIENTATION_TRANSVERSE:
			matrix.setRotate(-90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.setRotate(-90);
			break;
		default:
			return bitmap;
		}
		try {
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public static long getFolderSize(File f) {
		long size = 0;
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				size += getFolderSize(file);
			}
		} else {
			size = f.length();
		}
		return size;
	}

	public static String copy(File src, File dest, int sampleSize) {
		try {
			long Filesize = getFolderSize(src) / 1024;// call function and
														// convert bytes into Kb
			if (Filesize < 1024 * 5)
				return src.getAbsolutePath();

			FileInputStream inputStream = new FileInputStream(src);

			Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,
					null, null);
			inputStream.close();

			ExifInterface exif = new ExifInterface(src.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);
			selectedBitmap = rotateBitmap(selectedBitmap, orientation);
			// here i override the original image file
			dest.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(dest);
			// setExifVar(dest,
			// exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE),
			// exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
			selectedBitmap.compress(Bitmap.CompressFormat.JPEG,
					((int) (100 - (sampleSize * 16))), outputStream);

			return dest.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
	}

	public static String timeStampIt(File src, File destFile) {

		ExifInterface intf = null;
		try {
			intf = new ExifInterface(src.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(src.getAbsolutePath());
		if (intf != null) {
			int orientation = intf.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);

			bitmap = rotateBitmap(bitmap, orientation);

		}
		// Bitmap src = BitmapFactory.decodeResource(); // the original file is
		// cuty.jpg i added in resources
		Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		String dateTime = "";
		if (intf != null) {
			try {
				dateTime = intf.getAttribute(ExifInterface.TAG_DATETIME);
				if (dateTime == null) {
					dateTime = new Date(src.lastModified()).toString();
					dateTime = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
							.format(new Date(dateTime)));
				}
			} catch (Exception ex) {
				// dateTime = "crashed at 533";
			}
			try {
				if (dateTime == null)
					dateTime = android.text.format.DateFormat.format(
							"dd-MM-yyyy HH:mm:ss",
							Calendar.getInstance().getTime()).toString();
			} catch (Exception ex) {
				// dateTime += "crashed at 541";
			}
			try {
				// dateTime = android.text.format.DateFormat.format(
				// "yyyy-MM-dd HH:mm:ss", new Date(dateTime)).toString();
			} catch (Exception ex) {
				// dateTime += "crashed at 547";
			}
			// Log.i("Dated : "+ dateString.toString()); //Dispaly dateString.
			// You can do/use it your own way
		}

		Canvas cs = new Canvas(dest);
		Paint tPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		if (bitmap.getWidth() > 2000)
			tPaint.setTextSize(120);
		else if (bitmap.getWidth() > 1200)
			tPaint.setTextSize(100);
		else if (bitmap.getWidth() > 700)
			tPaint.setTextSize(70);
		else if (bitmap.getWidth() > 500)
			tPaint.setTextSize(56);
		else if (bitmap.getWidth() > 400)
			tPaint.setTextSize(52);
		else if (bitmap.getWidth() > 350)
			tPaint.setTextSize(50);
		else if (bitmap.getWidth() > 300)
			tPaint.setTextSize(46);
		else if (bitmap.getWidth() > 250)
			tPaint.setTextSize(38);
		else if (bitmap.getWidth() > 210)
			tPaint.setTextSize(28);
		else if (bitmap.getWidth() > 150)
			tPaint.setTextSize(25);

		tPaint.setColor(Color.GRAY);
		tPaint.setStyle(Paint.Style.FILL);
		cs.drawBitmap(bitmap, 0f, 0f, null);
		float height = tPaint.measureText("yY");
		if (dateTime.contains(" ")) {
			String[] split = dateTime.split(" ");
			cs.drawText(split[0], 20f, 20f + height, tPaint);
			cs.drawText(split[1], 20f, height + 35f + height, tPaint);
		}
		try {
			dest.compress(Bitmap.CompressFormat.JPEG, 100,
					new FileOutputStream(destFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dest.recycle();
		return destFile.getAbsolutePath();
	}

	public static String resizeFile(String uri, String sampleSize) {

		File root = CheckerApp.localFilesDir;//android.os.Environment.getExternalStorageDirectory();
		String path = root.getAbsolutePath() + "/mnt/sdcard/Checker_binaries/";
		File dir = new File(path);
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		File dest = new File(path, "temp_uploading_file.jpg");
		File src = new File(uri);
		try {
			dest.createNewFile();
			int sample = Integer.parseInt(sampleSize);
			return copy(src, dest, sample + 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dest.getAbsolutePath();
	}

	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static boolean setCookieManager(Context context) {
		if (context != null && cookies != null) {
			final CookieSyncManager cookieSyncManager = CookieSyncManager
					.createInstance(context);
			final CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			cookieManager.removeSessionCookie();

			// Save the two cookies: auth token and session info
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					String cookieString = cookie.getName() + "="
							+ cookie.getValue() + "; Domain="
							+ cookie.getDomain();
					cookieManager
							.setCookie(Helper.getSystemURL(), cookieString);
				}
				cookieSyncManager.sync();
				return true;
			}

		}
		return false;
	}

	private static DefaultHttpClient setCookie(DefaultHttpClient httpClient) {
		if (cookies != null) {
			CookieStore store = new BasicCookieStore();
			for (int i = 0; i < cookies.size(); i++)
				store.addCookie(cookies.get(i));
			httpClient.setCookieStore(store);
		} else {

		}

		return httpClient;
	}

	private static DefaultHttpClient setProxy(DefaultHttpClient httpClient) {
		if (Constants.getProxyPort() > -1) {
			String username = Constants.getProxyUSername();
			String password = Constants.getProxyPassword();
			if (Constants.getProxyUSername() != null
					&& !Constants.getProxyUSername().equals("")
					&& Constants.getProxyPassword() != null
					&& !Constants.getProxyPassword().equals("")) {
				httpClient.getCredentialsProvider().setCredentials(
						new AuthScope(Constants.getProxyHost(),
								Constants.getProxyPort()),
						new UsernamePasswordCredentials(Constants
								.getProxyUSername(), Constants
								.getProxyPassword()));
			}
			HttpHost proxy = new HttpHost(Constants.getProxyHost(),
					Constants.getProxyPort());
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		return httpClient;
	}

	private static void getCookie(DefaultHttpClient httpClient) {
		if (cookies == null || cookies.isEmpty() || isLogin) {
			cookies = httpClient.getCookieStore().getCookies();
		}
	}

	public static void setContext(Context jobListActivity) {
		context = jobListActivity;

	}
}
