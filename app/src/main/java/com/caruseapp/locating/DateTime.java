package com.caruseapp.locating;

import java.util.Date;
import java.lang.String;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class DateTime {

	private static String formatString="yyyy-MM-dd-HH-mm-ss-SSS";

	//

	public static String getFormatString() {
		return formatString;
	}

	public static void setFormatString(String formatString) {
		DateTime.formatString = formatString;
	}

	//

	public static long getTime() {
		Date date = new Date();
		long dt = date.getTime();
		return dt;
	}

	public static String getClock() {
		String dt = get();
		dt=dt.substring(11,22);
		return dt;
	}

	public static String getDate() {
		Date date = new Date();
		String dt = get();
		dt=dt.substring(0,10);
		return dt;
	}

	public static String get() {
		Date date = new Date();
		long dt = date.getTime();
		SimpleDateFormat format0 = new SimpleDateFormat(formatString);
		String newdt = format0.format(dt);
		return newdt;
	}

	public static String format(long datetime) {
		SimpleDateFormat format0 = new SimpleDateFormat(formatString);
		return format0.format(datetime);
	}

	public static String format(double datetime) {
		SimpleDateFormat format0 = new SimpleDateFormat(formatString);
		return format0.format(datetime);
	}
	
	public static long transform(String str) {
		long time = (new SimpleDateFormat(formatString)).parse(
				str, new ParsePosition(0)).getTime();
		return time;
	}

	public static String calTime(String lastgtime, String laststime, String nowstime) {
		SimpleDateFormat format0 = new SimpleDateFormat(formatString);
		String time=format(transform(lastgtime)+transform(nowstime)-transform(laststime));
		return time;
	}

	public static double getYear(int year,int month,int day){
		int days_year = 365;
		int num = 0;
		for(int i =1;i<month;i++)

			switch(i){
			case 1: case 3: case 5: case 7: case 8: case 10: case 12: num+=31; break;
			case 4: case 6: case 9: case 11: num+=30; break;
			case 2:
				if((year % 4 ==0 && year % 100 != 0 ) || (year % 400 ==0)){
					num+= 29;
					days_year +=1;
				}else{
					num += 28;
				}
				break;
		}
		return year+(num+day)/(double)days_year;
	}

}
