package edu.rosehulman.pluginplatform;

import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.rosehulman.plugin.IPlugin;

@SuppressWarnings("serial")
public class ListingPanel extends JPanel implements Runnable, ListSelectionListener {
	
	private final Kind<Path> ENTRY_CREATE = StandardWatchEventKinds.ENTRY_CREATE;
	private final Kind<Path> ENTRY_DELETE = StandardWatchEventKinds.ENTRY_DELETE;
	private final Kind<Path> ENTRY_MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
	private final Kind<Object> OVERFLOW = StandardWatchEventKinds.OVERFLOW;
	
	private DefaultListModel<String> plugins = new DefaultListModel<String>();
	private JList<String> listBox;
	
	private ExecutionPanel executionPanel;

	public ListingPanel(ExecutionPanel executionPanel) {
		this.executionPanel = executionPanel;
		this.setBounds(0, 0, 256, 576);
		this.setBackground(new Color(225, 225, 255));
		this.findPluginsOnStartup();
		this.listBox = new JList<String>(plugins);
		this.listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listBox.setBounds(this.getBounds());
		this.listBox.setBackground(this.getBackground());
		this.listBox.addListSelectionListener(this);
		this.add(listBox);
		
		this.repaint();
		
		Thread listenThread = new Thread(this);
		listenThread.start();
	}
	
	private void findPluginsOnStartup() {
		File dir = new File(PluginPlatform.PLUGIN_DIR);
		File [] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".jar");
		    }
		});
		for (File f : files) {
			String[] farray = f.toString().split("\\\\");
			String fname = farray[farray.length - 1];
			System.out.println(fname + " DISCOVERED");
		    this.plugins.addElement(fname);
		}
	}
	
	public void run() {
		WatchService listener;
		try {
			listener = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get("plugins");
			dir.register(listener, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		while (true) {
			WatchKey key;
			try {
				key = listener.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path fileName = ev.context();
				
				String[] farray = fileName.toString().split("\\.");
				if (farray.length != 2 || !farray[farray.length - 1].equals("jar")) {
					continue;
				}
				
				System.out.println(kind.name() + ": " + fileName);
				
				if (kind == OVERFLOW) {
					System.out.println("OVERFLOW");
					continue;
				}
				else if (kind == ENTRY_CREATE) {
					System.out.println("File Created");
					plugins.addElement(fileName.toString());
				}
				else if (kind == ENTRY_DELETE) {
					System.out.println("File Deleted");
					plugins.removeElement(fileName.toString());
				}
				else if (kind == ENTRY_MODIFY) {
					System.out.println("File Modified");
				}
			}
			this.repaint();
		    boolean valid = key.reset();
		    if (!valid) {
		        break;
		    }
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		int idx = listBox.getSelectedIndex();
        if (idx != -1) {
        	System.out.println("Current selection: " + plugins.get(idx));
        	String fname = PluginPlatform.PLUGIN_DIR + plugins.get(idx);
        	System.out.println(fname);
        	/*
        	JarClassLoader jarLoader = new JarClassLoader(fname);
        	try {
        		System.out.println("hi0");
				Class c = jarLoader.loadClass("WhoopiePlugin", true);
				System.out.println("hi1");
				Object o = c.newInstance();
				if (o instanceof IPlugin) {
					IPlugin plugin = (IPlugin) o;
					executionPanel.removeAll();
					executionPanel.add(plugin.getGUI());
					plugin.setStatusPanel(executionPanel.getStatusPanel());
				}
				else {
					System.out.println("hi3");
					throw new ClassNotFoundException();
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			*/
        }
	}

}
