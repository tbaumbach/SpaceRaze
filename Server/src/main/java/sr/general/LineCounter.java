package sr.general;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LineCounter{
	private int topLimit = 0;
	private Map<String,Integer> topList = new HashMap<String,Integer>(); 
	private int nrInList = 500;
    
    public LineCounter(String basePathString){
    	long nowTime = new Date().getTime();
        System.out.println("Sum: " + countDir(new File(basePathString)));
        System.out.println("Count time " + (new Date().getTime() - nowTime) + " ms");
        System.out.println("");
		printTop();
    }
    
    private int countDir(File dirFile){
//    	System.out.println("Reading dir: " + dirFile.getPath());
    	int counter = 0;
		File[] sourceFiles = dirFile.listFiles(new SourcefileNameFilter());
		for (int i = 0; i < sourceFiles.length; i++) {
			File aSourceFile = sourceFiles[i];
			counter += count(aSourceFile);
		}
		File[] dirFiles = dirFile.listFiles(); 
		for (int i = 0; i < dirFiles.length; i++) {
			File aDirFile = dirFiles[i];
			if (aDirFile.isDirectory()){
				counter += countDir(aDirFile);
			}
		}
		return counter;
    }
    
    private void replaceLowest(String newKey, int newLines){
    	String lowestKey = getLowestKey();
    	topList.remove(lowestKey);
    	topList.put(newKey, newLines);
    }
    
    // förutsätter att map har 10 poster
    private String getLowestKey(){
    	int lowestValue = Integer.MAX_VALUE;
    	String lowestkey = null;
    	for (Iterator<String> iterator = topList.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			int value = topList.get(key).intValue();
			if (value < lowestValue){
				lowestValue = value;
				lowestkey = key;
			}
		}
    	
    	return lowestkey;
    }
    
    @SuppressWarnings("deprecation")
	private int count(File aSourceFile){
        int lines = 0;
    	if (aSourceFile.getName().contains(".java")){
	        try{
	            FileInputStream fis = new FileInputStream(aSourceFile);
	            DataInputStream d = new DataInputStream(fis);
	            String temp = d.readLine();
	            while (temp != null){
	                lines++;
	                temp = d.readLine();
	            }
	            d.close();
	        }
	        catch(FileNotFoundException e){
	            System.out.println("File not found" + e);
	        }
	        catch(IOException e){
	            System.out.println("IOException: " + e);
	        }
//	    	System.out.println("Reading file: " + aSourceFile.getPath() + ", liner= " + lines);
	    	if (lines > topLimit){
	    		if (topList.size() < nrInList){
	    			topList.put(aSourceFile.getPath(), lines);
	    		}else{
	    			replaceLowest(aSourceFile.getPath(),lines);
	    			topLimit = topList.get(getLowestKey()).intValue();
	    		}
//	    		printTopTen();
	    	}
    	}else{
//	    	System.out.print(".");
    	}
        return lines;
    }
    
    private int getNextLowerIndex(List<String> printList, int value){
    	int index = 0;
    	int found = -1;
    	while ((found == -1) & (index < printList.size())){
    		int tmpLines = Integer.valueOf(printList.get(index).substring(0,printList.get(index).indexOf(" "))).intValue();
    		if (tmpLines < value){
    			found = index;
    		}else{
    			index++;
    		}
    	}
    	return index;
    }
    
    private void printTop(){
    	System.out.println(nrInList + " största filerna");
    	System.out.println("---------------------");
    	// create sorted list
    	List<String> printList = new LinkedList<String>();
    	for (Iterator<String> iterator = topList.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			int value = topList.get(key).intValue();
			int index = getNextLowerIndex(printList,value);
			printList.add(index, value + " " + key);
    	}
    	// print
    	int counter = 1;
    	for (String string : printList) {
		    System.out.println(counter + "\t" + string.replace(" ", "\t"));
		    counter++;
		}
	    System.out.println("");
    }
    
    public static void main(String[] args){
//        new LineCounter("C:\\eclipseworkspace_spaceraze\\SpaceRaze\\src\\sr");
//        new LineCounter("C:\\cygwin\\home\\developer\\sj\\workspaces\\accurev\\Dev-Itrl-10.05\\projects\\Applications");
//        new LineCounter("C:\\Users\\Bodin\\EclipseWorkspace\\SpaceRaze\\src\\sr"); // 2011-01-13, 103067 rader
        new LineCounter("C:\\Users\\Bodin\\EclipseWorkspace\\SpaceRazeDroid\\src\\se"); // 2011-01-13, 10729 rader
        
    }
}
