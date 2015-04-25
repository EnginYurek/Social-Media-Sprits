package com.example.uiexercisesplash;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class RussianBlogListActivity extends ListActivity {

	String[][] array = new String[10][3];
	String blogPostTitle[] =new String[10];
	String blogPostLink[] =new String[10];
	String blogPostDate[] =new String[10];
	

	ListView l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classisinternal_listview);
		//l=(ListView) findViewById(android.R.id.list);
		
		new PostAsync().execute();
		
	}	
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		openWebPage(blogPostLink[position]);
	}


	public void openWebPage(String link)
	{
		Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(webIntent);
	}

	class PostAsync extends AsyncTask<Void, Void,String[][]>{
		
		ProgressDialog pd;
		ClassIsInternalParser  parser;
		
		@Override
		protected void onPreExecute() {
			//we can set up variables here
			pd = ProgressDialog.show(RussianBlogListActivity.this,"Classisinternal","Loading last post...",true,false);	
			//pd.setContentView(R.layout.progres_dialog);

			//TextView tv =(TextView) pd.findViewById(R.id.textView1);
			//tv.setText("Engin is Loading");
			
		}

		protected void onPostExecute(String[][] result) {
		
			pd.dismiss();
			array=result;
			
			for(int i=0; i<10; i++){
				blogPostTitle[i]=array[i][0];
				blogPostLink[i]=array[i][1];
				blogPostDate[i]=array[i][2];
			}
			
			
			l=getListView();
			myAdapter adapter = new myAdapter(RussianBlogListActivity.this, blogPostTitle, blogPostDate);
			l.setAdapter(adapter);
			
			 
		}

		protected String[][] doInBackground(Void... params) {
			parser = new ClassIsInternalParser(RussianBlogListActivity.this, "http://classisinternal.com/ru/feed/");
			parser.get();
			
			return parser.dataArray;
		}
	}


}

	

