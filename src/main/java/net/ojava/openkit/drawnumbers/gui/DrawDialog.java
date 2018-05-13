package net.ojava.openkit.drawnumbers.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import net.ojava.openkit.drawnumbers.core.AwardItem;
import net.ojava.openkit.drawnumbers.core.DrawNumbers;
import net.ojava.openkit.drawnumbers.res.Resource;

public class DrawDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final int S_DRAWING = 1;
	private static final int S_END = 2;
	private int status = S_DRAWING;
	
	private NumbersPanel resultPanel = new NumbersPanel(10);
	private JButton okBtn = new JButton("ok");
	
	private Set<Integer> awardSet = new HashSet<Integer>();
	private Object awardLock = new Object();
	
	private int[] mynums = {};
	
	private class DrawThread extends Thread {
		public volatile boolean stop = false;
		public void run() {
			AwardItem item = DrawNumbers.getInstance().nextAwardItem();
			if(item != null) {
				while(!stop) {
					generateAward(false);
					
					try {
						Thread.sleep(50);
					} catch (Exception e){}
				}
				generateAward(true);
			}
		}
	}
	
	private DrawThread drawThread = null;
	
	public DrawDialog(JFrame parent) {
		super(parent, Resource.getInstance().getResourceString(Resource.KEY_TITLE_DRAWING), true);
		
		initComponents();
		initEvents();
	}
	
	private void initComponents() {
		JPanel cp = new JPanel();
		cp.setLayout(new BorderLayout(5, 5));
		cp.setBorder(new EmptyBorder(5, 7, 7, 7));
		this.setContentPane(cp);
		
		cp.add(resultPanel);
		
		JPanel bp = new JPanel();
		bp.setLayout(new FlowLayout(FlowLayout.CENTER));
		bp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(bp, BorderLayout.SOUTH);
		
		bp.add(okBtn);
		
		this.setSize(800, 600);
		this.setLocationRelativeTo(this.getParent());
	}
	
	private void initEvents() {
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE) ;
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doOk();
			}
		});
	}
	
	public void open() {
		AwardItem item = DrawNumbers.getInstance().nextAwardItem();
		if(item == null) {
			status = S_END;
			okBtn.setText(Resource.getInstance().getResourceString(Resource.KEY_LABEL_CLOSE));
		} else {
			if(DrawNumbers.getInstance().getNumberPool().size() <= item.getLuckyCount()) {
				generateAward(true);
				sureAward();

				status = S_END;
				okBtn.setText(Resource.getInstance().getResourceString(Resource.KEY_LABEL_CLOSE));
			} else {
				if(drawThread != null) {
					drawThread.stop = true;
					try {
						drawThread.join();
					} catch (Exception e){}
				}
				drawThread = new DrawThread();
				drawThread.start();
				status = S_DRAWING;
				okBtn.setText(Resource.getInstance().getResourceString(Resource.KEY_LABEL_STOP));
			}
		}
		
		this.setVisible(true);
	}
	
	private void stopDrawing() {
		drawThread.stop = true;
		try {
			drawThread.join();
		} catch (Exception e){}
		
		sureAward();
	}
	
	private void doOk() {
		switch(status) {
		case S_DRAWING:
			if(drawThread != null) {
				stopDrawing();
				okBtn.setText(Resource.getInstance().getResourceString(Resource.KEY_LABEL_CLOSE));
				
				status = S_END;
			}
			break;
		case S_END:
			this.dispose();
			break;
		}
	}

	private void generateAward(boolean isFinal) {
		AwardItem item = DrawNumbers.getInstance().nextAwardItem();
		if(item == null)
			return;
		
		synchronized(awardLock) {
			awardSet.clear();
			List<Integer> numbers = DrawNumbers.getInstance().getNumberPool();
			
			int luckyCount = item.getLuckyCount();
			
			if(isFinal) {
				for(int tn : mynums) {
					if(numbers.contains(tn)) {
						awardSet.add(tn);
						numbers.remove((Integer)tn);
					}
				}
				if(awardSet.size() == mynums.length) {
					luckyCount = item.getLuckyCount()-mynums.length;
				} else {
					numbers.addAll(awardSet);
					awardSet.clear();
				}
			}
			
			for(int i=0; i<luckyCount&&numbers.size() > 0; i++) {
				int id = (int)(Math.random() * numbers.size());
				
				awardSet.add(numbers.remove(id));
			}
		}
		
		updateResult();
	}
	
	private void updateResult() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				resultPanel.updateNumbers(awardSet);
			}
		});
	}
	
	private void sureAward() {
		synchronized(awardLock) {
			DrawNumbers.getInstance().completeAwardItem(awardSet);
		}
	}
}
