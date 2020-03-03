import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.border.TitledBorder;

import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;

import Distanzmasse.FastEMD;
import cluster.DBScan;
import keypointdetector.KeypointDetector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private File imageDirectory;
	private List<String> images1 = new ArrayList<String>();
	List<MatOfKeyPoint> descriptorList = new ArrayList<MatOfKeyPoint>();
	List<List<double[]>> centeredDescriptors = new ArrayList<List<double[]>>();
	private File mainImage;
	private int KPD;
	private int DALG;
	private int EDMP;

	
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 931, 659);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel infoLabel = new JLabel("");
		infoLabel.setBounds(565, 464, 272, 20);
		contentPane.add(infoLabel);
		
		JLabel lblKeypointDetectionAlgorithmus = new JLabel("Keypoint Detection Algorithmus:");
		lblKeypointDetectionAlgorithmus.setBounds(35, 464, 258, 20);
		contentPane.add(lblKeypointDetectionAlgorithmus);
		
		String kpdlist[] = {"SURF", "SIFT", "BRISK", "ORB"}; 
		JComboBox comboBox = new JComboBox(kpdlist);
		comboBox.setBounds(280, 459, 215, 31);
		contentPane.add(comboBox);
		
		JLabel lblClusteringAlgorithmus = new JLabel("Distance Algorithmus:");
		lblClusteringAlgorithmus.setBounds(99, 513, 174, 20);
		contentPane.add(lblClusteringAlgorithmus);
		
		String dalglist[] = {"EMD", "Hamming", "Jaccard"};
		JComboBox comboBox_1 = new JComboBox(dalglist);
		comboBox_1.setBounds(280, 508, 215, 31);
		contentPane.add(comboBox_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Selected Image", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(9, 16, 430, 292);
		contentPane.add(panel);
		panel.setLayout(null);
		
//		JLayeredPane layeredPane = new JLayeredPane();
//		layeredPane.setBounds(15, 28, 403, 248);
//		layeredPane.setLayout(null);
//		panel.add(layeredPane);
		
		
		JButton btnSelectImage = new JButton("Select Image");
		btnSelectImage.addActionListener(new ActionListener() {
			private Graphics g;

			public void actionPerformed(ActionEvent e) {
				// Open directory and choose file
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
//					BufferedImage i0 = null;
//					try {
//						i0 = ImageIO.read(selectedFile);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				    panel.add(new JLabel(new ImageIcon(i0.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH))));
//				    panel.setVisible(true);
//				    panel.repaint();
					images1.add(selectedFile.toString());
				}
			}
		});
		btnSelectImage.setBounds(19, 314, 420, 29);
		contentPane.add(btnSelectImage);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Compared Image", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(454, 16, 430, 292);
		contentPane.add(panel_1);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setLayout(null);
		layeredPane_1.setBounds(15, 28, 403, 248);
		panel_1.add(layeredPane_1);
		
		JButton button = new JButton("Select directory with images");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Choose directory with images
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setDialogTitle("Directory with images to compare");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	File imageDirectory = fileChooser.getSelectedFile();
			    	File[] images0 = imageDirectory.listFiles();
				    for (File file : images0) {
				    	if (file.isFile()) {
				            images1.add(imageDirectory.toString() + file.getName());
				        }
				    }
			      } else {
			    	JOptionPane.showMessageDialog(null, "No directory was selected!", "Warning", JOptionPane.ERROR_MESSAGE);
			      }
			    


			}
		});
		button.setBounds(464, 314, 420, 29);
		contentPane.add(button);
		
		JButton btnCompare = new JButton("Compare");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// run the comparison
				// First, check which keypoint detector was chosen
			    KeypointDetector KPDetector = new KeypointDetector(images1); 
			    descriptorList = KPDetector.getDescriptorList();
				
				String kpdetector = comboBox_1.getSelectedItem().toString();
				switch (kpdetector) {
				   	case "SIFT": KeypointDetector._kpDetector = 3;
				   	KeypointDetector._descriptorExtractor = 1;
			    	break;
				   	case "SURF": KeypointDetector._kpDetector = 4;
				   	KeypointDetector._descriptorExtractor = 2;
	    		    break;
				   	case "ORB": KeypointDetector._kpDetector = 5;
				   	KeypointDetector._descriptorExtractor = 3;
	    			break;
				   	case "BRISK": KeypointDetector._kpDetector = 11;
				   	KeypointDetector._descriptorExtractor = 5;
	    			break;
				}
				
				
				//KeypointDetector.SurfDetector(images1);    
			    descriptorList = KeypointDetector._descriptorList;
			    infoLabel.setText("Creating Clusters on Keypoints...");
			    infoLabel.repaint();
			    
			    for(MatOfKeyPoint kp : descriptorList)
			      {
			   	  List<double[]> clusterlist = DBScan.cluster(kp);
			   	  centeredDescriptors.add(clusterlist);   
			      }
				
			      infoLabel.setText("Clustering Ended...");
			      infoLabel.repaint();
			      try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			      infoLabel.setText("Calculating Distances...");
			      infoLabel.repaint();
			      List<Double> listOfDistances = FastEMD.calcDistances(centeredDescriptors, images1);
			}
		});
		btnCompare.setBounds(715, 542, 169, 45);
		contentPane.add(btnCompare);
		
		JButton btnAbort = new JButton("Abort");
		btnAbort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				infoLabel.setText("");
				infoLabel.repaint();
			}
		});
		btnAbort.setBounds(531, 542, 169, 45);
		contentPane.add(btnAbort);
		
		JLabel lblActualComparedImage = new JLabel("Actual compared Image:");
		lblActualComparedImage.setBounds(25, 371, 205, 20);
		contentPane.add(lblActualComparedImage);
		
		JLabel lblTotalDistance = new JLabel("Total Distance:");
		lblTotalDistance.setBounds(51, 404, 106, 20);
		contentPane.add(lblTotalDistance);
		
		textField = new JTextField();
		textField.setBounds(167, 401, 146, 26);
		textField.setEditable(false);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSelectOutputFolder = new JButton("Select output folder");
		btnSelectOutputFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Choose directory with images
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setDialogTitle("Output directory for compared images");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	String saveDirectory = fileChooser.getSelectedFile().toString();
			      } else {
			    	JOptionPane.showMessageDialog(null, "No directory was selected!", "Warning", JOptionPane.ERROR_MESSAGE);
			      }
			}
		});
		btnSelectOutputFolder.setBounds(464, 354, 420, 29);
		contentPane.add(btnSelectOutputFolder);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(280, 555, 215, 32);
		contentPane.add(comboBox_2);
		
		JLabel lblEdmPenalty = new JLabel("EMD Penalty:");
		lblEdmPenalty.setBounds(175, 561, 98, 20);
		contentPane.add(lblEdmPenalty);
		
		
	}
}
