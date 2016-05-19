package controllers;

import views.AdminCreateAccount;
import views.AdminPanel;
import views.AdminUserInfo;
import views.BasePanel;
import views.Chat;
import views.CompetitionCreate;
import views.CompetitionScreen;
import views.DrieZesNegenObserview;
import views.FinaleObserview;
import views.IngelijstObserview;
import views.InvitesPanel;
import views.JoinCompetition;
import views.Login;
import views.MainFrame;
import views.MainPanel;
import views.OpenDeurObserview;
import views.PuzzelObserview;
import views.ReplayGamesPanel;
import views.SettingsPanel;
import views.StatisticsScreen;
import views.TurnDrieZesNegenPanel;
import views.TurnFinalePanel;
import views.TurnIngelijstPanel;
import views.TurnOpenDeurPanel;
import views.TurnPuzzelPanel;
import views.TutorialScreen;
import views.ViewCompetitionMembers;

public class MainController {
	private DatabaseController databaseController;
	private LoginController loginController;
	private GameController gameController;
	private StatisticsController statisticsController;
	private AdminController adminController;
	private CompetitionController competitionController;
	private SettingsController settingsController;
	private ObserverController observerController;	
	private TurnDrieZesNegenController turnDrieZesNegenController;
	private TurnFinaleController turnFinaleController;
	private TurnIngelijstController turnIngelijstController;
	private TurnOpenDeurController turnOpenDeurController;
	private TurnPuzzelController turnPuzzelController;

	private MainFrame mainFrame;
	private BasePanel loginPanel, chatPanel, competitionCreatePanel,
			competitionScreenPanel, invitesPanel, joinCompetitionPanel,
			mainPanel, statisticsScreenPanel, tutorialScreenPanel,
			viewCompetitionMembersPanel, adminUserInfoPanel, adminPanel, adminCreateAccountPanel,
			settingsPanel, replayGamesPanel;

	private TurnDrieZesNegenPanel turnDrieZesNegenPanel;
	private TurnFinalePanel turnFinalePanel;
	private TurnIngelijstPanel turnIngelijstPanel;
	private TurnOpenDeurPanel turnOpenDeurPanel;
	private TurnPuzzelPanel turnPuzzelPanel;
	
	private DrieZesNegenObserview drieZesNegenObserview;
	private OpenDeurObserview openDeurObserview;
	private PuzzelObserview puzzelObserview;
	private IngelijstObserview ingelijstObserview;
	private FinaleObserview finaleObserview;

	private Thread databaseThread;
	private final static int REFRESH_SPEED = 10000;

	public MainController() {
		mainFrame = new MainFrame();

		initControllers();
		initViews();

		mainFrame.setContentView(loginPanel);
	}

