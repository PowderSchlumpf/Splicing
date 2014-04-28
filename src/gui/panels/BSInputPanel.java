package gui.panels;

import gui.BSMainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.FeatureDescriptor;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;

import utils.BSUserSettings;
import analysis.BSSequenceLogoGenerator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class BSInputPanel extends JPanel implements ActionListener {
	
	private BSMainWindow parent;
	
	private JTextField gffInput;
	private JButton gffFileChooser;
	private JTextArea gffQueries;
	private JTextField fastaInput;
	private JButton fastaFileChooser;
	private JTextField destination;
	private JButton destFileChooser;
	
	private JCheckBox chbxFivePrime;
	private JCheckBox chbxThreePrime;
	
	private JSpinner fivePrimePosBefore;
	private JSpinner fivePrimeLogoLength;
	private JSpinner threePrimeLogoLength;
	private JSpinner threePrimePosAfter;
	
	private JButton submit;
	private final String cmd_submit = "cmd_submit";
	private final String cmd_chooseFile = "cmd_choose";
	public BSInputPanel(BSMainWindow parent) {
		this.parent = parent;
		initGui();
	}
	
	private void initGui() {
		FormLayout layout = new FormLayout("5dlu,right:pref,5dlu,left:pref,2dlu,left:pref,5dlu,right:pref,5dlu,left:pref:grow,5dlu",
				"3dlu,top:pref,3dlu,top:pref:grow,5dlu,pref,3dlu,pref,3dlu,pref,5dlu,pref,3dlu,pref,3dlu,pref,3dlu,pref,3dlu");
		setLayout(layout);
		CellConstraints cc = new CellConstraints();
		
		gffInput = new JTextField(10);
		gffFileChooser = new JButton("...");
		gffFileChooser.setName("gff");
		gffFileChooser.setActionCommand(cmd_chooseFile);
		gffFileChooser.addActionListener(this);
		gffQueries = new JTextArea(5, 10);
		gffQueries.setBorder(gffInput.getBorder());
		fastaInput = new JTextField(10);
		fastaFileChooser = new JButton("...");
		fastaFileChooser.setName("fasta");
		fastaFileChooser.setActionCommand(cmd_chooseFile);
		fastaFileChooser.addActionListener(this);
		
		chbxFivePrime = new JCheckBox("5' Analysis");
		chbxThreePrime = new JCheckBox("3' Analysis");
		
		fivePrimePosBefore = new JSpinner();
		fivePrimePosBefore.setValue(10);
		fivePrimeLogoLength = new JSpinner();
		fivePrimeLogoLength.setValue(10);
		threePrimeLogoLength = new JSpinner();
		threePrimeLogoLength.setValue(10);
		threePrimePosAfter = new JSpinner();
		threePrimePosAfter.setValue(10);
		
		destination = new JTextField(10);
		destFileChooser = new JButton("...");
		destFileChooser.setName("destination");
		destFileChooser.setActionCommand(cmd_chooseFile);
		destFileChooser.addActionListener(this);
		
		submit = new JButton("Submit and Read");
		submit.setActionCommand(cmd_submit);
		submit.addActionListener(this);
		
		
		add(new JLabel("GFF-File:"), cc.xy(2, 2));
		add(gffInput, cc.xy(4, 2));
		add(gffFileChooser, cc.xy(6, 2));
		add(new JLabel("GFF-Queries:"), cc.xy(8, 2));
		add(gffQueries, cc.xywh(10, 2, 1, 3));
		add(new JLabel("Fasta-File:"), cc.xy(2, 4));
		add(fastaInput, cc.xy(4, 4));
		add(fastaFileChooser, cc.xy(6, 4));
		add(new JSeparator(JSeparator.HORIZONTAL), cc.xyw(2, 6, 7));
		add(chbxFivePrime, cc.xy(2, 8));
		add(chbxThreePrime, cc.xy(6, 8));
		add(new JLabel("Positions before 5' SS:"), cc.xy(2, 10));
		add(fivePrimePosBefore, cc.xy(4, 10));
		add(new JLabel("5' SS Logo length:"), cc.xy(2, 12));
		add(fivePrimeLogoLength, cc.xy(4, 12));
		add(new JLabel("3' SS Logo length:"), cc.xy(6, 10));
		add(threePrimeLogoLength, cc.xy(8, 10));
		add(new JLabel("Positions after 3' SS:"), cc.xy(6, 12));
		add(threePrimePosAfter, cc.xy(8, 12));
		add(new JSeparator(JSeparator.HORIZONTAL), cc.xyw(2, 14, 7));
		add(new JLabel("Destination Folder:"), cc.xy(2, 16));
		add(destination, cc.xy(4, 16));
		add(destFileChooser, cc.xy(6, 16));
		add(submit, cc.xy(2, 18));
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(cmd_submit)) {
			if (storeInput()) {
				if (parent.getController().readInput(BSUserSettings.getGffPath(), null, BSUserSettings.getFastaPath(), null, BSUserSettings.getPositionsBeforeFivePrimeSS(), BSUserSettings.getLogoLengthFivePrime())) {
					System.out.println("reading successful");
				} else {
					System.err.println("something went wrong when reading");
				}
			}
		}
		else if (cmd.equals(cmd_chooseFile)) {
			BSFileChooser fc = new BSFileChooser(parent);
			
		}
	}
	
	private boolean storeInput() {
		// input paths
		if (gffInput.getText() != null && !gffInput.getText().equals("")) {
			BSUserSettings.setGffPath(gffInput.getText());
		} else {
			JOptionPane.showMessageDialog(this, "The gff-Path is empty!\nEnter a valid path to gff-file!", "Empty path", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (fastaInput.getText() != null && !fastaInput.getText().equals("")) {
			BSUserSettings.setFastaPath(fastaInput.getText());
		} else {
			JOptionPane.showMessageDialog(this, "The fasta-Path is empty!\nEnter a valid path to fasta-file!", "Empty path", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// gff-queries
		if (gffQueries.getText() != null && !gffQueries.getText().equals("")) {
			BSUserSettings.setGffQuery(gffQueries.getText());
		}
		
		// analysis mode
		if (chbxFivePrime.isSelected() && chbxThreePrime.isSelected()) {
			BSUserSettings.setAnalyzationType(BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME);
		} else if (chbxFivePrime.isSelected()) {
			BSUserSettings.setAnalyzationType(BSSequenceLogoGenerator.MODE_FIVE_PRIME);
		} else if (chbxThreePrime.isSelected()) {
			BSUserSettings.setAnalyzationType(BSSequenceLogoGenerator.MODE_THREE_PRIME);
		} else {
			JOptionPane.showMessageDialog(this, "No analysis mode selected!\nPlease choose an analysis mode!", "No mode selected", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// lengths
		if (BSUserSettings.getAnalyzationType() == BSSequenceLogoGenerator.MODE_FIVE_PRIME || BSUserSettings.getAnalyzationType() == BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME) {
			BSUserSettings.setPositionsBeforeFivePrimeSS((int) fivePrimePosBefore.getValue());
			BSUserSettings.setLogoLengthFivePrime((int) fivePrimeLogoLength.getValue());
		}
		if (BSUserSettings.getAnalyzationType() == BSSequenceLogoGenerator.MODE_THREE_PRIME || BSUserSettings.getAnalyzationType() == BSSequenceLogoGenerator.MODE_FIVE_AND_THREE_PRIME) {
			BSUserSettings.setLogoLengthThreePrime((int) threePrimeLogoLength.getValue());
			BSUserSettings.setPositionsAfterThreePrimeSS((int) threePrimePosAfter.getValue());
		}
		
		// outputpath
		if (destination.getText() != null && !destination.getText().equals("")) {
			BSUserSettings.setOutputpath(destination.getText());
		} else {
			BSUserSettings.setOutputpath(gffInput.getText().substring(0, gffInput.getText().lastIndexOf("/")));
		}
		return true;
	}
	
	private class BSFileChooser extends JDialog {
		private JFileChooser fc;
		public BSFileChooser(JFrame owner) {
			super(owner);
			init();
			pack();
			setVisible(true);
		}
		
		private void init() {
			fc = new JFileChooser();
			FormLayout fl = new FormLayout("pref", "pref");
			setLayout(fl);
			CellConstraints cc = new CellConstraints();
			add(fc, cc.xy(1, 1));
		}
	}

}
