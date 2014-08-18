package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import org.w3c.dom.Document;

import parser.XMLParser;

/**
 * @author wallaceg09
 * 
 * License: http://opensource.org/licenses/GPL-3.0
 *
 */
public class ViewerJFrame extends JFrame implements ActionListener{
	
	private static String testQuestFilepath = "G:\\ExternalGit\\ProjectSWG\\clientdata\\quest\\tatooine\\speeder_quest.qst";
	
	private JTree questTree;
	private JTextArea questText;			//probably not going to use...
	private JScrollPane questTreeScrollPane;
	private GridBagConstraints gc;
	private XMLParser parser;
	
	private JMenuBar 	menuBar;
	private JMenu 		file;				//File menu 
	private JMenuItem	openQuest, 			//Open menu, doesn't ignore nulls
						openQuestIgnore;	//Open menu, ignores nulls
	
	QFileChooser chooser;
	
	public ViewerJFrame()
	{
		super("PSWG Quest Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parser = new XMLParser();
		
		//Layout code
		gc = new GridBagConstraints();
		gc.anchor = gc.NORTHWEST;
		gc.fill = gc.BOTH;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.ipadx = 0;
		gc.ipady = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		
		this.getContentPane().setLayout(new GridBagLayout());
		//End layout code
		
		//Menu code
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		openQuest = new JMenuItem("Open");
		openQuest.setToolTipText("Open quest normally.");
		openQuest.addActionListener(this);
		openQuestIgnore = new JMenuItem("Open ig");
		openQuestIgnore.setToolTipText("Open quest, but ignore null values.");
		openQuestIgnore.addActionListener(this);
		
		file.add(openQuest);
		file.add(openQuestIgnore);
		
		menuBar.add(file);
		
		this.setJMenuBar(menuBar);
		//End menu code
		
		//GUI content code
		
		updateQuestTreeScrollPane(null, false);
		
		this.getContentPane().add(questTreeScrollPane, gc);
		
		gc.gridx = 1;
		questText = new JTextArea();
		this.getContentPane().add(questText, gc);
		//End GUI content code
		
		chooser = new QFileChooser();
		this.pack();
		this.setVisible(true);
	}
	
	private void updateQuestTreeScrollPane(File file, boolean showNulls)
	{
		if(file != null)
		{
			//Inefficient but I can't be bothered to figure out how to fucking do this properly
			this.getContentPane().remove(questTreeScrollPane);
			questTree = parser.getJTree(testQuestFilepath, showNulls);
			questTree.invalidate();
			questTreeScrollPane = new JScrollPane(questTree);
			this.setTitle("PSWG Quest Viewer - " + file.getName());
		}
		else
		{
			questTreeScrollPane = new JScrollPane();
		}
		questTreeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		questTreeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		questTreeScrollPane.setPreferredSize(new Dimension(900, 600));	
		questTreeScrollPane.revalidate();
	}
	
	private void updateTree(File file, boolean showNulls)
	{
		updateQuestTreeScrollPane(file, showNulls);
		this.getContentPane().add(questTreeScrollPane);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Rudimentary, but it works.
		Object source = e.getSource();
		if(source == openQuest)
		{
			int returnCode = chooser.showOpenDialog(ViewerJFrame.this);
			if(returnCode == chooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				updateTree(file, true);
			}
		}
		else if(source == openQuestIgnore)
		{
			int returnCode = chooser.showOpenDialog(ViewerJFrame.this);
			if(returnCode == chooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				updateTree(file, false);
			}
		}
		
	}
}