	public Thread createDatabaseThread() {
		databaseThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (loginController.getLoggedIn()) {
					try {
						Thread.sleep(REFRESH_SPEED);
					}
					catch (Exception e) {
						System.out.println("Help, ik kan niet in slaap vallen.");
						e.printStackTrace();
					}
					
					if (loginController.getAccount() == null) {
						continue;
					}
					
					boolean isPlayer = loginController.getAccount().getAccountTypes().contains("speler");
					boolean isObserver = loginController.getAccount().getAccountTypes().contains("toeschouwer");
					if(isPlayer || isObserver) {
						if (isPlayer) {
							gameController.onRefreshGamesButtonClicked();
						}
						
						competitionController.onRefreshCompetitionsButtonClicked();
					}
					
					if(loginController.getAccount().getAccountTypes().contains("administrator")) {
						adminController.fetchAccounts();
					}					
				}
			}

		});

		return databaseThread;
	}
	
	public void stopThread(){
		databaseThread.interrupt();
	}
	
	public void restartDatabaseThread(){
		stopThread();
		this.createDatabaseThread();
		
		
	}

	private void initControllers() {

		databaseController = new DatabaseController(this);
		databaseController.connect();

		loginController = new LoginController(this);
		gameController = new GameController(this);
		statisticsController = new StatisticsController(this);
		competitionController = new CompetitionController(this);
		settingsController = new SettingsController(this);

		turnDrieZesNegenController = new TurnDrieZesNegenController(this);
		turnFinaleController = new TurnFinaleController(this);
		turnIngelijstController = new TurnIngelijstController(this);
		turnOpenDeurController = new TurnOpenDeurController(this);
		turnPuzzelController = new TurnPuzzelController(this);
		adminController = new AdminController(this);
		observerController = new ObserverController(this);
	}

	private void initViews() {

		loginPanel = new Login(this);
		chatPanel = new Chat(this);
		competitionCreatePanel = new CompetitionCreate(this);
		competitionScreenPanel = new CompetitionScreen(this);
		invitesPanel = new InvitesPanel(this);
		joinCompetitionPanel = new JoinCompetition(this);
		mainPanel = new MainPanel(this);
		statisticsScreenPanel = new StatisticsScreen(this);
		turnDrieZesNegenPanel = new TurnDrieZesNegenPanel(this);
		turnFinalePanel = new TurnFinalePanel(this);
		turnIngelijstPanel = new TurnIngelijstPanel(this);
		turnOpenDeurPanel = new TurnOpenDeurPanel(this);
		turnPuzzelPanel = new TurnPuzzelPanel(this);
		tutorialScreenPanel = new TutorialScreen(this);
		
		drieZesNegenObserview = new DrieZesNegenObserview(this);
		openDeurObserview = new OpenDeurObserview(this);
		puzzelObserview = new PuzzelObserview(this);
		ingelijstObserview = new IngelijstObserview(this);
		finaleObserview = new FinaleObserview(this);
		
		viewCompetitionMembersPanel = new ViewCompetitionMembers(this);
		settingsPanel = new SettingsPanel(this);
		replayGamesPanel = new ReplayGamesPanel(this);
		adminPanel = new AdminPanel(this);
		adminUserInfoPanel = new AdminUserInfo(this);
		adminCreateAccountPanel = new AdminCreateAccount(this);
	}

	public Thread getDatabaseThread() {
		return databaseThread;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public GameController getGameController() {
		return gameController;
	}
	
	public ObserverController getObserverController() {
		return observerController;
	}

	public AdminController getAdminController() {
		return adminController;
	}

	public StatisticsController getStatisticsController() {
		return statisticsController;
	}

	public SettingsController getSettingsController() {
		return settingsController;
	}

	public CompetitionController getCompetitionController() {
		return competitionController;
	}

	public TurnDrieZesNegenController getTurnDrieZesNegenController() {
		return turnDrieZesNegenController;
	}

	public TurnFinaleController getTurnFinaleController() {
		return turnFinaleController;
	}

	public TurnIngelijstController getTurnIngelijstController() {
		return turnIngelijstController;
	}

	public TurnOpenDeurController getTurnOpenDeurController() {
		return turnOpenDeurController;
	}

	public TurnPuzzelController getTurnPuzzelController() {
		return turnPuzzelController;
	}

	public BasePanel getAdminUserInfoPanel() {
		return adminUserInfoPanel;
	}

	public BasePanel getAdminPanel() {
		return adminPanel;
	}
	
	public BasePanel getAdminCreateAccountPanel(){
		return adminCreateAccountPanel;
	}

	public BasePanel getLoginPanel() {
		return loginPanel;
	}

	public BasePanel getSettingsPanel() {
		return settingsPanel;
	}

	public BasePanel getChatPanel() {
		return chatPanel;
	}

	public BasePanel getCompetitionCreatePanel() {
		return competitionCreatePanel;
	}

	public BasePanel getCompetitionScreenPanel() {
		return competitionScreenPanel;
	}

	public BasePanel getInvitesPanel() {
		return invitesPanel;
	}

	public BasePanel getJoinCompetitionPanel() {
		return joinCompetitionPanel;
	}

	public BasePanel getMainPanel() {
		return mainPanel;
	}

	public BasePanel getStatisticsScreenPanel() {
		return statisticsScreenPanel;
	}

	public TurnDrieZesNegenPanel getTurnDrieZesNegenPanel() {
		return turnDrieZesNegenPanel;
	}

	public TurnFinalePanel getTurnFinalePanel() {
		return turnFinalePanel;
	}

	public TurnIngelijstPanel getTurnIngelijstPanel() {
		return turnIngelijstPanel;
	}

	public TurnOpenDeurPanel getTurnOpenDeurPanel() {
		return turnOpenDeurPanel;
	}

	public TurnPuzzelPanel getTurnPuzzelPanel() {
		return turnPuzzelPanel;
	}
	
	public DrieZesNegenObserview getDrieZesNegenObserview() {
		return drieZesNegenObserview;
	}

	public OpenDeurObserview getOpenDeurObserview() {
		return openDeurObserview;
	}
	
	public PuzzelObserview getPuzzelObserview() {
		return puzzelObserview;
	}
	
	public IngelijstObserview getIngelijstObserview() {
		return ingelijstObserview;
	}
	
	public FinaleObserview getFinaleObserview() {
		return finaleObserview;
	}
	
	public BasePanel getTutorialScreenPanel() {
		return tutorialScreenPanel;
	}

	public BasePanel getViewCompetitionMembersPanel() {
		return viewCompetitionMembersPanel;
	}
	
	public BasePanel getReplayGamesPanel() {
		return replayGamesPanel;
	}
}
