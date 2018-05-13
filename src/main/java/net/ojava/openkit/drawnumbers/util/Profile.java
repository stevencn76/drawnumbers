package net.ojava.openkit.drawnumbers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class Profile {
	private static Profile instance;
	private static final String PROFILE_FILENAME = "drawnumbers.properties";
	private File profileFile;
	
	public static final int MAX_NUMBER_FONT_SIZE = 98;
	public static final int MIN_NUMBER_FONT_SIZE = 7;
	
	private int numberFontSize;
	private static final String KEY_NUMBER_FONT_SIZE = "number_font_size";
	
	public synchronized static Profile getInstance() {
		if(instance == null) {
			instance = new Profile();
		}
		
		return instance;
	}
	
	public synchronized static void reload() {
		instance = new Profile();
	}
	
	private Profile() {
		profileFile = new File(getHomePath(), PROFILE_FILENAME);
		
		load();
	}
	
	
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	public static String getHomePath() {
		String homeDir = System.getProperty("user.home");
		String fileSeparator = getFileSeparator();
		String path = MessageFormat.format("{0}{1}ojava", homeDir, fileSeparator);
		File f = new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		
		path = MessageFormat.format("{0}{1}ojava{2}drawnumbers", homeDir, fileSeparator, fileSeparator);
		f = new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		
		return path;
	}
	
	public synchronized void load() {
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream(profileFile));
		} catch (Exception e) {
		}

		String str = this.getPropertyString(pro, KEY_NUMBER_FONT_SIZE, "24");
		try {
			this.numberFontSize = Integer.parseInt(str);
		} catch (Exception e){}
		if(this.numberFontSize < 7 || this.numberFontSize > 98) {
			this.numberFontSize = 24;
		}
		
	}
	
	public synchronized void save() {
		Properties pro = new Properties();

		pro.setProperty(KEY_NUMBER_FONT_SIZE, Integer.toString(this.numberFontSize));
		
		try {
			pro.store(new FileOutputStream(profileFile), "draw numbers profile file");
		} catch (Exception e) {
		}
	}
	
	private String getPropertyString(Properties pro, String key, String defaultValue) {
		String str = pro.getProperty(key);
		if(str != null) {
			str = str.trim();
			if(str.length() > 0)
				return str.trim();
		}
		
		return defaultValue;
	}

	public int getNumberFontSize() {
		return numberFontSize;
	}

	public void setNumberFontSize(int numberFontSize) {
		this.numberFontSize = numberFontSize;
	}
}
