package com.johe.api.pump.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.johe.api.pump.util.AppConstants.TimePattern;

public class DateTimeUtil {

	public static String getServerTime(TimePattern pattern) {
		if(pattern == null) {
			return String.valueOf(System.currentTimeMillis());
		}
		String strTime;
		switch(pattern) {
		case ONE:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_ONE).format(Calendar.getInstance().getTime());
			break;
		case TWO:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_TWO).format(Calendar.getInstance().getTime());
			break;
		case THREE:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_THREE).format(Calendar.getInstance().getTime());
			break;
		case FOUR:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_FOUR).format(Calendar.getInstance().getTime());
			break;
		case FIVE:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_FIVE).format(Calendar.getInstance().getTime());
			break;
		case SIX:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_SIX).format(Calendar.getInstance().getTime());
			break;
		case SEVEN:
			strTime = new SimpleDateFormat(AppConstants.PATTERN_SEVEN).format(Calendar.getInstance().getTime());
			break;
		default:
			strTime = String.valueOf(System.currentTimeMillis());
			break;
		}
		return strTime;
	}
}
