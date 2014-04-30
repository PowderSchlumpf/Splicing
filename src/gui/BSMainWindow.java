package gui;

import execute.BSController;
import gui.panels.BSInputPanel;
import gui.panels.BSToolbar;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class BSMainWindow extends JFrame {
	
	private BSToolbar toolbar;
	private BSInputPanel inputPanel;
	
	private BSController controller;
	
	public BSMainWindow() {
		super();
//		controller = new BSController("yeast", true, false);
		controller = new BSController(null, true, false);
		setTitle("SpliceSite Analyzing");
//		setPreferredSize(new Dimension(900, 400));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initGui();
		pack();
//		setVisible(true);
	}
	
	private void initGui() {
		FormLayout layout = new FormLayout("pref","pref,pref");
		setLayout(layout);
		CellConstraints cc = new CellConstraints();
		inputPanel = new BSInputPanel(this);
		toolbar = new BSToolbar(this);
		add(toolbar, cc.xy(1, 1));
		add(inputPanel, cc.xy(1, 2));
	}

	public BSController getController() {
		return controller;
	}

}
