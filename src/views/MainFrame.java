package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private final static int MINIMUM_WIDTH = 825, MINUMUM_HEIGHT = 400;
	private final static String TITLE = "De Slimste Mens";

	private JPanel activeContentView;
	private ArrayList<BasePanel> viewHistory;
	private JMenu logout;
	
	public MainFrame() {
		viewHistory = new ArrayList<BasePanel>();
		logout = new JMenu("Uitloggen");

		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		if (System.getProperty("os.name").equals("Mac OS X")) {
			setIconImage(new ImageIcon("res\\icon.png").getImage());
		} else {
			setIconImage(new ImageIcon("res/icon.png").getImage());
		}
		setMinimumSize(new Dimension(MINIMUM_WIDTH, MINUMUM_HEIGHT));
		setLocationRelativeTo(null);
	}

	/**
	 * This function sets the current JPanel
	 * 
	 * @param panel
	 *            The new panel
	 */
	public void setContentView(BasePanel panel) {
		if (activeContentView != null) {
			remove(activeContentView);
		}

		add(panel, BorderLayout.CENTER);

		activeContentView = panel;

		if (!(panel instanceof Login || panel instanceof TurnBasePanel || panel instanceof EndGameView)) {
			this.viewHistory.add(panel);
		}
		
		if(panel instanceof Login || panel instanceof TurnBasePanel || panel instanceof EndGameView){
			this.viewHistory.clear();
		}
		

		if (panel instanceof MainPanel) {
			((MainPanel) panel).createButtons();
		}

		this.repaint();

		if (panel instanceof TurnBasePanel) {
			setJMenuBar(null);
		} else {
			createMenus();
		}


		revalidate();
	}

	public JPanel getContentView() {
		return activeContentView;
	}

	/**
	 * Function to create the menus
	 */
	private void createMenus() {
		JMenu exit = new JMenu("Afsluiten");

		exit.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				showConfirmDialog("Weet je zeker dat je het spel wilt afsluiten?");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JMenuBar bar = new JMenuBar();
		bar.add(exit);

		if (!(this.getContentView() instanceof Login || this.getContentView() == null)) {

			bar.add(logout);
		}

		if (this.viewHistory.size() > 1) {
			JMenu previousView = new JMenu("Terug");

			previousView.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					viewHistory.remove(viewHistory.size() - 1);
					BasePanel prevpanel = viewHistory.get(viewHistory.size() - 1);
					viewHistory.remove(viewHistory.size() - 1);
					
					setContentView(prevpanel);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});

			bar.add(previousView);
		}

		setJMenuBar(null);
		setJMenuBar(bar);
	}

	public void showConfirmDialog(String message) {
		String[] options = new String[2];

		// Place the no on the yes position yes[0], no [1]
		options[0] = new String("Nee");
		options[1] = new String("Ja");

		int reply = JOptionPane.showOptionDialog(this, message, "", 0,
				JOptionPane.ERROR_MESSAGE, null, options, null);

		// Because or yes is actually no we check for the NO_OPTION
		if (reply == JOptionPane.NO_OPTION) {
			System.exit(0); // temporary
		}
	}
	
	// Not private because it's called in LoginController
	public JMenu getLogoutButton() {
		return logout;
	}

	// Not private because it's called in LoginController
	public void clearViewHistory() {
		this.viewHistory.clear();
	}

}
