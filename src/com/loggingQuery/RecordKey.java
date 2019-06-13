package com.loggingQuery;

public class RecordKey {
	String server;
	int cpu;

	public RecordKey(String server, int cpu) {
		this.server = server;
		this.cpu = cpu;
	}

	@Override
    public int hashCode() {
		int hashcode = 0;
		hashcode = cpu * 20;
		hashcode += server.hashCode();
		return hashcode;
    }
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RecordKey) {
        	RecordKey rk = (RecordKey) obj;
        	return (rk.server.equals(this.server) && rk.cpu == this.cpu);
        }
		else 
			return false;
	}
}
