package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import models.Game;
import models.Round;
import models.Turn;
import observer.ObserverObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class OpenDeurObserview extends TurnBasePanel implements ObserverObserver {

	/**
	 * 
	 * View for the "Open Deur" panel.
	 * made by Sebastiaan
	 * 
	 */
	
	private static final int SCOREPANEL_WIDTH = 0;
	private static final int SCOREPANEL_HEIGHT = 50;
	
	private static final int ANSWERPANEL_WIDTH = 0;
	private static final int ANSWERPANEL_HEIGHT = 50;
	
	private static final int QUESTIONPANEL_WIDTH = 0;
	private static final int QUESTIONPANEL_HEIGHT = 100;
	
	private static final int ANSWERPANELBORDER_TOP = 5;
	private static final int ANSWERPANELBORDER_LEFT = 5;
	private static final int ANSWERPANELBORDER_BOTTOM = 5;
	private static final int ANSWERPANELBORDER_RIGHT = 5;
	
	private static final int MENUBORDER_X = 0;
	private static final int MENUBORDER_Y = 315;
	private static final int MENUBORDER_Y2 = 50;
	private static final int MENUBORDER_HEIGHT = 1;
	
	private static final int SCORESQUARE_Y = 10;
	private static final int SCORESQUARE_WIDTH = 60;
	private static final int SCORESQUARE_HEIGHT = 30;
	
	private static final int LABELBORDER_TOP = 15;
	private static final int LABELBORDER_LEFT = 15;
	private static final int LABELBORDER_BOTTOM = 0;
	private static final int LABELBORDER_RIGHT = 0;
	
	private static final int LABELTEXT_SIZE = 16;
	
	private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
	private static final Dimension TEXT_FIELD_SIZE = new Dimension(700, 40);
	
	private JPanel answerPanel;
	private JPanel questionPanel;
	private JPanel scorePanel;
	
	private JLabel questionLabel;
	private JLabel score;
	private JLabel score2;
	private JLabel userName;
	private JLabel userName2;
	
	private CustomButton previousButton;
	private CustomButton nextButton;
	
	private static final String PHRASE_PREVIOUS = "< Vorige";
	private static final String PHRASE_NEXT = "Volgende >";
	
//	private JTextField answer;
	
	public OpenDeurObserview(MainController mainController) {
		super(mainController);
		
		score = new JLabel("0");
		score.setForeground(CustomColor.white);
		score.setFont(HEADER_FONT);
		
		score2 = new JLabel("0");
		score2.setForeground(CustomColor.white);
		score2.setFont(HEADER_FONT);
		
		userName = new JLabel("Sebastiaan");
		userName.setForeground(CustomColor.white);
		userName.setFont(HEADER_FONT);
		userName.setHorizontalAlignment(SwingConstants.RIGHT);
		
		userName2 = new JLabel("Kevin");
		userName2.setForeground(CustomColor.white);
		userName2.setFont(HEADER_FONT);
		
		questionLabel = new JLabel();
		questionLabel.setForeground(CustomColor.white);
		questionLabel.setFont(new Font("Arial", Font.BOLD, LABELTEXT_SIZE));
		questionLabel.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));		
		
		answer = new JTextField();
		answer.setEditable(false);
		answer.setMinimumSize(TEXT_FIELD_SIZE);
		answer.setMaximumSize(TEXT_FIELD_SIZE);
		
		previousButton = new CustomButton(PHRASE_PREVIOUS, CustomColor.white, CustomColor.orange);
		previousButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getObserverController().onPreviousTurnClicked();
			}
			
		});
		
		nextButton = new CustomButton(PHRASE_NEXT, CustomColor.white, CustomColor.orange);
		nextButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				mainController.getObserverController().onNextTurnClicked();
				
			}
			
		});
		
		scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
		scorePanel.setPreferredSize(new Dimension(SCOREPANEL_WIDTH, SCOREPANEL_HEIGHT));
		scorePanel.setOpaque(false);
		
		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
		questionPanel.setPreferredSize(new Dimension(QUESTIONPANEL_WIDTH, QUESTIONPANEL_HEIGHT));
		questionPanel.setOpaque(false);
		
		answerPanel = new JPanel();
		answerPanel.setLayout(new BorderLayout());
		answerPanel.setPreferredSize(new Dimension(ANSWERPANEL_WIDTH, ANSWERPANEL_HEIGHT));
		answerPanel.setBorder(BorderFactory.createEmptyBorder(ANSWERPANELBORDER_TOP, ANSWERPANELBORDER_LEFT, ANSWERPANELBORDER_BOTTOM, ANSWERPANELBORDER_RIGHT));
		answerPanel.setOpaque(false);
		
		this.add(scorePanel, BorderLayout.NORTH);
		this.add(questionPanel, BorderLayout.CENTER);
		this.add(answerPanel, BorderLayout.SOUTH);
		
		// creates empty invisible areas with borders to "press" the labels
		
		scorePanel.add(previousButton);
		scorePanel.add(userName);
		scorePanel.add(score);
		scorePanel.add(score2);
		scorePanel.add(userName2);
		scorePanel.add(nextButton);
		
		questionPanel.add(questionLabel);
		
		answerPanel.add(answer);
		
		mainController.getObserverController().addObserverObserver(this);
	}
	
	/**
	 * Menuborders and scoresquares
	 */
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int x = (this.getWidth() / 2) / 2;
		int x2 = (this.getWidth() / 2) + 150;
		
		previousButton.setBounds(5, SCORESQUARE_Y, 90, 25);
		userName.setBounds(0, SCORESQUARE_Y, x - 10, SCORESQUARE_HEIGHT);
		score.setBounds(x + SCORESQUARE_WIDTH / 2, SCORESQUARE_Y,
				SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		score2.setBounds(x2 + SCORESQUARE_WIDTH / 2, SCORESQUARE_Y,
				SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		userName2.setBounds(x2 + SCORESQUARE_WIDTH + 10, SCORESQUARE_Y, x - 10,
				SCORESQUARE_HEIGHT);
		nextButton.setBounds(715, SCORESQUARE_Y, 100, 25);
		
		g.setColor(CustomColor.DARK_RED);
		g.drawRect(MENUBORDER_X, MENUBORDER_Y2, this.getWidth(), MENUBORDER_HEIGHT);
		g.drawRect(MENUBORDER_X, MENUBORDER_Y, this.getWidth(), MENUBORDER_HEIGHT);
		
		g.fillRect(x, SCORESQUARE_Y, SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		g.fillRect(x2, SCORESQUARE_Y, SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
	}

	@Override
	public void onGamesLoaded(ArrayList<Game> endedGames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnLoaded(Game game, Round round, Turn turn) {
		questionLabel.setText(turn.getQuestion().getQuestion());
	}
	
	@Override
	public void onAttemptLoaded(String attempt) {
		answer.setText(attempt);
		
		revalidate();
		System.out.println("ja hoor");
	}
	
	@Override
	public void onRoundLoaded(Game game, Round round) {
		userName.setText(game.getPlayer1());
		userName2.setText(game.getPlayer2());
	}
	
	@Override
	public void onScoresChanged(int scorePlayer1, int scorePlayer2) {
		score.setText("" + scorePlayer1);
		score2.setText("" + scorePlayer2);
	}
	
}

