package com.rhymthwave.Utilities;

import java.util.Calendar;
import java.util.Date;

public class GetCurrentTime {

	public static Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}
}
