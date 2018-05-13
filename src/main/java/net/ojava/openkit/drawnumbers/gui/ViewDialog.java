package net.ojava.openkit.drawnumbers.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.ojava.openkit.drawnumbers.core.DrawNumbers;
import net.ojava.openkit.drawnumbers.res.Resource;
import net.ojava.openkit.drawnumbers.util.Profile;

public class ViewDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton zoomInBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_ZOOMIN));
	private JButton zoomOutBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_ZOOMOUT));

	private JTextArea contentArea = new JTextArea(20, 40);
	private JButton okBtn = new JButton(Resource.getInstance().getResourceString(Resource.KEY_LABEL_OK));
	
	private int fontSize = Profile.getInstance().getNumberFontSize();
	
	public ViewDialog(JFrame parent) {
		super(parent, Resource.getInstance().getResourceString(Resource.KEY_TITLE_VIEWAWARDINFO), true);
		
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		JPanel cp = new JPanel();
		this.setContentPane(cp);
		cp.setLayout(new BorderLayout(5, 5));
		cp.setBorder(new EmptyBorder(5, 7, 7, 7));
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		northPanel.add(zoomInBtn);
		northPanel.add(zoomOutBtn);
		cp.add(northPanel, BorderLayout.NORTH);
		
		cp.add(new JScrollPane(contentArea));

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		cp.add(p3, BorderLayout.SOUTH);
		
		p3.add(okBtn);
		
		StringBuffer contentBuf = new StringBuffer();
		contentBuf.append(DrawNumbers.getInstance().getNumberPoolDesc());
		contentBuf.append("\r\n");
		contentBuf.append(DrawNumbers.getInstance().getAwardsDesc());
		
		contentArea.setFont(getZoomFont());
		contentArea.setLineWrap(true);
		contentArea.setWrapStyleWord(true);
		contentArea.setText(contentBuf.toString());
		contentArea.setEditable(false);
		
		this.setSize(1000, 700);
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void initEvents() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				doOk();
			}
		});
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doOk();
			}
		});
		
		zoomInBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doZoomIn();
			}
		});
		
		zoomOutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doZoomOut();
			}
		});
	}
	
	private void doOk() {
		this.dispose();
	}
	
	private Font getZoomFont() {
		return new Font("宋体", Font.PLAIN, fontSize);
	}
	
	private void doZoomIn() {
		if(this.fontSize < Profile.MAX_NUMBER_FONT_SIZE) {
			this.fontSize ++ ;
			this.contentArea.setFont(getZoomFont());
		}
	}
	
	private void doZoomOut() {
		if(this.fontSize >= Profile.MIN_NUMBER_FONT_SIZE) {
			this.fontSize -- ;
			this.contentArea.setFont(getZoomFont());
		}
	}
}
