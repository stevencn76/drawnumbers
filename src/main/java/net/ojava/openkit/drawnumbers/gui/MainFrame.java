package net.ojava.openkit.drawnumbers.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import net.ojava.openkit.drawnumbers.core.AwardItem;
import net.ojava.openkit.drawnumbers.core.DrawNumbers;
import net.ojava.openkit.drawnumbers.res.Resource;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MainFrame.class);

	private JButton initBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_AWARDSETTING));
	private JButton viewResultBtn = new JButton(
			Resource.getInstance().getResourceString(Resource.KEY_LABEL_VIEWPOOLNUMS));
	private JTextArea resultArea = new JTextArea();
	private JLabel curAwardField = new JLabel("                ");
	private JLabel titleLabel = new JLabel(Resource.getInstance().getResourceString(Resource.KEY_APPTITLE));
	private JButton drawBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_DRAW));
	private JButton printBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_PRINT));

	private static Color TITLE_FONT_COLOR = new Color(251, 167, 68);
	private static Color SUBTITLE_FONT_COLOR = new Color(236, 254, 255);
	private static Color CONTENT_FONT_COLOR = new Color(133, 128, 221);
	private static Color CONTENT_BG_COLOR = Color.WHITE;
	
	private static Font resultViewFont = new Font("宋 体", Font.BOLD, 32);
	private static Font resultPrintFont = new Font("宋 体", Font.BOLD, 24);

	public MainFrame() {
		super(Resource.getInstance().getResourceString(Resource.KEY_APPNAME));

		initComponents();
		initEvents();
	}

	private void initComponents() {
		resultArea.setEditable(false);
		resultArea.setBackground(CONTENT_BG_COLOR);
		resultArea.setForeground(CONTENT_FONT_COLOR);
		resultArea.setFont(resultViewFont);
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true);
		resultArea.setText(Resource.getInstance().getResourceString(Resource.KEY_TIP_SETNUMSANDAWARDS));
		this.getContentPane().add(new JScrollPane(resultArea));

		GradientPanel p1 = new GradientPanel();
		p1.setLayout(new BorderLayout(5, 5));
		this.getContentPane().add(p1, BorderLayout.NORTH);
		p1.setOpaque(true);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		p2.add(curAwardField);
		p2.add(drawBtn);
		p2.add(initBtn);
		p2.add(viewResultBtn);
		p2.add(printBtn);
		p2.setOpaque(false);

		drawBtn.setEnabled(false);

		p1.add(p2);

		p1.add(titleLabel, BorderLayout.NORTH);

		curAwardField.setOpaque(false);
		curAwardField.setForeground(SUBTITLE_FONT_COLOR);
		curAwardField.setFont(new Font("宋 体", Font.BOLD, 36));

		titleLabel.setOpaque(false);
		titleLabel.setForeground(TITLE_FONT_COLOR);
		titleLabel.setFont(new Font("宋 体", Font.BOLD, 36));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);

		this.setSize(new Dimension(1204, 768));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	private void initEvents() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		initBtn.addActionListener(this);
		viewResultBtn.addActionListener(this);
		drawBtn.addActionListener(this);
		printBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == initBtn) {
			doInitNumberPool();
		} else if (e.getSource() == viewResultBtn) {
			doViewNumberPool();
		} else if (e.getSource() == drawBtn) {
			doDraw();
		} else if (e.getSource() == printBtn) {
			doPrint();
		}
	}

	private void doInitNumberPool() {
		InitDialog dlg = new InitDialog(this);
		dlg.setVisible(true);

		if (dlg.isOk()) {
			resultArea.setText(DrawNumbers.getInstance().getAwardsDesc());

			AwardItem item = DrawNumbers.getInstance().nextAwardItem();
			if (item != null) {
				curAwardField.setText(
						Resource.getInstance().getResourceString(Resource.KEY_TIP_READYDRAW) + ": " + item.getName());
				drawBtn.setEnabled(true);
			}
		}
	}

	private void doViewNumberPool() {
		ViewDialog dlg = new ViewDialog(this);
		dlg.setVisible(true);
	}

	private DrawDialog getDrawDlg() {
		return new DrawDialog(this);
	}

	private void doDraw() {
		getDrawDlg().open();

		resultArea.setText(DrawNumbers.getInstance().getAwardsDesc());

		AwardItem item = DrawNumbers.getInstance().nextAwardItem();
		if (item != null) {
			curAwardField.setText(
					Resource.getInstance().getResourceString(Resource.KEY_TIP_READYDRAW) + ": " + item.getName());
			drawBtn.setEnabled(true);
		} else {
			curAwardField.setText(Resource.getInstance().getResourceString(Resource.KEY_TIP_DRAWEND));
			drawBtn.setEnabled(false);
		}
	}

	private void doPrint() {
		resultArea.setFont(resultPrintFont);
		try {
			resultArea.print();
		} catch (PrinterException e) {
			log.debug("Print result failed", e);
		}
		
		resultArea.setFont(resultViewFont);
	}
}
