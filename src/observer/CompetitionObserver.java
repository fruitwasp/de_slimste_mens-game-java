package observer;

import java.util.ArrayList;

import models.Competition;

public interface CompetitionObserver {
	
	public void onCompetitionsLoaded(ArrayList<Competition> competitions);
	public void onCompetitionPlayersLoaded(ArrayList<String> members);
	public void onCompetitionAdded(Competition competition);
	public void onCompetitionRemoved(Competition competition);
	public void onPlayerAdded(String username);
	public void onPlayerJoinFailed(String username);
	public void onCreateCompetitionFailed(String name);
	public void onJoinedCompetitionsLoaded(
			ArrayList<Competition> joinedCompetitions);
	public void onMembersLoaded(int competitionId, ArrayList<String> members);
}
