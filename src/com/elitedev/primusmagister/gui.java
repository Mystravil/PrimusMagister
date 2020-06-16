package com.elitedev.primusmagister;

// comment for christian because he cant merge
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	//gen
	private JLabel lblLastSrcWord;
	private JLabel lblLastTarWord;
	//
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
	private JButton btnResetSkill;
	private JList listLanguage;
	private JList listVocable;
	private JSpinner spinnerLanguageLeft;
	private JSpinner spinnerLanguageRight;
	private JProgressBar progressBar;
	private JSpinner spinnerLanguage;
	private JLabel lblHeaderSrcLanguage;
	private JLabel lblHeaderDestLanguage;

	private ComConfig _comConfig = new ComConfig();
	private String[] _languageArray = ComDatabase.getLanguages().toArray(new String[0]);


	private VocablePair _currentPair;


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
		
		btnConfirm = new JButton("Auswahl bestätigen");
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

					ComViewModel.setSourceLang(spinnerLanguageLeft.getValue().toString());
					ComViewModel.setTargetLang(spinnerLanguageRight.getValue().toString());

					// Progressbar calculation
					progressBar.setMaximum(ComDatabase.getPairList(ComViewModel.getSourceLang(), ComViewModel.getTargetLang()).size() * 4);
					progressBar.setMinimum(0);
					progressBar.setValue(ComDatabase.getTotalSkillValue(ComViewModel.getSourceLang(), ComViewModel.getTargetLang()));

					// new Voc
					_currentPair = ComDatabase.getPairRandomLowestSkill(ComViewModel.getSourceLang(), ComViewModel.getTargetLang());
					lblSrcWord.setText(_currentPair.voc1.name);
					lblSkill.setText("Skill: " + _currentPair.skill_value);
				}
			}
		});

		btnConfirm.setBounds(581, 294, 150, 32);
		contentPane.add(btnConfirm);
		btnConfirm.setVisible(false);

		//------------------------------------------------------------

		btnResetSkill = new JButton("Skill zurücksetzen");
		btnResetSkill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ComDatabase.resetSkillvalue(spinnerLanguageLeft.getValue().toString(), spinnerLanguageRight.getValue().toString());
			}
		});
		btnResetSkill.setBounds(175, 294, 150, 32);
		contentPane.add(btnResetSkill);
		btnResetSkill.setVisible(false);
		
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
			String[] values = _languageArray;
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
		
		//-------------------------------------------------------------------------------
		// fill JSpinner with our data and set its position
		//-------------------------------------------------------------------------------
		
		spinnerLanguageLeft = new JSpinner();
		spinnerLanguageLeft.setModel(new SpinnerListModel(_languageArray));
		spinnerLanguageLeft.setBounds(216, 121, 141, 32);
		spinnerLanguageLeft.setVisible(false);
		contentPane.add(spinnerLanguageLeft);
		
		//------------------------------------------------------------
		
		spinnerLanguageRight = new JSpinner();
		spinnerLanguageRight.setModel(new SpinnerListModel(_languageArray));
		spinnerLanguageRight.setBounds(547, 121, 141, 32);
		spinnerLanguageRight.setVisible(false);
		contentPane.add(spinnerLanguageRight);
		
		//------------------------------------------------------------
		
		spinnerLanguage = new JSpinner();
		spinnerLanguage.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				fullUpdateVocables();
			}
		});
		spinnerLanguage.setModel(new SpinnerListModel(_languageArray));
		spinnerLanguage.setBounds(387, 121, 141, 32);
		spinnerLanguage.setVisible(false);
		contentPane.add(spinnerLanguage);

		//------------------------------------------------------------

		listVocable = new JList();

		listVocable.setModel(new AbstractListModel() {
			String[] values =  {};
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
		
		lblSrcLanguage = new JLabel("");
		lblSrcLanguage.setHorizontalAlignment(SwingConstants.CENTER);
		lblSrcLanguage.setBounds(207, 193, 150, 32);
		lblSrcLanguage.setVisible(false);
		contentPane.add(lblSrcLanguage);
		
		//------------------------------------------------------------
		
		lblSrcWord = new JLabel("");
		lblSrcWord.setHorizontalAlignment(SwingConstants.CENTER);
		lblSrcWord.setBounds(207, 242, 150, 32);
		lblSrcWord.setVisible(false);
		contentPane.add(lblSrcWord);

		//------------------------------------------------------------

		lblLastSrcWord = new JLabel("");
		lblLastSrcWord.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastSrcWord.setBounds(207, 285, 150, 32);
		lblLastSrcWord.setVisible(false);
		contentPane.add(lblLastSrcWord);

		//------------------------------------------------------------

		lblLastTarWord = new JLabel("");
		lblLastTarWord.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastTarWord.setBounds(581, 285, 150, 20);
		lblLastTarWord.setVisible(false);
		contentPane.add(lblLastTarWord);
		
		//------------------------------------------------------------
		
		lblTranslateLanguage = new JLabel("");
		lblTranslateLanguage.setHorizontalAlignment(SwingConstants.CENTER);
		lblTranslateLanguage.setBounds(581, 189, 150, 41);
		lblTranslateLanguage.setVisible(false);
		contentPane.add(lblTranslateLanguage);	
		
		//------------------------------------------------------------
		
		lblHeaderText = new JLabel("Herzlich Willkommen zu Primus Magister by EliteDev");
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
		textFieldTranslateWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_submitEntry();
			}
		});
		textFieldTranslateWord.setBounds(581, 248, 150, 20);
		textFieldTranslateWord.setColumns(10);
		textFieldTranslateWord.setVisible(false);
		contentPane.add(textFieldTranslateWord);
	}

	private void _submitEntry() {
		if (textFieldTranslateWord.getText().equals(_currentPair.voc2.name)) {
			ComDatabase.updateSkillvalue(ComViewModel.getSourceLang(), ComViewModel.getTargetLang(), _currentPair.id, _currentPair.skill_value + 1);
			lblLastTarWord.setForeground(Color.green);
		}
		else {
			ComDatabase.updateSkillvalue(ComViewModel.getSourceLang(), ComViewModel.getTargetLang(), _currentPair.id, 0);
			lblLastTarWord.setForeground(Color.red);
		}

		// last voc
		lblLastSrcWord.setText(_currentPair.voc1.name);
		lblLastTarWord.setText(_currentPair.voc2.name);

		// Progressbar calculation
		progressBar.setMaximum(ComDatabase.getPairList(ComViewModel.getSourceLang(), ComViewModel.getTargetLang()).size() * 4);
		progressBar.setMinimum(0);
		progressBar.setValue(ComDatabase.getTotalSkillValue(ComViewModel.getSourceLang(), ComViewModel.getTargetLang()));

		// new Voc
		_currentPair = ComDatabase.getPairRandomLowestSkill(ComViewModel.getSourceLang(), ComViewModel.getTargetLang());
		lblSrcWord.setText(_currentPair.voc1.name);
		lblSkill.setText("Skill: " + _currentPair.skill_value);
		textFieldTranslateWord.setText("");
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
		lblLastSrcWord.setVisible(show);
		lblLastTarWord.setVisible(show);
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
		btnQuit.setText("Zurück");
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
		btnResetSkill.setVisible(show);
		btnQuit.setText("Zurück");
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
//		btnEdit.setVisible(show);
		listLanguage.setVisible(show);
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------
	// makes the vocable config elements visible/invisible
	// show = true ->  visible
	// show = true ->  invisible
	//------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showConfigVocable(boolean show){
		fullUpdateVocables();

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
		fullUpdateVocablePairs();

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
			if (JOptionPane.OK_OPTION == 0) {
				ArrayList<String> existingLang = ComDatabase.getLanguages();
				if (existingLang.contains(input.toLowerCase())) {
					JOptionPane.showMessageDialog(null, "Dieser Eintrag existiert bereits");
				}
				ComDatabase.createDictionaryTable(input.toLowerCase());
				_languageArray = ComDatabase.getLanguages().toArray(new String[0]);
				fullUpdateLanguages();
			}
        break;
		
		case "configVocable":
			input = JOptionPane.showInputDialog(null, "Geben Sie eine Vokabel ein", "Vokabeleingabe", JOptionPane.PLAIN_MESSAGE);
			if (JOptionPane.OK_OPTION == 0) {
				ComDatabase.addVocable(spinnerLanguage.getValue().toString(), input);
				fullUpdateVocables();
			}
        break;
		
		case "configConnectVocable":
			
		    if (!validateLanguage()) {
					//If language is invalid the function already shows an error message
			}
			else {
				// declaration of components needed by popup
				JSpinner srcWordSpinner = new JSpinner();
				String[] srcWordList = {"Baum", "Apfel", "Tee"};
				srcWordSpinner.setModel(new SpinnerListModel(srcWordList));
				
				JSpinner destWordSpinner = new JSpinner();
				String[] destWordList = {"Tree", "Apple", "Tea"};
				destWordSpinner.setModel(new SpinnerListModel(destWordList));

				// contentlist of popup dialog
				Object[] message = {"Quellwort", srcWordSpinner, "übersetzung", destWordSpinner};

				// popup call
		        JOptionPane pane = new JOptionPane( message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		        pane.createDialog(null, "Vokabelpaar hinzufügen").setVisible(true);
		        
		        // get values
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
				input = JOptionPane.showOptionDialog(null, "Möchten Sie die Sprache '" + selected + "' wirklich löschen?", "Sprache löschen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (input == 0) {
					ComDatabase.deleteDictionaryTable(selected);
					_languageArray = ComDatabase.getLanguages().toArray(new String[0]);
					fullUpdateLanguages();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Keine Sprache ausgewählt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			}
			break;
	
		case "configVocable":
			if (listVocable.getSelectedValue() != null) {
				selected = (String) listVocable.getSelectedValue();
				input = JOptionPane.showOptionDialog(null, "Möchten Sie die Vokabel '" +selected+"' wirklich löschen?", "Vokabel löschen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
				if (input == 0) {
					ComDatabase.deleteVocable(spinnerLanguage.getValue().toString(), selected);
				}
				fullUpdateVocables();
			}
			else {
				JOptionPane.showMessageDialog(null, "Keine Vokabel ausgewählt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			}
			break;
		
		case "configConnectVocable":
			if (listVocable.getSelectedValue() != null) {
				String voc = (String) listVocable.getSelectedValue();
				input = JOptionPane.showOptionDialog(null, "Möchten Sie die übersetzung '" +voc + "' wirklich löschen?", "übersetzung löschen", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
			}
			else {
				JOptionPane.showMessageDialog(null, "Keine gültige übersetzung ausgewählt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
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
					input = JOptionPane.showInputDialog(null, "Geben Sie ihre Änderung ein", "Sprache ändern", JOptionPane.PLAIN_MESSAGE);
//					if (input == 0) {
//						ComDatabase.deleteDictionaryTable(selected);
//						_languageArray = ComDatabase.getLanguages().toArray(new String[0]);
//						fullUpdateLanguages();
//					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Keine Sprache ausgewählt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
				}
				break;

			case "configVocable":
				if (listVocable.getSelectedValue() != null) {
					selected = (String) listVocable.getSelectedValue();
					input = JOptionPane.showInputDialog(null, "Geben Sie ihre Änderung ein", "Vokabel ändern", JOptionPane.PLAIN_MESSAGE);
						ComDatabase.updateVocable(spinnerLanguage.getValue().toString(), selected, input);
						fullUpdateVocables();
				}
				else {
					JOptionPane.showMessageDialog(null, "Keine Vokabel ausgewählt!", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
				}
				break;
				}
			}

	public void fullUpdateLanguages() {
		spinnerLanguageLeft.setModel(new SpinnerListModel(_languageArray));
		spinnerLanguageRight.setModel(new SpinnerListModel(_languageArray));
		spinnerLanguage.setModel(new SpinnerListModel(_languageArray));
		listLanguage.setModel(new AbstractListModel() {
			String[] values = _languageArray;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}

	public void fullUpdateVocables() {
		List<Vocable> _vocableArrayPart = ComDatabase.getVocableList(spinnerLanguage.getValue().toString());
		ArrayList<String> _vocableArrayList = new ArrayList<String>();

		for(Vocable voc : _vocableArrayPart) {
			_vocableArrayList.add(voc.name);
		}

		String[] _vocableArray = _vocableArrayList.toArray(new String[0]);

		listVocable.setModel(new AbstractListModel() {
			String[] values = _vocableArray;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}

	public void fullUpdateVocablePairs() {
//		List<VocablePair> _vocablePairArrayPart = ComDatabase.getPairList(spinnerLanguageLeft.getValue().toString(), spinnerLanguageRight.getValue().toString());
		List<VocablePair> _vocablePairArrayPart = ComDatabase.getPairList("german", "english");
		ArrayList<String> _vocablePairArrayList = new ArrayList<String>();

		for(VocablePair voc : _vocablePairArrayPart) {
			_vocablePairArrayList.add(voc.voc1.name + " - " + voc.voc2.name);
		}

		String[] _vocablePairArray = _vocablePairArrayList.toArray(new String[0]);

		listVocable.setModel(new AbstractListModel() {
			String[] values = _vocablePairArray;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
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
			lblHeaderText.setText("Sprache auswählen");
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
			lblHeaderText.setText("übersetzungen bearbeiten");
			break;
		}
	}
}
