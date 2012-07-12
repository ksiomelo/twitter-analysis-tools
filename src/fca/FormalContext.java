package fca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import experiment.Constants;

import lattice.util.concept.DefaultFormalAttribute;
import lattice.util.concept.DefaultFormalObject;
import lattice.util.concept.FormalAttribute;
import lattice.util.concept.FormalObject;
import twitter.io.Input;
import twitter.model.UserImpl;
import twitter4j.User;
import utils.CollectionUtils;

public class FormalContext {
	 PrintStream ps;
	 public static final double MIN_EXT_SUPP = 0.005;
	 public static final double MIN_INT_SUPP = 6;
	 
	 
	 public FormalContext() {
		 
	 }
	 
	 /*
	  * READS FORMAT: 576967212#~#PuspaCiaaciaCia iya mii%0.982531%,aku tau mii%0.830347%
	  */
	 public void convertTermFileToFormalContext(String usersFile, String inputFile, String outputFile) throws Exception {
		 
		 List<User> users = Input.readUsers(usersFile);
		 
		 
		 
		 FileOutputStream fout = new FileOutputStream(new File(outputFile));
		   ps = new PrintStream(fout);//.print(connection.getInputStream());
		   
		ArrayList<String> attributes = new ArrayList<String>();
		ArrayList<String> objects = new ArrayList<String>();
		//ArrayList<ArrayList<Boolean>> rel = new ArrayList<ArrayList<Boolean>>();
		HashMap<String, Set<String>> objAttrs = new HashMap<String, Set<String>>();
		
		// read input file line by line
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line = null;

		line = reader.readLine();

		int count = 0;
		int outUsersCount = 0;
		
		HashMap<String,Integer> attrCount = new HashMap<String, Integer>();

		while (line != null) {
			
			Set<String> curRel = new HashSet<String>();
			
			String [] lineArray = line.split(Constants.NEW_IDENTIFIER); // format: 576967212#~#PuspaCiaaciaCia iya mii%0.982531%,aku tau mii%0.830347%
			line = reader.readLine();
			
			if (lineArray.length < 2) {
				//System.out.println("no terms for this person");
				outUsersCount++;
				continue;
			}
			
			
			int userIdx = users.indexOf(new UserImpl(Long.parseLong(lineArray[0].replaceAll( "[^\\d]", "" ))));
			String screeName = users.get(userIdx).getScreenName();
			
			int indexObj = CollectionUtils.addIfNotThere(objects, screeName); //objects.add(lineArray[1]); // juanluco
			
			// for each term...
			
			
			String [] terms = lineArray[1].split(",");
			for (int i = 0; i < terms.length; i++) {
				
				String attrName = terms[i].split("%")[0];
				
				int indexAtt = this.addIfNotThere(attributes, attrName);
				
				if (attrCount.containsKey(attrName)) {
					attrCount.put(attrName, attrCount.get(attrName)+1);
				} else {
					attrCount.put(attrName, 1);
				}
				
				
				this.addIfNotThere(curRel, attrName);
				
				//curRel.add(indexAtt);
			}
			
			// add the relationship
			objAttrs.put(screeName, curRel);
			
			
			count++;
		}
		
		System.out.println("finished scaling. Total excluded users: "+outUsersCount);
		
		// select attributes to removal
		ArrayList<String> toRemove = new ArrayList<String>();
		for (int i = 0; i < attributes.size(); i++) {
			if (attrCount.get(attributes.get(i)) < MIN_INT_SUPP) {
				
				toRemove.add(attributes.get(i));
				
			}
		}
		
		// Remove attributes
		System.out.println("Removing attributes by min supp "+MIN_INT_SUPP + ". To be removed: "+toRemove.size() +"/"+ attributes.size());
		for (String attrName : toRemove) {
			attributes.remove(attrName);
			
			for(Set objAttrNames : objAttrs.values()) {
				objAttrNames.remove(attrName);
			}
			
		}
		
		// Select objects to removal
		toRemove = new ArrayList<String>();
		for (int i = 0; i < objects.size(); i++) {
			
			
			if (objAttrs.get(objects.get(i)).isEmpty()) {
				
				toRemove.add(objects.get(i));
				
			}
		}
		
		System.out.println("Removing empty objects. To be removed: "+toRemove.size() +"/"+ objects.size());
		for (String objName : toRemove) {
			objects.remove(objName);
			objAttrs.remove(objName);
			
		}
		
		saveToFormalContext(attributes, objects, objAttrs);
		
	 }
	 
