package helper;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

/**
 * 
 * @author Rick Arts
 * @version 1.2
 * @since 04 june 2015
 *
 */
@SuppressWarnings("serial")
public class CustomButton extends JButton {
	
	public static final Font font = new Font("Arial", Font.PLAIN, 12);		
	
	
	public CustomButton(String string, CustomColor foreground){
		super(string);
		this.setForeground(foreground);
		this.setFont(font);
		
		
	}
	
	public CustomButton(String string, CustomColor foreground, CustomColor background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
		this.setFont(font);
	}
	
	public CustomButton(String string, Color color) {
		super(string);
		this.setForeground(color);
		this.setFont(font);
	}
	
	public CustomButton(String string, Color foreground, CustomColor background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
		this.setFont(font);
	}
	
	public CustomButton(String string, Color foreground, Color background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
		this.setFont(font);
	}
	
	public CustomButton(String string, CustomColor foreground, Color background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
		this.setFont(font);
	}

	public void setForeground(Color color){
		super.setForeground(color);
	}
	
	public void setBackground(CustomColor color){
		super.setBackground(color);
		this.setOpaque(true);
		this.setBorderPainted(false);
	}

}
