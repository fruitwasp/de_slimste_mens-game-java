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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import models.Game;
import observer.GameObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class MainPanel extends BasePanel implements GameObserver {

	private JPanel mainButtonPanel, mainInfoPanel, yourTurnPanel,
			enemyTurnPanel, yourTurnLabelPanel, enemyTurnLabelPanel;
	private JLabel enemyTurnsLabel, yourTurnsLabel;

	private Dimension panelSize, labelPanelSize;

	private JScrollPane yourTurnPanelScroller, enemyTurnPanelScroller;

	private static final int PANEL_WIDTH = 500, PANEL_HEIGHT = 120;
	private static final int LABEL_PANEL_WIDTH = 500, LABEL_PANEL_HEIGHT = 40;
	private final static int MENUBORDER_X = 150, MENUBORDER_Y = 0, MENUBORDER_WIDTH = 2, MENUBORDER_HEIGHT = 350;
	private final static Dimension SPACE_BETWEEN_BUTTONS = new Dimension(0, 10);
	private final static Dimension BUTTON_SIZE = new Dimension(150, 40);
	private final static Dimension MAIN_INFO_PANEL_SIZE = new Dimension(650,
			400), MAIN_BUTTON_PANEL_SIZE = new Dimension(150, 400);

	private static final String PHRASE_RULES = "Spelregels",
			PHRASE_COMPETITIONS = "Jouw competities",
			PHRASE_INVITES = "Uitnodigingen", PHRASE_SETTINGS = "Instellingen",
			PHRASE_ADMIN = "Administrator", PHRASE_OBSERVER = "Toeschouwer";;
	private ArrayList<JPanel> yourTurnPanels, enemyTurnPanels;

	private ArrayList<JButton> mainButtons;

	public MainPanel(MainController mainController) {
		super(mainController);

		panelSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		labelPanelSize = new Dimension(LABEL_PANEL_WIDTH, LABEL_PANEL_HEIGHT);
		yourTurnPanels = new ArrayList<>();
		enemyTurnPanels = new ArrayList<>();
		mainButtons = new ArrayList<JButton>();

		mainButtonPanel = new JPanel();
		mainButtonPanel.setPreferredSize(MAIN_BUTTON_PANEL_SIZE);
		mainButtonPanel.setOpaque(false);
		mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel,
				BoxLayout.Y_AXIS));
		mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));

		add(mainButtonPanel, BorderLayout.WEST);

		createButtons();
		createInfoPanels();

		mainInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		mainController.getGameController().addGameObserver(this);
	}

	private void createInfoPanels() {
		mainInfoPanel = new JPanel();
		mainInfoPanel.setPreferredSize(MAIN_INFO_PANEL_SIZE);
		mainInfoPanel.setOpaque(false);
		mainInfoPanel.setLayout(new BoxLayout(mainInfoPanel, BoxLayout.Y_AXIS));

		this.add(mainInfoPanel, BorderLayout.CENTER);

		yourTurnPanel = new JPanel();
		enemyTurnPanel = new JPanel();
		yourTurnLabelPanel = new JPanel();
		enemyTurnLabelPanel = new JPanel();

		yourTurnsLabel = new JLabel("Spellen waar jij aan de beurt bent:");
		enemyTurnsLabel = new JLabel(
				"Spellen waar de tegenstander aan de beurt is:");

		enemyTurnsLabel.setForeground(CustomColor.WHITE);
		yourTurnsLabel.setForeground(CustomColor.WHITE);

		enemyTurnsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		yourTurnsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		yourTurnPanel.setOpaque(false);
		enemyTurnPanel.setOpaque(false);
		yourTurnLabelPanel.setOpaque(false);
		enemyTurnLabelPanel.setOpaque(false);

		yourTurnPanel.setLayout(new BoxLayout(yourTurnPanel, BoxLayout.Y_AXIS));
		enemyTurnPanel
				.setLayout(new BoxLayout(enemyTurnPanel, BoxLayout.Y_AXIS));
		yourTurnLabelPanel.setLayout(null);
		enemyTurnLabelPanel.setLayout(null);

		yourTurnPanelScroller = new JScrollPane(yourTurnPanel);
		yourTurnPanelScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		yourTurnPanelScroller.setBackground(CustomColor.RED);

		yourTurnPanelScroller.setVisible(true);
		yourTurnPanelScroller.getViewport().setBackground(CustomColor.RED);
		yourTurnPanelScroller.setBorder(BorderFactory.createLineBorder(
				CustomColor.DARK_RED, 2));

		enemyTurnPanelScroller = new JScrollPane(enemyTurnPanel);
		enemyTurnPanelScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		enemyTurnPanelScroller.setBackground(CustomColor.RED);

		enemyTurnPanelScroller.setVisible(true);
		enemyTurnPanelScroller.getViewport().setBackground(CustomColor.RED);
		enemyTurnPanelScroller.setBorder(BorderFactory.createLineBorder(
				CustomColor.DARK_RED, 2));

		yourTurnPanelScroller.setMaximumSize(panelSize);
		yourTurnPanelScroller.setMinimumSize(panelSize);
		enemyTurnPanelScroller.setMaximumSize(panelSize);
		enemyTurnPanelScroller.setMinimumSize(panelSize);
		yourTurnLabelPanel.setMaximumSize(labelPanelSize);
		yourTurnLabelPanel.setMinimumSize(labelPanelSize);
		enemyTurnLabelPanel.setMaximumSize(labelPanelSize);
		enemyTurnLabelPanel.setMinimumSize(labelPanelSize);

		yourTurnLabelPanel.add(yourTurnsLabel);
		enemyTurnLabelPanel.add(enemyTurnsLabel);

		yourTurnsLabel.setBounds(10, 15, 500, 30);
		enemyTurnsLabel.setBounds(10, 15, 500, 30);

		mainInfoPanel.add(yourTurnLabelPanel);
		mainInfoPanel.add(yourTurnPanelScroller);
		mainInfoPanel.add(enemyTurnLabelPanel);
		mainInfoPanel.add(enemyTurnPanelScroller);

	}

	// Not private because it's called in MainFrame
	public void createButtons() {

		mainButtonPanel.removeAll();
		mainButtons.clear();
		JButton competitions = new CustomButton(PHRASE_COMPETITIONS,
				Color.WHITE, CustomColor.DARK_RED);
		JButton invites = new CustomButton(PHRASE_INVITES, Color.WHITE,
				CustomColor.DARK_RED);
		JButton rules = new CustomButton(PHRASE_RULES, Color.WHITE,
				CustomColor.DARK_RED);
		JButton settings = new CustomButton(PHRASE_SETTINGS, Color.WHITE,
				CustomColor.DARK_RED);
		JButton admin = new CustomButton(PHRASE_ADMIN, Color.WHITE,
				CustomColor.DARK_RED);
		JButton observer = new CustomButton(PHRASE_OBSERVER, Color.white,
				CustomColor.DARK_RED);

		competitions.setMaximumSize(BUTTON_SIZE);
		invites.setMaximumSize(BUTTON_SIZE);
		rules.setMaximumSize(BUTTON_SIZE);
		settings.setMaximumSize(BUTTON_SIZE);
		admin.setMaximumSize(BUTTON_SIZE);
		observer.setMaximumSize(BUTTON_SIZE);

		mainButtons.add(settings);

		mainButtons.add(competitions);
		mainButtons.add(rules);
		mainButtons.add(invites);

		boolean isAdmin = false;
		if (this.getMainController().getLoginController().getAccount() != null) {
			if(this.getMainController().getLoginController()
					.getAccount().getAccountTypes().contains("administrator")) {
				
					isAdmin = true;
			}
			if (isAdmin) {
				mainButtons.add(admin);
			}

		}

		boolean isObserver = false;
		if (this.getMainController().getLoginController().getAccount() != null) {
			if (this.getMainController().getLoginController().getAccount()
					.getAccountTypes().contains("toeschouwer")) {
				isObserver = true;
				mainButtons.add(observer);
			}
		}

		for (JButton button : mainButtons) {
			mainButtonPanel.add(button);
			mainButtonPanel.add(Box.createRigidArea(SPACE_BETWEEN_BUTTONS));
		}

		competitions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainController.getCompetitionController().onViewJoinedCompetitionsButtonClicked();
			}

		});

		invites.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getGameController().onViewInvitesButtonClicked();

			}

		});

		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainController.getGameController().onViewRulesButtonClicked();
			}
		});

		if (isObserver) {
			observer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mainController.getObserverController().onObserveGamesButtonClicked();
				}
			});
		}

		if (isAdmin) {

			admin.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mainController.getMainFrame().setContentView(
							mainController.getAdminPanel());

				}

			});
		}

		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getSettingsController()
						.onSettingsButtonClicked();
			}

		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(CustomColor.DARK_RED);
		g.fillRect(MENUBORDER_X, MENUBORDER_Y, MENUBORDER_WIDTH,
				MENUBORDER_HEIGHT);

	}

	@Override
	public void onGamesLoaded(ArrayList<Game> games) {

		for (JPanel pa : yourTurnPanels) {
			yourTurnPanel.remove(pa);
		}
		for (JPanel pa : enemyTurnPanels) {
			enemyTurnPanel.remove(pa);
		}

		yourTurnPanels.clear();
		enemyTurnPanels.clear();
		
		if (mainController.getLoginController().getAccount() == null) {
			return;
		}
		
		String username = mainController.getLoginController().getAccount().getUsername();
		
		for (Game game : games) {			
			
			if (!game.getState().equals("bezig")) {
				continue;
			}

			if (mainController.getGameController().hasTurn(game)) {

				JPanel p = new JPanel();
				p.setPreferredSize(new Dimension(yourTurnPanel.getWidth(), 40));
				p.setMinimumSize(new Dimension(yourTurnPanel.getWidth(), 40));
				p.setMaximumSize(new Dimension(yourTurnPanel.getWidth(), 40));
				p.setBackground(CustomColor.DARK_RED);
				p.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0,
						CustomColor.RED));
				p.setLayout(null);

				JButton start = new CustomButton("Ga verder",
						CustomColor.BLACK, CustomColor.WHITE);
				start.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mainController.getGameController()
								.onPlayGameButtonClicked(game);
					}
				});

				JLabel text = new JLabel();
				JLabel round = new JLabel();
				JLabel turn = new JLabel();

				if (game.getPlayer1().equals(username)) {
					text.setText("Spel tegen " + game.getPlayer2());
				}
				else {
					text.setText("Spel tegen " + game.getPlayer1());
				}

				text.setForeground(CustomColor.WHITE);
				round.setForeground(CustomColor.WHITE);
				turn.setForeground(CustomColor.WHITE);
				round.setText("In de ronde: " + game.getLatestRound().getName());
				
				if (game.getLatestRound().getLatestTurn() != null) {
					turn.setText("In de Turn: " + (game.getLatestRound().getLatestTurn().getId() + 1));
				}

				p.add(text);
				p.add(round);
				p.add(start);
				p.add(turn);

				start.setBounds(340, 3, 100, 30);
				text.setBounds(10, -5, 300, 30);
				round.setBounds(11, 12, 300, 30);
				turn.setBounds(200, -5, 300, 30);

				yourTurnPanels.add(p);

				for (JPanel pp : yourTurnPanels) {
					yourTurnPanel.add(pp);
				}

			} else {

				JPanel p = new JPanel();
				p.setPreferredSize(new Dimension(yourTurnPanel.getWidth(), 40));
				p.setMinimumSize(new Dimension(yourTurnPanel.getWidth(), 40));
				p.setMaximumSize(new Dimension(yourTurnPanel.getWidth(), 40));
				p.setBackground(CustomColor.DARK_RED);
				p.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0,
						CustomColor.RED));
				p.setLayout(null);

				JButton chat = new CustomButton("Open chat venster",
						CustomColor.BLACK, CustomColor.WHITE);
				chat.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mainController.getGameController().onChatButtonClicked(
								game);
					}
				});

				JLabel text = new JLabel();
				JLabel round = new JLabel();
				JLabel turn = new JLabel();

				if (game.getPlayer1().equals(mainController.getLoginController().getAccount().getUsername())) {
					text.setText("Spel tegen " + game.getPlayer2());
				}
				if (game.getPlayer2().equals(
						mainController.getLoginController().getAccount()
								.getUsername())) {
					text.setText("Spel tegen " + game.getPlayer1());
				}

				text.setForeground(CustomColor.WHITE);
				round.setText("In de ronde: " + game.getLatestRound().getName());
				round.setForeground(CustomColor.WHITE);
				turn.setForeground(CustomColor.WHITE);
				
				if (game.getLatestRound().getLatestTurn() != null) {				
					turn.setText("In de Turn: " + (game.getLatestRound().getLatestTurn().getId() + 1));
				}

				p.add(text);
				p.add(round);
				p.add(chat);
				p.add(turn);

				chat.setBounds(340, 3, 100, 30);
				text.setBounds(10, -5, 300, 30);
				round.setBounds(11, 12, 300, 30);
				turn.setBounds(200, -5, 300, 30);

				enemyTurnPanels.add(p);

				for (JPanel pp : enemyTurnPanels) {
					enemyTurnPanel.add(pp);
				}

			}

		}

		yourTurnPanel.revalidate();
		enemyTurnPanel.revalidate();
	}

	@Override
	public void onGameAdded(Game game) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGameRemoved(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameStateChanged(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameReplyChanged(Game game) {
		// TODO Auto-generated method stub

	}

}
