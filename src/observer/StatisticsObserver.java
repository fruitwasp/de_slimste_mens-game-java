package observer;

import java.util.ArrayList;

import models.Statistic;

public interface StatisticsObserver {
	
	public void onStatisticsLoaded(ArrayList<Statistic> statistics);
	public void onStatisticsChanged();
}
