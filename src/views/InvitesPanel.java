package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import controllers.GameController;
import controllers.MainController;

@SuppressWarnings("serial")
public class InvitesPanel extends BasePanel implements GameObserver {
	
	private GameController gameController;
	
	private JPanel mainButtonPanel;
	private JPanel mainInfoPanel;
	private JPanel invitePanel;
	private JScrollPane invitePanelScroller;
	private Dimension spaceBetweenButtons;
	private Dimension buttonSize;
	private Dimension invitePanelSize;
	private static final int INVITE_PANEL_WIDTH = 100, INVITE_PANEL_HEIGHT = 100;
	private static final int SPACE_WIDTH = 0, SPACE_HEIGHT = 10;
	private static final int BUTTON_WIDTH = 150, BUTTON_HEIGHT = 40;
	private static final int PANEL_HEIGHT = 400;
	private static final int BUTTON_PANEL_WIDTH = 150;
	private static final int GAMES_PANEL_WIDTH = 650;
	private static final int MENUBORDER_X = 150, MENUBORDER_Y = 0, MENUBORDER_WIDTH = 2, MENUBORDER_HEIGHT = 350;
	private static final String COMP_HEAD_PANEL_TEXT = "Jouw competities";
	private static final String COMP_BUTTON_TEXT = "<html><center>" + COMP_HEAD_PANEL_TEXT.replaceAll("\\n", "<br>") + "</center></html>";
	private static final String RULES_BUTTON_TEXT = "Spelregels";
	private static final String ACTIVE_GAMES_BUTTON_TEXT = "Actieve spellen";
	private ArrayList<JPanel> panels;
	
	public InvitesPanel(MainController mainController) {
		super(mainController);
		
		this.gameController = mainController.getGameController();
		
		panels = new ArrayList<>();
		spaceBetweenButtons = new Dimension(SPACE_WIDTH, SPACE_HEIGHT);
		buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
		invitePanelSize = new Dimension(INVITE_PANEL_WIDTH, INVITE_PANEL_HEIGHT);
		
		this.setLayout(new BorderLayout());
		
		mainButtonPanel = new JPanel();
		mainButtonPanel.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH,PANEL_HEIGHT));
		mainButtonPanel.setOpaque(false);
		mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel, BoxLayout.Y_AXIS));
		
		createButtons();
		
		this.add(mainButtonPanel, BorderLayout.WEST);
		
		mainInfoPanel = new JPanel();
		mainInfoPanel.setLayout(new BorderLayout());
		mainInfoPanel.setPreferredSize(new Dimension(GAMES_PANEL_WIDTH,PANEL_HEIGHT));
		mainInfoPanel.setOpaque(false);
		
		this.add(mainInfoPanel, BorderLayout.CENTER);		
		
		mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(80, 5, 5, 5));
		mainInfoPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		this.createInvitePanel();
		
		mainController.getGameController().addGameObserver(this);
	}
	
	private void createInvitePanel() {
	
		invitePanel = new JPanel();
		invitePanel.setMaximumSize(invitePanelSize);
		invitePanel.setMinimumSize(invitePanelSize);
		invitePanel.setOpaque(false);
		invitePanel.setLayout(new BoxLayout(invitePanel, BoxLayout.Y_AXIS));
		
//		invitePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		invitePanelScroller = new JScrollPane(invitePanel);
		invitePanelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		invitePanelScroller.setBackground(CustomColor.RED);
		
		invitePanelScroller.setVisible(true);
		invitePanelScroller.getViewport().setBackground(CustomColor.RED);
		invitePanelScroller.setBorder(BorderFactory.createLineBorder(CustomColor.DARK_RED, 2));
	
		
		
		mainInfoPanel.add(invitePanelScroller, BorderLayout.CENTER);
	}
	
	private void createButtons() {	
		JButton competitieHoofdscherm = new CustomButton(COMP_BUTTON_TEXT, Color.WHITE, CustomColor.DARK_RED);
		JButton spelregels = new CustomButton(RULES_BUTTON_TEXT, Color.WHITE, CustomColor.DARK_RED);
		JButton activeGames = new CustomButton(ACTIVE_GAMES_BUTTON_TEXT, Color.WHITE, CustomColor.DARK_RED);
		
		competitieHoofdscherm.setMinimumSize(buttonSize);
		spelregels.setMinimumSize(buttonSize);
		activeGames.setMinimumSize(buttonSize);
				
		competitieHoofdscherm.setMaximumSize(buttonSize);
		spelregels.setMaximumSize(buttonSize);
		activeGames.setMaximumSize(buttonSize);
				
		mainButtonPanel.add(competitieHoofdscherm);
		mainButtonPanel.add(Box.createRigidArea(spaceBetweenButtons));
		mainButtonPanel.add(spelregels);
		mainButtonPanel.add(Box.createRigidArea(spaceBetweenButtons));
		mainButtonPanel.add(activeGames);
		
		competitieHoofdscherm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				mainController.getCompetitionController().onViewJoinedCompetitionsButtonClicked();
				
			}
			
		});
		
		activeGames.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getMainFrame().setContentView(new MainPanel(mainController));								
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
		for (JPanel p : panels) {
			invitePanel.remove(p);
		}
		
		panels.clear();
		
		if (mainController.getLoginController().getAccount() == null) {
			return;			
		}
		
		String username = mainController.getLoginController().getAccount().getUsername();
		
		for (Game game : games) {
			if (!game.getState().equals("uitdaging")) {
				continue;
			}
			
			if (!game.getPlayer2().equals(username)) {
				continue;
			}

			JPanel p = new JPanel();
			p.setPreferredSize(new Dimension(invitePanel.getWidth(), 40));
			p.setMinimumSize(new Dimension(invitePanel.getWidth(), 40));
			p.setMaximumSize(new Dimension(invitePanel.getWidth(), 40));
			p.setBackground(CustomColor.DARK_RED);
			p.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, CustomColor.RED));
			p.setLayout(null);
			
			JButton accept = new CustomButton("Accepteer", CustomColor.WHITE, CustomColor.GREEN);
			accept.setBounds(340, 3, 100, 30);
			accept.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gameController.onAcceptGameInviteButtonClicked(game);
				}
			});
			
			JButton decline = new CustomButton("Verwerp", CustomColor.WHITE, CustomColor.RED);
			decline.setBounds(450, 3, 100, 30);
			decline.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gameController.onRejectGameInviteButtonClicked(game);
				}
			});
			
			JLabel text = new JLabel();
			text.setBounds(10, 3, 300, 30);
			text.setText("Uitgenodigd door " + game.getPlayer1());			
			text.setForeground(CustomColor.WHITE);

			p.add(text);
			p.add(accept);
			p.add(decline);			

			panels.add(p);
			invitePanel.add(p);
		}
		
		revalidate();		
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
