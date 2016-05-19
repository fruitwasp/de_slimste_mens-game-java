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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import models.Answer;
import models.Game;
import models.Question;
import models.Round;
import models.Turn;
import observer.TurnObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class TurnFinalePanel extends TurnBasePanel implements TurnObserver {

	private static final int SCOREPANEL_WIDTH = 0;
	private static final int SCOREPANEL_HEIGHT = 50;

	private static final int ANSWERPANEL_WIDTH = 0;
	private static final int ANSWERPANEL_HEIGHT = 50;

	private static final int QUESTIONPANEL_WIDTH = 0;
	private static final int QUESTIONPANEL_HEIGHT = 100;

	private static final int ANSWER1TO5_WIDTH = 400;
	private static final int ANSWER1TO5_HEIGHT = 0;

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

	private static final int LABELTEXT_SIZE = 16;
	private static final int MAX_LABEL_WIDTH = 700;
	
	private int x, x2;

	private static final Font ANSWER_FONT = new Font("Arial", Font.PLAIN, 14);

	private static final Dimension ANSWER_PANEL_BUTTON_SIZE = new Dimension(120, 40);
	private static final Dimension TEXT_FIELD_SIZE = new Dimension(575, 40);

	private JPanel questionPanel;
	private JPanel answerPanel;
	private JPanel scorePanel;
	private JPanel answer1To5;
	private JLabel answer1, answer2, answer3, answer4, answer5;

	private JLabel question;
	private JLabel questionText;
	private JLabel score;
	private JLabel score2;
	private JLabel userName;
	private JLabel userName2;
	
	private ArrayList<JLabel> labels;

	private CustomButton pas, send;

	private static final String PASSBUTTON_TEXT = "Pas",
			ANTWOORD_TEXT = "Antwoord";

	private JTextField answer;

	public TurnFinalePanel(MainController mainController) {
		super(mainController);

		labels = new ArrayList<>();

		score = new JLabel("0");
		score.setForeground(CustomColor.white);
		score.setFont(new Font("Arial", Font.BOLD, 16));
		
		score2 = new JLabel("0");
		score2.setForeground(CustomColor.white);
		score2.setFont(new Font("Arial", Font.BOLD, 16));

		userName = new JLabel("safdfwafeasafdfwafea");
		userName.setForeground(CustomColor.white);
		userName.setFont(new Font("Arial", Font.BOLD, 14));
		userName.setHorizontalAlignment(SwingConstants.RIGHT);

		userName2 = new JLabel("Kevin");
		userName2.setForeground(CustomColor.white);
		userName2.setFont(new Font("Arial", Font.BOLD, 14));

		question = new JLabel("");
		question.setForeground(CustomColor.white);
		question.setFont(new Font("Arial", Font.BOLD, LABELTEXT_SIZE));
		question.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP,
				LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));

		questionText = new JLabel("Batman wordt altijd geholpen door Robin. Door wie wordt Sherk Holmes geholpen?");
		questionText.setForeground(Color.WHITE);
		questionText.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP2, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		pas = new CustomButton(PASSBUTTON_TEXT, Color.white, CustomColor.DARK_RED);
		pas.setMinimumSize(ANSWER_PANEL_BUTTON_SIZE);
		pas.setMaximumSize(ANSWER_PANEL_BUTTON_SIZE);
		pas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getTurnFinaleController().onSkipButtonClicked();
				
				answer.setText("");
			}
		});

		send = new CustomButton(ANTWOORD_TEXT, Color.WHITE, CustomColor.DARK_RED);
		send.setMinimumSize(ANSWER_PANEL_BUTTON_SIZE);
		send.setMaximumSize(ANSWER_PANEL_BUTTON_SIZE);
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getTurnFinaleController().onAttemptSubmitted(answer.getText().toLowerCase());
				
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

		answerLabelInit();
		
		mainController.getTurnFinaleController().setTurnObserver(this);
	}
	
	/**
	 * label initialization
	 */
	
	private void answerLabelInit() {
		answer1To5 = new JPanel();
		answer1To5.setLayout(new BoxLayout(answer1To5, BoxLayout.Y_AXIS));
		answer1To5.setPreferredSize(new Dimension(ANSWER1TO5_WIDTH, ANSWER1TO5_HEIGHT));
		answer1To5.setOpaque(false);
		questionPanel.add(answer1To5);
		
		answer1 = new JLabel("- Aap");
		answer1.setForeground(CustomColor.white);
		answer1.setFont(ANSWER_FONT);
		answer1.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP2, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer2 = new JLabel("- Vis");
		answer2.setForeground(CustomColor.white);
		answer2.setFont(ANSWER_FONT);
		answer2.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer3 = new JLabel("- Olifant");
		answer3.setForeground(CustomColor.white);
		answer3.setFont(ANSWER_FONT);
		answer3.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer4 = new JLabel("- Paard");
		answer4.setForeground(CustomColor.white);
		answer4.setFont(ANSWER_FONT);
		answer4.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer5 = new JLabel("- Kip");
		answer5.setForeground(CustomColor.white);
		answer5.setFont(ANSWER_FONT);
		answer5.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
	
		answer1To5.add(answer1);
		answer1To5.add(answer2);
		answer1To5.add(answer3);
		answer1To5.add(answer4);
		answer1To5.add(answer5);
		
		labels.add(answer1);
		labels.add(answer2);
		labels.add(answer3);
		labels.add(answer4);
		labels.add(answer5);
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
		score2.setBounds(x2 + SCORESQUARE_WIDTH / 2, SCORESQUARE_Y,
				SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		userName2.setBounds(x2 + SCORESQUARE_WIDTH + 10, SCORESQUARE_Y, x - 10,
				SCORESQUARE_HEIGHT);

		g.setColor(CustomColor.DARK_RED);
		g.drawRect(MENUBORDER_X, MENUBORDER_Y, this.getWidth(),
				MENUBORDER_HEIGHT);
		g.drawRect(MENUBORDER_X, MENUBORDER_Y2, this.getWidth(),
				MENUBORDER_HEIGHT);

		g.fillRect(x, SCORESQUARE_Y, SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
		g.fillRect(x2, SCORESQUARE_Y, SCORESQUARE_WIDTH, SCORESQUARE_HEIGHT);
	}

	@Override
	public void onTurnBegin(Game game, Round round, Turn turn) {		
		Question question = turn.getQuestion();
		
		String qstnText = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", MAX_LABEL_WIDTH, question.getQuestion());
		questionText.setText(qstnText);

		int i = 0;
		for (Answer answer : question.getAnswers()) {			
			labels.get(i).setText(answer.getAnswer());
			labels.get(i).setVisible(false);
			
			i++;
		}

		userName.setText(game.getPlayer1());
		userName2.setText(game.getPlayer2());
		score.setText("" + game.getPlayer1Points());
		score2.setText("" + game.getPlayer2Points());
	}

	@Override
	public void onTurnEnd(Game game, Round round, Turn turn) {
		// TODO auto-generated method stub
	}

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
	public void onAnswerCorrect(String answer) {
		System.out.println(answer);		
		
		for (JLabel answr : labels) {
			if (answr.getText().equals(answer)) {
				answr.setVisible(true);
			}
		}

		this.answer.setText("");
	}

	@Override
	public void onAnswerIncorrect() {		
		this.answer.setText("");		
	}
}
