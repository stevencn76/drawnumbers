package net.ojava.openkit.drawnumbers.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.ojava.openkit.drawnumbers.res.Resource;

public class DrawNumbers {
	private static DrawNumbers instance;
	
	private List<Integer> numberPool = new LinkedList<Integer>();
	private List<AwardItem> awardItems = new LinkedList<AwardItem>();
	private String numberPoolText;
	private String awardText;
	private int completedCount = 0;
	
	
	private DrawNumbers() {
	}
	
	public static synchronized DrawNumbers getInstance() {
		if(instance == null) {
			instance = new DrawNumbers();
		}
		
		return instance;
	}
	
	public synchronized void reset(String numberPoolText, String awardText, List<Integer> pool, List<AwardItem> items) {
		this.numberPoolText = numberPoolText;
		this.awardText = awardText;
		
		numberPool.clear();
		if(pool != null)
			numberPool.addAll(pool);
		
		awardItems.clear();
		if(items != null) 
			awardItems.addAll(items);
		
		completedCount = 0;
	}
	
	public synchronized AwardItem nextAwardItem() {
		if(awardItems.size() == 0 || completedCount >= awardItems.size())
			return null;
		
		return awardItems.get(completedCount);
	}
	
	public synchronized void completeAwardItem(Collection<Integer> numbers) {
		AwardItem item = nextAwardItem();
		if(item != null && numbers.size() == item.getLuckyCount()) {
			item.getLuckyNumbers().addAll(numbers);
			numberPool.removeAll(numbers);
			completedCount++;
		}
	}
	
	public synchronized boolean isInitiated() {
		return awardItems.size() > 0;
	}
	
	public List<Integer> getNumberPool() {
		Set<Integer> tmpSet = new HashSet<Integer>();
		tmpSet.addAll(numberPool);
		
		List<Integer> tmpPool = new ArrayList<Integer>();
		tmpPool.addAll(tmpSet);
		
		return tmpPool;
	}
	
	public String getNumberPoolDesc() {
		StringBuffer sb = new StringBuffer(Resource.getInstance().getResourceString(Resource.KEY_TIP_POOLINGNUMS));
		sb.append(" : ");
		if(numberPool.size() <= 0)
			sb.append(Resource.getInstance().getResourceString(Resource.KEY_TIP_UNINIT));
		else {
			int count = 0;
			for(Integer ti : numberPool) {
				if(count > 0)
					sb.append(", ");
				sb.append(ti);
				count++;
			}
		}
		sb.append("\r\n");
		
		return sb.toString();
	}
	
	public String getAwardsDesc() {
		StringBuffer sb = new StringBuffer(Resource.getInstance().getResourceString(Resource.KEY_TIP_AWARDINFO));
		sb.append(": \r\n");
		for(AwardItem tai : awardItems) {
			sb.append(tai.toString());
		}
		
		return sb.toString();
	}

	public String getNumberPoolText() {
		return numberPoolText;
	}

	public void setNumberPoolText(String numberPoolText) {
		this.numberPoolText = numberPoolText;
	}

	public String getAwardText() {
		return awardText;
	}

	public void setAwardText(String awardText) {
		this.awardText = awardText;
	}
}
