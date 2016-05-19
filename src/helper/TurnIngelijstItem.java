package helper;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class TurnIngelijstItem extends JLabel {

	public TurnIngelijstItem(String text) {
		super(text);
		
		setFont(new Font("Arial", Font.PLAIN, 14));
		setBorder(new EmptyBorder(10, 60, 10, 60));
		setForeground(CustomColor.white);
	}

}
