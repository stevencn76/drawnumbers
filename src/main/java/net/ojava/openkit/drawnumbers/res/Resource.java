package net.ojava.openkit.drawnumbers.res;

import java.awt.Cursor;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public class Resource {
	private static Logger log = Logger.getLogger(Resource.class);
	
	public static final String KEY_APPNAME = "APP.NAME";
	public static final String KEY_APPTITLE = "APP.TITLE";
	public static final String KEY_APPVERSION = "APP.VERSION";
	public static final String KEY_APPRELEASEDATE = "APP.RELEASEDATE";
	
	public static final String KEY_LABEL_CLOSE = "LABEL.CLOSE";
	public static final String KEY_LABEL_STOP = "LABEL.STOP";
	public static final String KEY_LABEL_POOLNUMS = "LABEL.POOLNUMS";
	public static final String KEY_LABEL_AWARDS = "LABEL.AWARDS";
	public static final String KEY_LABEL_OK = "LABEL.OK";
	public static final String KEY_LABEL_CANCEL = "LABEL.CANCEL";
	public static final String KEY_LABEL_AWARDSETTING = "LABEL.AWARDSETTING";
	public static final String KEY_LABEL_VIEWPOOLNUMS = "LABEL.VIEWPOOLNUMS";
	public static final String KEY_LABEL_DRAW = "LABEL.DRAW";
	public static final String KEY_LABEL_SYSTEM = "LABEL.SYSTEM";
	public static final String KEY_LABEL_PRINT = "LABEL.PRINT";
	public static final String KEY_LABEL_ZOOMIN = "LABEL.ZOOMIN";
	public static final String KEY_LABEL_ZOOMOUT = "LABEL.ZOOMOUT";
	
	public static final String KEY_TIP_UNDRAWING="TIP.UNDRAWING";
	public static final String KEY_TIP_POOLINGNUMS="TIP.POOLINGNUMS";
	public static final String KEY_TIP_UNINIT="TIP.UNINIT";
	public static final String KEY_TIP_AWARDINFO="TIP.AWARDINFO";
	public static final String KEY_TIP_DRAWING="TIP.DRAWING";
	public static final String KEY_TIP_SETNUMSANDAWARDS="TIP.SETNUMSANDAWARDS";
	public static final String KEY_TIP_INPUTPOOLNUMBS="TIP.INPUTPOOLNUMBS";
	public static final String KEY_TIP_INPUTVALIDPOOLNUMBS="TIP.INPUTVALIDPOOLNUMBS";
	public static final String KEY_TIP_INPUTAWARDITEMS="TIP.INPUTAWARDITEMS";
	public static final String KEY_TIP_INPUTVALIDAWARDITEMS="TIP.INPUTVALIDAWARDITEMS";
	public static final String KEY_TIP_AWARDCOUNTERROR="TIP.AWARDCOUNTERROR";
	public static final String KEY_TIP_READYDRAW="TIP.READYDRAW";
	public static final String KEY_TIP_DRAWEND="TIP.DRAWEND";
	
	
	public static final String KEY_TITLE_AWARDINIT="TITLE.AWARDINIT";
	public static final String KEY_TITLE_DRAWING="TITLE.DRAWING";
	public static final String KEY_TITLE_ERRORTIP="TITLE.ERRORTIP";
	public static final String KEY_TITLE_VIEWAWARDINFO="TITLE.VIEWAWARDINFO";
	
	public static final String KEY_SAMPLE_AWARDS="SAMPLE.AWARDS";
	public static final String KEY_SAMPLE_NUMBERS="SAMPLE.NUMBERS";
	
	public static final String KEY_ERROR_EMPTYEXTNAME="ERROR.EMPTYEXTNAME";
	public static final String KEY_ERROR_INVALIDOUTPUTPATH="ERROR.INVALIDOUTPUTPATH";
	public static final String KEY_ERROR_INVALIDSOURCEPATH="ERROR.INVALIDSOURCEPATH";
	
	public static final String KEY_MSG_CONFIRMDELETERESULT="MSG.CONFIRMDELETERESULT";
	
	public static final int TABLE_ROW_HEIGHT = 24;
	
	private File dataPath;
	public static Locale cnLocale = new Locale("zh", "CN");
	public static Locale enLocale = new Locale("us", "EN");
	private ResourceBundle resources;
	
	private static  Resource instance;
	
	// Stock images
	public enum IMAGES {
//		ICON_MAIN16,
//		ICON_MAIN32,
//		ICON_MAIN64,
//		ICON_MAIN128
	}
	
	private final String[] stockImageLocations = {
//		"/res/main_icon16.png",
//		"/res/main_icon32.png",
//		"/res/main_icon48.png",
//		"/res/main_icon64.png",
//		"/res/main_icon128.png"
	};
	private ImageIcon stockImages[];
	
	// Stock cursors
	public final int
		cursorDefault = 0,
		cursorWait = 1;
	public Cursor stockCursors[];
	
	private Resource() {
		String homeDir = System.getProperty("user.home");
		try {
			File dir = new File(homeDir, "ojava");
			if(!dir.exists())
				dir.mkdir();
			dir = new File(dir, "drawernumbers");
			if(!dir.exists())
				dir.mkdir();
			
			dataPath = dir;
		} catch (Exception e) {
			dataPath = new File(homeDir);
		}
	}
	
	public static Resource getInstance() {
		if(instance == null)
			instance = new Resource();
		return instance;
	}
	
	/**
	 * Loads the resources
	 * 
	 * @param display the display
	 */
	public void initResources() throws Exception {
		resources = ResourceBundle.getBundle("res.App", Locale.getDefault());

		if (stockImages == null) {
			stockImages = new ImageIcon[stockImageLocations.length];
				
			for (int i = 0; i < stockImageLocations.length; ++i) {
				ImageIcon image = createStockImage(stockImageLocations[i]);
				if (image == null) {
					freeResources();
					throw new IllegalStateException("load resources failed! [" + stockImageLocations[i] + "]");
				}
				stockImages[i] = image;
			}
		}	
		if (stockCursors == null) {
			stockCursors = new Cursor[] {
				null,
				new Cursor(Cursor.WAIT_CURSOR)
			};
		}
	}
	/**
	 * Frees the resources
	 */
	public void freeResources() {
		if (stockImages != null) {
			stockImages = null;
		}
		if(stockCursors != null) {
			stockCursors = null;
		}
	}
	/**
	 * Creates a stock image
	 * 
	 * @param display the display
	 * @param path the relative path to the icon
	 */
	private ImageIcon createStockImage(String path) {
		try {
			URL res = Resource.class.getResource(path);
			if (res != null) {
				return new ImageIcon(res);
			}
		} catch (Throwable e) {
			log.debug("createStockImage", e);
		}
		return null;
	}
	
	public ImageIcon getImage(IMAGES img) {
		return stockImages[img.ordinal()];
	}
	
	public File getDataPath() {
		return dataPath;
	}
	

	public String getResourceString(String key) {
		try {
			 return resources.getString(key);
		} catch (Exception e) {
		}

		return "";
	}
}
