package edu.rosehulman.plugin;

import javax.swing.JPanel;

import edu.rosehulman.pluginplatform.StatusPanel;

public interface IPlugin {
	public void setStatusPanel(StatusPanel statusPanel);
	public JPanel getGUI();
}
