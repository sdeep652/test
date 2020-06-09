package com.tcts.foresight.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	
	public static  Calendar stringToCal(String dateStr, SimpleDateFormat sdf)
	{
		
		Logger logger = LoggerFactory.getLogger(DateUtil.class);
		SimpleDateFormat sdf1 = new SimpleDateFormat(Constant.DATE_FORMAT);
		try {
			Calendar cal = Calendar.getInstance();
			Date date = sdf1.parse(dateStr);
			cal.setTime(date);
			return cal;
		}catch (Exception e) {
			logger.error("Exception occured in DateUtil: " + e.getMessage(), e);  
			return null;
		}
	}
	
	public static String currentDateInString()
	{
		SimpleDateFormat sdf1 = new SimpleDateFormat(Constant.DATE_FORMAT);
		
		return sdf1.format(Calendar.getInstance().getTime());
	}
	
	public static Long dateDifference(String date1, String date2) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		Logger logger = LoggerFactory.getLogger(DateUtil.class);
		long diff = 0L;
		try {
		if(StringUtil.isNotNullNotEmpty(date1) && StringUtil.isNotNullNotEmpty(date2)) {
		Calendar firstDate = DateUtil.stringToCal(date1, sdf);
		Calendar secondDate = DateUtil.stringToCal(date2, sdf);
		diff = secondDate.getTimeInMillis() - firstDate.getTimeInMillis(); 
		}
		}
		catch (Exception e) {
			logger.error("Exception occured in dateSubtraction: " + e.getMessage(), e);
		}
		return diff;
	}
	
	public static int getPercentagePassed(Date start, Date end) {

		int returnPercentagePassed = 0;
		long now = new Date().getTime();
		long s = start.getTime();
		long e = end.getTime();
		if (s >= e || now >= e) {
			return 100;
		} else if (now <= s) {
			return 0;
		} else {
			long timeRemainigInPercentage = (long) ((e - now) * 100) / (e - s);
			// System.out.println("timeRemainigInPercentage " + timeRemainigInPercentage);

			returnPercentagePassed = 100 - (int) timeRemainigInPercentage;
			// System.out.println("returnPercentagePassed" + returnPercentagePassed);
			return returnPercentagePassed;
		}

	}
}
