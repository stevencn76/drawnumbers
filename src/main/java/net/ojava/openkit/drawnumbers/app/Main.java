package net.ojava.openkit.drawnumbers.app;

import java.awt.Font;
import java.io.File;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import net.ojava.openkit.drawnumbers.gui.MainFrame;
import net.ojava.openkit.drawnumbers.res.Resource;
import net.ojava.openkit.drawnumbers.util.Profile;

public class Main {
	private static Logger log = Logger.getLogger(Main.class);
	public static MainFrame mainFrame;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Locale.setDefault(Resource.cnLocale);
		
		initLogger();

		initGUIFont();
		
		
		try {
			if(!initApp()) {
				destroyApp();
			} else {
				showMainWnd();
			}
		} catch (Throwable e) {
			log.debug("main", e);
			JOptionPane.showMessageDialog(null, e.getMessage());
			destroyApp();
		}
	}

	private static void initLogger() {
		//初始化日志输出配置
		try {
			PatternLayout layout = new PatternLayout();
			layout.setConversionPattern("%d [%t] %-5p %c- %m%n");
			
			ConsoleAppender cAppender = new ConsoleAppender(layout, "System.out");
			cAppender.setEncoding("utf-8");
			Logger.getRootLogger().addAppender(cAppender);
			
			File logDir = new File(Resource.getInstance().getDataPath(), "log");
			if(!logDir.exists()) {
				logDir.mkdir();
			}
			String fileSeparator = System.getProperty("file.separator");
			String datePattern = "'.'yyyy-MM-dd";
			String logFile = MessageFormat.format("{0}{1}{2}", logDir, fileSeparator, "drawnumbers.log");
			DailyRollingFileAppender rfAppender = new DailyRollingFileAppender(layout, logFile, datePattern);
			rfAppender.setAppend(true);
			rfAppender.setEncoding("utf-8");
			Logger.getRootLogger().addAppender(rfAppender);

			
			Logger.getRootLogger().setLevel(Level.DEBUG);
		}
		catch(Exception e) {
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void initGUIFont() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Font f = new Font("宋 体", Font.PLAIN, 13);

			Enumeration keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = UIManager.get(key);
				if (value instanceof javax.swing.plaf.FontUIResource) {
					UIManager.put(key, f);
				}
			}
		} catch (Throwable e) {
		}
	}
	
	public static boolean initApp() throws Exception {
		Profile.getInstance();
		Resource.getInstance().initResources();
		try {
//			System.setProperty("CFBundleName", "aaaa");
//			Application.getApplication().setDockIconImage(Resource.getInstance().stockImages[Resource.iconMain128].getImage());
		} catch (Throwable t){}
		
		return true;
	}
	
	public static void destroyApp() {
//		try {
//			DbUtil.disconnectDb();
//		} catch (Throwable e) {
//			log.debug("exitApp", e);
//		}
//		
		Resource.getInstance().freeResources();
		log.info("exit FileCollector application");
		
		Profile.getInstance().save();
		
		System.exit(0);
	}
	
	public static void showMainWnd() {
		mainFrame = new MainFrame();
//		mainFrame.setLocationRelativeTo(null);
//		mainFrame.setVisible(true);
	}
}
