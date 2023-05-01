package com.checker.sa.android.data;

import java.io.Serializable;

public class FilterData implements Serializable {
    public String region, project, bprop, bcode, jobtype, city, date1, date3, status;

    public FilterData(String region, String project, String bprop, String bcode, String jobtype, String city, String date1, String date3, String status) {
        this.region = region;
        this.project = project;
        this.bprop = bprop;
        this.bcode = bcode;
        this.jobtype = jobtype;
        this.city = city;
        this.date1 = date1;
        this.date3 = date3;
        this.status = status;
    }
}
