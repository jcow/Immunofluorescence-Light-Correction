
package ImmunofluorescenceLightCorrection;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;



/**
 *
 * @author Jason.Cowan
 */
public class ProcessController extends JFrame implements ActionListener{
	
	int windowWidth = 800;
	int windowHeight = 600;
	
	JPanel bottomPanel;
	JPanel topPanel;
	JLabel selectedDirectory;
	LinkedList<JLabel> output;
	
	JButton fChooserOpen;
	JButton process;
	
	String directory = "";
	
	
	
	public ProcessController(){
		output = new LinkedList();
		makeDisplay();
	}
	
	private void makeDisplay(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Immunofluorescence Correction");   
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null); // center of screen
		
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));
		this.setLayout(new GridLayout(2,1, 20, 20));
		
		makeTopPanel();
		makeBottomPanel();
	}
	
	private void makeTopPanel(){
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,2));
		
		this.add(topPanel);
		makeTopLeftPanel();
		makeTopRightPanel();
	}
	
	private void makeTopLeftPanel(){
		fChooserOpen = new JButton();
		fChooserOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = chooser.showOpenDialog(ProcessController.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					directory = chooser.getSelectedFile().getAbsolutePath();
					String into = directory;
					if(directory.length() > 40){
						into = "..."+directory.substring(directory.length()-60);
					}
					selectedDirectory.setText(into);
				}
			}
		});
		
		JPanel topLeftPanel = new JPanel();
		GUIHelper.setBorder(topLeftPanel, "Run Options");
		
		fChooserOpen.setText("Choose Directory");
		
		process = new JButton("Process Directory");
		process.addActionListener(this);
		
		topLeftPanel.add(fChooserOpen);
		topLeftPanel.add(process);
		
		topPanel.add(topLeftPanel);
	}
	
	private void makeTopRightPanel(){
		JPanel topRightPanel = new JPanel();
		GUIHelper.setBorder(topRightPanel, "Selected Directory");
		
		selectedDirectory = new JLabel("No Directory Selected");
		topRightPanel.add(selectedDirectory);
		topPanel.add(topRightPanel);
	}
	
	private void makeBottomPanel(){
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
		
		JScrollPane panelScroller = new JScrollPane(bottomPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GUIHelper.setBorder(panelScroller, "Results");
		this.add(panelScroller);
		
		JLabel nothing = new JLabel("No process has been run");
		bottomPanel.add(nothing);
		output.add(nothing);
	}
	
	private void addToBottomPanel(String text){
		JLabel nLabel = new JLabel(text);
		bottomPanel.add(nLabel);
		output.add(nLabel);
		refreshGUI();
	}
	
	private void processed(String filename, String fileextension){
		addToBottomPanel("Processed: "+filename+"."+fileextension);
	}
	
	private void refreshOutput(){
		for(int i = 0; i < output.size(); i++){
			bottomPanel.remove(output.get(i));
		}
		output = new LinkedList();
		refreshGUI();
	}
	
	private void refreshGUI(){
		bottomPanel.revalidate();  
		bottomPanel.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		refreshOutput();
		if(e.getSource() == process && directory.compareTo("") != 0){

			String[] files = FileSystemHelper.getFilesWithinDirectory(directory);
			String correctedDirectory = directory+"\\Corrected Images";
			
			addToBottomPanel("Placing images into");
			addToBottomPanel(correctedDirectory);
			addToBottomPanel(" ");
			
			
			File dir = new File(correctedDirectory);
			if(!dir.exists()){
				dir.mkdir();
			}

			for(int i = 0; i < files.length; i++){
				String[] fileChar = FileSystemHelper.getFileCharacteristics(files[i]);
				String path = fileChar[0];
				String filename = fileChar[1];
				String extension = fileChar[2];
				
				if(Config.isProcessableImageFile(extension)){
					ImageAdjuster imgAdj = new ImageAdjuster();
					boolean p = imgAdj.adjustImage(files[i], correctedDirectory+"\\"+filename+"."+extension);
					if(p){
						processed(filename, extension);
					}
					else{
						addToBottomPanel("Processing file "+filename+" failed");
					}
				}
			}
			
			addToBottomPanel(" ");
			addToBottomPanel("Process Finished");
		}
		else{
			addToBottomPanel("No directory has been chosen");
		}
	}
	
	
}