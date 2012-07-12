package fca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import lattice.util.concept.FormalAttribute;
import lattice.util.relation.BinaryRelation;
import utils.CollectionUtils;
import experiment.Constants;

public class AdvancedBinaryRelation extends BinaryRelation {

	
	
	public AdvancedBinaryRelation() {
		super("context");
	}
	
	public AdvancedBinaryRelation(int nbObjs, int nbAtts) {
		super(nbObjs, nbAtts);
		// TODO Auto-generated constructor stub
	}
	
	
	public Vector<FormalAttribute> getAttributeSet() {
		return attributeSet;
	}
	
	public Vector getHasProperty() {
		return hasProperty;
	}
	
	public Vector getObjectSet() {
		return objectSet;
	}
	
	public String getWordleString(){
		
		ArrayList<String> stopWords = new ArrayList<String>(Arrays.asList(Constants.STOP_WORDS));
		String ret = "";
		for (FormalAttribute fa: this.getAttributeSet()) {
			
			String [] words = fa.getName().split(" ");
			boolean skip = false;
			for (int i = 0; i < words.length; i++) {
				if (CollectionUtils.containsIgnoreCase(stopWords,words[i].replace("\"", ""))) {
					skip = true;
					break;
				}
				
			}
			
			if (!skip) ret += fa.getName() + " ";
			
			
		}
		return ret;
	}

}
