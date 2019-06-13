package com.loggingQuery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LoggingQuery {

	static HashMap<RecordKey, List<RecordValue>> map = new HashMap<>();
	static boolean init = false;
	public static void main(String[] args) {
		
		LoggingQuery lq = new LoggingQuery();
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("Enter the query or type exit to close the program:");
			String scan = sc.nextLine();
			String[] qury = scan.split("\\s+");
			if(qury[0].toLowerCase().equals("exit") )
				break;
			else if(qury[0].toLowerCase().equals("query")) {
				if(!init) {
					File file = new File(args[0]);
					if(file.exists())
						lq.processData(args[0]);
					else
						System.out.println("Invalid Path");
				}
				lq.findRecord(scan);
			}
			else
				System.out.println("Invalid Command");
		}
		sc.close();
	}
	
	// finds the record in the query given by user
	private void findRecord(String scan) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] qury = scan.split("\\s+");
		
		String serve = qury[1];
		int cp = Integer.parseInt(qury[2]);
		
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = sdf.parse(qury[3] + " " + qury[4]);
			endDate = sdf.parse(qury[5] + " " + qury[6]);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		RecordKey rke = new RecordKey(serve, cp);
		List<RecordValue> rv = map.get(rke);
		Comparator<RecordValue> compareByTime = (RecordValue r1, RecordValue r2) -> r1.getLogTime().compareTo(r2.getLogTime());
		Collections.sort(rv, compareByTime);
		
		long stDate = startDate.getTime()/1000L;
		long edDate = endDate.getTime()/1000L;
		int startIndex = rv.indexOf(getRecord(rv, stDate));
		int endIndex = rv.indexOf(getRecord(rv, edDate));
		
		for(int i = startIndex; i < endIndex; i++) {
			RecordValue rvs = rv.get(i);
			String logDate = sdf.format(rvs.logTime*1000L);
			System.out.println("("+ logDate + ", "+ rvs.usage+")");
		}
	}
	
	// process the data to store in HashMap
	private void processData(String path) {
		System.out.println("Processing Data...");
		try{
		   FileInputStream fstream = new FileInputStream(path);
		   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		   String strLine;
		   while ((strLine = br.readLine()) != null)   {
			   if(strLine.contains("INFO")) {
				   String record = strLine.split("INFO: ")[1].trim();
				   String[] rec = record.split(",");
				   long date = Long.parseLong(rec[0]);
				   
				   String ser = rec[1];
				   int cpu = Integer.parseInt(rec[2]);
				   
				   RecordKey recKey = new RecordKey(ser, cpu);
				   String usage = rec[3];
				   RecordValue recVal = new RecordValue(date, usage);
				   
				   if(map.containsKey(recKey)) {
					   map.get(recKey).add(recVal);
				   }
				   else {
					      List<RecordValue> recList = new ArrayList<RecordValue>();
					   recList.add(recVal);
					   map.put(recKey, recList);
				   }
			   }
		   }
		   fstream.close();
		} catch (Exception e) {
		     System.err.println("Error: " + e.getMessage());
		}
		System.out.println("Processing Completed Sucessfully");
		init = true;
	}
	
	// finds the record from list
	public RecordValue getRecord(final List<RecordValue> list, final long logTime){
	    return list.stream().filter(o -> o.getLogTime().equals(logTime)).findFirst().get();
	}

}
