package com.example.uiexercisesplash;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class ZamanServisi extends Service
{
	private static final int NOTIFICATION_ID=1453;
	Timer timer;
	Handler handler;
	public static int counter=0;
	
	boolean newBlogPost=false;
	boolean newRussianChannelPost = false;
	boolean newEnglishChannelPost= false;
	
	CharSequence englishBlogTitle=" a";
	CharSequence russianVideoTitle="b ";
	CharSequence englishVideoTitle= "c ";
	CharSequence russianBlogTitle="d";
	
	ClassIsInternalParser EnglishBlogParser= new ClassIsInternalParser(this,"http://classisinternal.com/feed/");  //parserden bilgi gliyor
	ClassIsInternalParser RussianBlogParser = new ClassIsInternalParser(this, "http://classisinternal.com/ru/feed/");
	EnglishChannelParser EYoutubeParser = new EnglishChannelParser(this);
	RussianChannelParser RYoutubeParser = new RussianChannelParser(this);
	
	Post post = new Post(this);		//databasede tutulan bilgi
	 
	final static long ZAMAN = 15000;
	
	@Override			//burdan diðer aktivitelere bilgi gönderiliyor
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override					//servis ana aktiviteden baðýmsýz çalýþýyor. Anaaktivitede 1 defa çaðýrýlsa yeter sonra uygulama kapansa bile o çalýþýyor
	public void onCreate()
	{
		super.onCreate();
		
		timer = new Timer();
		handler = new Handler(Looper.getMainLooper());
		
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				
				bilgiVer();
				
			}
		}, 0, ZAMAN);
	}
	
	private void bilgiVer()
	{
	checkNewPosts();
		
		if(newBlogPost){
			createNotification(getString(R.string.NewBlogPostonClassisinternal), post.getTitle(1), EnglishBlogListActivity.class);
			newBlogPost=false;
		}
		if(newRussianChannelPost){
			createNotification(getString(R.string.NewVideoonClassisinternalyoutubeChannel), russianVideoTitle, RussianChannelListActivity.class);
			newRussianChannelPost=false;
		}
		if(newEnglishChannelPost){
			createNotification(getString(R.string.NewBlogPostonthatsophiakidyoutubeChannel), englishVideoTitle, EnglishChannelListActivity.class);
			newEnglishChannelPost=false;
		}
		
		
		
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(ZamanServisi.this, "Ekrana bu bilgi yazdýrýlacak Engin"+ post.getTitle(1), Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	
	
	public void checkNewPosts(){
		post.open();
		
		if(!post.getTitle(5).equals("counter")){
			post.createEntr(" ", " "); 	//1-english blog parser
			post.createEntr(" ", " ");  //2-russian youtube parser
			post.createEntr(" ", " ");	//3-english youtube parser
			post.createEntr(" ", " ");	//4-russian blog parser
			post.createEntr(" ", " ");	//5-counter
			post.updateEntry(5, "counter", " ");
		}
		
		EnglishBlogParser.get();
		RYoutubeParser.get();
		EYoutubeParser.get();
		RussianBlogParser.get();
		 
		
		if(!post.getTitle(1).equals(EnglishBlogParser.dataArray[0][0])){
			
			englishBlogTitle=EnglishBlogParser.dataArray[0][0];
			post.updateEntry(1, EnglishBlogParser.dataArray[0][0], EnglishBlogParser.dataArray[0][2]);
			newBlogPost=true;
		}
		
		if(!post.getTitle(2).equals(RYoutubeParser.dataArray[0][0]))
		{
			russianVideoTitle=RYoutubeParser.dataArray[0][0];
			post.updateEntry(2, RYoutubeParser.dataArray[0][0],	RYoutubeParser.dataArray[0][1]);
			newRussianChannelPost=true;
		}
		
		if(!post.getTitle(3).equals(EYoutubeParser.dataArray[0][0]))
		{
			englishVideoTitle= EYoutubeParser.dataArray[0][0];
			post.updateEntry(3, EYoutubeParser.dataArray[0][0], EYoutubeParser.dataArray[0][1]);
			newEnglishChannelPost=true;
		}
		
		 if(post.getTitle(4).equals(RussianBlogParser.dataArray[0][0])){
			 russianBlogTitle=RussianBlogParser.dataArray[0][0];
			 post.updateEntry(4, RussianBlogParser.dataArray[0][0], RussianBlogParser.dataArray[0][2]);
		 }
		//post.close();
	}
	
	 public void createNotification( CharSequence contentTitle, CharSequence contentText, Class classNameObject ){
    	 // NotificationManager üzerine sistemdeki notifikasyonlarý aldýk.
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // notification'a bir tane resim verdik.Istediðiniz bir tane resim
        // verebilirsiniz fakat boyutlara dikkat etmek lazým
        int icon = R.drawable.favicon;
        CharSequence tickerText = "Notification tetiklendi";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
    	notification.defaults=Notification.DEFAULT_SOUND;
    	//notification.sound=Uri.parse("string yol");
    	/*
    	long[] vibrate ={0,100,200,300};
    	notification.vibrate=vibrate;
        
    	notification.ledARGB=0xff00ff00;
    	notification.ledOnMS=300;
    	notification.ledOffMS=1000;
    	notification.flags=Notification.FLAG_SHOW_LIGHTS;*/
    	  
        Context context = getApplicationContext(); 
     
        // Notification'a týklandýðýnda gideceði actvity
        Intent notificationIntent = new Intent(this, classNameObject);
        /*
         * Normalde intent hemen olmasý gereken bir olaydýr.Fakat kullanýcý
         * týklamadýðý zaman intent kaybolur.bunu önlemek için PendingIntent
         * devreye gider Kullanýcý notification'a týklayana kadar
         * kaydediir.Kullanýcý týkladýðý anda Intent aktive edilir.
         */
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
 
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);
        //notification oluþtuðunda bize uyarý sesi verecektir.
        //notification.defaults = Notification.DEFAULT_SOUND;
       // notification.sound = Uri.parse("file:///sdcard/music/Eminem - without me.mp3");
        // NOTIFICATION_ID unique bir Id dir.
        notifManager.notify(NOTIFICATION_ID, notification);
        
    }
	@Override
	public void onDestroy()
	{
		post.close();
		timer.cancel();
		super.onDestroy();
	}
	
}