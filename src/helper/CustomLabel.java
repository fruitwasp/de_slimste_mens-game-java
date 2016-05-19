package helper;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * 
 * @author Rick Arts
 * @version 1.0
 * @since 25 april 2015
 *
 */
@SuppressWarnings("serial")
public class CustomLabel extends JLabel {
	
	
	public CustomLabel(String string, CustomColor foreground){
		super(string);
		this.setForeground(foreground);
		
	}
	
	public CustomLabel(String string, CustomColor foreground, CustomColor background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
	}
	
	public CustomLabel(String string, Color color) {
		super(string);
		this.setForeground(color);
	}
	
	public CustomLabel(String string, Color foreground, CustomColor background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
	}
	
	public CustomLabel(String string, Color foreground, Color background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
	}
	
	public CustomLabel(String string, CustomColor foreground, Color background){
		super(string);
		this.setForeground(foreground);
		this.setBackground(background);
	}

	public void setForeground(Color color){
		super.setForeground(color);
	}
	
	public void setBackground(CustomColor color){
		super.setBackground(color);
		this.setOpaque(true);
	}

}
