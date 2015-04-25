package com.example.uiexercisesplash;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class EnglishChannelListActivity extends ListActivity{
 
    String[][] array = new String[25][3];
	
	String rYoutubePostTitle[] =new String[25];
	String rYoutubePostLink[] =new String[25];
	String rYoutubePostDate[] =new String[25];
	
	ListView l;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classisinternal_listview);
		
	new PostAsync().execute();
	
	}	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		openWebPage(rYoutubePostLink[position]);
	}


	public void openWebPage(String link)
	{
		Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(webIntent);
	}

	class PostAsync extends AsyncTask<Void, Void,String[][]>
	{
		ProgressDialog pd;
		EnglishChannelParser  parser;
		
		@Override
		protected void onPreExecute()
		{
			//we can set up variables here
			pd = ProgressDialog.show(EnglishChannelListActivity.this,"Classisinternal","Loading last post...",true,false);	
		}

		protected void onPostExecute(String[][] result) 
		{
			array=result;
			for(int i=0; i<25; i++){
				rYoutubePostTitle[i]=array[i][0];
				rYoutubePostLink[i]=array[i][2];
				rYoutubePostDate[i]=array[i][1];
			}
			l=getListView();
			myAdapter adapter = new myAdapter(EnglishChannelListActivity.this, rYoutubePostTitle, rYoutubePostDate);
			l.setAdapter(adapter);
			
			pd.dismiss();
		}

		protected String[][] doInBackground(Void... params) 
		{
			parser = new EnglishChannelParser(EnglishChannelListActivity.this);
			parser.get();
		
			return parser.dataArray;
		}
	}
}

