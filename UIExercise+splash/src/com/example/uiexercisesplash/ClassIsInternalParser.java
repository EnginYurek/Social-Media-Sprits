package com.example.uiexercisesplash;

import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

public class ClassIsInternalParser extends DefaultHandler {

	Context context;
	String TAG="XMLHelper";
	
	String currTagValue;
	
	int titleCounter=0;
	int pubDateCounter=0;
	int linkCounter=0;
	
	static  String sonyaURL;
	
	
	
	Boolean currTag = false;
	
	//ilk sütun title tagının, ikinci sütun link tagının, üçüncü sütun da pubDate tagının içeriğini tutuyor
	//her satır da 1 tane posta ait içerik olacak şekilde
	String dataArray[][]= new String[10][3];   
	
	public ClassIsInternalParser(Context c, String URL){
		context=c;
		sonyaURL=URL;
	}
	
	public void get(){
		try{
		/*	if(Locale.getDefault().toString().equals("ru_RU")){
				sonyaURL="http://classisinternal.com/ru/feed/";
			}else
					sonyaURL="http://classisinternal.com/feed/";*/
			
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser mySaxParser = factory.newSAXParser();
			XMLReader myXMLReader = mySaxParser.getXMLReader();
			myXMLReader.setContentHandler(this);
			InputStream myInputStream = new URL(sonyaURL).openStream();
			myXMLReader.parse(new InputSource(myInputStream));			
		} catch(Exception e){
			Log.e(TAG,"Exception: Engin 234"+ e.getMessage());
				//e.printStackTrace();
		}
		
		
	}//end of get method

	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		if(currTag)
		{
			currTagValue = currTagValue + new String(ch,start,length);
			currTag=false;
		}
	}


	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		currTag=false;
		if(localName.equalsIgnoreCase("title")){
			
			if(titleCounter>=1 && titleCounter<11){
			dataArray[titleCounter-1][0]= editTitle(currTagValue);
			}
			titleCounter++;
			
		}else if(localName.equalsIgnoreCase("link")){
		
			if(linkCounter>=2 && linkCounter<12){
			dataArray[linkCounter-2][1]= currTagValue;
			}
			linkCounter++;
		}
		else if(localName.equalsIgnoreCase("pubDate")){
			if(pubDateCounter<10){
			dataArray[pubDateCounter][2]=parsePublishDate(currTagValue);
			}
			
			pubDateCounter++;
		}
			
	}


	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		Log.i(TAG, "TAG: "+ localName);
		
		currTag=true;
		currTagValue="";
			
	} 
	 
	private String editTitle(String titleValue){
		
		String title = titleValue;
		String delim="[.]+";
		String parts[]= title.split(delim);
		String edited="";
		
		for(int j=0; j<parts.length;j++)
			edited+=parts[j];
		
		return edited;
	}
	
	
	private String parsePublishDate(String dateValue){
		
		String phrase = currTagValue;
		String delims="[ ]+";
		String parts [] =phrase.split(delims);
		
		
		
		parts[5]=null;
		
		String hour =parts[4];
		String dots="[:]+";
		String[] hourParts=hour.split(dots);
		String hourMin= hourParts[0]+ ":"+hourParts[1];
		
		
		
		if(parts[0].equalsIgnoreCase("Wed,"))
			
			parts[0]= context.getString(R.string.wednesday);
		 
		if(parts[0].equals("Mon,"))
			parts[0]=context.getString(R.string.monday);
		
		if(parts[0].equals("Fri,"))
			parts[0]=context.getString(R.string.friday);
		
		if(parts[0].equals("Thu,"))
			parts[0]=context.getString(R.string.thursday);
		
		if(parts[0].equalsIgnoreCase("Tue,"))
			parts[0]=context.getString(R.string.tuesday);
		
		if(parts[0].equals("Sun,"))
			parts[0]=context.getString(R.string.sunday);

		if(parts[0].equals("Sat,"))
			parts[0]=context.getString(R.string.saturday);
		
		return context.getString(R.string.publishedIn)+" "+ parts[1]+ " "+parts[2] +" "+parts[3]+ " "+ parts[0]+ " " +context.getString(R.string.at) + " "+hourMin;
	}
}
	

