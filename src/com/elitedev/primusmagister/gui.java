package com.elitedev.primusmagister;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerListModel;
import javax.swing.JTable;

public class gui extends JFrame {

	//global variables
	String srcLanguage_ = "";
	String destLanguage_ = "";
	String menu_ = "start";
	
	//window
	private JPanel contentPane; 
	
	//GUI elements which need to be global, so we gain access from functions/ earlier declared GUI-elements
	private JLabel lblSrcLanguage;
	private JLabel lblSrcWord;
	private JLabel lblTranslateLanguage;
	private JLabel lblHeaderText;
	private JLabel lblSkill;
	private JTextField textFieldTranslateWord;
	private JButton btnConfirm;
	private JButton btnConfig;
	private JButton btnLearn;
	private JButton btnQuit;
	private JButton btnLanguage;
	private JButton btnVocable;
	private JButton btnConnectVocable;
	private JButton btnAdd;
	private JButton btnDel;
	private JButton btnEdit;
	private JList listLanguage;
	private JList listVocable;
	private JSpinner spinnerLanguageLeft;
	private JSpinner spinnerLanguageRight;
	private JProgressBar progressBar;
	private JSpinner spinnerLanguage;
	private JLabel lblHeaderSrcLanguage;
	private JLabel lblHeaderDestLanguage;
	private JTable table;

	private ComConfig _comConfig = new ComConfig();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui frame = new gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public gui() {
		
		//languagelist for spinner
		String[] languageArray = ComDatabase.getLanguages().toArray(new String[0]);
//		String[] languageArray = { "Test", "Test2"};
//		List<String> test = ComDatabase.getLanguages();
//		Vocable voc = ComDatabase.getVocable("german", 1);

		//-------------------------------------------------------------------------------
		// configuration of the gui itself
		//-------------------------------------------------------------------------------
		
		setResizable(false);
		setTitle("Primus Magister");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 950, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);		
		contentPane.setLayout(null);
		
		//-------------------------------------------------------------------------------
		// fill JButtons with our data and set its position add functionality
		//-------------------------------------------------------------------------------
		
