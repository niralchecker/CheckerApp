package com.checker.sa.android.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.CheckerApp;

public class DBAdapter extends SQLiteOpenHelper {

	/** The Android's default system path of your application database. */
	private static String DB_PATH = "/data/data/com.mor.sa.android.activity/databases/";

	private final static String DB_NAME_FAKE = "mor_survey_app_android_db_fake.sqlite";
	public final static String DB_NAME = "mor_survey_app_android_db.sqlite";
	public static SQLiteDatabase db;
	private static Context myContext;
	private static int next_version = 37;
	private static int current_version;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, next_version);
		DBAdapter.myContext = context;
		DB_PATH = "/data/data/"
				+ myContext.getApplicationContext().getPackageName()
				+ "/databases/";

		// DB_PATH = Environment.getExternalStorageDirectory() + "";
	}

	public void deleteDB(String DB_NAME) {

		myContext.deleteDatabase(DB_NAME);
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public String createDataBase(String username, String link,
			String isOnlyForCopyPath) throws IOException {
		String path = null;

		link = link.replace("https", "http");
		link = link.replace("/", "_");
		link = link.replace(".", "_");
		link = link.replace(":", "_");
		String myPath = DB_PATH + username + link + DB_NAME;
		com.checker.sa.android.helper.Helper.setDBPath(username + link
				+ DB_NAME);
		boolean dbExist = checkDataBase(false);
		// boolean oldDbExist = checkDataBase(true);
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase(false);
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		} else if (isOnlyForCopyPath != null) {
			path = makeCopyofCurrentDB(isOnlyForCopyPath);
		}
		return path;
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @param link
	 * @param username
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(boolean isOld) {
		SQLiteDatabase checkDB = null;
		// File f = new File(DB_PATH + DB_NAME);
		// // File dbFile = getDatabasePath(DB_PATH + DB_NAME);
		// String ff = f.getAbsolutePath();

		try {
			if (isOld) {
				checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
						SQLiteDatabase.OPEN_READWRITE);

				current_version = checkDB.getVersion();
				if (next_version > current_version) {
					updateDatabase(checkDB, current_version, next_version);
				}
			} else {
				checkDB = SQLiteDatabase.openDatabase(DB_PATH
						+ com.checker.sa.android.helper.Helper.getDBPath(),
						null, SQLiteDatabase.OPEN_READWRITE);
			}

			String path = checkDB.getPath();
			path += "";

		} catch (SQLiteException e) {
			// database does't exist yet.
			System.out.print("SQLiteException   " + e.toString());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	public String makeCopyofCurrentDB(String outFileName) {
		//outFileName += "/checkerbackupdbs";
		File outputDir = new File(outFileName);
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}
		File dir = new File(DB_PATH);
		try {
			if (dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					try {
						String inFileName = DB_PATH
								+ com.checker.sa.android.helper.Helper
										.getDBPath();
						InputStream myInput = null;

						// outFileName = outFileName + "/" + children[i];
						// Open the empty db as the output stream
						OutputStream myOutput = new FileOutputStream(
								outFileName + "/" + children[i]);
						// transfer bytes from the inputfile to the outputfile
						myInput = new FileInputStream(inFileName);
						byte[] buffer = new byte[1024];
						int length;
						while ((length = myInput.read(buffer)) > 0) {
							myOutput.write(buffer, 0, length);
						}
						// Close the streams

						myOutput.flush();
						myOutput.close();
						myInput.close();
					} catch (IOException ex) {

					}
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return outFileName;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase(boolean isoldexist) throws IOException {
		InputStream myInput = null;
		String inFileName = null;

		if (isoldexist) {

			inFileName = DB_PATH + DB_NAME;
			String myPath = DB_PATH
					+ com.checker.sa.android.helper.Helper.getDBPath();
			renameFile(inFileName, myPath);

			return;
		} else {
			// Open your local db as the input stream
			myInput = myContext.getAssets().open(DB_NAME);
			//

			// File file = new File(Environment.getExternalStorageDirectory() +
			// "/" + DB_NAME);
			// myInput = new FileInputStream(file);
		}
		// String inFileName = DB_PATH
		// + com.checker.sa.android.helper.Helper.getDBPath();
		// InputStream myInput = null;
		// myInput = new FileInputStream(inFileName);

		// Path to the just created empty db
		// String myPath = DB_PATH +username+link.replace("/", "_")+ DB_NAME;
		String outFileName = DB_PATH
				+ com.checker.sa.android.helper.Helper.getDBPath();
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams

		myOutput.flush();
		myOutput.close();
		myInput.close();
		if (inFileName != null) {
			// deleteAllSavedJobs();
		}

	}

	private void renameFile(String inFileName, String myPath) {
		String currentFileName = inFileName;
		// Log.i("Current file name", currentFileName);

		File from = new File(inFileName);
		File to = new File(myPath);
		from.renameTo(to);
	}

	private void deleteAllSavedJobs() {
		String myPath = DB_PATH + DB_NAME;
		SQLiteDatabase thisdb = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		String where = "StatusName=" + "\"Scheduled\"" + " OR StatusName="
				+ "\"Assigned\"" + " OR StatusName=" + "\"survey\""
				+ " OR StatusName=" + "\"Completed\"" + " OR StatusName="
				+ "\"In progress\"";
		if (thisdb != null)
			thisdb.delete(Constants.DB_TABLE_SUBMITSURVEY, where, null);
		thisdb.close();
	}

	public synchronized static SQLiteDatabase openDataBase() {
		// try {
		// Open the database
		String myPath = DB_PATH
				+ com.checker.sa.android.helper.Helper.getDBPath();

		// myPath = Environment.getExternalStorageDirectory() + "/" +
		// DB_NAME;
		if (db != null && db.isOpen()) {
			return db;
		}
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		if (!db.isOpen()) {
			// makeDbCopy();
			// Alert user that unable to open databse for XYZ reason
			// Close the app after that
			progressHandler1.sendEmptyMessage(0);
		}
		current_version = db.getVersion();
		if (next_version > current_version) {
			updateDatabase(db, current_version, next_version);

		}
		return db;
		// } catch (Exception e) {
		// System.out.println("Data Base Open Exception:  " + e.getMessage());
		// // msgg = e.getMessage();
		// // progressHandler.sendEmptyMessage(0);
		// }
		// return null;
	}

	public synchronized static SQLiteDatabase openDataBase(boolean permanent) {
		// try {
		// Open the database
		String myPath = DB_PATH
				+ com.checker.sa.android.helper.Helper.getDBPath();
		// myPath = Environment.getExternalStorageDirectory() + "/" +
		// DB_NAME;

		if (db != null && isTransaction)
			return db;
		if (db != null && db.isOpen() && !permanent) {
			return db;
		}
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		if (!db.isOpen()) {
			// makeDbCopy();
			// Alert user that unable to open databse for XYZ reason
			// Close the app after that
			progressHandler1.sendEmptyMessage(0);
		}
		current_version = db.getVersion();
		if (next_version > current_version) {
			updateDatabase(db, current_version, next_version);

		}
		return db;
		// } catch (Exception e) {
		// System.out.println("Data Base Open Exception:  " + e.getMessage());
		// // msgg = e.getMessage();
		// // progressHandler.sendEmptyMessage(0);
		// }
		// return null;
	}

	private static void makeDbCopy() {
		// TODO Auto-generated method stub

	}

	public static BufferedWriter out;

	public static Boolean LogCommunication(String fileName, String message) {

		Time now = new Time();
		now.setToNow();
		message = now.toString() + "," + message;
		File root = android.os.Environment.getExternalStorageDirectory();

		File dir = new File(
				CheckerApp.localFilesDir//android.os.Environment.getExternalStorageDirectory()
						+ "/chkrLogs/");
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		File file = new File(dir, fileName);
		FileOutputStream fos;
		if (!file.exists()) {
			try {
				String msg = message;
				file.createNewFile();
				fos = new FileOutputStream(file);
				fos.write(msg.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			fos = new FileOutputStream(file, true);
			fos.write((message + "\r\n").getBytes());
			fos.flush();
			fos.close();

			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	private static void updateDatabase(SQLiteDatabase db, int current_version2,
			int next_version2) {
		if (current_version2 == 0) {
			// update db to version 1 here
			current_version2 = 1;
		}
		if (current_version2 == 1) {
			try {
				// update db to version 2 here
				String altertable1 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"BranchInputMandatory\" VARCHAR";
				String altertable2 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"WorkerInputMandatory\" VARCHAR";
				db.execSQL(altertable1);
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 2;
		}
		if (current_version2 == 2) {
			// update db to version 3 here
			try {
				String altertable2 = "CREATE TABLE \"PngFiles\" (\"OrderID\" VARCHAR, \"MediaFile\" VARCHAR, \"DataID\" VARCHAR, \"SetID\" VARCHAR)";
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 3;
		}
		if (current_version2 == 3) {
			// update db to version 3 here
			// <xml_Key>value16280</xml_Key>
			// <PropID>1525</PropID>
			// <ValueID>16280</ValueID>
			// <PropertyName>All Branches</PropertyName>
			// <Content>J&amp;K</Content>
			try {
				String altertable2 = "CREATE TABLE \"BranchProps\" (\"Content\" VARCHAR, \"PropID\" VARCHAR, \"ValueID\" VARCHAR, \"PropertyName\" VARCHAR)";
				db.execSQL(altertable2);

			} catch (Exception ex) {
				int i = 0;
				i++;
			}

			try {
				String altertable1 = "ALTER TABLE \"BranchProps\" ADD COLUMN \"BranchID\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
				int i = 0;
				i++;
			}

			current_version2 = 4;
		}

		if (current_version2 == 4) {
			// update db to version 3 here
			// <xml_Key>value16280</xml_Key>
			// <PropID>1525</PropID>
			// <ValueID>16280</ValueID>
			// <PropertyName>All Branches</PropertyName>
			// <Content>J&amp;K</Content>
			try {

				String altertable1 = "ALTER TABLE \"main\".\"BranchProps\" ADD COLUMN \"BranchID\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 5;
		}

		if (current_version2 == 5) {
			// update db to version 3 here
			// <xml_Key>value16280</xml_Key>
			// <PropID>1525</PropID>
			// <ValueID>16280</ValueID>
			// <PropertyName>All Branches</PropertyName>
			// <Content>J&amp;K</Content>
			try {
				String altertable1 = "ALTER TABLE \"main\".\"Branches\" ADD COLUMN \"BranchLat\" VARCHAR";
				db.execSQL(altertable1);
				String altertable2 = "ALTER TABLE \"main\".\"Branches\" ADD COLUMN \"BranchLong\" VARCHAR";
				db.execSQL(altertable2);

			} catch (Exception ex) {
			}
			current_version2 = 6;
		}

		if (current_version2 == 6) {
			// update db to version 3 here
			// <xml_Key>value16280</xml_Key>
			// <PropID>1525</PropID>
			// <ValueID>16280</ValueID>
			// <PropertyName>All Branches</PropertyName>
			// <Content>J&amp;K</Content>
			try {
				String altertable2 = "CREATE TABLE \"tblLoops\" (\"SetId\" VARCHAR, \"ColumnNumber\" INTEGER, \"ColumnName\" VARCHAR, \"ColumnData\" VARCHAR, \"LastColumnData\" VARCHAR, \"ListName\" VARCHAR)";
				db.execSQL(altertable2);

			} catch (Exception ex) {
			}
			current_version2 = 7;
		}

		if (current_version2 == 7) {
			// update db to version 3 here
			// <xml_Key>value16280</xml_Key>
			// <PropID>1525</PropID>
			// <ValueID>16280</ValueID>
			// <PropertyName>All Branches</PropertyName>
			// <Content>J&amp;K</Content>
			try {
				String altertable1 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"AnswersSource\" VARCHAR";
				db.execSQL(altertable1);
				String altertable2 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"AnswersCondition\" VARCHAR";
				db.execSQL(altertable2);
				String altertable3 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"AnswersFormat\" VARCHAR";
				db.execSQL(altertable3);

			} catch (Exception ex) {
			}
			current_version2 = 8;
		}

		if (current_version2 == 8) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\"JobList\" ADD COLUMN \"sPurchaseLimit\" VARCHAR";
				db.execSQL(altertable1);
				String altertable2 = "ALTER TABLE \"main\".\"JobList\" ADD COLUMN \"sNonRefundableServicePayment\" VARCHAR";
				db.execSQL(altertable2);
				String altertable3 = "ALTER TABLE \"main\".\"JobList\" ADD COLUMN \"sTransportationPayment\" VARCHAR";
				db.execSQL(altertable3);
				String altertable4 = "ALTER TABLE \"main\".\"JobList\" ADD COLUMN \"sCriticismPayment\" VARCHAR";
				db.execSQL(altertable4);
				String altertable5 = "ALTER TABLE \"main\".\"JobList\" ADD COLUMN \"sBonusPayment\" VARCHAR";
				db.execSQL(altertable5);

			} catch (Exception ex) {
			}
			current_version2 = 9;
		}

		if (current_version2 == 9) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"LoopName\" VARCHAR";
				db.execSQL(altertable1);
				String altertable2 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"LoopSource\" VARCHAR";
				db.execSQL(altertable2);
				String altertable3 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"RandomizeLoop\" VARCHAR";
				db.execSQL(altertable3);
				String altertable4 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"LoopCondition\" VARCHAR";
				db.execSQL(altertable4);
				String altertable5 = "ALTER TABLE \"main\".\"Questionnaire\" ADD COLUMN \"LoopFormat\" VARCHAR";
				db.execSQL(altertable5);

			} catch (Exception ex) {
			}
			current_version2 = 10;
		}

		if (current_version2 == 10) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\"tblLoops\" ADD COLUMN \"ListRowIndex\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 11;
		}

		if (current_version2 == 11) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\"tblLoops\" ADD COLUMN \"ListID\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 12;
		}

		if (current_version2 == 12) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\"Questions\" ADD COLUMN \"LoopInfo\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 13;
		}

		if (current_version2 == 13) {

			try {

				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_DefaultBonusPayment
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AskForServiceDetails
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_DefaultPaymentForChecker
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AskForPurchaseDetails
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AskForTransportationDetails
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AutoApproveTransportation
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AutoApprovePurchase
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AutoApprovePayment
						+ "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AutoApproveService
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 14;
		}

		if (current_version2 == 14) {

			try {

				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_purchase_details
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_purchase_payment
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_purchase_description
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY
						+ "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_service_invoice_number
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_service_payment
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_service_description
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY
						+ "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_transportation_payment
						+ "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY
						+ "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_transportation_description
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 15;
		}

		if (current_version2 == 15) {

			try {

				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.UPLOAD_FILE_TABLE + "\" ADD COLUMN \""
						+ Constants.UPLOAD_FILe_DATE + "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.UPLOAD_FILE_TABLE + "\" ADD COLUMN \""
						+ Constants.UPLOAD_FILe_BRANCH_NAME + "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.UPLOAD_FILE_TABLE + "\" ADD COLUMN \""
						+ Constants.UPLOAD_FILe_CLIENT_NAME + "\" VARCHAR";
				db.execSQL(altertable1);

				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.UPLOAD_FILE_TABLE + "\" ADD COLUMN \""
						+ Constants.UPLOAD_FILe_SET_NAME + "\" VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
				int i = 0;
				i++;
			}
			current_version2 = 16;
		}

		if (current_version2 == 16) {

			try {

				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SURVEY_Branch_NAME + "\" VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
				int i = 0;
				i++;
			}
			current_version2 = 17;
		}

		if (current_version2 == 17) {

			try {

				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_ANSWERS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_ANSWERS_RANK + "\" VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
				int i = 0;
				i++;
			}
			current_version2 = 18;
		}

		if (current_version2 == 18) {

			try {

				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_QA + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_QA_ClearOtherAnswers
						+ "\" VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
				int i = 0;
				i++;
			}
			current_version2 = 19;
		}

		if (current_version2 == 19) {
			// update db to version 3 here
			try {
				String altertable2 = "CREATE TABLE \"CustomOrderFields\" (\"OrderID\" VARCHAR, \"customfield_name\" VARCHAR, \"customfield_value\" VARCHAR)";
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 20;
		}

		if (current_version2 == 20) {
			// update db to version 3 here
			try {
				String altertable2 = "CREATE TABLE \"CustomOrderFieldsAnswers\" (\"OrderID\" VARCHAR, \"customfield_name\" VARCHAR, \"customfield_value\" VARCHAR, \"customfield_text\" VARCHAR)";
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 21;
		}

		if (current_version2 == 21) {
			// update db to version 3 here
			// <lang_list ID="28" type="array">
			// <AltLangID>28</AltLangID>
			// <AltLangName>Espa�ol</AltLangName>
			// <CompanyLink>68</CompanyLink>
			// <AltLangDirection>1</AltLangDirection>
			// <IsActive>1</IsActive>
			// <InterfaceLanguage>19</InterfaceLanguage>
			// </lang_list>
			try {
				String altertable2 = "CREATE TABLE \"tbl_language\" (\"AltLangID\" VARCHAR, \"CompanyLink\" VARCHAR, \"AltLangName\" VARCHAR, \"AltLangDirection\" VARCHAR, \"IsActive\" VARCHAR, \"InterfaceLanguage\" VARCHAR, \"IsSelected\" VARCHAR)";
				db.execSQL(altertable2);

				altertable2 = "CREATE TABLE \"tbl_alt_questions\" (\"AltLangID\" VARCHAR,\"SetID\" VARCHAR, \"DataID\" VARCHAR, \"Question\" VARCHAR, \"QuestionDescription\" VARCHAR, \"MiDescription\" VARCHAR,\"QText\" VARCHAR)";
				db.execSQL(altertable2);

				altertable2 = "CREATE TABLE \"tbl_alt_answers\" (\"AltLangID\" VARCHAR,\"SetID\" VARCHAR, \"DataID\" VARCHAR, \"AnswerID\" VARCHAR, \"Answer\" VARCHAR)";
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 22;
		}

		if (current_version2 == 22) {
			// AllowCheckerToSetLang
			// isDifferentLangsAvailable

			try {
				String altertable1 = "ALTER TABLE \"main\".\"Sets\" ADD COLUMN \"AllowCheckerToSetLang\" VARCHAR";
				String altertable2 = "ALTER TABLE \"main\".\"Sets\" ADD COLUMN \"isDifferentLangsAvailable\" VARCHAR";
				db.execSQL(altertable1);
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 23;
		}

		if (current_version2 == 23) {
			// AllowCheckerToSetLang
			// isDifferentLangsAvailable

			try {
				Helper helper = new Helper();
				helper.deleteSetsInFolder();
				String altertable2 = "CREATE TABLE \"tbl_alt_titles\" (\"AltLangID\" VARCHAR,\"SetID\" VARCHAR, \"DataID\" VARCHAR, \"qgtid\" VARCHAR, \"Title\" VARCHAR)";
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 24;
		}

		if (current_version2 == 24) {
			// GroupName in alternate language

			try {
				Helper helper = new Helper();
				helper.deleteSetsInFolder();

				String altertable1 = "ALTER TABLE \"main\".\"tbl_alt_questions\" ADD COLUMN \"groupName\" VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
			}
			current_version2 = 25;
		}

		if (current_version2 == 25) {
			// GroupName in alternate language

			try {

				String altertable1 = "ALTER TABLE \"main\".\"UploadFile\" ADD COLUMN \"samplesize\" VARCHAR";
				db.execSQL(altertable1);
			} catch (Exception ex) {
			}
			current_version2 = 26;
		}
		if (current_version2 == 26) {
			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SETS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SET_AutoApproveService
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 27;
		}
		if (current_version2 == 27) {
			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_JOBLIST + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_JOBLIST_AllowShopperToReject
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 28;
		}
		if (current_version2 == 28) {
			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_JOBLIST + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_JOBLIST_sinprogressonserver
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 29;
		}
		if (current_version2 == 29) {
			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_JOBLIST + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_JOBLIST_sRegionName + "\" VARCHAR";
				db.execSQL(altertable1);
				altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_JOBLIST + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_JOBLIST_sProjectName
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 30;
		}
		if (current_version2 == 30) {
			// public static final String DB_TABLE_ANSWERS = "Answers";
			// public static final String DB_TABLE_ANSWERS_ENCRYPTED =
			// "encrypted";
			// we will do encryption if

			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_ANSWERS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_ANSWERS_ENCRYPTED + "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
				int i = 0;
				i++;
			}
			current_version2 = 31;
		}
		if (current_version2 == 31) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_JOBLIST + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_JOBLIST_sdeletedjob + "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
				int i = 0;
				i++;
			}
			current_version2 = 32;
		}
		if (current_version2 == 32) {
			try {
				Helper helper = new Helper();
				helper.deleteSetsInFolder();
				String altertable2 = "CREATE TABLE \"tbl_alt_titles\" (\"AltLangID\" VARCHAR,\"SetID\" VARCHAR, \"DataID\" VARCHAR, \"qgtid\" VARCHAR, \"Title\" VARCHAR)";
				db.execSQL(altertable2);
			} catch (Exception ex) {
			}
			current_version2 = 33;
		}
		if (current_version2 == 33) {
			try {
				String altertable1 =  "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_JOBLIST + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_JOBLIST_sProjectID
						+ "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}
			current_version2 = 34;
		}
        if (current_version2 == 34) {

            try {
                String altertable1 = "ALTER TABLE \"main\".\""
                        + Constants.UPLOAD_FILE_TABLE + "\" ADD COLUMN \""
                        + Constants.UPLOAD_FILe_PRODUCTID + "\" VARCHAR";
                db.execSQL(altertable1);
                altertable1 = "ALTER TABLE \"main\".\""
                        + Constants.UPLOAD_FILE_TABLE + "\" ADD COLUMN \""
                        + Constants.UPLOAD_FILe_LOCATIONID
                        + "\" VARCHAR";
                db.execSQL(altertable1);

            } catch (Exception ex) {
            }

            current_version2 = 35;
        }
		if (current_version2 == 35) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_ORDERS + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_ORDERS_LASTDATAID + "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}

			current_version2 = 36;
		}
		if (current_version2 == 36) {

			try {
				String altertable1 = "ALTER TABLE \"main\".\""
						+ Constants.DB_TABLE_SUBMITSURVEY + "\" ADD COLUMN \""
						+ Constants.DB_TABLE_SUBMITSURVEY_RS + "\" VARCHAR";
				db.execSQL(altertable1);

			} catch (Exception ex) {
			}

			current_version2 = 37;
		}
		db.setVersion(next_version2);
	}

	public synchronized static void closeDataBase(String string)
			throws SQLException {
		try {

			isTransaction = false;
			db.endTransaction();
			db.setTransactionSuccessful();
			if (db != null && db.isOpen())
				db.close();

		} catch (Exception e) {
			System.out.println("no database connected to close");
		}
	}

	public synchronized static void closeDataBase() throws SQLException {
		try {
			if (isTransaction)
				return;
			if (db != null && db.isOpen())
				db.close();
		} catch (Exception e) {
			System.out.println("no database connected to close");
		}
	}

	@Override
	public synchronized void close() {
		// if(db != null && db.isOpen())
		// db.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int i = oldVersion;
		int j = newVersion;
		i++;
		j++;
	}

	static String msgg = "";
	static Handler progressHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			AlertDialog.Builder alert = new AlertDialog.Builder(myContext);
			alert.setMessage("Database not open");
			alert.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog1,
								int whichButton) {
							System.exit(1);
							return;
						}
					}).create().show();
			return;
		}
	};

	private static boolean isTransaction;

	public static SQLiteDatabase openDataBase(String string) {
		try {
			// Open the database
			String myPath = DB_PATH
					+ com.checker.sa.android.helper.Helper.getDBPath();

			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

			if (!db.isOpen()) {
				// makeDbCopy();
				// Alert user that unable to open databse for XYZ reason
				// Close the app after that
				progressHandler1.sendEmptyMessage(0);
			}
			current_version = db.getVersion();
			if (next_version > current_version) {
				updateDatabase(db, current_version, next_version);

			}
			isTransaction = true;
			db.execSQL("PRAGMA foreign_keys=ON;");
			db.execSQL("PRAGMA synchronous=OFF;");
			db.execSQL("PRAGMA journal_mode=MEMORY;");
			db.execSQL("PRAGMA default_cache_size=10000;");
			db.execSQL("PRAGMA locking_mode=EXCLUSIVE;");
			db.beginTransaction();
			return db;
		} catch (Exception e) {
			System.out.println("Data Base Open Exception:  " + e.getMessage());
			// msgg = e.getMessage();
			// progressHandler.sendEmptyMessage(0);
		}
		return null;

	}

	public static String encrypt(String strClearText, String strKey) {
		String strData = "";

		try {
			strKey = "Bar12345Bar12345";
			// Create key and cipher
			Key aesKey = new SecretKeySpec(strKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(strClearText.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : encrypted) {
				sb.append((char) b);
			}
			// the encrypted String
			strData = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			// throw new Exception(e);
		}
		return strData;
	}

	public static String decrypt(String strEncrypted, String strKey) {
		String strData = "";

		try {
			Cipher cipher = Cipher.getInstance("AES");
			strKey = "Bar12345Bar12345";
			byte[] bb = new byte[strEncrypted.length()];
			for (int i = 0; i < strEncrypted.length(); i++) {
				bb[i] = (byte) strEncrypted.charAt(i);
			}
			Key aesKey = new SecretKeySpec(strKey.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			strData = new String(cipher.doFinal(bb));
			// System.err.println(decrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

}
