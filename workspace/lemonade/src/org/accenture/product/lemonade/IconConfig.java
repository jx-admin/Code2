package org.accenture.product.lemonade;

import org.accenture.product.lemonade.model.SceneBean;

import android.content.Intent;

public class IconConfig
{

	public static int CURRENT_ICON = 0;

	private static final String BROWSER = "{com.android.browser/com.android.browser.BrowserActivity}";
	private static final String CALCULATOR = "{com.android.calculator2/com.android.calculator2.Calculator}";
	private static final String CALENDAR = "{com.google.android.calendar/com.android.calendar.LaunchActivity}";
	private static final String CLOCK = "{com.google.android.deskclock/com.android.deskclock.DeskClock}";
	private static final String CLOCK_ANDROID = "{com.android.deskclock/com.android.deskclock.DeskClock}";
	private static final String DIALER = "{com.android.contacts/com.android.contacts.DialtactsActivity}";
	private static final String EMAIL = "{com.google.android.email/com.android.email.activity.Welcome}";
	private static final String EMAIL_ANDROID = "{com.android.email/com.android.email.activity.Welcome}";
	private static final String GALLERY = "{com.google.android.gallery3d/com.cooliris.media.Gallery}";
	private static final String GALLERY_ANDROID = "{com.android.gallery/com.android.camera.GalleryPicker}";
	private static final String GMAIL = "{com.google.android.gm/com.google.android.gm.ConversationListActivityGmail}";
	private static final String MAP = "{com.google.android.apps.maps/com.google.android.maps.MapsActivity}";
	private static final String MARKET = "{com.android.vending/com.android.vending.AssetBrowserActivity}";
	private static final String MESSAGE = "{com.android.mms/com.android.mms.ui.ConversationList}";
	private static final String MUSIC = "{com.google.android.music/com.android.music.MusicBrowserActivity}";
	private static final String MUSIC_ANDROID = "{com.android.music/com.android.music.MusicBrowserActivity}";
	private static final String SETTING = "{com.android.settings/com.android.settings.Settings}";
	
	private static final String CAMERA = "{com.google.android.camera/com.android.camera.Camera}";
	private static final String CONTACTS = "{com.android.contacts/com.android.contacts.DialtactsContactsEntryActivity}";
	private static final String LOCAL_SEARCH = "{com.google.android.apps.maps/com.google.android.maps.PlacesActivity}";
	private static final String NAVIGATOR = "{com.google.android.apps.maps/com.google.android.maps.driveabout.app.DestinationActivity}";
	private static final String NEWS_WEATHER = "{com.google.android.apps.genie.geniewidget/com.google.android.apps.genie.geniewidget.activities.NewsActivity}";
	private static final String SEARCH = "{com.google.android.googlequicksearchbox/com.google.android.googlequicksearchbox.SearchActivity}";
	private static final String VOICE_DIALER = "{com.android.voicedialer/com.android.voicedialer.VoiceDialerActivity}";
	private static final String VOICE_SEARCH = "{com.google.android.voicesearch/com.google.android.voicesearch.RecognitionActivity}";

	public static String config(Intent intent)
	{

		if (intent == null)
			return null;

		SceneBean currentBean = Launcher.luanchr.getCurrentScene();

		StringBuffer sb = new StringBuffer();
		sb.append("iconinfo/");
		if(currentBean.getIconsId()==1){
			return null;
		}
		sb.append(currentBean.getIconsId());
		sb.append("/");
		String component = intent.getComponent().toShortString();
		if (BROWSER.equals(component))
		{
			sb.append("browser.png");
		}
		else if (CALCULATOR.equals(component))
		{
			sb.append("calculator.png");
		}
		else if (CALENDAR.equals(component))
		{
			sb.append("calendar.png");
		}
		else if (CLOCK.equals(component) || CLOCK_ANDROID.equals(component))
		{
			sb.append("clock.png");
		}
		else if (DIALER.equals(component))
		{
			sb.append("dialer.png");
		}
		else if (EMAIL_ANDROID.equals(component) || EMAIL.equals(component))
		{
			sb.append("email.png");
		}
		else if (GALLERY_ANDROID.equals(component) || GALLERY.equals(component))
		{
			sb.append("gallery.png");
		}
		else if (GMAIL.equals(component))
		{
			sb.append("gmail.png");
		}
		else if (MAP.equals(component))
		{
			sb.append("map.png");
		}
		else if (MARKET.equals(component))
		{
			sb.append("market.png");
		}
		else if (MESSAGE.equals(component))
		{
			sb.append("message.png");
		}
		else if (MUSIC_ANDROID.equals(component) || MUSIC.equals(component))
		{
			sb.append("music.png");
		}
		else if (SETTING.equals(component))
		{
			sb.append("settings.png");
		}
		else if (CAMERA.equals(component))
		{
			sb.append("camera.png");
		}
		else if (CONTACTS.equals(component))
		{
			sb.append("contacts.png");
		}
		else if (LOCAL_SEARCH.equals(component))
		{
			sb.append("local_search.png");
		}
		else if (NAVIGATOR.equals(component))
		{
			sb.append("navigator.png");
		}
		else if (NEWS_WEATHER.equals(component))
		{
			sb.append("news_weather.png");
		}
		else if (SEARCH.equals(component))
		{
			sb.append("search.png");
		}
		else if (VOICE_DIALER.equals(component))
		{
			sb.append("voice_dialer.png");
		}
		else if (VOICE_SEARCH.equals(component))
		{
			sb.append("voice_search.png");
		}
		else
		{
			return null;
		}
		return sb.toString();
	}
}
