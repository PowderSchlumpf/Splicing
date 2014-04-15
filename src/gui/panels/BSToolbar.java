package gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import utils.BSUserSettings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import execute.BSController;

public class BSToolbar extends JMenuBar implements ActionListener {

	private BSController controller;
	
	private final String cmdLogos = "cmd_logos";
	private final String cmdIntronLength = "cmd_intronlength";
	
	private JMenu analysing;
	private JMenuItem analyselogos;
	private JMenuItem analyseIntronLengths;
	
	public BSToolbar(BSController controller) {
		this.controller = controller;
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
		analysing.add(analyselogos);
		analysing.add(analyseIntronLengths);
		add(analysing, cc.xy(2, 2));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(cmdLogos) || cmd.equals(cmdIntronLength)) {
			if (controller.readInput(BSUserSettings.getGffPath(), null, BSUserSettings.getFastaPath(), null, BSUserSettings.getPositionsBeforeFivePrimeSS(), BSUserSettings.getLogoLengthFivePrime())) {
				System.out.println("reading successful");
			} else {
				System.err.println("something went wrong when reading");
			}
		}
		
		if (cmd.equals(cmdLogos)) {
			controller.analyseLogos(BSUserSettings.getAnalyzationType(), BSUserSettings.getLogoLengthFivePrime(), BSUserSettings.getOutputpath()+"rawlogo", BSUserSettings.getOutputpath()+"perclogo", false);
		} else if (cmd.equals(cmdIntronLength)) {
			controller.analyseIntronLengths(BSUserSettings.getOutputpath()+"intronLengths");
		}
	}
}
