package com.example.uiexercisesplash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class myAdapter extends ArrayAdapter<String> {

	Context context;
	String[] titles;
	String[] description;
	myAdapter(Context c, String [] titles,String description[]){
		super(c,R.layout.single_row, R.id.textTitle, titles);
	this.context=c;
	this.titles=titles;
	this.description= description;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row= convertView;
		
		if(row==null)
		{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row =inflater.inflate(R.layout.single_row, parent, false);
		}
		
		TextView titleView =(TextView) row.findViewById(R.id.textTitle);
		TextView decriptionView = (TextView) row.findViewById(R.id.textDate);
		
		titleView.setText(titles[position]);
		decriptionView.setText(description[position]);
		
		return row;
	}
	
}
