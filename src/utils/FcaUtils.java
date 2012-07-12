package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lattice.util.concept.Concept;
import lattice.util.concept.Extent;
import lattice.util.concept.FormalAttributeValue;
import lattice.util.concept.FormalObject;
import lattice.util.concept.Intent;
import lattice.util.concept.SetIntent;
import lattice.util.relation.BinaryRelation;
import lattice.util.structure.CompleteConceptLattice;
import lattice.util.structure.ConceptNode;

import org.apache.commons.collections.CollectionUtils;

import similarity.FCASimilarity;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import fca.AdvancedBinaryRelation;
import fca.ConceptEdge;

public class FcaUtils {

	public static AdvancedBinaryRelation exampleContext() {
		AdvancedBinaryRelation context;

		context = new AdvancedBinaryRelation(5, 4);
		context.setRelation(0, 0, true);
		context.setRelation(0, 3, true);
		context.setRelation(1, 1, true);
		context.setRelation(1, 2, true);
		context.setRelation(2, 0, true);
		context.setRelation(2, 1, true);
		context.setRelation(2, 2, true);
		context.setRelation(3, 3, true);
		context.setRelation(4, 2, true);
		
		return context;

	}
	
	public static BinaryRelation exampleStability() {
		BinaryRelation context;

		context = new BinaryRelation(8, 4);
		context.setRelation(0, 0, true);
		context.setRelation(1, 1, true);
		context.setRelation(1, 2, true);
		context.setRelation(2, 1, true);
		context.setRelation(2, 3, true);
		context.setRelation(3, 1, true);
		context.setRelation(3, 2, true);
		context.setRelation(4, 0, true);
		context.setRelation(5, 0, true);
		context.setRelation(5, 1, true);
		context.setRelation(5, 2, true);
		context.setRelation(6, 2, true);
		context.setRelation(7, 1, true);
		context.setRelation(7, 3, true);
		
		return context;

	}
	
	public static BinaryRelation exampleStability2() {
		BinaryRelation context;

		context = new BinaryRelation(8, 4);
		context.setRelation(0, 0, true);
		context.setRelation(1, 1, true);
		context.setRelation(1, 2, true);
		context.setRelation(2, 1, true);
		context.setRelation(2, 3, true);
		context.setRelation(3, 1, true);
		context.setRelation(3, 2, true);
		context.setRelation(4, 0, true);
		context.setRelation(5, 0, true);
		context.setRelation(5, 1, true);
		context.setRelation(5, 2, true);
		context.setRelation(6, 2, true);
		context.setRelation(7, 1, true);
		context.setRelation(7, 3, true);
		
		return context;

	}
	
	
	
	/* **********************************************
	 *  Auxiliar functions
	 * **********************************************/
	
	/*
	 * Transforms a similarity matrix in a normalized dissimilarity matrix
	 */
	public static void convertSimToDissimilarityMatrix(double[][]sim) {
		//double[][] dissim = new double [sim.length][sim.length];
		
		double max = Double.MIN_VALUE;
		
		//find max value
//		for (int i = 0; i < sim.length; i++) {
//			for (int j = 0; j < sim.length; j++) {
//				if (sim[i][j] > max) max = sim[i][j];
//			}
//		}
//		
//		
//		// norms
//		for (int i = 0; i < sim.length; i++) {
//			for (int j = 0; j < sim.length; j++) {
//				sim[i][j] = /*1.0-*/((double)((double)sim[i][j]/(double)max));
//			}
//		}
		
		
		for (int i = 0; i < sim.length; i++) {
			for (int j = 0; j < sim.length; j++) {
				sim[i][j] = 1.0-sim[i][j];
			}
		}
		
		//return sim;//dissim;
		
		
	}
	
	/*
	 * Gets the lower most concept containing a given object
	 */
	public static ConceptNode getMostSpecificConceptContaining(CompleteConceptLattice lattice, FormalObject fo) {
		lattice.setBottom(lattice.findBottom());
		//Iterator it = lattice.bottomUp();//iterator();
		ArrayList<ConceptNode> bottomUp = new ArrayList<ConceptNode>();
		
		bottomUp.add(lattice.getBottomConceptNode());
		
		for (int i = 0; i < bottomUp.size(); i++) {
			
			ConceptNode p = (ConceptNode) bottomUp.get(i);
			Concept c = p.getConcept();
			
			if (c.getExtent().contains(fo)) return p; // return conceptnode
			else  bottomUp.addAll(p.getParents());
		}
		return null;
	}
	
	/*
	 * Get the set of shared attributes for a set of objects (even if they're not formal concepts)
	 */
	public static Intent getIntentOf(AdvancedBinaryRelation context, Extent extent) {
		Intent result = new SetIntent();
		
		
		if (!extent.isEmpty()) {
			Collection collections = new ArrayList(context.getAttributeSet());
			Iterator it1 = extent.iterator();
			Object c1;
			
			while (it1.hasNext()) {
				Vector attrs = new Vector();
				c1 = it1.next();
				int idx = context.getObjectSet().indexOf(c1);
				Vector<FormalAttributeValue> rel = (Vector<FormalAttributeValue>) context
						.getHasProperty().get(idx);

				for (int i = 0; i < rel.size(); i++) {
					FormalAttributeValue val = rel.get(i);
					
					if (val != null && !val.isEmpty()/*equals("0")*/) {
						attrs.add(context.getAttributeSet().get(i));
					}
				}
				collections = CollectionUtils.intersection(collections, attrs);
			}
			
			result.addAll(collections);
			
		} else { // empty set
			result.addAll(context.getAttributeSet());
		}
		return result;
	}
	
	/*
	 * Transforms the Galicia concept lattice in a JUNG Graph
	 */
	public static Graph<ConceptNode, ConceptEdge> LatticeToGraph(AdvancedBinaryRelation context, CompleteConceptLattice lattice, boolean computeSimilarity){
		
		Graph<ConceptNode, ConceptEdge> graph = new UndirectedSparseGraph<ConceptNode, ConceptEdge>();
		
		ArrayList<ConceptNode> concepts = new ArrayList<ConceptNode>();
		
		concepts.add(lattice.getTopConceptNode());
		
		int childCount = 0;
		int parentIdx = 0;
		ConceptNode parent = null;
		
		for (int i = 0; i < concepts.size(); i++) {
			ConceptNode current = (ConceptNode)concepts.get(i);
			
			if (!graph.containsVertex(current)) graph.addVertex(current);
			
			List children = current.getChildren();
			concepts.addAll(children); // adds to the list
			
			
			for (int j = 0; j < children.size(); j++) { // adds link
				ConceptNode theChild = (ConceptNode) children.get(j);
				
				ConceptEdge ce = new ConceptEdge(current.toString() + "-" +theChild.toString());
				
				if (computeSimilarity) { // if to include similarity in the edge
					FCASimilarity cfca = new FCASimilarity(context, lattice);
					double sim = cfca.conceptSimilarity(current, theChild);
					ce.setSimilarity(sim);
				}
				
				graph.addEdge(ce, current, theChild);
			}
			
		}
		 
		return graph;
	}

}
