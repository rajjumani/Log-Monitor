package com.logging;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.*;

public class LoggingSystem {
	int i = 0, j = 0, serverCount = 0;

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("CPULog");
		logger.setUseParentHandlers(false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = "2014-10-31";
		Date curDate = null;
		try {
			curDate = sdf.parse(strDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		LoggingSystem lgs = new LoggingSystem();
		System.out.println("Generating Logs for "+strDate+"...");
		lgs.witeLogs(logger, curDate, args[0]);
	}

	// writes the log to the file
	private void witeLogs(Logger logger, Date curDate, String path) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		FileHandler fh;
		try {
			fh = new FileHandler(path);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			while (getZeroTime(curDate).compareTo(getZeroTime(cal.getTime())) == 0) {
				while (serverCount < 1000) {
					String server = getServerIP();

					long dtl = cal.getTime().getTime() / 1000L;
					logger.info(dtl + "," + server + ",0" + "," + new Random().nextInt(101) + "%");
					logger.info(dtl + "," + server + ",1" + "," + new Random().nextInt(101) + "%");
				}
				cal.add(Calendar.MINUTE, 1);
				i = 0;
				j = 0;
				serverCount = 1;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Logs Generation Completed");
	}

	// generates the server IP address
	private String getServerIP() {
		String serverIP = "192" + "." + "168" + "." + j + "." + i;
		if (i < 255) {
			i++;
			serverCount++;
		} else {
			i = 0;
			j++;
		}
		return serverIP;
	}

	// fetches the date only from Date Object
	private Date getZeroTime(Date curDate) {
		Date res = curDate;
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(curDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		res = calendar.getTime();

		return res;
	}
}
