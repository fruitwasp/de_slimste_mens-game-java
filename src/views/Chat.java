package views;

import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import models.ChatMessage;
import models.Game;
import observer.ChatObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class Chat extends BasePanel implements ChatObserver, KeyListener {
	private Game game;
	
	private JPanel messagesPanel, inputPanel;
	private JTextField messageInput;
	private JButton messageSendButton;
	private JTextArea chatArea;
	private int borderSize = 20;
	
	private final static int INPUT_PANEL_WIDTH = 0, INPUT_PANEL_HEIGHT = 40;
	
	public Chat(MainController mainController) {		
		super(mainController);
		
		createView();
		
		mainController.getGameController().addChatObserver(this);
	}
	
	private void createView() {		
		messageSendButton = new JButton(" > ");
		messageSendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = messageInput.getText();
				
				mainController.getGameController().sendChatMessage(game, message);
			}
		});
		
		messageInput = new JTextField();
		messageInput.addKeyListener(this);
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setPreferredSize(new Dimension(INPUT_PANEL_WIDTH, INPUT_PANEL_HEIGHT));
		inputPanel.setBackground(CustomColor.DARK_RED);		
		inputPanel.add(messageInput, BorderLayout.CENTER);
		inputPanel.add(messageSendButton, BorderLayout.LINE_END);
		
		add(inputPanel, BorderLayout.PAGE_END);
		
		messagesPanel = new JPanel();
		messagesPanel.setLayout(new BorderLayout());
		messagesPanel.setOpaque(false);
		add(messagesPanel, BorderLayout.CENTER);
		
		this.createChatPanel();
		
		
	}
	
	private void createChatPanel() {
		chatArea = new JTextArea();
		chatArea.setBackground(CustomColor.RED);
		chatArea.setEditable(false);
		chatArea.setFont(new Font("Arial", Font.PLAIN, 12));
		chatArea.setForeground(Color.WHITE);
		chatArea.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
		
		JScrollPane chatScroller = new JScrollPane(chatArea);
		chatScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		messagesPanel.add(chatScroller, BorderLayout.CENTER);
	}

	@Override
	public void onChatMessageAdded(String message, String sender, Game game) {
		String oldText = chatArea.getText();
		String newMessage = (sender + ": " + message + "\n");
		String newText = (oldText + newMessage);
		
		chatArea.setText(newText);
		messageInput.setText("");
		
	}

	@Override
	public void onChatMessagesLoaded(ArrayList<ChatMessage> chatMessages) {	
		String chatLog = "";
		
		for (ChatMessage chatMessage : chatMessages) {			
			chatLog += chatMessage.getSender() + ": " + chatMessage.getMessage() + "\n";
		}
		
		chatArea.setText(chatLog);
	}

	@Override
	public void onChatView(Game game) {
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			messageSendButton.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
