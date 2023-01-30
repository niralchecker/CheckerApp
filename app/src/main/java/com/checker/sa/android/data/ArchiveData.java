package com.checker.sa.android.data;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArchiveData implements Serializable
{
	static final long serialVersionUID=1L;
	private ArrayList<ArchiveNameValue> nvp;
	private SubmitQuestionnaireData thisOrder;
	ArrayList<filePathDataID> uploadList;
	String orderId,url,user;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public SubmitQuestionnaireData getThisOrder() {
        return thisOrder;
    }

    public void setThisOrder(SubmitQuestionnaireData thisOrder) {
        this.thisOrder = thisOrder;
    }

    public ArchiveData(SubmitQuestionnaireData thisOrder, String url, String user, String orderid, List<NameValuePair> nvp, ArrayList<filePathDataID> uploadList) {
		this.orderId=orderid;
		this.thisOrder=thisOrder;
		this.url=url;
		this.user=user;
		this.nvp=new ArrayList<ArchiveNameValue>();

		for (int i=0;nvp!=null && i<nvp.size();i++)
		{
			this.nvp.add(new ArchiveNameValue(nvp.get(i).getName(),nvp.get(i).getValue()));
		}
		this.uploadList=uploadList;
	}

	public List<ArchiveNameValue> getNvp() {
		return nvp;
	}

	public void setNvp(ArrayList<ArchiveNameValue> nvp) {
		this.nvp = nvp;
	}

	public ArrayList<filePathDataID> getUploadList() {
		return uploadList;
	}

	public void setUploadList(ArrayList<filePathDataID> uploadList) {
		this.uploadList = uploadList;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
