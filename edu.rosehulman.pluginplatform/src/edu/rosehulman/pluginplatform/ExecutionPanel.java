package edu.rosehulman.pluginplatform;

import java.awt.Color;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ExecutionPanel extends JPanel {
	
	private StatusPanel statusPanel;
	
	public ExecutionPanel(StatusPanel statusPanel) {
		this.setBounds(256, 0, 768, 576);
		this.setBackground(new Color(225, 255, 255));
		
		this.statusPanel = statusPanel;
	}
	
	public StatusPanel getStatusPanel() {
		return this.statusPanel;
	}
	
}
