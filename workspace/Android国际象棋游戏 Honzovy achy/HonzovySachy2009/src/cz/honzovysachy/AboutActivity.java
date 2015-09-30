package cz.honzovysachy;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView wv = new WebView(this);
		wv.loadData(
				"<H2>Honza's Chess</H2>" +
				"<A HREF=\"http://honzovysachy.sf.net\">http://honzovysachy.sf.net</A><BR>" +
				"A simple chess program for Android" +
				"<P>" +
				"You can find description, links to download, bugtracker, forum etc. on the program's web page." +
				"<P>" +
				"Feel free to contact me at jnem6403@seznam.cz"
				, "text/html", "utf-8");
		setContentView(wv);
	}
}
