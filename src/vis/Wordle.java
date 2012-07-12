package vis;

import fca.AdvancedBinaryRelation;
import fca.FormalContext;

public class Wordle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		AdvancedBinaryRelation context;
		try {
			
			//"/Users/cassiomelo/code/datasets/Twitter/twitter6-cluster-3-supp_4.cxt"
			// "/Users/cassiomelo/code/datasets/Twitter/twitter6-cluster-2-supp_4.cxt"
			context = FormalContext.loadFormalContext("/Users/cassiomelo/code/datasets/Twitter/twitter6-cluster-10-supp_4.cxt");
			System.out.println(context.getWordleString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
