import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

/**
 * The Class OdysseusApp.
 */
public class OdysseusApp implements MessageListener {

	/** The moteIF. */
	private MoteIF moteIF;
	
	/** The full interface. */
	private static FullInterface full;
	
	/** The mini interface. */
	private static MiniInterface mini;
	
	/** The map of sensors indexes. */
	private static Map<Integer, Integer> sensors;
	
	/** The number of sensors. */
	private int nSensors = 0;
	
	/** The start time. */
	private final long startTime = System.currentTimeMillis();

	/** The mini interface menu bar. */
	private JMenuBar miniMenuBar;

	/** The mini interface menu. */
	private JMenu miniMenu;

	/** The mini interface refresh button. */
	private JMenuItem miniRefresh;

	/** The mini interface display button. */
	private JMenuItem miniDisplay;

	/** The mini interface graphic button. */
	private JMenuItem miniGraphic;
	
	/** The mini interface help button. */
	private JMenuItem miniHelp;

	/** The full interface menu bar. */
	private JMenuBar fullMenuBar;
	
	/** The full interface menu. */
	private JMenu fullMenu;
	
	/** The full interface refresh button. */
	private JMenuItem fullRefresh;
	
	/** The full interface display button. */
	private JMenuItem fullDisplay;
	
	/** The full interface graphic button. */
	private JMenuItem fullGraphic;
	
	/** The full interface help button. */
	private JMenuItem fullHelp;
	
	/**
	 * Instantiates a new Odysseus application.
	 *
	 * @param moteIF the moteIF
	 */
	public OdysseusApp(MoteIF moteIF) {
		this.moteIF = moteIF;
		this.moteIF.registerListener(new Odysseus(), this);
	}

	/**
	 * Send packets.
	 *
	 * @throws Exception the exception
	 */
	public void sendPackets() throws Exception {
		int id = 0;
		int light = 0;
		Odysseus payload = new Odysseus();
			    
		System.out.println("Sending packet");
		payload.set_id(id);
		payload.set_light(light);
		moteIF.send(0, payload);
	}

	/**
	 * Runs when a message is received.
	 *
	 * @param to the to
	 * @param message the message
	 */
	public void messageReceived(int to, Message message) {
		Odysseus msg = (Odysseus)message;
		long currentTime = (System.currentTimeMillis() - this.startTime)/1000;
		
		if(!sensors.containsKey(msg.get_id())) {
			sensors.put(msg.get_id(), nSensors);
			full.addSensor(msg.get_id());
			mini.addSensor(msg.get_id());
			nSensors++;
		}
		full.addText(sensors.get(msg.get_id()), msg.get_light(), currentTime);
		mini.setText(sensors.get(msg.get_id()), msg.get_light());
		full.addData(msg.get_id(), msg.get_light(), currentTime);
		mini.addData(msg.get_id(), msg.get_light(), currentTime);
	}
	
	/**
	 * Initiates the menu.
	 *
	 * @throws Exception the exception
	 */
	private void initMenu() throws Exception {
		/* The mini interface */
		miniMenuBar = new JMenuBar();
		miniMenu = new JMenu("Menu");
		
		miniRefresh = new JMenuItem("Actualiser");		
		miniRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					sendPackets();
				} catch (Exception e) {
					System.out.println("Erreur lors de l'envoi de paquets");
				}
			}
		});
		
		miniDisplay = new JMenuItem("Changer d'affichage (terminal)");		
		miniDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(mini.getFrame().isVisible()) {
					mini.getFrame().setVisible(false);
					full.getFrame().setVisible(true);
				} else {
					mini.getFrame().setVisible(true);
					full.getFrame().setVisible(false);
				}
			}
		});
		
		miniGraphic = new JMenuItem("Graphique");
		miniGraphic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mini.drawGraph();
			}
		});
		
		miniHelp = new JMenuItem("A propos");
		miniHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane jop = new JOptionPane();
				String text = "Projet d'INFO0703 : l'application Odysseus !\n\nUne application permettant de mesurer la luminosite a partir d'un reseau de capteurs Telosb.\n\nPar Jeremy, Romain & Soheyb.";
				jop.showMessageDialog(mini.getFrame(), text, "A propos", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		miniMenu.add(miniRefresh);
		miniMenu.add(miniDisplay);
		miniMenu.add(miniGraphic);
		miniMenu.add(miniHelp);
		miniMenuBar.add(miniMenu);
		mini.getFrame().setJMenuBar(miniMenuBar);
		mini.getFrame().pack();
		
		/* The full interface */
		fullMenuBar = new JMenuBar();
		fullMenu = new JMenu("Menu");
		
		fullRefresh = new JMenuItem("Actualiser");		
		fullRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					sendPackets();
				} catch (Exception e) {
					System.out.println("Erreur lors de l'envoi de paquets");
				}
			}
		});
		
		fullDisplay = new JMenuItem("Changer d'affichage (compact)");		
		fullDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(full.getFrame().isVisible()) {
					full.getFrame().setVisible(false);
					mini.getFrame().setVisible(true);
				} else {
					full.getFrame().setVisible(true);
					mini.getFrame().setVisible(false);
				}
			}
		});
		
		fullGraphic = new JMenuItem("Graphique");
		fullGraphic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				full.drawGraph();
			}
		});
		
		fullHelp = new JMenuItem("A propos");
		fullHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane jop = new JOptionPane();
				String text = "Projet d'INFO0703 : l'application Odysseus !\n\nUne application permettant de mesurer la luminosite a partir d'un reseau de capteurs Telosb.\n\nPar Jeremy, Romain & Soheyb.";
				jop.showMessageDialog(full.getFrame(), text, "A propos", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		fullMenu.add(fullRefresh);
		fullMenu.add(fullDisplay);
		fullMenu.add(fullGraphic);
		fullMenu.add(fullHelp);
		fullMenuBar.add(fullMenu);
		full.getFrame().setJMenuBar(fullMenuBar);
		full.getFrame().pack();
	}

	/**
	 * Shows usual usage.
	 */
	private static void usage() {
		System.err.println("Usage: OdysseusApp [-comm <source>]");
		System.err.println("Essayez java -cp \".:jfreechart-1.0.19/jfreechart-1.0.19-demo.jar:/opt/tinyos-2.1.2/support/sdk/java/tinyos.jar\" OdysseusApp -comm serial@/dev/ttyUSB0:telosb\n");
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		full = new FullInterface();
		mini = new MiniInterface();
		sensors = new HashMap<Integer, Integer>();
		String source = null;
		if (args.length == 2) {
			if (!args[0].equals("-comm")) {
				usage();
				System.exit(1);
			}
			source = args[1];
		} else if (args.length != 0) {
			usage();
			System.exit(1);
		}

		PhoenixSource phoenix;

		if (source == null) {
			phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
		} else {
			phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
		}
		
		MoteIF mif = new MoteIF(phoenix);
		OdysseusApp serial = new OdysseusApp(mif);
		
		serial.initMenu();
		mini.getFrame().setVisible(true);
	}
}