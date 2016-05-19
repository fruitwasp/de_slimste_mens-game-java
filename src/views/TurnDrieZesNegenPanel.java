package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import models.Game;
import models.Round;
import models.Subquestion;
import models.Turn;
import observer.TurnObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class TurnDrieZesNegenPanel extends TurnBasePanel implements	TurnObserver {

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

	private static final int BUTTONBORDER_WIDTH = 5;
	private static final int BUTTONBORDER_HEIGHT = 0;

	private static final int LABELBORDER_TOP = 10;
	private static final int LABELBORDER_TOP2 = 15;
	private static final int LABELBORDER_LEFT = 15;
	private static final int LABELBORDER_BOTTOM = 5;
	private static final int LABELBORDER_RIGHT = 15;

	private static final int MAX_LABEL_WIDTH = 700;
	private static final int LABELTEXT_SIZE = 16;

	private int x, x2;

	private static final Dimension ANSWER_PANEL_BUTTON_SIZE = new Dimension(120, 40);;
	private static final Dimension TEXT_FIELD_SIZE = new Dimension(575, 40);

	private JPanel questionPanel;
	private JPanel answerPanel;
	private JPanel scorePanel;

	private JLabel question;
	private JLabel questionText;
	private JLabel score;
	private JLabel score2;
	private JLabel userName;
	private JLabel userName2;
	private JLabel timeLeft;

	private CustomButton pas, send;

	private static final String PASSBUTTON_TEXT = "Pas",
			ANTWOORD_TEXT = "Antwoord";

	private JTextField answer;

	public TurnDrieZesNegenPanel(MainController mainController) {
		super(mainController);

		timeLeft = new JLabel("0");
		timeLeft.setForeground(CustomColor.white);
		timeLeft.setFont(new Font("Arial", Font.BOLD, 16));
		
		score = new JLabel("0");
		score.setForeground(CustomColor.white);
		score.setFont(new Font("Arial", Font.BOLD, 16));

		score2 = new JLabel("0");
		score2.setForeground(CustomColor.white);
		score2.setFont(new Font("Arial", Font.BOLD, 16));

		userName = new JLabel("Sebastiaan");
		userName.setForeground(CustomColor.white);
		userName.setFont(new Font("Arial", Font.BOLD, 14));
		userName.setHorizontalAlignment(SwingConstants.RIGHT);

		userName2 = new JLabel("Kevin");
		userName2.setForeground(CustomColor.white);
		userName2.setFont(new Font("Arial", Font.BOLD, 14));

		question = new JLabel("Vraag 1");
		question.setForeground(CustomColor.white);
		question.setFont(new Font("Arial", Font.BOLD, LABELTEXT_SIZE));
		question.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP,
				LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));

		questionText = new JLabel(
				"Batman wordt altijd geholpen door Robin. Door wie wordt Sherk Holmes geholpen?");
		questionText.setForeground(Color.WHITE);
		questionText.setBorder(BorderFactory.createEmptyBorder(
				LABELBORDER_TOP2, LABELBORDER_LEFT, LABELBORDER_BOTTOM,
				LABELBORDER_RIGHT));

		pas = new CustomButton(PASSBUTTON_TEXT, Color.WHITE,
				CustomColor.DARK_RED);
		pas.setMinimumSize(ANSWER_PANEL_BUTTON_SIZE);
		pas.setMaximumSize(ANSWER_PANEL_BUTTON_SIZE);
		pas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainController.getTurnDrieZesNegenController().onSkipButtonClicked();
				
				answer.setText("");
			}
		});

		send = new CustomButton(ANTWOORD_TEXT, Color.WHITE,
				CustomColor.DARK_RED);
		send.setMinimumSize(ANSWER_PANEL_BUTTON_SIZE);
		send.setMaximumSize(ANSWER_PANEL_BUTTON_SIZE);

		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getTurnDrieZesNegenController().onAttemptSubmitted(answer.getText().toLowerCase());
				
				answer.setText("");
			}

		});

		answer = new JTextField();
		answer.setMinimumSize(TEXT_FIELD_SIZE);
		answer.setMaximumSize(TEXT_FIELD_SIZE);
		answer.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					send.doClick();
				}

			}
		});
		
		scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
		scorePanel.setPreferredSize(new Dimension(SCOREPANEL_WIDTH,
				SCOREPANEL_HEIGHT));
		scorePanel.setOpaque(false);

		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
		questionPanel.setPreferredSize(new Dimension(QUESTIONPANEL_WIDTH,
				QUESTIONPANEL_HEIGHT));
		questionPanel.setOpaque(false);

		answerPanel = new JPanel();
		answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.X_AXIS));
		answerPanel.setPreferredSize(new Dimension(ANSWERPANEL_WIDTH,
				ANSWERPANEL_HEIGHT));
		answerPanel.setBorder(BorderFactory.createEmptyBorder(
				ANSWERPANELBORDER_TOP, ANSWERPANELBORDER_LEFT,
				ANSWERPANELBORDER_BOTTOM, ANSWERPANELBORDER_RIGHT));
		answerPanel.setOpaque(false);

		this.add(scorePanel, BorderLayout.NORTH);
		this.add(questionPanel, BorderLayout.CENTER);
		this.add(answerPanel, BorderLayout.SOUTH);

		// creates empty invisible areas with borders to "press" the labels
		
		scorePanel.add(userName);
		scorePanel.add(score);
		scorePanel.add(timeLeft);
		scorePanel.add(score2);
		scorePanel.add(userName2);

		questionPanel.add(question);
		questionPanel.add(questionText);

		answerPanel.add(pas);
		answerPanel.add(Box.createRigidArea(new Dimension(BUTTONBORDER_WIDTH,
				BUTTONBORDER_HEIGHT)));
		answerPanel.add(answer);
		answerPanel.add(Box.createRigidArea(new Dimension(BUTTONBORDER_WIDTH,
				BUTTONBORDER_HEIGHT)));
		answerPanel.add(send);

		mainController.getTurnDrieZesNegenController().setTurnObserver(this);
	}

	/**
	 * Menuborders and scoresquares
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		x = (this.getWidth() / 2) / 2;
		x2 = (this.getWidth() / 2) + 150;

		userName.setBounds(0, SCORESQUARE_Y, x - 10, SCORESQUARE_HEIGHT);
		score.setBounds(x + SCORESQUARE_WIDTH / 2, SCORESQUARE_Y,
				SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		timeLeft.setBounds(this.getWidth() / 2, SCORESQUARE_Y, 20, 30);
		score2.setBounds(x2 + SCORESQUARE_WIDTH / 2, SCORESQUARE_Y,
				SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		userName2.setBounds(x2 + SCORESQUARE_WIDTH + 10, SCORESQUARE_Y, x - 10,
				SCORESQUARE_HEIGHT);

		g.setColor(CustomColor.DARK_RED);
		g.drawRect(MENUBORDER_X, MENUBORDER_Y, this.getWidth(), MENUBORDER_HEIGHT);
		g.drawRect(MENUBORDER_X, MENUBORDER_Y2, this.getWidth(), MENUBORDER_HEIGHT);

		g.fillRect(x, SCORESQUARE_Y, SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		g.fillRect(x2, SCORESQUARE_Y, SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
	}

	@Override
	public void onTurnBegin(Game game, Round round, Turn turn) {
		userName.setText(game.getPlayer1());
		userName2.setText(game.getPlayer2());

		score.setText("" + game.getPlayer1Points());
		score2.setText("" + game.getPlayer2Points());
		
		Subquestion subQuestionObj = turn.getLatestSubquestion();

		question.setText("Vraag " + subQuestionObj.getFollowId() + " / 9");
		
		String qstnText = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", MAX_LABEL_WIDTH, subQuestionObj.getQuestion().getQuestion());
		questionText.setText(qstnText);
		
		revalidate();
	}

	@Override
	public void onTurnEnd(Game game, Round round, Turn turn) {
		
	}

	@Override
	public void onTimePlayer1Changed(int time) {
		score.setText("" + time);
		revalidate();
	}
	
	@Override
	public void onTimePlayer2Changed(int time) {
		score2.setText("" + time);
		revalidate();
	}
	
	@Override
	public void onSubquestionBegin(Game game, Round round, Turn turn, Subquestion subquestion) {
		question.setText("Vraag " + subquestion.getFollowId() + " / 9");
		questionText.setText(subquestion.getQuestion().getQuestion());
		
		revalidate();	
	}
	
	@Override
	public void onAnswerCorrect(String answer) {
		this.answer.setText("");		
	}

	@Override
	public void onAnswerIncorrect() {
		this.answer.setText("");		
	}
	
	@Override
	public void onTimerChanged(int time) {
		timeLeft.setText("" + time);		
	}

}
