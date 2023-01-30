package com.checker.sa.android.data;

public class BasicLog {

    private String orderid;
    private String user;
    private String url;
    private String actualUrl;
    private String message;
    private String time;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActualUrl() {
        return actualUrl;
    }

    public void setActualUrl(String actualUrl) {
        this.actualUrl = actualUrl;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUser() {
        if (user!=null)
            user=user.replace(".","DOT");
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        if (url!=null) {
            url = url.replace(".", "_");
            url = url.replace("/", "_");
            url = url.replace(":", "_");
        }
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BasicLog(String url,String user, String message,String orderid) {
        this.user=user;
        this.url=url;
        this.message=message;
        this.orderid=orderid;
    }

    public BasicLog(String actualURL,String url,String user, String message,String orderid) {
        this.user=user;
        this.url=url;
        this.message=message;
        this.orderid=orderid;
        this.actualUrl=actualURL;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
