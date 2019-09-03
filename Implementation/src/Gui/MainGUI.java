package Gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import keypointdetector.KeypointDetector;
import keypointdetector.ORBDetector;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import org.opencv.core.Core;

import javax.swing.JButton;
import javax.swing.JComboBox;

public class MainGUI {

	private JFrame frmWhip;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frmWhip.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWhip = new JFrame();
		frmWhip.setTitle("Bild\u00E4hnlichkeitssuchprogramm-GUI");
		frmWhip.setBounds(100, 100, 699, 554);
		frmWhip.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWhip.getContentPane().setLayout(new MigLayout("", "[][][][][][][][][grow]", "[][][][][][][][][][][][][][][][][][]"));
		
		JLabel lblZuUntersuchendesBild = new JLabel("Zu untersuchendes Bild:");
		frmWhip.getContentPane().add(lblZuUntersuchendesBild, "cell 1 1");
		
		JLabel lblPfadZumBild = new JLabel("[Pfad zum Bild]");
		frmWhip.getContentPane().add(lblPfadZumBild, "cell 3 1");
		
		JButton btnDateiAuswhlen = new JButton("Datei ausw\u00E4hlen");
		frmWhip.getContentPane().add(btnDateiAuswhlen, "cell 8 1");
		
		JLabel lblbildvorschau = new JLabel("[Bildvorschau?]");
		frmWhip.getContentPane().add(lblbildvorschau, "cell 3 2");
		
		JLabel lblVergleichsbilder = new JLabel("Vergleichsbilder");
		frmWhip.getContentPane().add(lblVergleichsbilder, "cell 1 3");
		
		JButton btnOrdnerAuswhlen = new JButton("Ordner ausw\u00E4hlen");
		frmWhip.getContentPane().add(btnOrdnerAuswhlen, "cell 8 3");
		
		JLabel lblEinstellungen = new JLabel("Einstellungen:");
		frmWhip.getContentPane().add(lblEinstellungen, "cell 1 8");
		
		JLabel lblDetektor = new JLabel("Detektor:");
		frmWhip.getContentPane().add(lblDetektor, "cell 1 11");
		
		JComboBox comboBox = new JComboBox();
		frmWhip.getContentPane().add(comboBox, "cell 8 11,growx");
		
		JLabel lblDistanzma = new JLabel("Distanzma\u00DF:");
		frmWhip.getContentPane().add(lblDistanzma, "cell 1 13");
		
		JComboBox comboBox_1 = new JComboBox();
		frmWhip.getContentPane().add(comboBox_1, "cell 8 13,growx");
		
		JLabel lblNewLabel = new JLabel("TODO: Einstellungsm\u00F6glichkeiten diskutieren");
		frmWhip.getContentPane().add(lblNewLabel, "cell 3 14 1 2");
		
		JButton btnNewButton = new JButton("Suche starten");
		frmWhip.getContentPane().add(btnNewButton, "cell 2 17 6 1");
		
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String image1 =  "resources/images/HansSarpei.jpg";
			     String image2 =  "resources/images/Schalketrikot.jpg";
			     
			     
			    //JFrame frame = new JFrame("Result");
			    //frame.setVisible(true);
			     ORBDetector det = new ORBDetector();
			     det.detect_keypoints(image1, image2);
				
			}
		});
	}

}
