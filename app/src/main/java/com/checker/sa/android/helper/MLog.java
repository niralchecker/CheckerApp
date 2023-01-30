/**
 *M.Log.java
 *Purpose : to write the log cat into a text file
 *
 */


package com.checker.sa.android.helper;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import android.content.Context;
import android.util.Log;

/**
 * A Wrapper class of android.util.Log
 *
 */
public class MLog {

	/**
     * Log Level- VERBOSE; use Log.v
     */
    public static final int VERBOSE = 0xA1;

    /**
     * Log Level- DEBUG; use Log.d.
     */
    public static final int DEBUG = 0xA2;

    /**
     * Log Level- INFO; use Log.i.
     */
    public static final int INFO = 0xA3;

    /**
     * Log Level- WARN; use Log.w.
     */
    public static final int WARN = 0xA4;

    /**
     * Log Level- ERROR; use Log.e.
     */
    public static final int ERROR = 0xA5;

    
	private static String TAG = "MLog";
	
	/**
	 * Enable or Disable Logging
	 */
	public static boolean enableLog = true;
	
	/**
	 * All logs >= logLevel will be printed
	 */
	public static int logLevel = DEBUG;
	
	private static PrintStream stream;
	
	/**
	 * This should be called prior to using logging methods
	 * 
	 * @param context
	 * @param logfile
	 */
	public static void init(Context context, String logfile){
		if(!enableLog || stream != null)
			return;
		try{
			FileOutputStream out = context.openFileOutput(logfile, 
					Context.MODE_WORLD_READABLE);
			stream = new PrintStream(out);
		}catch(Exception ex){
			Log.e(TAG, "Error in creating Log File: "+ ex.getMessage());
		}finally{
			if(stream != null)
				println("New Log file created...");
		}
	}

	public static void i(String tag, String msg){
		if(!enableLog || logLevel > INFO)
			return;
		Log.i(tag, msg);
		println(tag +" "+ msg);
	}
	
	public static void i(String tag, String msg, Throwable  tr){
		if(!enableLog || logLevel > INFO)
			return;
		Log.i(tag, msg, tr);
		println(tag +" "+ msg + "\n"+ Log.getStackTraceString(tr));
	}
	
	public static void v(String tag, String msg){
//		if(!enableLog || logLevel > VERBOSE)
//			return;
		Log.v(tag, msg);
		println(tag +" "+ msg);
	}
	
	public static void v(String tag, String msg, Throwable  tr){
		if(!enableLog || logLevel > VERBOSE)
			return;
		Log.v(tag, msg, tr);
		println(tag +" "+ msg + "\n"+ Log.getStackTraceString(tr));
	}
	
	public static void d(String tag, String msg){
		if(!enableLog || logLevel > DEBUG)
			return;
		Log.d(tag, msg);
		println("\n"+" "+tag +" "+ msg);
		println("\n");
	}
	
	public static void d(String tag, String msg, Throwable  tr){
		if(!enableLog || logLevel > DEBUG)
			return;
		Log.d(tag, msg, tr);
		println(tag +" "+ msg + "\n"+ Log.getStackTraceString(tr));
	}
	
	public static void w(String tag, String msg){
		if(!enableLog || logLevel > WARN)
			return;
		Log.w(tag, msg);
		println(tag +" "+ msg);
	}
	
	public static void w(String tag, String msg, Throwable  tr){
		if(!enableLog || logLevel > WARN)
			return;
		Log.w(tag, msg, tr);
		println(tag +" "+ msg + "\n"+ Log.getStackTraceString(tr));
	}
	
	public static void e(String tag, String msg){
		if(!enableLog || logLevel > ERROR)
			return;
		Log.e(tag, msg);
		println(tag +" "+ msg);
	}
	
	public static void e(String tag, String msg, Throwable  tr){
		if(!enableLog || logLevel > ERROR)
			return;
		Log.e(tag, msg, tr);
		println(tag +" "+ msg + "\n"+ Log.getStackTraceString(tr));
	}
	
	/**
	 * Write the string into System Output Stream
	 * @param str
	 */
	private static void println(String str){
		if(stream != null){
			stream.println(new Date().toString() +" "+ str);
			stream.flush();
		}
	}
	
	/**
	 * Close the stream
	 */
	public static void close(){
		stream.close();
		stream = null;
	}
}
