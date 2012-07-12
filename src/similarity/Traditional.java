package similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lattice.util.concept.Extent;
import lattice.util.concept.FormalObject;
import lattice.util.concept.Intent;
import lattice.util.concept.SetExtent;
import lattice.util.concept.SetIntent;

import org.apache.commons.collections.CollectionUtils;

import utils.FcaUtils;
import fca.AdvancedBinaryRelation;

public class Traditional {

	
	public double[][] computeJaccardDistanceMatrix(AdvancedBinaryRelation context){
		
		
		ArrayList<FormalObject> objects = new ArrayList<FormalObject>(context.getObjectSet());
		
		double[][] array = new double[objects.size()][objects.size()];
		
		for (int i = 0; i < objects.size(); i++) {
			FormalObject a = objects.get(i);
			
			for (int j = 0; j < objects.size(); j++) {
				FormalObject b = objects.get(j);
				
				if (!a.equals(b)) { // to assure I'm not comparing the same concepts
					
					if (array[j][i] != 0) array[i][j] = array[j][i]; //TODO comparar != null // symmetric
					

					Extent extentA = new SetExtent();
					extentA.add(a);
					
					Intent intentA = FcaUtils.getIntentOf(context, extentA);
					
					
					Extent extentB = new SetExtent();
					extentB.add(b);
					
					Intent intentB = FcaUtils.getIntentOf(context, extentB);
					
//					Intent union = new SetIntent();
//					union.addAll(intentA);
//					union.addAll(intentB);
					
					Collection union = CollectionUtils.union(intentA, intentB);
					
					// intersection:
//					intentA.retainAll(intentB);
					
					Collection intersect = CollectionUtils.intersection(intentA, intentB);
					
					
					double jaccardIdx = (double) intersect.size()/(double) union.size();
					
					double d = Math.random();
					if (d > 0.5) { //move closer
						jaccardIdx = d*jaccardIdx;
					} else { // move farther
						jaccardIdx = (1-jaccardIdx)*d + jaccardIdx;
					}
					
					array[i][j] = 1-jaccardIdx;
					
					//System.out.println(a + " - " + b + " = "+ array[i][j]);
					
				} //else {}
				
				
			}
		}
		
		
		
		
		return array;
	}
	
	
}
