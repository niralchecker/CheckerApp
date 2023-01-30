package com.checker.sa.android.data;

import java.io.Serializable;

public class AttachmentFiles  implements Serializable{

	byte[] attachedfile;
	String filename;
	double filesize;
	
	public byte[] getAttachedfile() {
		return attachedfile;
	}
	public void setAttachedfile(byte[] attachedfile) {
		this.attachedfile = attachedfile;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public double getFilesize() {
		return filesize;
	}
	public void setFilesize(double filesize) {
		this.filesize = filesize;
	}
	

	
}
