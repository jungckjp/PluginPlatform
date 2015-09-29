package edu.rosehulman.pluginplatform;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {

	private JLabel out = new JLabel();
	
	public StatusPanel() {
		this.setBounds(0, 576, 1024, 192);
		this.setBackground(new Color(225, 225, 225));
		this.add(out);
	}
	
	public void addStatus(String status) {
		out.setText(out.getText() + status + "\n");
	}
	
}
