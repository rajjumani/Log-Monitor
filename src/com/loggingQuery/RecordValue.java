package com.loggingQuery;

public class RecordValue {
	long logTime;
	String usage;
	
	public RecordValue(long logTime, String usage) {
		this.logTime = logTime;
		this.usage = usage;
	}
	
	public Long getLogTime() {
        return logTime;
    }
}
