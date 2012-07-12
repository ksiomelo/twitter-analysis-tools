package experiment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import lattice.algorithm.LatticeAlgorithm;
import lattice.iceberg.algorithm.BordatIceberg;
import lattice.util.concept.FormalObject;
import lattice.util.structure.CompleteConceptLattice;
import similarity.FCASimilarity;
import similarity.Traditional;
import utils.ClassicalMDSProjection;
import utils.FcaUtils;
import vis.ChartOne;
import vis.Scatterplot;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import fca.AdvancedBinaryRelation;
import fca.FormalContext;

public class Experiment {
	
	public static final int USER_TWEETS = 1;
	public static final int FRIENDS_TWEETS = 2;
	
	
	
	private static void plotChart(double [][] output, List<String> objects, boolean inverted ) {
		System.out.print("Visualizing... ");
		double [] xdata = new double [objects.size()];
		double [] ydata = new double [objects.size()];
		
		for(int i=0; i<output.length; i++) {  // output all coordinates
		    //System.out.println(output[0][i]+" "+output[1][i]);
		    
			if (inverted) {
				xdata[i] = output[0][i];
			    ydata[i] = output[1][i];
			} else {
			    xdata[i] = output[i][0];
			    ydata[i] = output[i][1];
			}
		}
		
		
		ChartOne frame = new ChartOne(xdata,ydata, objects);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 500, 500);
		frame.setTitle("MDS visualization");
		frame.setVisible(true);
	}
	
	
	// very ugly method did it with copy n paste to be quicker, needs to be refactored
	private static void plotChart(double [][] output1, double [][] output2, double [][] output3, List<String> objects1, List<String> objects2, List<String> objects3, boolean inverted ) {
		System.out.print("Visualizing... ");
		double [] xdata1 = new double [objects1.size()];
		double [] ydata1 = new double [objects1.size()];
		double [] xdata2 = new double [objects2.size()];
		double [] ydata2 = new double [objects2.size()];
		double [] xdata3 = new double [objects3.size()];
		double [] ydata3 = new double [objects3.size()];
		
		for(int i=0; i<output1.length; i++) {  // output all coordinates
		    //System.out.println(output[0][i]+" "+output[1][i]);
		    
			if (inverted) {
				xdata1[i] = output1[0][i];
			    ydata1[i] = output1[1][i];
			} else {
			    xdata1[i] = output1[i][0];
			    ydata1[i] = output1[i][1];
			}
		}
		
		for(int i=0; i<output2.length; i++) {  // output all coordinates
		    //System.out.println(output[0][i]+" "+output[1][i]);
		    
			if (inverted) {
				xdata2[i] = output2[0][i];
			    ydata2[i] = output2[1][i];
			} else {
			    xdata2[i] = output2[i][0];
			    ydata2[i] = output2[i][1];
			}
		}
		
		for(int i=0; i<output3.length; i++) {  // output all coordinates
		    //System.out.println(output[0][i]+" "+output[1][i]);
		    
			if (inverted) {
				xdata3[i] = output3[0][i];
			    ydata3[i] = output3[1][i];
			} else {
			    xdata3[i] = output3[i][0];
			    ydata3[i] = output3[i][1];
			}
		}
		
		
		Scatterplot frame = new Scatterplot(xdata1,ydata1,xdata2,ydata2,xdata3,ydata3,
				objects1,objects2,objects3);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 500, 500);
		frame.setTitle("Conceptual Similarity (MDS)");
		frame.setVisible(true);
	}
	
	
	
	public static Object[] computeTraditionalSimilarity(String contextFile) throws Exception {
		
		//double [][] sample = (double[][]) Sample.denmarkCitiesSample()[0];
//		List names = (List) Sample.denmarkCitiesSample()[1];
		
		
		AdvancedBinaryRelation context = FormalContext.loadFormalContext(contextFile);
		
		//AdvancedBinaryRelation context = FormalContext.loadFormalContext("C:\\data\\music.cxt");


		// get objects ordered list (not the original set)
		ArrayList<String> objectNames = new ArrayList<String>();
		Iterator it = context.getObjectSet().iterator();
		while (it.hasNext()) {
			FormalObject fo = (FormalObject) it.next();
			objectNames.add(fo.getName());
		}
		
		// calcular distancia
		Traditional jaccSim = new Traditional();
		double[][] dist = jaccSim.computeJaccardDistanceMatrix(context);
		
		// save table as graph format
		
//		int n=dist[0].length;    // number of data objects
//		double[][] output=MDSJ.classicalScaling(dist,n); // apply MDS
		
		ClassicalMDSProjection mds = new ClassicalMDSProjection();
		
		DoubleMatrix2D matrix = new DenseDoubleMatrix2D(dist);
		
		DoubleMatrix2D res = mds.project(matrix);
		
		
		return new Object[]{res.toArray(),objectNames };
	}
	
	
	
	public static void runTraditionalExperiment(){
		try {
						
			// transformar cxt em binary relation
			// "/Users/cassiomelo/Dropbox/code/twitter-analysis-tools/data/music.cxt"
			//"/Users/cassiomelo/Dropbox/code/twitter-analysis-tools/data/twitter4-3-3-clusters_statuses_terms-alchemy.cxt"
			
			// SERIES 1
			System.out.println("context 1");
			Object[] res1 = computeTraditionalSimilarity("/Users/cassiomelo/code/datasets/Twitter/twitter6-cluster-10-supp_4.cxt");
			
			double [][] sim1 = (double[][]) res1[0];
			List<String> names1 = (List<String>) res1[1];
			
			// SERIES 1
			System.out.println("context 2");
			Object[] res2 = computeTraditionalSimilarity("/Users/cassiomelo/code/datasets/Twitter/twitter6-cluster-3-supp_4.cxt");
			
			double [][] sim2 = (double[][]) res2[0];
			List<String> names2 = (List<String>) res2[1];
			
			// SERIES 1
			System.out.println("context 3"); //
			Object[] res3 = computeTraditionalSimilarity("/Users/cassiomelo/code/datasets/Twitter/twitter6-cluster-2-supp_6.cxt");
			
			double [][] sim3 = (double[][]) res3[0];
			List<String> names3 = (List<String>) res3[1];

			// visualize
			System.out.println("visualizing...");
			plotChart(sim1, sim2, sim3, names1, names2, names3, false);
			
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void runFCAExperiment(){
		try {
			
			// transformar cxt em binary relation
			System.out.print("Loading context file... ");
			AdvancedBinaryRelation context = FormalContext.loadFormalContext("C:\\data\\twitter6-cluster-10-supp_4.cxt");
			//AdvancedBinaryRelation context = FormalContext.loadFormalContext("C:\\data\\music.cxt");
			System.out.print("OK\n");
			
			ArrayList<String> objectNames = new ArrayList<String>();
			Iterator it = context.getObjectSet().iterator();
			while (it.hasNext()) {
				FormalObject fo = (FormalObject) it.next();
				objectNames.add(fo.getName());
			}
			
			
			// calculate concepts
			System.out.print("Calculating concept lattice... ");
			LatticeAlgorithm algo = new BordatIceberg(context);
			algo.doAlgorithm();
			CompleteConceptLattice lattice = algo.getLattice();
			System.out.print("OK\n");
			
			// calcular distancia
			System.out.print("Calculating similarity... ");
			FCASimilarity fcaSim = new FCASimilarity(context, lattice);
			double[][] sim = fcaSim.computeFCASimilarityMatrix(null,FCASimilarity.PROXIMITY_AND_STRENGTH, 0.8);
			System.out.print("OK\n");
			
			
			//FcaUtils.convertSimToDissimilarityMatrix(sim); // normalize
			// Save as social network
//			fcaSim.saveAsCSVMatrixFile(sim, Constants.SIMILARITY_NETWORK_FILE, 0.8);//0.192
			
			//if (true) return;
			
			// normalize
			System.out.print("Normalizing... ");
			FcaUtils.convertSimToDissimilarityMatrix(sim);
			System.out.print("OK\n");
			
			// MDS
			System.out.print("Scaling (MDS)... ");

			ClassicalMDSProjection mds = new ClassicalMDSProjection();
			
			DoubleMatrix2D matrix = new DenseDoubleMatrix2D(sim);
			
			DoubleMatrix2D res = mds.project(matrix);
			
			double[][] output2= res.toArray();
			
			
			for (int i = 0; i < output2.length; i++) {
				for (int j = 0; j < output2.length; j++) {
					System.out.println(objectNames.get(i) + " - " + objectNames.get(j) + " = "+ sim[i][j]);
				}
			}

			// visualize
			//plotChart(output, objectNames, true);
			
			System.out.print("OK\n");
			
			// visualize

			plotChart(output2, objectNames, false);
			
			System.out.print("OK\n");
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	
	public static void main(String[] args) {
//		try {
//			Object[] res = Experiment.computeTraditionalSimilarity("/Users/cassiomelo/Dropbox/code/twitter-analysis-tools/data/music.cxt");
//			plotChart((double[][])res[0], (List<String>) res[1], false);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		Experiment.runTraditionalExperiment();
		//Experiment.runFCAExperiment();
	}

}