 public void convertTermFileToFormalContext(String inputFile, String outputFile) throws Exception {
		 
		 
		 FileOutputStream fout = new FileOutputStream(new File(outputFile));
		   ps = new PrintStream(fout);//.print(connection.getInputStream());
		   
		ArrayList<String> attributes = new ArrayList<String>();
		ArrayList<String> objects = new ArrayList<String>();
		//ArrayList<ArrayList<Boolean>> rel = new ArrayList<ArrayList<Boolean>>();
		HashMap<String, Set<String>> objAttrs = new HashMap<String, Set<String>>();
		
		// read input file line by line
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line = null;

		line = reader.readLine();

		int count = 0;
		int outUsersCount = 0;
		
		HashMap<String,Integer> attrCount = new HashMap<String, Integer>();

		while (line != null) {
			
			Set<String> curRel = new HashSet<String>();
			
			String [] lineArray = line.split(Constants.NEW_IDENTIFIER); // format: 576967212#~#PuspaCiaaciaCia iya mii%0.982531%,aku tau mii%0.830347%
			line = reader.readLine();
			
			if (lineArray.length < 2) {
				//System.out.println("no terms for this person");
				outUsersCount++;
				continue;
			}
			
			
			//int userIdx = this.addIfNotThere(users, new UserImpl(lineArray[0]));//users.indexOf(new UserImpl(Long.parseLong(lineArray[0].replaceAll( "[^\\d]", "" ))));
			//String screeName = users.get(userIdx).getScreenName();
			
			int indexObj = CollectionUtils.addIfNotThere(objects, lineArray[0]); //objects.add(lineArray[1]); // juanluco
			
			// for each term...
			
			
			String [] terms = lineArray[1].split(",");
			for (int i = 0; i < terms.length; i++) {
				
				String attrName = terms[i].split("%")[0];
				
				int indexAtt = this.addIfNotThere(attributes, attrName);
				
				if (attrCount.containsKey(attrName)) {
					attrCount.put(attrName, attrCount.get(attrName)+1);
				} else {
					attrCount.put(attrName, 1);
				}
				
				
				this.addIfNotThere(curRel, attrName);
				
				//curRel.add(indexAtt);
			}
			
			// add the relationship
			objAttrs.put(lineArray[0], curRel);
			
			
			count++;
		}
		
		System.out.println("finished scaling. Total excluded users: "+outUsersCount);
		
		// select attributes to removal
		ArrayList<String> toRemove = new ArrayList<String>();
		for (int i = 0; i < attributes.size(); i++) {
			if (attrCount.get(attributes.get(i)) < MIN_INT_SUPP) {
				
				toRemove.add(attributes.get(i));
				
			}
		}
		
		// Remove attributes
		System.out.println("Removing attributes by min supp "+MIN_INT_SUPP + ". To be removed: "+toRemove.size() +"/"+ attributes.size());
		for (String attrName : toRemove) {
			attributes.remove(attrName);
			
			for(Set objAttrNames : objAttrs.values()) {
				objAttrNames.remove(attrName);
			}
			
		}
		
		// Select objects to removal
		toRemove = new ArrayList<String>();
		for (int i = 0; i < objects.size(); i++) {
			
			
			if (objAttrs.get(objects.get(i)).isEmpty()) {
				
				toRemove.add(objects.get(i));
				
			}
		}
		
		System.out.println("Removing empty objects. To be removed: "+toRemove.size() +"/"+ objects.size());
		for (String objName : toRemove) {
			objects.remove(objName);
			objAttrs.remove(objName);
			
		}
		
		saveToFormalContext(attributes, objects, objAttrs);
		
	 }
 
