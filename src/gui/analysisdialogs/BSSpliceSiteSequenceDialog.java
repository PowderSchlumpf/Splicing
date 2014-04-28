package gui.analysisdialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import utils.BSUserSettings;
import analysis.BSSequenceLogoGenerator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class BSSpliceSiteSequenceDialog extends JDialog implements ActionListener {

	private JComboBox<String> cbSpliceSite;
	private JTextField positions;
	
	private JButton okBtn;
	private JButton cancelBtn;
	private boolean canceled = false;
	
	private final String itemFivePrime = "5' SpliceSite";
	private final String itemThreePrime = "3' SpliceSite";
	
	private final String cmd_ok = "cmd_ok";
	private final String cmd_cancel = "cmd_cancel";
	
	public BSSpliceSiteSequenceDialog(JFrame owner) {
		super(owner, "SpliceSite Sequence Positions", true);
		initGui();
		pack();
		setVisible(true);
	}
	
	private void initGui() {
		cbSpliceSite = new JComboBox<String>();
		cbSpliceSite.addItem(itemFivePrime);
		cbSpliceSite.addItem(itemThreePrime);
		positions = new JTextField(10);
		positions.setToolTipText("Comma separated numbers");
		
		okBtn = new JButton("Ok");
		okBtn.setActionCommand(cmd_ok);
		okBtn.addActionListener(this);
		cancelBtn = new JButton("Cancel");
		cancelBtn.setActionCommand(cmd_cancel);
		cancelBtn.addActionListener(this);
		
		FormLayout layout = new FormLayout("5dlu,right:pref,3dlu,left:pref,5dlu", "3dlu,pref,3dlu,pref,5dlu,pref,3dlu");
		setLayout(layout);
		CellConstraints cc = new CellConstraints();
		add(new JLabel("SpliceSite:"), cc.xy(2, 2));
		add(cbSpliceSite, cc.xy(4, 2));
		add(new JLabel("Positions:"), cc.xy(2, 4));
		add(positions, cc.xy(4, 4));
		add(okBtn, cc.xy(2, 6));
		add(cancelBtn, cc.xy(4, 6));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(cmd_ok)) {
			commit();
		} else if (cmd.equals(cmd_cancel)) {
			canceled = true;
		}
	}
	
	public boolean wasCanceled() {
		return canceled;
	}
	
	private void commit() {
		if (cbSpliceSite.getSelectedItem().equals(itemFivePrime)) {
			BSUserSettings.setAnalysisSequenceSpliceSite(BSSequenceLogoGenerator.MODE_FIVE_PRIME);
		} else {
			BSUserSettings.setAnalysisSequenceSpliceSite(BSSequenceLogoGenerator.MODE_THREE_PRIME);
		}
		ArrayList<Integer> positionList = new ArrayList<>();
		String position = positions.getText();
		if (position != null && position.contains(";")) {
			String[] pos = position.split(";");
			for (int i = 0; i < pos.length; i++) {
				positionList.add(Integer.valueOf(pos[i]));
			}
		} else if (position != null && !position.equals("")){
			positionList.add(Integer.valueOf(position));
		}
		BSUserSettings.setSpliceSitePositionList(positionList);
	}
	
}
