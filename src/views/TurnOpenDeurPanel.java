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
import javax.swing.JButton;
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
public class TurnOpenDeurPanel extends TurnBasePanel implements TurnObserver {

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
	
	private static final int BUTTONBORDER_WIDTH = 5;
	private static final int BUTTONBORDER_HEIGHT = 0;
	

	private static final int LABELBORDER_TOP = 10;
	private static final int LABELBORDER_LEFT = 15;
	private static final int LABELBORDER_BOTTOM = 5;
	private static final int LABELBORDER_RIGHT = 15;
	
	private static final int LABELTEXT_SIZE = 16;
	private static final int MAX_LABEL_WIDTH = 700;
	
	private static final Font HEADER_FONT = new Font("Arial", Font.PLAIN, 16);
	
	private static final Dimension BUTTON_SIZE_ANSWER_PANEL = new Dimension(120, 40);;
	private static final Dimension TEXT_FIELD_SIZE = new Dimension(500, 40);	
	
	private JPanel answerPanel;
	private JPanel questionPanel;
	private JPanel scorePanel;
	
	private JLabel questionLabel;
	private JLabel score;
	private JLabel score2;
	private JLabel userName;
	private JLabel userName2;
	private JLabel answer1, answer2, answer3, answer4;
	
	private ArrayList<JLabel> labels;
	
	private JButton pas;
	private JButton send;
	
	private static final String STOPBUTTON_TEXT = "pas";
	private static final String ANTWOORD_TEXT = "Antwoord";
	
	private JTextField answer;
	
	public TurnOpenDeurPanel(MainController mainController) {
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
		
		pas = new CustomButton(STOPBUTTON_TEXT, Color.WHITE, CustomColor.DARK_RED);
		pas.setMinimumSize(BUTTON_SIZE_ANSWER_PANEL);
		pas.setMaximumSize(BUTTON_SIZE_ANSWER_PANEL);
		pas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				mainController.getTurnOpenDeurController().onSkipButtonClicked();
				
				answer.setText("");
			}
			
		});
		
		send = new CustomButton(ANTWOORD_TEXT, Color.WHITE, CustomColor.DARK_RED);
		send.setMinimumSize(BUTTON_SIZE_ANSWER_PANEL);
		send.setMaximumSize(BUTTON_SIZE_ANSWER_PANEL);
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				mainController.getTurnOpenDeurController().onAttemptSubmitted(answer.getText().toLowerCase());
				
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
		scorePanel.setPreferredSize(new Dimension(SCOREPANEL_WIDTH, SCOREPANEL_HEIGHT));
		scorePanel.setOpaque(false);
		
		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
		questionPanel.setPreferredSize(new Dimension(QUESTIONPANEL_WIDTH, QUESTIONPANEL_HEIGHT));
		questionPanel.setOpaque(false);

		answerPanel = new JPanel();
		answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.X_AXIS));
		answerPanel.setPreferredSize(new Dimension(ANSWERPANEL_WIDTH, ANSWERPANEL_HEIGHT));
		answerPanel.setBorder(BorderFactory.createEmptyBorder(ANSWERPANELBORDER_TOP, ANSWERPANELBORDER_LEFT, ANSWERPANELBORDER_BOTTOM, ANSWERPANELBORDER_RIGHT));
		answerPanel.setOpaque(false);
		
		this.add(scorePanel, BorderLayout.NORTH);
		this.add(questionPanel, BorderLayout.CENTER);
		this.add(answerPanel, BorderLayout.SOUTH);
		
		// creates empty invisible areas with borders to "press" the labels
		
		scorePanel.add(userName);
		scorePanel.add(score);
		scorePanel.add(score2);
		scorePanel.add(userName2);		
		questionPanel.add(questionLabel);
		
		answerPanel.add(pas);
		answerPanel.add(Box.createRigidArea(new Dimension(BUTTONBORDER_WIDTH, BUTTONBORDER_HEIGHT)));
		answerPanel.add(answer);
		answerPanel.add(Box.createRigidArea(new Dimension(BUTTONBORDER_WIDTH, BUTTONBORDER_HEIGHT)));
		answerPanel.add(send);
		
		labels = new ArrayList<>();
		answerLabelInit();
		
		mainController.getTurnOpenDeurController().setTurnObserver(this);
	}
	
	/**
	 * label initialization
	 */
	
	public void answerLabelInit() {
		
		answer1 = new JLabel("- Aap");
		answer1.setForeground(CustomColor.white);
		answer1.setFont(HEADER_FONT);
		answer1.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer2 = new JLabel("- Vis");
		answer2.setForeground(CustomColor.white);
		answer2.setFont(HEADER_FONT);
		answer2.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer3 = new JLabel("- Olifant");
		answer3.setForeground(CustomColor.white);
		answer3.setFont(HEADER_FONT);
		answer3.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		answer4 = new JLabel("- Paard");
		answer4.setForeground(CustomColor.white);
		answer4.setFont(HEADER_FONT);
		answer4.setBorder(BorderFactory.createEmptyBorder(LABELBORDER_TOP, LABELBORDER_LEFT, LABELBORDER_BOTTOM, LABELBORDER_RIGHT));
		
		questionPanel.add(answer1);
		questionPanel.add(answer2);
		questionPanel.add(answer3);
		questionPanel.add(answer4);
		
		labels.add(answer1);
		labels.add(answer2);
		labels.add(answer3);
		labels.add(answer4);
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

	/**
	 * 
	 * gets the question, adds the possible answers to an arraylist
	 * these will be shown on the panel.
	 * 
	 */
	
	@Override
	public void onTurnBegin(Game game, Round round, Turn turn) {
		
		userName.setText(game.getPlayer1());
		userName2.setText(game.getPlayer2());
		
		Question question = turn.getQuestion();
		
		String qstnText = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", MAX_LABEL_WIDTH, question.getQuestion());
		questionLabel.setText(qstnText);
		
		score.setText("" + game.getPlayer1Points());
		score2.setText("" + game.getPlayer2Points());
				
		int i = 0;
		
		for (Answer answer : question.getAnswers()) {
			labels.get(i).setText(answer.getAnswer());
			labels.get(i).setVisible(false);
			i++;
		}	
	}

	@Override
	public void onTurnEnd(Game game, Round round, Turn turn) {
		// TODO Auto-generated method stub
		
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
	public void onAnswerCorrect(String answer) {
		
		System.out.println("antwoord: " + answer);		
		
		for (JLabel jLabel : labels) {
			System.out.println("label: " + jLabel.getText());
			
			if (!answer.equals(jLabel.getText())) { 
				continue;
			}
			
			jLabel.setVisible(true);
			revalidate();			
		}
		
		this.answer.setText("");
		
	}

	@Override
	public void onAnswerIncorrect() {
		answer.setText("");
		
	}
}

