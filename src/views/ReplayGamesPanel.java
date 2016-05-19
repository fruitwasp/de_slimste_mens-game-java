package views;

import helper.CustomButton;
import helper.CustomColor;
import helper.CustomLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import models.Game;
import models.Round;
import models.Subquestion;
import models.Turn;
import observer.ObserverObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class ReplayGamesPanel extends BasePanel implements ObserverObserver {

	private JPanel mainInfoPanel, allGamesPanel, allGamesLabelPanel;
	private JLabel allGamesLabel;

	private Dimension panelSize, labelPanelSize;

	private JScrollPane allGamesPanelScroller;

	private static final int PANEL_WIDTH = 500, PANEL_HEIGHT = 250;
	private static final int LABEL_PANEL_WIDTH = 500, LABEL_PANEL_HEIGHT = 40;
	private final static Dimension MAIN_INFO_PANEL_SIZE = new Dimension(650,
			400);
	
	private static final String BEKIJK_TEXT = "Bekijk";

	private ArrayList<JPanel> allGamesPanels;
	
	public ReplayGamesPanel(MainController mainController) {
		super(mainController);

		panelSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		labelPanelSize = new Dimension(LABEL_PANEL_WIDTH, LABEL_PANEL_HEIGHT);
		allGamesPanels = new ArrayList<>();
		createInfoPanels();

		mainInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		mainController.getObserverController().addObserverObserver(this);
	}

	private void createInfoPanels() {
		mainInfoPanel = new JPanel();
		mainInfoPanel.setPreferredSize(MAIN_INFO_PANEL_SIZE);
		mainInfoPanel.setOpaque(false);
		mainInfoPanel.setLayout(new BoxLayout(mainInfoPanel, BoxLayout.Y_AXIS));

		this.add(mainInfoPanel, BorderLayout.CENTER);

		allGamesPanel = new JPanel();
		allGamesLabelPanel = new JPanel();

		allGamesLabel = new JLabel("Alle spellen:");

		allGamesLabel.setForeground(CustomColor.WHITE);

		allGamesPanel.setOpaque(false);
		allGamesLabelPanel.setOpaque(false);

		allGamesPanel.setLayout(new BoxLayout(allGamesPanel, BoxLayout.Y_AXIS));
		allGamesLabelPanel.setLayout(null);

		allGamesPanelScroller = new JScrollPane(allGamesPanel);
		allGamesPanelScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allGamesPanelScroller.setBackground(CustomColor.RED);

		allGamesPanelScroller.setVisible(true);
		allGamesPanelScroller.getViewport().setBackground(CustomColor.RED);
		allGamesPanelScroller.setBorder(BorderFactory.createLineBorder(
				CustomColor.DARK_RED, 2));

		allGamesPanelScroller.setMaximumSize(panelSize);
		allGamesPanelScroller.setMinimumSize(panelSize);
		allGamesLabelPanel.setMaximumSize(labelPanelSize);
		allGamesLabelPanel.setMinimumSize(labelPanelSize);

		allGamesLabelPanel.add(allGamesLabel);

		allGamesLabel.setBounds(10, 5, 300, 30);

		mainInfoPanel.add(allGamesLabelPanel);
		mainInfoPanel.add(allGamesPanelScroller);

	}

	@Override
	public void onGamesLoaded(ArrayList<Game> games) {

		for (JPanel pa : allGamesPanels) {
			allGamesPanel.remove(pa);
		}

		allGamesPanels.clear();

		for (Game game : games) {

			JPanel p = new JPanel();
			p.setPreferredSize(new Dimension(allGamesPanel.getWidth(), 50));
			p.setMinimumSize(new Dimension(allGamesPanel.getWidth(), 50));
			p.setMaximumSize(new Dimension(allGamesPanel.getWidth(), 50));
			p.setBackground(CustomColor.DARK_RED);
			p.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0,
					CustomColor.RED));
			p.setLayout(null);

			JButton forwardButton = new CustomButton(BEKIJK_TEXT, CustomColor.BLACK,
					CustomColor.WHITE);
			forwardButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mainController.getObserverController().onPlayedGameClicked(
							game);
				}
			});

			JLabel text = new CustomLabel("", CustomColor.WHITE);

			text.setText(game.getPlayer1() + " vs " + game.getPlayer2());

			JLabel comp = new CustomLabel("", CustomColor.WHITE);

			comp.setText(this.mainController.getObserverController()
					.getCompetitionName(game));
			
			JLabel status = new CustomLabel("", CustomColor.WHITE);
			
			status.setText("Status: " + game.getState());

			p.add(text);
			p.add(comp);
			p.add(status);
			p.add(forwardButton);

			forwardButton.setBounds(360, 9, 100, 30);
			text.setBounds(10, -5, 300, 30);
			comp.setBounds(text.getX(), text.getY() + 12, 200, 30);
			status.setBounds(text.getX(), comp.getY() + 12, 200, 30);

			allGamesPanels.add(p);

			for (JPanel pp : allGamesPanels) {
				allGamesPanel.add(pp);
			}

		}

		revalidate();

	}

	@Override
	public void onTurnLoaded(Game game, Round round, Turn turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttemptLoaded(String attempt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubQuestionLoaded(Game game, Round round, Turn turn, Subquestion subQuestion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubquestionsLoaded(Game game, Round round, Turn turn, ArrayList<Subquestion> subquestions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoundLoaded(Game game, Round round) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScoresChanged(int scorePlayer1, int scorePlayer2) {
		// TODO Auto-generated method stub
		
	}

}
