package gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author wallaceg09
 * 
 * License: http://opensource.org/licenses/GPL-3.0
 *
 */
public class QFileChooser extends JFileChooser{
	//Change this to suit your filepath structure.
	private static String baseFilepath = "G:\\ExternalGit\\ProjectSWG";
	public QFileChooser()
	{
		this.setCurrentDirectory(new File(baseFilepath + "\\clientdata\\quest"));
		/*
		this.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return ".qst";
			}
			
			@Override
			public boolean accept(File f) {
				if(f.isDirectory())
				{
					return true;
				}
				//FIXME not working.
				String[] split = f.getName().split(".");
				if(split.length >0)
				{
					String extension = split[split.length-1];
					if(extension.toLowerCase() == "qst"){
						return true;
					}					
				}
				return false;
			}
		});*/
	}
}
