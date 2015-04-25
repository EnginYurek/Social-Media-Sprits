package com.example.uiexercisesplash;

import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	TextView tv1, tv2, tv3, mailTv;
	Button blog;
	Button youtubeR;
	String language;
	TextView tv;
	ImageButton imgB, twitter, facebook, blogvin, instagram, thumblr;
	Post post = new Post(this);

	int a = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		post.open();

		startService(new Intent(this, ZamanServisi.class));

		blog = (Button) findViewById(R.id.button_blog);
		youtubeR = (Button) findViewById(R.id.youtube);
		// contact=(Button) findViewById(R.id.contact);

		imgB = (ImageButton) findViewById(R.id.languageButton);

		twitter = (ImageButton) findViewById(R.id.twitter);
		facebook = (ImageButton) findViewById(R.id.facebook);
		blogvin = (ImageButton) findViewById(R.id.blogvin);
		instagram = (ImageButton) findViewById(R.id.instagram);
		thumblr = (ImageButton) findViewById(R.id.thumblr);

		tv = (TextView) findViewById(R.id.textView1);
		mailTv = (TextView) findViewById(R.id.email);

		blog.setText(R.string.Blog);
		youtubeR.setText(R.string.Vlog);

		blog.setOnClickListener(this);
		youtubeR.setOnClickListener(this);
		imgB.setOnClickListener(this);
		twitter.setOnClickListener(this);
		facebook.setOnClickListener(this);
		blogvin.setOnClickListener(this);
		instagram.setOnClickListener(this); 
		thumblr.setOnClickListener(this);
		mailTv.setOnClickListener(this);
 
		if (!post.getTitle(7).equals("language button info")) {
			post.createEntr(" ", " "); // 6-dil butonu bilgisi
			post.createEntr("", " "); // if koþulu için
			post.updateEntry(6, Locale.getDefault().getLanguage().toString(),
					" ");
			post.updateEntry(7, "language button info", " ");
			imgB.setImageResource(R.drawable.usa_flag);
		}

		if (post.getTitle(6).equals("ru")) {
			imgB.setImageResource(R.drawable.usa_flag);
		} else if (post.getTitle(6).equals("en"))
			imgB.setImageResource(R.drawable.russian_flag);

		if (!isServiceWorking()) {
			startService(new Intent(this, ZamanServisi.class));
		}

	}

	// if(Locale.getDefault().getLanguage().toString()!="ru")

	@Override
	public void onClick(View v) {
		if (v == imgB) {

			if (post.getTitle(6).equals("en")) {
				post.updateEntry(6, "ru", " ");
				imgB.setImageResource(R.drawable.usa_flag);
			} else if (post.getTitle(6).equals("ru")) {
				post.updateEntry(6, "en", " ");
				imgB.setImageResource(R.drawable.russian_flag);
			}
		} else if (v == blog) {

			if (post.getTitle(6).equalsIgnoreCase("ru")) {
				Intent russianBlogIntent = new Intent(MainActivity.this,
						RussianBlogListActivity.class);
				startActivity(russianBlogIntent);
			} else {
				Intent englishBlogIntent = new Intent(MainActivity.this,
						EnglishBlogListActivity.class);
				startActivity(englishBlogIntent);
			}
		} else if (v == youtubeR) {

			if (post.getTitle(6).equals("ru")) {
				Intent russianChannelIntent = new Intent(MainActivity.this,
						RussianChannelListActivity.class);
				startActivity(russianChannelIntent);
			} else {
				Intent englishChannelIntent = new Intent(MainActivity.this,
						EnglishChannelListActivity.class);
				startActivity(englishChannelIntent);
			}
		} else if (v == facebook) {
			openWeb("https://www.facebook.com/classisinternal");
		} else if (v == twitter) {
			openWeb("http://www.twitter.com/classisinternal");
		} else if (v == blogvin) {
			openWeb("http://www.bloglovin.com/blog/10755921/");
		} else if (v == instagram) {
			openWeb("http://instagram.com/classisinternal");
		} else if (v == thumblr) {
			openWeb("http://sonyaesman.tumblr.com/");
		} else if (v == mailTv) {

			Intent intent = null, chooser = null;
			intent = new Intent(Intent.ACTION_SEND);
			intent.setData(Uri.parse("mailto:"));
			String[] to = { "yurekengin@gmail.com" };
			intent.putExtra(Intent.EXTRA_EMAIL, to);
			// intent.putExtra(Intent.EXTRA_SUBJECT, "hi this is my app");
			// intent.putExtra(Intent.EXTRA_TEXT, "Emailin Ýçeriði");
			intent.setType("message/rfc822");
			chooser = Intent.createChooser(intent, "Send Email");
			startActivity(chooser);
		}

	}

	private void openWeb(String link) {
		Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(webIntent);
	}

	private boolean isServiceWorking() {
		ActivityManager serviceManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		for (RunningServiceInfo service : serviceManager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (getApplication().getPackageName().equals(
					service.service.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public void SendMail(String targetMail) {

		Intent intent = null, chooser = null;

		intent = new Intent(Intent.ACTION_SEND);
		intent.setData(Uri.parse("mailto:"));
		intent.putExtra(Intent.EXTRA_EMAIL, targetMail);
		intent.putExtra(Intent.EXTRA_SUBJECT, "hi this is my app");
		intent.putExtra(Intent.EXTRA_TEXT, "Emailin Ýçeriði");
		intent.setType("message/rfc822");
		chooser = Intent.createChooser(intent, "Send Email");
		startActivity(chooser);
	}

}
