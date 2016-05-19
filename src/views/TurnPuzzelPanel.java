package views;

import helper.CustomButton;
import helper.CustomColor;
import helper.TurnPuzzelItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

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
import models.Subquestion;
import models.Turn;
import observer.TurnObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class TurnPuzzelPanel extends TurnBasePanel implements TurnObserver {

	/**
	 * 
	 * View for the "Puzzel" panel.
	 * 
	 */

	private static final int SCOREPANEL_WIDTH = 0;
	private static final int SCOREPANEL_HEIGHT = 50;

	private static final int ANSWERPANEL_WIDTH = 0;
	private static final int ANSWERPANEL_HEIGHT = 50;

	private static final int ANSWER1TO5_WIDTH = 400;
	private static final int ANSWER1TO5_HEIGHT = 80;
	
	private static final int QUESTIONPANEL_WIDTH = 0;
	private static final int QUESTIONPANEL_HEIGHT = 200;

	private static final int ANSWERPANELBORDER_TOP = 5;
	private static final int ANSWERPANELBORDER_LEFT = 5;
	private static final int ANSWERPANELBORDER_BOTTOM = 5;
	private static final int ANSWERPANELBORDER_RIGHT = 5;

	private static final int MENUBORDER_X = 0;
	private static final int MENUBORDER_Y = 290;
	private static final int MENUBORDER_Y2 = 50;
	private static final int MENUBORDER_HEIGHT = 1;

	private static final int SCORESQUARE_Y = 10;
	private static final int SCORESQUARE_WIDTH = 60;
	private static final int SCORESQUARE_HEIGHT = 30;

	private static final int ANSWERPANELBUTTON_WIDTH = 120;
	private static final int ANSWERPANELBUTTON_HEIGHT = 40;

	private static final int BUTTONBORDER_WIDTH = 5;
	private static final int BUTTONBORDER_HEIGHT = 0;

	private static final int TEXTFIELDSIZE_WIDTH = 700;
	private static final int TEXTFIELDSIZE_HEIGHT = 40;

	private static final int LABELBORDER_TOP = 10;
	private static final int LABELBORDER_TOP2 = 15;
	private static final int LABELBORDER_LEFT = 15;
	private static final int LABELBORDER_BOTTOM = 5;
	private static final int LABELBORDER_RIGHT = 15;
	
	private static final Dimension buttonSizeAnswerPanel = new Dimension(
			ANSWERPANELBUTTON_WIDTH, ANSWERPANELBUTTON_HEIGHT);;
	private static final Dimension textFieldSize = new Dimension(
			TEXTFIELDSIZE_WIDTH, TEXTFIELDSIZE_HEIGHT);

	private static final Font answerFont = new Font("Arial", Font.PLAIN, 14);
	
	private JPanel answerPanel;
	private JPanel questionPanel;
	private JPanel answer1To3;
	private JPanel scorePanel;
	private JLabel answer1, answer2, answer3, answer4;
	
	private JLabel score;
	private JLabel score2;
	private JLabel userName;
	private JLabel userName2;

	private ArrayList<JLabel> labels;
	private ArrayList<TurnPuzzelItem> turnPuzzelItems;
	
	private CustomButton pas, send;
	
	private static final String PASSBUTTON_TEXT = "Pas", ANTWOORD_TEXT = "Antwoord";
	private static final Font HEADER_FONT = new Font("Arial", Font.PLAIN, 16);
	
	private JTextField answer;

	public TurnPuzzelPanel(MainController mainController) {
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

		answer1 = new JLabel("Dit is de vraag?");
		answer1.setFont(new Font("Arial", Font.PLAIN, 14));
		answer1.setForeground(CustomColor.WHITE);

		pas = new CustomButton(PASSBUTTON_TEXT, Color.WHITE, CustomColor.orange);
		pas.setMinimumSize(buttonSizeAnswerPanel);
		pas.setMaximumSize(buttonSizeAnswerPanel);		
		pas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainController.getTurnPuzzelController().onSkipButtonClicked();
				
				answer.setText("");
			}
			
		});
		
		send = new CustomButton(ANTWOORD_TEXT, Color.WHITE, CustomColor.DARK_RED);
		send.setMinimumSize(buttonSizeAnswerPanel);
		send.setMaximumSize(buttonSizeAnswerPanel);		
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				mainController.getTurnPuzzelController().onAttemptSubmitted(answer.getText().toLowerCase());
				
				answer.setText("");
			}
			
		});

		answer = new JTextField();
		answer.setMinimumSize(textFieldSize);
		answer.setMaximumSize(textFieldSize);
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
		questionPanel.setPreferredSize(new Dimension(QUESTIONPANEL_WIDTH, QUESTIONPANEL_HEIGHT));
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

		scorePanel.add(userName);
		scorePanel.add(score);
		scorePanel.add(score2);
		scorePanel.add(userName2);

		answerPanel.add(pas);
		answerPanel.add(Box.createRigidArea(new Dimension(BUTTONBORDER_WIDTH,
				BUTTONBORDER_HEIGHT)));
		answerPanel.add(answer);
		answerPanel.add(Box.createRigidArea(new Dimension(BUTTONBORDER_WIDTH,
				BUTTONBORDER_HEIGHT)));
		answerPanel.add(send);

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(questionPanel, BorderLayout.CENTER);
		container.add(answerPanel, BorderLayout.SOUTH);
		container.setOpaque(false);
		add(container, BorderLayout.CENTER);

		initLabels();
		
		mainController.getTurnPuzzelController().setTurnObserver(this);
	}

	private void initLabels() {
		labels = new ArrayList<>();
		turnPuzzelItems = new ArrayList<>();
		
		answer1 = new JLabel("- Aap");
		answer1.setForeground(CustomColor.white);
		answer1.setFont(answerFont);
		answer1.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP2, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer2 = new JLabel("- Vis");
		answer2.setForeground(CustomColor.white);
		answer2.setFont(answerFont);
		answer2.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer3 = new JLabel("- Olifant");
		answer3.setForeground(CustomColor.white);
		answer3.setFont(answerFont);
		answer3.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer4 = new JLabel("- Olifant");
		answer4.setForeground(CustomColor.white);
		answer4.setFont(answerFont);
		answer4.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer1To3 = new JPanel();
		answer1To3.setLayout(new FlowLayout());
		answer1To3.setPreferredSize(new Dimension(ANSWER1TO5_WIDTH, ANSWER1TO5_HEIGHT));
		answer1To3.setOpaque(false);
		answer1To3.add(answer1);
		answer1To3.add(answer2);
		answer1To3.add(answer3);		

		labels.add(answer1);
		labels.add(answer2);
		labels.add(answer3);
	}
	
	/**
	 * Menuborders and scoresquares
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int x = (this.getWidth() / 2) / 2;
		int x2 = (this.getWidth() / 2) + 150;

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
		userName.setText(game.getPlayer1());
		userName2.setText(game.getPlayer2());
		score.setText("" + game.getPlayer1Points());
		score2.setText("" + game.getPlayer2Points());
		
		int i = 0;
		for (Subquestion subQuestion : turn.getSubQuestions()) {
			Question question = subQuestion.getQuestion();
			
			ArrayList<Answer> questions = question.getAnswers();
			Collections.shuffle(questions);
			
			for (Answer answer : questions) {
				TurnPuzzelItem turnPuzzelItem = new TurnPuzzelItem(answer.getAnswer());
				
				questionPanel.add(turnPuzzelItem);
				turnPuzzelItems.add(turnPuzzelItem);
			}
						
			labels.get(i).setText(question.getQuestion());
			labels.get(i).setVisible(false);		
			
			i++;
		}
		
		questionPanel.add(answer1To3);
	}

	@Override
	public void onTurnEnd(Game game, Round round, Turn turn) {
		for (TurnPuzzelItem turnPuzzelItem : turnPuzzelItems) {
			questionPanel.remove(turnPuzzelItem);
		}
		
		turnPuzzelItems.clear();
		questionPanel.remove(answer1To3);
	}

	@Override
	public void onTimePlayer1Changed(int time) {
		score.setText("" + time);
	}
	
	@Override
	public void onTimePlayer2Changed(int time) {
		score2.setText("" + time);
	}

	@Override
	public void onAnswerCorrect(String answer) {
		for (JLabel jLabel : labels) {
			if(!answer.equals(jLabel.getText())) { continue; }
			
			System.out.println("label: " + jLabel.getText());
			
			jLabel.setVisible(true);
			revalidate();			
		}
		
		this.answer.setText("");				
	}

	@Override
	public void onAnswerIncorrect() {		
		this.answer.setText("");
	}
}
