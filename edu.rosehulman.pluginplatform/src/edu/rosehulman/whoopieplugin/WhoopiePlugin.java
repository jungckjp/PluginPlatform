package edu.rosehulman.whoopieplugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.rosehulman.plugin.IPlugin;
import edu.rosehulman.pluginplatform.StatusPanel;

public class WhoopiePlugin implements IPlugin, ActionListener {

	private StatusPanel statusPanel;
	
	public void setStatusPanel(StatusPanel statusPanel) {
		this.statusPanel = statusPanel;
	}

	public JPanel getGUI() {
		JPanel panel = new JPanel();
		JButton whoopieButton = new JButton("WHOOPIE!");
		whoopieButton.addActionListener(this);
		panel.add(whoopieButton);
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		statusPanel.addStatus("WHOOPIE!");
	}

}
