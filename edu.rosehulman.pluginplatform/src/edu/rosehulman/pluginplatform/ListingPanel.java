package edu.rosehulman.pluginplatform;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ListingPanel extends JPanel implements Runnable {
	
	private final Kind<Path> ENTRY_CREATE = StandardWatchEventKinds.ENTRY_CREATE;
	private final Kind<Path> ENTRY_DELETE = StandardWatchEventKinds.ENTRY_DELETE;
	private final Kind<Path> ENTRY_MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
	private final Kind<Object> OVERFLOW = StandardWatchEventKinds.OVERFLOW;
	
	private DefaultListModel plugins = new DefaultListModel();
	private JList listBox;

	@SuppressWarnings("unchecked")
	public ListingPanel() {
		this.setBounds(0, 0, 256, 576);
		this.setBackground(new Color(225, 225, 255));
		this.listBox = new JList(plugins);
		this.listBox.setBounds(this.getBounds());
		this.add(listBox);
		
		Thread listenThread = new Thread(this);
		listenThread.start();
	}

	@Override
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
				
				System.out.println(kind.name() + ": " + fileName);
				
				if (kind == OVERFLOW) {
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
	
	private void recreateListBox() {
		this.listBox = new JList(plugins);
		this.listBox.setBounds(this.getBounds());
		this.repaint();
	}

}
