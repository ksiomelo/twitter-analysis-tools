package similarity;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.Graph;
import fca.AdvancedBinaryRelation;
import fca.ConceptEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import lattice.util.concept.Concept;
import lattice.util.concept.Extent;
import lattice.util.concept.FormalObject;
import lattice.util.concept.Intent;
import lattice.util.structure.CompleteConceptLattice;
import lattice.util.structure.ConceptNode;

import org.apache.commons.collections15.Transformer;

import utils.FcaUtils;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class FCASimilarity {
	

	private AdvancedBinaryRelation context;
	private CompleteConceptLattice lattice;
	
	public static final short CONCEPT_SIMILARITY = 1;
	public static final short PROXIMITY = 2;
	public static final short CONNEXTION_STRENGTH = 3;
	public static final short PROXIMITY_AND_STRENGTH = 4;
	
	
	public FCASimilarity(AdvancedBinaryRelation ctx, CompleteConceptLattice lat){
		this.context = ctx;
		this.lattice = lat;
	}
	
	Transformer<ConceptEdge, Double> wtTransformer = new Transformer<ConceptEdge, Double>() {
		public Double transform(ConceptEdge link) {
			return link.getSimilarity();
		}
	};
	
	
	/*
	 * Creates a table of distances between concepts based on conceptsimilarity (Dice coefficient)
	 */
	public ArrayList<FormalObject> objects;
	
	public double[][] computeFCASimilarityMatrix(Hashtable objToPeople, short metric, double alpha){
		objects = new ArrayList<FormalObject>();
		
		int nbObjs = context.getObjectsNumber();
		
		double[][] array = new double[nbObjs][nbObjs];
		
		Iterator it = context.getObjectSet().iterator();
		Iterator it2 = context.getObjectSet().iterator();
		
		int i = 0;
		while(it.hasNext()){ // iterate over objects
			
			FormalObject A = (FormalObject) it.next();
			objects.add(A);
			
			int j = 0;
			while(it2.hasNext()){
				FormalObject B = (FormalObject) it2.next();
				
				if (A != B ) { // to assure I'm not comparing the same concepts
					
					if (array[j][i] != 0) array[i][j] = array[j][i]; //TODO comparar != null // if it was already calculated, reflect
					
					else {
						ConceptNode cA = FcaUtils.getMostSpecificConceptContaining(lattice, A);
						ConceptNode cB =  FcaUtils.getMostSpecificConceptContaining(lattice, B);
						
						double val;
						
						switch(metric) {
							case PROXIMITY_AND_STRENGTH :
								val = this.proximityAndStrength(cA, cB, FcaUtils.LatticeToGraph(context, lattice, true), alpha); // TODO usar AR
								break;
							case CONCEPT_SIMILARITY :
								val = this.conceptSimilarity(cA, cB);
								break;
							case PROXIMITY :
								val = this.proximity(cA, cB, FcaUtils.LatticeToGraph(context, lattice, true));
								break;
							case CONNEXTION_STRENGTH :
								val = this.connectionStrength(cA, cB, FcaUtils.LatticeToGraph(context, lattice, true));
								break;
							default :
								val = this.conceptSimilarity(cA, cB);
								break;
						}
						
								
								
						if (Double.isNaN(val))
							array[i][j] = 0;
						else 
							array[i][j] = val;
					}
				}
				j++;
			}
			i++;
			it2 = context.getObjectSet().iterator();
		}
		
		return array;
	}
	
	
	/* ****************************************
	 * Conceptual measures
	 * ****************************************/
	
	/*
	 * Computes concept similarity [Evaluating term concept association measures for short text expansion ...]
	 * - this is the Dice coefficient for two concepts
	 */
	public Double conceptSimilarity(ConceptNode a, ConceptNode b){
		
		Double sim = null;//new Double(0);
		
		//a.
		Concept conceptA = a.getConcept();
		Concept conceptB = b.getConcept();
		
		Extent aext = conceptA.getExtent();
		Extent bext = conceptB.getExtent();
		
		Extent aIntB = aext.intersection(bext);
		
		Intent aextInt = FcaUtils.getIntentOf(context, aext);
		Intent bextInt = FcaUtils.getIntentOf(context, bext);
		
		Intent aextIntIntbaextInt = aextInt.intersection(bextInt);
		
		double t1 = (double)aIntB.size()/(aext.size() + bext.size());
//		double t2b = (aextInt.size() + bextInt.size());
//		double t2a = aextIntIntbaextInt.size();
		double t2 = (double)aextIntIntbaextInt.size()/(aextInt.size() + bextInt.size());
		
		sim = new Double(t1+t2);
		
		return sim;
		
	}
	
	/*
	 * Computes concept proximity [Evaluating term concept association measures for short text expansion ...]
	 * - this is the normalized topological [unweighted] distance between two concepts.
	 */
	public Double proximity(ConceptNode a, ConceptNode b, Graph<ConceptNode, ConceptEdge> latticeGraph){
		
		DijkstraShortestPath<ConceptNode, ConceptEdge> distance = new DijkstraShortestPath<ConceptNode, ConceptEdge>(latticeGraph);
		double diameter = DistanceStatistics.diameter(latticeGraph, distance, true);
		
		 double shortDist = (Double) distance.getDistance(a, b);
		 
		 return 1-(shortDist/diameter);
		
	}
	
	
	/*
	 * Computes concept connection strength [Evaluating term concept association measures for short text expansion ...]
	 * - this is the sum of the shortest path between two concepts (when edges are weighted by similarity)
	 */
	public Double connectionStrength(ConceptNode a, ConceptNode b, Graph<ConceptNode, ConceptEdge> latticeGraph){
		
		DijkstraShortestPath<ConceptNode, ConceptEdge> dsp = new DijkstraShortestPath<ConceptNode, ConceptEdge>(latticeGraph, wtTransformer);
		 double shortDist = (Double) dsp.getDistance(a, b);
		 
		 return shortDist;
	}
	
	
	/*
	 * Computes proximity and strength [Evaluating term concept association measures for short text expansion ...]
	 * - here proximity and strength are combined by a given factor alpha
	 */
	public Double proximityAndStrength(ConceptNode a, ConceptNode b, Graph<ConceptNode, ConceptEdge> latticeGraph, double alpha){
		
		
		double proximity = this.proximity(a, b, latticeGraph);
		double strength = this.connectionStrength(a, b, latticeGraph);

		return alpha*proximity + (1-alpha)*strength;
	}
	
	
	
	public void saveAsSocialNetwork(double[][]simMatrix, String ouputfile, double thres) throws IOException {
		FileOutputStream fout = new FileOutputStream(new File(ouputfile));
		 PrintStream ps = new PrintStream(fout);
		 
		 
		 for (int i = 0; i < simMatrix.length; i++) {
			for (int j = 0; j < simMatrix.length; j++) {
				if (i!=j && simMatrix[i][j] > thres) {
					String pi = objects.get(i).getName();
					String pj = objects.get(j).getName();
					ps.println(pi+";"+pj);
				}
			}
		}
		
		 ps.close();
		 fout.close();
		
	}
	
	
	public void saveAsCSVMatrixFile(double[][]simMatrix, String ouputfile, double thres) throws IOException {
		FileOutputStream fout = new FileOutputStream(new File(ouputfile));
		 PrintStream ps = new PrintStream(fout);
		 
		 int count = 0;
		 
		 for (int i = 0; i < simMatrix.length; i++) {
			 ps.print(";"+objects.get(i).getName());
		}
			 
		 
		 for (int i = 0; i < simMatrix.length; i++) {
			 ps.print("\n");
			for (int j = 0; j < simMatrix.length; j++) {
				if (j == 0) {
					 ps.print(objects.get(i).getName()+";");
				}
				if (i!=j && simMatrix[i][j] > thres) 
					ps.print(simMatrix[i][j]);
				else ps.print("0");
				if (j < simMatrix.length-1) {
					 ps.print(";");
				}
			}
		}
		
		 ps.close();
		 fout.close();
		
	}
	
	
	
	
}
