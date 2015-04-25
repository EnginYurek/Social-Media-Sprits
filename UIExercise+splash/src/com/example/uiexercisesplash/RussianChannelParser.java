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

public class RussianChannelParser extends DefaultHandler {

	Context context;
	String TAG="XMLHelper";
	
	String currTagValue;
	
	int titleCounter=0;
	int pubDateCounter=0;
	int linkCounter=0;
	
	static final String russianChannelURL="http://gdata.youtube.com/feeds/base/users/classisinternal/uploads?alt=rss&v=2&orderby=published&client=ytapi-youtube-profile";
										   
	public RussianChannelParser(Context c){
		context=c;
	}
	
	
Boolean currTag = false;
	
	//ilk sütun title ikinci sütun date üçüncü sütun link
	//her satır da 1 tane posta ait içerik olacak şekilde
	String dataArray[][]= new String[25][3];   
	
	
	public void get(){
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser mySaxParser = factory.newSAXParser();
			XMLReader myXMLReader = mySaxParser.getXMLReader();
			myXMLReader.setContentHandler(this);
			InputStream myInputStream = new URL(russianChannelURL).openStream();
			myXMLReader.parse(new InputSource(myInputStream));			
		} catch(Exception e){
			Log.e(TAG,"Exception: Engin "+ e.getMessage());
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
			
			if(titleCounter>=2 && titleCounter<27){
			dataArray[titleCounter-2][0]= currTagValue;
			}
			titleCounter++;
			
		}else if(localName.equalsIgnoreCase("link")){
		
			if(linkCounter>=4 && linkCounter<29){
			dataArray[linkCounter-4][2]= currTagValue;
			}
			linkCounter++;
		}
		else if(localName.equalsIgnoreCase("pubDate")){
			if(pubDateCounter<25){
			dataArray[pubDateCounter][1]= parsePublishDate(currTagValue);
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
	

