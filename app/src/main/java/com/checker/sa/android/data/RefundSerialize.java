package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class RefundSerialize implements Serializable {
		String date_time;
		ArrayList<RefundData> records;
		public String getDate_time() {
			return date_time;
		}
		public void setDate_time(String date_time) {
			this.date_time = date_time;
		}
		public ArrayList<RefundData> getRecords() {
			return records;
		}
		public void setRecords(ArrayList<RefundData> records) {
			this.records = records;
		}
		
		public RefundSerialize(String datetime,ArrayList<RefundData> records)
		{
			this.date_time=datetime;
			this.records=records;
		}
}
