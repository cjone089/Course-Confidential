package com.courseconfidential.core;

public class CCFile {

	private String fileNumber;
	private String filename; 
	private String filetype; 
	private byte[] data;
	private String contentHeader;

	

	public static String HEADER_OCTET_STREAM ="application/octet-stream";
	public static String HEADER_PDF ="application/pdf";
	public static String HEADER_MSWORD ="application/msword";
	public static String HEADER_EXCEL ="application/vnd.ms-excel";

	
	public CCFile(String ID, String name, String type,  byte[] blob)
	{
		this(name, type);
	    data = blob;
	    fileNumber = ID;
	    contentHeader = HEADER_OCTET_STREAM;//default
	    buildHeader();
	}
	
	public CCFile(String name, String type)
	{
		filename = name;
		filetype = type;
	}

	public final void buildHeader()
	{
		switch(filetype)
		{
			case ".txt":
				contentHeader = HEADER_OCTET_STREAM;
			break;

			case ".pdf":
				contentHeader = HEADER_PDF;
			break;

			case ".doc":
				contentHeader = HEADER_MSWORD;
			break;

			case ".docx":
				contentHeader = HEADER_MSWORD;
			break;

			case ".xls":
				contentHeader = HEADER_EXCEL;
			break;

			case ".xlsx":
				contentHeader = HEADER_EXCEL;
			break;
		}

	}

	
	public String getFilename() {
		return filename;
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}



	public String getFiletype() {
		return filetype;
	}



	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}



	public byte[] getData() {
		return data;
	}



	public void setData(byte[] data) {
		this.data = data;
	}



	public String getContentHeader() {
		return contentHeader;
	}


	public void setContentHeader(String contentHeader) {
		this.contentHeader = contentHeader;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	
}