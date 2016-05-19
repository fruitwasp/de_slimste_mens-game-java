package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controllers.MainController;

@SuppressWarnings("serial")
public class TutorialScreen extends BasePanel {
	private JTextArea rules, head;
	private JButton previous, next;
	private JPanel topPanel, bottomPanel, textPanel;
	private Font headFont, rulesFont;
	private int roundCount = 1;
	private static final String DRIEZESNEGENHEAD = "Uitleg ronde 1: 3-6-9",
			OPENDEURHEAD = "Uitleg ronde 2: open deur",
			PUZZELHEAD = "Uitleg ronde 3: puzzel",
			INGELIJSTHEAD = "Uitleg ronde 4: ingelijst",
			FINALEHEAD = "Uitleg ronde 5: finale";
	private static final String DRIEZESNEGENRULES = "In deze eerste ronde speel je 9 vragen. Elk 3e goede anwoord levert 20 seconden op. Heb je een antwoord fout dan gaat de beurt naar de \ntegenstander en mag die verder spelen. Aan \nhet einde van deze ronde krijg je 60 extra \nseconden als premie voor de rest van het spel.",
			OPENDEURRULES = "In deze ronde krijg je een vraag voorgelegd \nwaar we vier kenwoorden bij zoeken. Elk goed antwoord levert je 40 seconden op. Als je past gaat de beurt naar de tegenstander en mag diejouw vraag afmaken. Maar let op de tijd want \nde klok tikt af terwijl jij nadenkt!",
			PUZZELRULES = "In deze ronde zoeken we 3 verbanden die elk worden omschreven met 4 trefwoorden. Het is \neen puzzel dus hebben we de woorden door \nelkaar gezet. Bij elk goed antwoord win je 60 \nseconden. Als je past gat de beurt naar de \ntegenstander. Denk niet te lang na want dan \nverlies je kostbare tijd.",
			INGELIJSTRULES = "In deze ronde krijg je één vraag. Je \ntegenstander speelt dezelfde vraag. We \nzoeken een lijst met 10 goede antwoorden. \nVoor elk goed antwoord krijg je 20 \nbonusseconden. Als je hier past is de ronde \nvoorbij. Let ook hier op je tijd: die tikt weg \nterwijl je nadenkt!",
			FINALERULES = "Speel je tegenstander naar de 0 seconden. \nHoe? Je krijgt steeds 1 vraag waar we 5 \nantwoorden bij zoeken. Voor elk goed \nantwoord gaan er 30 seconden af bij de \ntegenstander. De speler die achterstaat start \ntelkens de nieuwe vraag. Maar als je past mag de tegenstander jouw vraag afmaken! Na 10 \nvragen wint de speler met de meeste \nseconden.";

	public static final Dimension BUTTON_SIZE = new Dimension(150, 40),
			TEXT_AREA_SIZE = new Dimension(300, 200);

	public TutorialScreen(MainController mainController) {
		super(mainController);

		this.setLayout(new BorderLayout());

		previous = new CustomButton("vorige ronde", Color.WHITE, CustomColor.DARK_RED);
		previous.setPreferredSize(BUTTON_SIZE);
		previous.setVisible(false);
		previous.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (roundCount > 1) {
					roundCount--;
					fillAreas();
				}
			}
		});

		next = new CustomButton("volgende ronde", Color.WHITE, CustomColor.DARK_RED);
		next.setPreferredSize(BUTTON_SIZE);
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (roundCount < 5) {
					roundCount++;
					fillAreas();
				}
			}

		});

		createLabels();

		textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BorderLayout());
		textPanel.add(head, BorderLayout.NORTH);
		textPanel.add(rules, BorderLayout.CENTER);

		topPanel = new JPanel();
		topPanel.setOpaque(false);
		topPanel.add(textPanel);

		bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.add(previous);
		bottomPanel.add(next);

		this.add(topPanel, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	private void createLabels() {
		headFont = new Font("Arial", Font.BOLD, 16);
		rulesFont = new Font("Arial", Font.ITALIC, 14);

		head = new JTextArea(DRIEZESNEGENHEAD);
		head.setOpaque(false);
		head.setForeground(Color.WHITE);
		head.setFont(headFont);
		head.setEditable(false);

		rules = new JTextArea(DRIEZESNEGENRULES);
		rules.setPreferredSize(TEXT_AREA_SIZE);
		rules.setOpaque(false);
		rules.setForeground(Color.WHITE);
		rules.setFont(rulesFont);
		rules.setEditable(false);
		rules.setLineWrap(true);
	}

	private void fillAreas() {
		switch (roundCount) {
		case 1:
			head.setText(DRIEZESNEGENHEAD);
			rules.setText(DRIEZESNEGENRULES);
			previous.setVisible(false);
		case 2:
			head.setText(OPENDEURHEAD);
			rules.setText(OPENDEURRULES);
			previous.setVisible(true);
		case 3:
			head.setText(PUZZELHEAD);
			rules.setText(PUZZELRULES);
		case 4:
			head.setText(INGELIJSTHEAD);
			rules.setText(INGELIJSTRULES);
			next.setVisible(true);
		default:
			head.setText(FINALEHEAD);
			rules.setText(FINALERULES);
			next.setVisible(false);
		}
		
	}

}
