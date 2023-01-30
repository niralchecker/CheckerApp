package com.checker.sa.android.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator {

	private static Pattern pattern;
	private static Matcher matcher;

	// yyyy-MM-dd kk:mm:ss
	private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
	private static final String Time_PATTERN = "([01]?[0-9]|2[0-4]):([0-5][0-9]):([0-5][0-9])";
	private static final String Time_PATTERN_ss = "([0-5][0-9])";
	private static final String Time_PATTERN_mm_ss = "([0-5][0-9]):([0-5][0-9])";
	private static final String Time_PATTERN_hh_mm = "([01]?[0-9]|2[0-3]):([0-5][0-9])";
	// private static final String Time_PATTERN =
	// "(0?[1-9]|[12][0-9]):(0?[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9]):(0?[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9])";
	// 12:48:21
	private static final String DATE_Time_PATTERN = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
			+ Time_PATTERN;
	private static final String DATE_Time_PATTERN_WITH_SPACE = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])  "
			+ Time_PATTERN;

	public DateValidator() {

	}

	/**
	 * Validate time format with regular expression
	 * ((19|20)\d\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-4]):([0-5][0-9]):([0-5][0-9])
	 * @param time
	 *            time address for validation
	 * @return true valid date fromat, false invalid date format
	 */
	public static boolean validateDateTimeOnly(final String date) {
		pattern = Pattern.compile(DATE_Time_PATTERN);
		matcher = pattern.matcher(date);

		if (matcher.matches()) {

			return true;

		}

		if (date == null | date.equals(""))
			return true;
		else
		{
			pattern = Pattern.compile(DATE_Time_PATTERN_WITH_SPACE);
			matcher = pattern.matcher(date);

			if (matcher.matches()) {

				return true;

			}

			if (date == null | date.equals(""))
				return true;

		}
		return false;
	}

	/**
	 * Validate time format with regular expression
	 * 
	 * @param time
	 *            time address for validation
	 * @return true valid date fromat, false invalid date format
	 */
	public static boolean validateTimeOnly(final String date, String mitype) {

		if (mitype.equals("4"))//HH:MM:SS
			pattern = Pattern.compile(Time_PATTERN);
		if (mitype.equals("6"))//HH:MM
			pattern = Pattern.compile(Time_PATTERN_hh_mm);
		if (mitype.equals("7"))//MM:SS
			pattern = Pattern.compile(Time_PATTERN_mm_ss);
		if (mitype.equals("8"))//SS
			pattern = Pattern.compile(Time_PATTERN_ss);

		matcher = pattern.matcher(date);

		if (matcher.matches()) {

			return true;

		}
		if (date == null | date.equals(""))
			return true;

		return false;
	}

	/**
	 * Validate date format with regular expression
	 * 
	 * @param date
	 *            date address for validation
	 * @return true valid date fromat, false invalid date format
	 */
	public static boolean validateDateOnly(final String date) {
		pattern = Pattern.compile(DATE_PATTERN);
		matcher = pattern.matcher(date);

		if (date == null || date.equals(""))
			return true;
		if (matcher.matches()) {

			matcher.reset();

			if (matcher.find()) {

				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (day.equals("31")
						&& (month.equals("4") || month.equals("6")
								|| month.equals("9") || month.equals("11")
								|| month.equals("04") || month.equals("06") || month
									.equals("09"))) {
					return false; // only 1,3,5,7,8,10,12 has 31 days
				} else if (month.equals("2") || month.equals("02")) {
					// leap year
					if (year % 4 == 0) {
						if (day.equals("30") || day.equals("31")) {
							return false;
						} else {
							return true;
						}
					} else {
						if (day.equals("29") || day.equals("30")
								|| day.equals("31")) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}