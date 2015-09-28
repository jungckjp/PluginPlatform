package edu.rosehulman.pluginplatform;

import javax.swing.JFrame;

public class PluginPlatform {

	public static void main(String[] args) {
		JFrame frame = new JFrame("T.E.A.M.");
		frame.setSize(1024, 768);
		frame.setResizable(false);
		
		ListingPanel listingPanel = new ListingPanel();
		frame.add(listingPanel);
		
		ExecutionPanel executionPanel = new ExecutionPanel();
		frame.add(executionPanel);
		
		StatusPanel statusPanel = new StatusPanel();
		frame.add(statusPanel);
		
		frame.setVisible(true);
	}

}
