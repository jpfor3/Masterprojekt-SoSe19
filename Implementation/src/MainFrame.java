import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
	private String _inputImage;
	private String _compareImages;
	private int _minSamples;
	private float _eps;

	
		
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
		
		JLabel lblActualComparedImage = new JLabel("Actual compared Img:");
		lblActualComparedImage.setBounds(25, 371, 205, 20);
		contentPane.add(lblActualComparedImage);
		
		JLabel lblTotalDistance = new JLabel("Total Distance:");
		lblTotalDistance.setBounds(161, 371, 106, 20);
		contentPane.add(lblTotalDistance);
		
		textField = new JTextField();
		textField.setBounds(280, 371, 146, 26);
		textField.setEditable(false);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblDBScan = new JLabel("DBScan Clusterer:");
		lblDBScan.setBounds(25, 415, 258, 20);
		contentPane.add(lblDBScan);
		
		JLabel lblSamples = new JLabel("min_samples:");
		lblSamples.setBounds(165, 415, 258, 20);
		contentPane.add(lblSamples);
		
		JTextField min_samples = new JTextField("4");
		min_samples.setBounds(280, 415, 215, 31);
		min_samples.setSize(new Dimension(80,31));
		contentPane.add(min_samples);
		_minSamples = Integer.parseInt(min_samples.getText());
		
		JLabel lblEps = new JLabel("eps:");
		lblEps.setBounds(219, 464, 258, 20);
		contentPane.add(lblEps);
		
		JTextField eps = new JTextField("0.1");
		eps.setBounds(280, 459, 215, 31);
		eps.setSize(new Dimension(80,31));
		contentPane.add(eps);
		_eps = Float.parseFloat(eps.getText());
		
		JLabel lblClusteringAlgorithmus = new JLabel("Distance Algorithmus:");
		lblClusteringAlgorithmus.setBounds(121, 513, 174, 20);
		contentPane.add(lblClusteringAlgorithmus);
		
		String dalglist[] = {"EMD", "Hamming", "Jaccard"};
		JComboBox comboBox_1 = new JComboBox(dalglist);
		comboBox_1.setBounds(280, 508, 215, 31);
		contentPane.add(comboBox_1);
		
		JLabel lblEdmPenalty = new JLabel("EMD Penalty:");
		lblEdmPenalty.setBounds(175, 561, 98, 20);
		contentPane.add(lblEdmPenalty);
			
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(280, 555, 215, 32);
		contentPane.add(comboBox_2);		
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Compared Image Folder", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(454, 16, 430, 292);
		contentPane.add(panel_1);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setLayout(null);
		layeredPane_1.setBounds(15, 28, 403, 248);
		panel_1.add(layeredPane_1);
		
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
				fileChooser.setCurrentDirectory(new java.io.File("."));
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					
				    File selectedFile = fileChooser.getSelectedFile();
				    
				    _inputImage = selectedFile.toString();
				    
				    SwingUtilities.invokeLater(new Runnable() {
				        public void run() {
				        	JLabel lblInput;
				        	if(_inputImage.length() > 55)
				        	{
							    lblInput = new JLabel(_inputImage.substring(0, 55) + " ...");
				        	} else {
				        		lblInput = new JLabel(_inputImage);
				        	}
						    lblInput.setBounds(49, 16, 430, 292);
							contentPane.add(lblInput);
							contentPane.repaint();
							
							contentPane.setComponentZOrder(lblInput, 1);
				        }
				    });
				    
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
//					images1.add(selectedFile.toString());
				}
			}
		});
		btnSelectImage.setBounds(19, 314, 420, 29);
		contentPane.add(btnSelectImage);
		

		
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
			    	_compareImages = imageDirectory.getAbsolutePath();
//			    	File[] images0 = imageDirectory.listFiles();
//				    for (File file : images0) {
//				    	if (file.isFile()) {
//				            images1.add(imageDirectory.toString() + file.getName());
//				        }
//				    }
			    	
			    	SwingUtilities.invokeLater(new Runnable() {
				        public void run() {
				        	JLabel lblCompare;
				        	if(_compareImages.length() > 55)
				        	{
				        		lblCompare = new JLabel(_compareImages.substring(0, 55) + " ...");
				        	} else {
				        		lblCompare = new JLabel(_compareImages);
				        	}
				        	lblCompare.setBounds(502, 16, 430, 292);
							contentPane.add(lblCompare);
							contentPane.repaint();
							
							contentPane.setComponentZOrder(lblCompare, 1);
				        }
				    });
			    	
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
				
				try {
					Controller.compareImages(_inputImage, _compareImages, _minSamples, _eps);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				// run the comparison
				// First, check which keypoint detector was chosen
//			    KeypointDetector KPDetector = new KeypointDetector(images1); 
//			    descriptorList = KPDetector.getDescriptorList();
//				
//				String kpdetector = comboBox_1.getSelectedItem().toString();
//				switch (kpdetector) {
//				   	case "SIFT": KeypointDetector._kpDetector = 3;
//				   	KeypointDetector._descriptorExtractor = 1;
//			    	break;
//				   	case "SURF": KeypointDetector._kpDetector = 4;
//				   	KeypointDetector._descriptorExtractor = 2;
//	    		    break;
//				   	case "ORB": KeypointDetector._kpDetector = 5;
//				   	KeypointDetector._descriptorExtractor = 3;
//	    			break;
//				   	case "BRISK": KeypointDetector._kpDetector = 11;
//				   	KeypointDetector._descriptorExtractor = 5;
//	    			break;
//				}
//				
//				
//				//KeypointDetector.SurfDetector(images1);    
//			    descriptorList = KeypointDetector._descriptorList;
//			    infoLabel.setText("Creating Clusters on Keypoints...");
//			    infoLabel.repaint();
//			    
//			    for(MatOfKeyPoint kp : descriptorList)
//			      {
//			   	  List<double[]> clusterlist = DBScan.cluster(kp);
//			   	  centeredDescriptors.add(clusterlist);   
//			      }
//				
//			      infoLabel.setText("Clustering Ended...");
//			      infoLabel.repaint();
//			      try {
//					TimeUnit.SECONDS.sleep(2);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			      infoLabel.setText("Calculating Distances...");
//			      infoLabel.repaint();
//			      List<Double> listOfDistances = FastEMD.calcDistances(centeredDescriptors, images1);
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
		
		JButton btnDeleteBuffer = new JButton("Clear bufferd images");
		btnDeleteBuffer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 PrintWriter writer;
				try {
					writer = new PrintWriter("resources/index/idx.txt");
					writer.print("");
					writer.close();
			    	 
					//Clearing image_distances file
					writer = new PrintWriter("resources/index/image_distances.txt");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
			    	JOptionPane.showMessageDialog(null, "No File was found", "Warning", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnDeleteBuffer.setBounds(464, 354, 420, 29);
		contentPane.add(btnDeleteBuffer);
		
		
	}
}
