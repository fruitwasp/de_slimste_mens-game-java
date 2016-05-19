package models;

import java.util.ArrayList;

public class Turn {

	private Round round;
	private Question question;
	private ArrayList<Subquestion> subquestions;

	private int id;
	private String status;

	private int secEarned;
	private int secFinaleOff;

	private String username;
	private ArrayList<Attempt> attempts;

	public Turn(Round round, int id, String status, String username) {
		this.round = round;

		this.id = id;
		this.status = status;
		this.username = username;

		attempts = new ArrayList<>();
		subquestions = new ArrayList<>();
	}

	public Round getRound() {
		return round;
	}

	public Question getQuestion() {
		return question;
	}

	public ArrayList<Subquestion> getSubQuestions() {
		return subquestions;
	}

	public int getLatestSubquestionId() {
		if (subquestions == null || subquestions.size() == 0) {
			return 0;
		}

		return getLatestSubquestion() == null ? 0 : getLatestSubquestion()
				.getFollowId();
	}

	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public int getSecEarned() {
		return secEarned;
	}
	
	public int getSecFinaleOff() {
		return secFinaleOff;
	}

	public String getUsername() {
		return username;
	}

	public ArrayList<Attempt> getAttempts() {
		return attempts;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setSubquestions(ArrayList<Subquestion> subQuestions) {
		this.subquestions = subQuestions;
	}

	public Subquestion addSubquestion(int followId, Question question) {
		Subquestion subquestiobObj = new Subquestion(this, followId, question);
		
		subquestions.add(subquestiobObj);
		
		return subquestiobObj;
	}
	
	public Subquestion getLatestSubquestion() {
		Subquestion latestSubquestion = null;
		
		for (Subquestion subQuestion : subquestions) {
			if (latestSubquestion == null || subQuestion.getFollowId() > latestSubquestion.getFollowId()) {
				latestSubquestion = subQuestion;
			}
		}
		
		return latestSubquestion;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSecEarned(int secEarned) {
		this.secEarned = secEarned;
	}
	
	public void setSecFinaleOff(int secFinaleOff) {
		this.secFinaleOff = secFinaleOff;
	}

	public void setAttempts(ArrayList<Attempt> attempts) {
		this.attempts = attempts;
	}

	public Attempt addAttempt(int attemptId, String attempt, int moment) {
		Attempt attemptObj = new Attempt(this, attemptId, attempt, moment);

		attempts.add(attemptObj);

		return attemptObj;
	}
	
	public boolean getAllQuestionCorrect() {
	
		for(Subquestion subquestion : subquestions) {
			if (subquestion.getQuestion().isCorrect()) {
				continue;
			}
			
			return false;
		}
		
		return true;
	}
	
	public Attempt getPreviousAttempt(Attempt attempt) {
		if (attempt == null) {
			return getLatestAttempt();
		}
		
		for (Attempt previousAttempt : attempts) {
			if (previousAttempt.getId() == attempt.getId() - 1) {
				return previousAttempt;
			}
		}
		
		return null;
	}
	
	public Attempt getNextAttempt(Attempt attempt) {
		if (attempt == null) {
			return getFirstAttempt();
		}
		
		for (Attempt nextAttempt : attempts) {
			if (nextAttempt.getId() == attempt.getId() + 1) {
				return nextAttempt;
			}
		}
		
		return null;
	}

	public Attempt getFirstAttempt() {
		Attempt firstAttempt = null;
		
		for (Attempt attempt : attempts) {
			if (firstAttempt == null || firstAttempt.getId() > attempt.getId()) {
				firstAttempt = attempt;
			}
		}
		
		return firstAttempt;
	}
	
	public Subquestion getFirstSubquestion() {
		Subquestion firstSubquestion = null;
		
		for (Subquestion subquestion : subquestions) {
			if (firstSubquestion == null || firstSubquestion.getFollowId() > subquestion.getFollowId()) {
				firstSubquestion = subquestion;
			}
		}
		
		return firstSubquestion;
	}
	
	public Subquestion getPreviousSubQuestion(Subquestion subQuestion) {
		if (subQuestion == null) {
			return getLatestSubquestion();
		}
		
		for (Subquestion nextSubQuestion : subquestions) {
			if (nextSubQuestion.getFollowId() == subQuestion.getFollowId() - 1) {
				return nextSubQuestion;
			}
		}
		
		return null;
	}
	
	public Subquestion getNextSubQuestion(Subquestion subquestion) {
		if (subquestion == null) {
			return getFirstSubquestion();
		}
		
		for (Subquestion nextSubquestion : subquestions) {
			if (nextSubquestion.getFollowId() == subquestion.getFollowId() + 1) {
				return nextSubquestion;
			}
		}
		
		return null;
	}
	
	public Attempt getLatestAttempt() {
		Attempt latestAttempt = null;
		
		for (Attempt attempt : attempts) {
			if (latestAttempt == null || latestAttempt.getId() < attempt.getId()) {
				latestAttempt = attempt;
			}
		}
		
		return latestAttempt;
	}

	public void addSecEarned(int secEarned) {
		this.secEarned += secEarned;		
	}
}
