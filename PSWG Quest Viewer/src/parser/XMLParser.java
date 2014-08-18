package parser;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author wallaceg09
 * 
 * License: http://opensource.org/licenses/GPL-3.0
 *
 */
public class XMLParser {
	
	public XMLParser() 
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Not going to be used, however keeping it for reference...
	 */
	private static String[] dataValueNames = 
		{
		"journalEntryTitle", 
		"journalEntryDescription", 
		"isVisible", 
		"prerequisiteTasks", 
		"exclusionTasks", 
		"allowRepeats", 
		"taskName", 
		"showSystemMessages", 
		"musicOnActivate", 
		"musicOnComplete",
		"musicOnFail",
		"target",
		"parameter",
		"grantQuestOnComplete",
		"grantQuestOnCompleteShowSystemMessage",
		"grantQuestOnFail",
		"grantQuestOnFailShowSystemMessage",
		"createWaypoint",
		"Planet",
		"LocationX(m)",
		"LocationY(m)",
		"LocationZ(m)",
		"interiorWaypointAppearance",
		"buildingCellName",
		"waypointName",
		"Comm Message Text",
		"NPC Appearance Server Template"
		};
	
	/**
	 * Constructs a JTree object of an SWG quest file, ignoring any null valued attributes.
	 * @param filepath Path to the quest file. Assumed .qst file extension.
	 * @return
	 */
	public JTree getJTree(String filepath)
	{
		return getJTree(filepath, false);
	}
	
	public JTree getJTree(String filepath, boolean showNulls)
	{
		File file = new File(filepath);
		return getJTree(file, showNulls);
	}
	
	public JTree getjJTree(File xmlFile)
	{
		return getJTree(xmlFile, false);
	}
	
	/**
	 * Constructs a JTree object of an SWG quest file, if specified it will ignore any null valued attributes, or otherwise keep them.
	 * @param xmlFile File containing the quest file. Assumed .qst file extension.
	 * @param showNulls Ignores null values on false, otherwise it ignores nothing.
	 * @return
	 */
	public JTree getJTree(File xmlFile, boolean showNulls)
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		DefaultMutableTreeNode root = null;
		DefaultMutableTreeNode tasks = null;
		DefaultMutableTreeNode list = null;
		
		List<QuestTask> questList = new ArrayList<QuestTask>();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);//Parsed document
			doc.getDocumentElement().normalize();//Normalize the document
			
			//Root node
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			root = new DefaultMutableTreeNode(doc.getDocumentElement().getNodeName());
			
			//End root node
			//Tasks node
			NodeList nList = doc.getElementsByTagName("tasks");
			Node taskListNode = nList.item(0);
			tasks = new DefaultMutableTreeNode(taskListNode.getNodeName());
			root.add(tasks);
			System.out.println(taskListNode.getNodeName());
			//End tasks node
			//Task node
			if(taskListNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element taskNodeListElement = (Element) taskListNode;
				NodeList taskNL = taskNodeListElement.getElementsByTagName("task");
				for(int taskNum = 0; taskNum < taskNL.getLength(); ++taskNum)
				{
					Element taskElement;
					if((taskElement = getElement(taskNL.item(taskNum))) != null)
					{
						//questList.add(new QuestTask(taskElement));
						QuestTask questTask = new QuestTask(taskElement);
						DefaultMutableTreeNode task = new DefaultMutableTreeNode(questTask);
						
						Set<Entry<String, String>> questMapEntry = questTask.dataMap.entrySet();
												
						for(Entry<String, String> entry : questMapEntry)
						{
							if(!showNulls && !entry.getValue().equals(""))
							{
								QuestEntry convertedQuestmapEntry = new QuestEntry(entry);
								DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(convertedQuestmapEntry);
								entryNode.add(new DefaultMutableTreeNode(convertedQuestmapEntry.getValue()));
								task.add(entryNode);
							}
						}
						
						tasks.add(task);
					}
				}
			}
			//End task node

			return new JTree(root);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JTree();	
	}
	
	private static Element getElement(Node node)
	{
		if(node.getNodeType() == Node.ELEMENT_NODE)
		{
			return (Element) node;
		}
		return null;
	}
	
	private class QuestTask
	{

		String type;
		String id;
		String taskOnFail;
		Map<String, String> dataMap;
		
		public QuestTask(Element taskElement)
		{
			dataMap = new TreeMap<String, String>();
			this.type = taskElement.getAttribute("type");
			this.id = taskElement.getAttribute("id");
			this.taskOnFail = taskElement.getAttribute("taskOnFail");
			
			NodeList dataNL = taskElement.getElementsByTagName("data");
			for(int dataNodeNum = 0; dataNodeNum < dataNL.getLength(); ++dataNodeNum)
			{
				//Data nodes
				Node dataNode = dataNL.item(dataNodeNum);
				Element dataElement;
				if((dataElement = getElement(dataNode)) != null)
				{
					//System.out.println(dataElement.getAttribute("name") + " = " + dataElement.getAttribute("value"));
					dataMap.put(dataElement.getAttribute("name"), dataElement.getAttribute("value"));
				}
				//End data nodes
			}
			
			System.out.println(this.toString());
		}

		@Override
		public String toString() {
			return String.format("Quest Task %s", id);
		}
		
		
	}
	
	private class QuestEntry<K extends Comparable<? super K>, V extends Comparable<? super V>> extends SimpleEntry<K, V> implements Comparable<QuestEntry<K, V>>
	{

		public QuestEntry(Entry<? extends K, ? extends V> arg0) {
			super(arg0);
		}

		@Override
		public String toString() {
			return getKey().toString();
		}

		@Override
		public int compareTo(QuestEntry<K, V> otherQuest) {
			if(otherQuest != null && this != null)
			{
				int comparison = getKey().compareTo(otherQuest.getKey());
				if(comparison == 0)
				{
					comparison = getValue().compareTo(otherQuest.getValue());
				}
			}
			return 0;
		}

		
	}
}
