package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.MainController;

@SuppressWarnings("serial")
public class EndGameView extends BasePanel {

	private JPanel labelPanel;
	private JPanel buttonPanel;
	private JLabel winOrLose;
	private JButton hoofdScherm;
	private static final String PHRASE_MAIN_PANEL_RETURN = "Terug naar hoofdscherm",
			PHRASE_YOU_WON = "Heuj, je hebt gewonnen!",
			PHRASE_YOU_LOST = "Helaas, je hebt verloren!";
	private MainController mainController;

	public EndGameView(MainController mainController, boolean won) {
		super(mainController);

		this.mainController = mainController;

		createPanels();
		createWinOrLoseLabel(won);
		createButton();
	}

	private void createPanels() {

		buttonPanel = new JPanel();
		labelPanel = new JPanel();

		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		labelPanel.setLayout(new GridBagLayout());

		buttonPanel.setOpaque(false);
		labelPanel.setOpaque(false);

		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		labelPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		this.add(labelPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void createWinOrLoseLabel(boolean won) {

		winOrLose = new JLabel();
		winOrLose.setForeground(Color.WHITE);
		winOrLose.setFont(new Font("Arial", Font.PLAIN, 40));
		if (won) {
			winOrLose.setText(PHRASE_YOU_WON);
		} else {
			winOrLose.setText(PHRASE_YOU_LOST);
		}
		winOrLose.setVisible(true);

		labelPanel.add(winOrLose);

	}

	private void createButton() {
		hoofdScherm = new CustomButton(PHRASE_MAIN_PANEL_RETURN, Color.WHITE,
				CustomColor.DARK_RED);
		buttonPanel.add(hoofdScherm);
		
		hoofdScherm.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				mainController.getMainFrame().setContentView(mainController.getMainPanel());
			}
			
		});

	}
}
