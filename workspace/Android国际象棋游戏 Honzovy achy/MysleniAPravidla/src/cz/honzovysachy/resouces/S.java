package cz.honzovysachy.resouces;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class S {
	private static Properties strings;

	public static boolean init(String language) {
		URL url = S.class.getResource("strings_" + language + ".txt");
		strings = new Properties();
		try {
			strings.load(url.openStream());
		} catch (Exception e) {
			String def = "en";
			if (language.equals(def)) return false;
			return init(def);
		};
		return true;
	}
	
	public static boolean init(int type, String file) {
		strings = new Properties();
		switch (type) {
		case 2: try {
			strings.load(new FileInputStream(file));
			return true;
		} catch (IOException e) { return false;}
		case 3: return init("cs");
		case 4: return init("en");
		case 5: return init("es");
		case 6: return init("ca");
		default: return init(Locale.getDefault().getLanguage());
		}
	}
	
	public static String g(String key) {
		try {
			String ret = strings.getProperty(key);
			if (ret == null) return key;
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
