package gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import utils.BSUserSettings;
import analysis.BSSequenceLogoGenerator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import execute.BSController;
import gui.BSMainWindow;
import gui.analysisdialogs.BSSpliceSiteSequenceDialog;

public class BSToolbar extends JMenuBar implements ActionListener {

	private BSController controller;
	private BSMainWindow parent;
	
	private final String cmdLogos = "cmd_logos";
	private final String cmdIntronLength = "cmd_intronlength";
	private final String cmdPhases = "cmd_phases";
	private final String cmdSSSequences = "cmd_ssSequences";
	private final String cmdTranscPerGene = "cmd_TranscPerGene";
	private final String cmdIntronsPerGene = "cmd_IntronsPerGene";
	
	private JMenu analysing;
	private JMenuItem analyselogos;
	private JMenuItem analyseIntronLengths;
	private JMenuItem analysePhases;
	private JMenuItem analyseSpliceSiteSequences;
	private JMenuItem analyseTranscriptsPerGene;
	private JMenuItem analyseIntronsPerGene;
	
	public BSToolbar(BSMainWindow parent) {
		this.controller = parent.getController();
		this.parent = parent;
		initGui();
	}
	
	private void initGui() {
		FormLayout layout = new FormLayout("3dlu,pref,3dlu,pref,3dlu", "3dlu,pref,3dlu");
		CellConstraints cc = new CellConstraints();
		setLayout(layout);
		analysing = new JMenu("Analysing...");
		analyselogos = new JMenuItem("SpliceSite Logos");
		analyselogos.setActionCommand(cmdLogos);
		analyselogos.addActionListener(this);
		analyseIntronLengths = new JMenuItem("Intron lengths");
		analyseIntronLengths.setActionCommand(cmdIntronLength);
		analyseIntronLengths.addActionListener(this);
		analysePhases = new JMenuItem("Phases");
		analysePhases.setActionCommand(cmdPhases);
		analysePhases.addActionListener(this);
		analyseSpliceSiteSequences = new JMenuItem("Splicesite Sequences");
		analyseSpliceSiteSequences.setActionCommand(cmdSSSequences);
		analyseSpliceSiteSequences.addActionListener(this);
		analyseTranscriptsPerGene = new JMenuItem("Transcripts per Gene");
		analyseTranscriptsPerGene.setActionCommand(cmdTranscPerGene);
		analyseTranscriptsPerGene.addActionListener(this);
		analyseIntronsPerGene = new JMenuItem("Introns per Gene");
		analyseIntronsPerGene.setActionCommand(cmdIntronsPerGene);
		analyseIntronsPerGene.addActionListener(this);
		
		analysing.add(analyselogos);
		analysing.add(analyseIntronLengths);
		analysing.add(analysePhases);
		analysing.add(analyseSpliceSiteSequences);
		analysing.add(analyseTranscriptsPerGene);
		analysing.add(analyseIntronsPerGene);
		add(analysing, cc.xy(2, 2));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals(cmdLogos)) {
			controller.analyseLogos(BSUserSettings.getAnalyzationType(), BSUserSettings.getLogoLengthFivePrime(), BSUserSettings.getOutputpath()+"rawlogo", BSUserSettings.getOutputpath()+"perclogo", false);
		} else if (cmd.equals(cmdIntronLength)) {
			controller.analyseIntronLengths(BSUserSettings.getOutputpath()+"intronLengths");
		} else if(cmd.equals(cmdPhases)) {
			controller.analysePhase();
		} else if (cmd.equals(cmdSSSequences)) {
			BSSpliceSiteSequenceDialog dialog = new BSSpliceSiteSequenceDialog(parent);
			if (!dialog.wasCanceled()) {
				if (BSUserSettings.getAnalysisSequenceSpliceSite() == BSSequenceLogoGenerator.MODE_FIVE_PRIME) {
					controller.analyseClusteredSpliceSitesFivePrimeSpecializedPositions(BSUserSettings.getSpliceSitePositionList(), 
							BSUserSettings.getPositionsBeforeFivePrimeSS(), BSUserSettings.getLogoLengthFivePrime(), BSUserSettings.getOutputpath());
				} else {
					controller.analyseClusteredSpliceSitesThreePrimeSpecializedPositions(BSUserSettings.getSpliceSitePositionList(), 
							BSUserSettings.getLogoLengthThreePrime(), BSUserSettings.getPositionsAfterThreePrimeSS(), BSUserSettings.getOutputpath());
				}
			}
		} else if (cmd.equals(cmdTranscPerGene)) {
			controller.analyseTranscriptsPerGene();
		} else if (cmd.equals(cmdIntronsPerGene)) {
			controller.analyseIntronsPerGene();
		}
	}
}