	public void OLDconvertTermExtractorFileToFormalContext(String inputFile, String outputFile) throws Exception {
		 FileOutputStream fout = new FileOutputStream(new File(outputFile));
		   ps = new PrintStream(fout);//.print(connection.getInputStream());
		   
		HashSet<String> attributes = new HashSet<String>();
		HashSet<String> objects = new HashSet<String>();
		//ArrayList<ArrayList<Boolean>> rel = new ArrayList<ArrayList<Boolean>>();
		HashMap<Integer, ArrayList<Integer>> objAttrs = new HashMap<Integer, ArrayList<Integer>>();
		
		// read input file line by line
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line = null;

		line = reader.readLine();

		int count = 0;
		
		HashMap<String,Integer> attrCount = new HashMap<String, Integer>();

		while (line != null) {
			
			ArrayList<Integer> curRel = new ArrayList<Integer>();
			
			String [] lineArray = line.split("#\\*#"); // format: 10222421#*#juanluco#*#buzz,world
			line = reader.readLine();
			
			if (lineArray.length < 3) continue;
			
			int indexObj = CollectionUtils.addIfNotThere(objects, lineArray[1]); //objects.add(lineArray[1]); // juanluco
			
			
			
			// for each term...
			String [] terms = lineArray[2].replace("\"", "").split(",");
			for (int i = 0; i < terms.length; i++) {
				int indexAtt = this.addIfNotThere(attributes, terms[i]);
				
				if (attrCount.containsKey(terms[i])) {
					attrCount.put(terms[i], attrCount.get(terms[i])+1);
				} else {
					attrCount.put(terms[i], 1);
				}
				
				curRel.add(indexAtt);
			}
			
			// add the relationship
			objAttrs.put(indexObj, curRel);
			
			
			count++;
		}
		
		System.out.println("Filtering attributes with min_supp: "+FormalContext.MIN_EXT_SUPP);
		// filter min_supp
		Object [] attrArray =  attributes.toArray();
		for (int i = 0; i < attrArray.length; i++) {
			String tattr = (String) attrArray[i];
			System.out.println(((double)attrCount.get(tattr)/(double)objects.size()));
			
			if (((double)attrCount.get(tattr)/(double)objects.size()) < FormalContext.MIN_EXT_SUPP) {
				
				for (int j = 0; j < objects.size(); j++) {
					ArrayList<Integer> relArray = objAttrs.get(j);
					
					if (relArray != null && relArray.contains(i))
							relArray.remove(new Integer(i));
				}
				attributes.remove(tattr);
			}
		}
		
		
		System.out.println("Cleaning empty objects..."); // TODO arraylist
		// remove 0 attribute objects
		Iterator it2 = objects.iterator();
		ArrayList toremove = new ArrayList();
		
		int idx = 0; // TODO it does not garanteee!!
		while(it2.hasNext()) {
			String objt= (String) it2.next();
			ArrayList rels = objAttrs.get(idx);
			if (rels.size() == 0) {
				//toremove.add(idx);\
			}
			
			idx++;
		}
		//objects.removeAll(toremove);
		
		
		
		//saveToFormalContext(attributes, objects, objAttrs, toremove);
	}
	
	
public int addIfNotThere(Collection<String> c, String a) {
		
		Iterator it = c.iterator();
		int index = 0;
		while(it.hasNext()){
			String b = (String) it.next();
			
			if (b.equalsIgnoreCase(a)) return index;
			
			index++;
		}
		
		c.add(a);
		
		return c.size()-1; // return the index of last added element
		
		
	}
	
	public static AdvancedBinaryRelation loadFormalContext(String inputFile) throws Exception{
		
		// read input file line by line
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line = null;

		line = reader.readLine(); // B
		line = reader.readLine(); // 
		int nbObjs = Integer.parseInt(reader.readLine());
		int nbAtts = Integer.parseInt(reader.readLine());

		AdvancedBinaryRelation rel = new AdvancedBinaryRelation();//(nbObjs, nbAtts);
		line = reader.readLine(); //
		line = reader.readLine(); // first obj
		int counter = 0;
		int matrixRowIndex = 0;
		while (line != null) {
			
			if (counter < nbObjs ){ // objects
				FormalObject fo = new DefaultFormalObject(line);
				rel.addObject(fo);
			} else if (counter < nbObjs+nbAtts) { // attributes
				FormalAttribute fa = new DefaultFormalAttribute(line);
				rel.addAttribute(fa);
			} else { // matrix
				//System.out.println("[SAVE CONTEXT] Reading Matrix");
				for (int i = 0; i < line.length(); i++) {
					rel.setRelation(matrixRowIndex, i, (line.charAt(i) != '.') );
				}
				matrixRowIndex ++;
			}
			
			counter++;
			line = reader.readLine();
		}
		
		return rel;
	}
	
	
	/*
	 * Saves the array of attributes and objects in a CXT file
	 */
	public void saveToFormalContext(List<String> attributes, List<String> objects, HashMap<String, Set<String>> objAttrs){
		
		System.out.println("creating formal context...");
		ps.println("B");
		ps.println();
		ps.println(objects.size()); 
		ps.println(attributes.size());
		ps.println();

		for (String obj : objects) {
				ps.println(obj);
		}
		
		for (String attr : attributes) {
				ps.println(attr);
		}
		
		
		for (int i = 0; i < objects.size(); i++) {
			
			for (int j = 0; j < attributes.size(); j++) {
				if (objAttrs.containsKey(objects.get(i)) && objAttrs.get(objects.get(i)).contains(attributes.get(j))) ps.print("X");
				else ps.print(".");
			}
			ps.print("\n");
		}
		
		ps.close();
		
		
	}
	
	
	public static void main(String[] args) {
		System.out.println("Starting creation of formal context..");
		FormalContext fc = new FormalContext();
		try {
			
			fc.convertTermFileToFormalContext(
					"C:\\data\\twitter6-cluster-10-terms.txt",
					"C:\\data\\twitter6-cluster-10-supp_4.cxt");
			
//			fc.convertTermFileToFormalContext("/Users/cassiomelo/Dropbox/code/twitter-analysis-tools/data/twitter4-users.txt",
//					"/Users/cassiomelo/Dropbox/code/twitter-analysis-tools/data/twitter4-3-3-clusters_statuses_terms-alchemy.txt",
//					"/Users/cassiomelo/Dropbox/code/twitter-analysis-tools/data/twitter4-3-3-clusters_statuses_terms-alchemy.cxt");
			System.out.println("creation of formal context: \t\t OK!! ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
