package com.example.uiexercisesplash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_xml);
		
		Thread logoTime = new Thread(){
			
			public void run(){
				try{
					sleep(500);
					Intent myIntent=new Intent("com.example.uiexercisesplash.FIRSTSCREEN");
					
					startActivity(myIntent);
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
					finish();
				}
			}
		};
		
		logoTime.start();
	}

}
