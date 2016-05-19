package views;

import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controllers.MainController;

@SuppressWarnings("serial")
public class BasePanel extends JPanel {
	protected MainController mainController;
	protected Font font;

	/**
	 * The standard panel
	 * 
	 * @param mainController
	 */
	public BasePanel(MainController mainController) {
		this.mainController = mainController;
		this.setFocusable(true);

		setLayout(new BorderLayout());
		setBackground(CustomColor.RED);

		font = new Font("Arial", Font.ITALIC, 12);
		setFont(font);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	protected void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "",
				JOptionPane.INFORMATION_MESSAGE);
	}

	protected MainController getMainController() {
		return mainController;
	}

	public Font getFont() {
		return font;
	}
}