		btnConfirm = new JButton("Auswahl best\u00E4tigen");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check if its the starting window	
				if (validateLanguage()) {
					//Update visibility of labels/textfields
					menu_ = "learn";
					showLearningWindow(true);
					showLearnMenu(false);
					updateLanguageLabelTexts();	
					changeHeaderText();
					// TODO main programm 
					// TODO fill label srcWord
					ComViewModel.setSourceVoc(spinnerLanguageLeft.getValue().toString());
					ComViewModel.setSourceVoc(spinnerLanguageRight.getValue().toString());

				}
			}
		});
		btnConfirm.setBounds(581, 294, 150, 32);
		contentPane.add(btnConfirm);
		btnConfirm.setVisible(false);
		
		//------------------------------------------------------------
		
		btnConfig = new JButton("Konfiguration");
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu_ = "config";
				showConfigMenu(true);
				showMainMenu(false);
				changeHeaderText();
			}
		});
		btnConfig.setBounds(302, 208, 330, 46);
		contentPane.add(btnConfig);
		
		//------------------------------------------------------------
		
		btnLearn = new JButton("Lernen");
		btnLearn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu_ = "learnMenu";
				showLearnMenu(true);
				showMainMenu(false);
				changeHeaderText();
				// TODO fill language spinner
			}
		});
		btnLearn.setBounds(302, 136, 330, 46);
		contentPane.add(btnLearn);
		
		//------------------------------------------------------------
		
		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oneStepBack();
			}
		});
		btnQuit.setBounds(780, 415, 89, 23);
		contentPane.add(btnQuit);
		
		//------------------------------------------------------------
		
		btnLanguage = new JButton("Sprachen");
		btnLanguage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu_ = "configLanguage";
				showConfigLanguage(true);
				showConfigMenu(false);
				changeHeaderText();
				//TODO fill language list
			}
		});
		btnLanguage.setBounds(302, 136, 330, 46);
		btnLanguage.setVisible(false);
		contentPane.add(btnLanguage);
		
		//------------------------------------------------------------
		
		btnVocable = new JButton("Vokabeln");
		btnVocable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// changes the gui to the configuration dialog of the vocables
				menu_ = "configVocable";
				showConfigVocable(true);
				showConfigMenu(false);
				changeHeaderText();
				_comConfig.constructVocables(spinnerLanguage.getValue().toString());
			}
		});
		btnVocable.setBounds(302, 208, 330, 46);
		contentPane.add(btnVocable);
		btnVocable.setVisible(false);
		
		//------------------------------------------------------------
		
		btnConnectVocable = new JButton("Vokabelpaare");
		btnConnectVocable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// changes the gui to the configuration dialog of the vocablepairs
				menu_ = "configConnectVocable";
				showConfigConnectVocable(true);
				showConfigMenu(false);
				changeHeaderText();
			}
		});
		btnConnectVocable.setBounds(302, 279, 330, 46);
		btnConnectVocable.setVisible(false);
		contentPane.add(btnConnectVocable);
		
		//------------------------------------------------------------
		
		btnAdd = new JButton("+");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addfn();
			}
		});
		btnAdd.setBounds(350, 360, 60, 30);
		btnAdd.setVisible(false);
		contentPane.add(btnAdd);
		
		//------------------------------------------------------------
		
		btnDel = new JButton("-");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		btnDel.setBounds(420, 360, 60, 30);
		btnDel.setVisible(false);
		contentPane.add(btnDel);
		
		//------------------------------------------------------------
		
		btnEdit = new JButton("edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit();
			}
		});
		btnEdit.setBounds(490, 360, 120, 30);
		btnEdit.setVisible(false);
		contentPane.add(btnEdit);
		
		//-------------------------------------------------------------------------------
		// fill JList with our data and set its position
		//-------------------------------------------------------------------------------
		
		listLanguage = new JList();
		listLanguage.setModel(new AbstractListModel() {
			String[] values = languageArray;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listLanguage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listLanguage.setBounds(354, 156, 225, 170);
		listLanguage.setVisible(false);
		contentPane.add(listLanguage);
		
		//------------------------------------------------------------
		
		listVocable = new JList();
		listVocable.setModel(new AbstractListModel() {
			String[] values = new String[] {"a", "b", "c"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listVocable.setBounds(272, 156, 360, 193);
		listVocable.setVisible(false);
		contentPane.add(listVocable);
		
		//-------------------------------------------------------------------------------
		// fill JSpinner with our data and set its position
		//-------------------------------------------------------------------------------
		
		spinnerLanguageLeft = new JSpinner();
		spinnerLanguageLeft.setModel(new SpinnerListModel(languageArray));
		spinnerLanguageLeft.setBounds(216, 121, 141, 32);
		spinnerLanguageLeft.setVisible(false);
		contentPane.add(spinnerLanguageLeft);
		
		//------------------------------------------------------------
		
		spinnerLanguageRight = new JSpinner();
		spinnerLanguageRight.setModel(new SpinnerListModel(languageArray));
		spinnerLanguageRight.setBounds(547, 121, 141, 32);
		spinnerLanguageRight.setVisible(false);
		contentPane.add(spinnerLanguageRight);
		
		//------------------------------------------------------------
		
		spinnerLanguage = new JSpinner();
		spinnerLanguage.setModel(new SpinnerListModel(languageArray));
		spinnerLanguage.setBounds(387, 121, 141, 32);
		spinnerLanguage.setVisible(false);
		contentPane.add(spinnerLanguage);
		
		//-------------------------------------------------------------------------------
		// fill JLabels with our data and set its position
		//-------------------------------------------------------------------------------
		
		lblHeaderSrcLanguage = new JLabel("Quellsprache");
		lblHeaderSrcLanguage.setBounds(216, 96, 141, 14);
		lblHeaderSrcLanguage.setVisible(false);
		contentPane.add(lblHeaderSrcLanguage);
		
		//------------------------------------------------------------
		
		lblHeaderDestLanguage = new JLabel("Zielsprache");
		lblHeaderDestLanguage.setBounds(547, 96, 141, 14);
		lblHeaderDestLanguage.setVisible(false);
		contentPane.add(lblHeaderDestLanguage);
		
		//------------------------------------------------------------
		
		lblSkill = new JLabel("Skill:");
		lblSkill.setBounds(443, 121, 47, 20);
		lblSkill.setVisible(false);
		contentPane.add(lblSkill);	
		
		//------------------------------------------------------------
		
		lblSrcLanguage = new JLabel("New label");
		lblSrcLanguage.setHorizontalAlignment(SwingConstants.CENTER);
		lblSrcLanguage.setBounds(207, 193, 150, 32);
		lblSrcLanguage.setVisible(false);
		contentPane.add(lblSrcLanguage);
		
		//------------------------------------------------------------
		
		lblSrcWord = new JLabel("New label");
		lblSrcWord.setHorizontalAlignment(SwingConstants.CENTER);
		lblSrcWord.setBounds(207, 242, 150, 32);
		lblSrcWord.setVisible(false);
		contentPane.add(lblSrcWord);
		
		//------------------------------------------------------------
		
		lblTranslateLanguage = new JLabel("New label");
		lblTranslateLanguage.setHorizontalAlignment(SwingConstants.CENTER);
		lblTranslateLanguage.setBounds(581, 189, 150, 41);
		lblTranslateLanguage.setVisible(false);
		contentPane.add(lblTranslateLanguage);	
		
		//------------------------------------------------------------
		
		lblHeaderText = new JLabel("Herzlich Willkomen zu Primus Magister by EliteDev");
		lblHeaderText.setFont(new Font("Palatino Linotype", Font.ITALIC, 23));
		lblHeaderText.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeaderText.setBounds(62, 33, 810, 77);
		contentPane.add(lblHeaderText);
		
		//-------------------------------------------------------------------------------
		// fill JProgressbar with our data and set its position
		//-------------------------------------------------------------------------------
		
		progressBar = new JProgressBar();
		progressBar.setBounds(62, 360, 810, 26);
		contentPane.add(progressBar);
		progressBar.setVisible(false);
		
		//-------------------------------------------------------------------------------
		// fill JTextfield with our data and set its position
		//-------------------------------------------------------------------------------

		textFieldTranslateWord = new JTextField();
		textFieldTranslateWord.setBounds(581, 248, 150, 20);
		textFieldTranslateWord.setColumns(10);
		textFieldTranslateWord.setVisible(false);
		contentPane.add(textFieldTranslateWord);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBounds(62, 130, 120, 124);
		contentPane.add(table);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	//
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	public boolean validateLanguage(){
		//check if something is selected
		if (spinnerLanguageLeft.getValue() != null &&
			spinnerLanguageRight.getValue() != null) {
			srcLanguage_ = (String) spinnerLanguageLeft.getValue();
			destLanguage_ = (String) spinnerLanguageRight.getValue();
		}
		//check if value is valid
		if ((srcLanguage_.isEmpty() || srcLanguage_ == null) ||
			(destLanguage_.isEmpty() || destLanguage_ == null)) {
			JOptionPane.showMessageDialog(null,"Es wurde keine Auswahl getroffen", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			return false;		
		}
		else if(srcLanguage_ == destLanguage_){
			JOptionPane.showMessageDialog(null,"Quell- und Zielsprache sind identisch!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
		
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the learn window visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showLearningWindow(boolean show){
		
		lblSrcLanguage.setVisible(show);
		lblSrcWord.setVisible(show);
		lblTranslateLanguage.setVisible(show);
		textFieldTranslateWord.setVisible(show);
		progressBar.setVisible(show);
		lblSkill.setVisible(show);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	//
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void updateLanguageLabelTexts() {
		lblSrcLanguage.setText(srcLanguage_);
		lblTranslateLanguage.setText(destLanguage_);
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes main menu elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showMainMenu(boolean show){
		btnLearn.setVisible(show);
		btnConfig.setVisible(show);
		if(show) {
			btnQuit.setText("Quit");
		}	
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the config menu elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showConfigMenu(boolean show){
		btnLanguage.setVisible(show);
		btnVocable.setVisible(show);
		btnConnectVocable.setVisible(show);
		btnQuit.setText("Zur�ck");
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the learn menu elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showLearnMenu(boolean show){
		spinnerLanguageLeft.setVisible(show);
		spinnerLanguageRight.setVisible(show);
		btnConfirm.setVisible(show);
		btnQuit.setText("Zur�ck");
		lblHeaderSrcLanguage.setVisible(show);
		lblHeaderDestLanguage.setVisible(show);
		
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the language config elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showConfigLanguage(boolean show){
		btnAdd.setVisible(show);
		btnDel.setVisible(show);
		btnEdit.setVisible(show);
		listLanguage.setVisible(show);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the vocable config elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showConfigVocable(boolean show){
		btnAdd.setVisible(show);
		btnDel.setVisible(show);
		btnEdit.setVisible(show);
		spinnerLanguage.setVisible(show);
		listVocable.setVisible(show);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the vocable pair config elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showConfigConnectVocable(boolean show){
		spinnerLanguageRight.setVisible(show);
		spinnerLanguageLeft.setVisible(show);
		btnAdd.setVisible(show);
		btnDel.setVisible(show);
		listVocable.setVisible(show);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// handles the '+' Button of every confiq menu
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void addfn(){
		String input;
		Object[] options = {"Ja", "Nein"};
		
		switch (menu_) {
		case "configLanguage":
			input = JOptionPane.showInputDialog(null, "Geben Sie eine Sprache ein", "Spracheingabe", JOptionPane.PLAIN_MESSAGE);
        break;
		
		case "configVocable":
			input = JOptionPane.showInputDialog(null, "Geben Sie eine Vokabel ein", "Vokabeleingabe", JOptionPane.PLAIN_MESSAGE);
        break;
		
		case "configConnectVocable":
			
		    if (!validateLanguage()) {
					//If language is invalid the function already shows an error message
			}
			else {
				// declaration of components needed by popup
				// TODO Validierung
				JSpinner srcWordSpinner = new JSpinner();
				//TODO Vokabeln aus der DB holen
				String[] srcWordList = {"Baum", "Apfel", "Tee"};
				srcWordSpinner.setModel(new SpinnerListModel(srcWordList));
				
				JSpinner destWordSpinner = new JSpinner();
				//TODO Vokabeln aus der DB holen
				String[] destWordList = {"Tree", "Apple", "Tea"};
				destWordSpinner.setModel(new SpinnerListModel(destWordList));

				// contentlist of popup dialog
				Object[] message = {"Quellwort", srcWordSpinner, "�bersetzung", destWordSpinner};

				// popup call
		        JOptionPane pane = new JOptionPane( message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		        pane.createDialog(null, "Vokabelpaar hinzuf�gen").setVisible(true);
		        
		        // get values
		        // TODO validate and write into DB
		        String srcWord = srcWordSpinner.getValue().toString();
		        String destWord = destWordSpinner.getValue().toString();
			}
        break;
        
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// handles the '-' Button of every confiq menu
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void delete(){
		int input;
		String selected;
		Object[] options = {"Ja", "Nein"};
		
		switch (menu_) {
		case "configLanguage":
			if (listLanguage.getSelectedValue() != null) {
				selected = (String) listLanguage.getSelectedValue();
				input = JOptionPane.showOptionDialog(null, "M�chten Sie die Sprache '" + selected +"' wirklich l�schen?", "Sprache l�schen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
			}
			else {
				JOptionPane.showMessageDialog(null, "Keine Sprache ausgew�hlt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			}
			break;
	
		case "configVocable":
			if (listVocable.getSelectedValue() != null) {
				selected = (String) listVocable.getSelectedValue();
				input = JOptionPane.showOptionDialog(null, "M�chten Sie die Vokabel '" +selected+"' wirklich l�schen?", "Vokabel l�schen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
			}
			else {
				JOptionPane.showMessageDialog(null, "Keine Vokabel ausgew�hlt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			}
			break;
		
		case "configConnectVocable":
			if (listVocable.getSelectedValue() != null) {
				String voc = (String) listVocable.getSelectedValue();
				input = JOptionPane.showOptionDialog(null, "M�chten Sie die �bersetzung '" +voc + "' wirklich l�schen?", "�bersetzung l�schen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
				// TODO validieren, dass eingabe eine existierende �bersetzung ist
			}
			else {
				JOptionPane.showMessageDialog(null, "Keine g�ltige �bersetzung ausgew�hlt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			}
			break;
        
		}	
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// handles the 'edit' Button of every confiq menu
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void edit(){
		String input;
		String selected;
		
		switch (menu_) {
			case "configLanguage":
				if (listLanguage.getSelectedValue() != null) {
					selected = (String) listLanguage.getSelectedValue();
					input = JOptionPane.showInputDialog(null, "Geben Sie ihre �nderung ein", "Sprache �ndern", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Keine Sprache ausgew�hlt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
				}
				break;
		
			case "configVocable":
				if (listVocable.getSelectedValue() != null) {
					selected = (String) listVocable.getSelectedValue();
					input = JOptionPane.showInputDialog(null, "Geben Sie ihre �nderung ein", "Vokabel �ndern", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Keine Vokabel ausgew�hlt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}	
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// handles the window update functions which should be called at which point
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void oneStepBack(){
		switch(menu_) {
		case "start":
			System.exit(0);
			break;
			
		case "config":
			showConfigMenu(false);
			showMainMenu(true);
			menu_ = "start";
			changeHeaderText();
			break;
			
		case "learnMenu":
			showLearnMenu(false);
			showMainMenu(true);
			menu_ = "start";
			changeHeaderText();
			break;
			
		case "learn":
			showLearningWindow(false);
			showLearnMenu(true);
			menu_ = "learnMenu";
			changeHeaderText();
			break;
			
		case "configLanguage":
			showConfigLanguage(false);
			showConfigMenu(true);	
			menu_ = "config";
			changeHeaderText();
			break;
			
		case "configVocable":
			showConfigVocable(false);
			showConfigMenu(true);	
			menu_ = "config";
			changeHeaderText();
			break;
			
		case "configConnectVocable":
			showConfigConnectVocable(false);
			showConfigMenu(true);	
			menu_ = "config";
			changeHeaderText();
			break;
			
		}	
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	//  updates header, depending on the current window
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void changeHeaderText(){
		switch(menu_) {
		case "start":
			lblHeaderText.setText("Herzlich Willkomen zu Primus Magister by EliteDev");
			break;
			
		case "config":
			lblHeaderText.setText("Konfiguration");
			break;
			
		case "learnMenu":
			lblHeaderText.setText("Sprache ausw�hlen");
			break;
			
		case "learn":
			lblHeaderText.setText("Primus Magister");
			break;
			
		case "configLanguage":
			lblHeaderText.setText("Sprachen bearbeiten");
			break;
			
		case "configVocable":
			lblHeaderText.setText("Vokabeln bearbeiten");
			break;
			
		case "configConnectVocable":
			lblHeaderText.setText("�bersetzungen bearbeiten");
			break;
		}
	}
}
