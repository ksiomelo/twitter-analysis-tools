package utils;

import java.util.ArrayList;

public class Sample {
	
	public static Object[] denmarkCitiesSample() {
		
		
		ArrayList names = new ArrayList();
		names.add("cph"); names.add("aar"); names.add("ode"); names.add("aal");
		
		Object[] ret = new Object[2];
		
		
		double [][]sample = new double[4][4];
		sample[0][0] = 0;
		sample[0][1] = 93;
		sample[0][2] = 82;
		sample[0][3] = 133;
		sample[1][0] = 93;
		sample[1][1] = 0;
		sample[1][2] = 52;
		sample[1][3] = 60;
		sample[2][0] = 82;
		sample[2][1] = 52;
		sample[2][2] = 0;
		sample[2][3] = 111;
		sample[3][0] = 133;
		sample[3][1] = 60;
		sample[3][2] = 111;
		sample[3][3] = 0;
		
		ret[0] = sample;
		ret[1] = names;
		
		return ret;
	}
	
	public static Object[] crimeRatesSample() {
		
		ArrayList names = new ArrayList();
		names.add("murder"); 
		names.add("rape"); 
		names.add("robbery"); 
		names.add("assault");
		names.add("burglary");
		names.add("larceny");
		names.add("auto theft");
		
		Object[] ret = new Object[2];
		
		double [][]sample = new double[7][7];
		sample[0][0] = 1;
		sample[0][1] = 0.52;
		sample[0][2] = 0.34;
		sample[0][3] = 0.81;
		sample[0][4] = 0.28;
		sample[0][5] = 0.06;
		sample[0][6] = 0.11;
		sample[1][0] = 0.52;
		sample[1][1] = 1;
		sample[1][2] = 0.55;
		sample[1][3] = 0.7;
		sample[1][4] = .68;
		sample[1][5] = .6;
		sample[1][6] = .44;
		sample[2][0] = .34;
		sample[2][1] = .55;
		sample[2][2] = 1;
		sample[2][3] = .56;
		sample[2][4] = .62;
		sample[2][5] = .44;
		sample[2][6] = .62;
		sample[3][0] = .81;
		sample[3][1] = .7;
		sample[3][2] = .56;
		sample[3][3] = 1;
		sample[3][4] = .52;
		sample[3][5] = .32;
		sample[3][6] = .33;

		sample[4][0] = 0.28;
		sample[4][1] = .68;
		sample[4][2] = .62;
		sample[4][3] = .52;
		sample[4][4] = 1;
		sample[4][5] = .8;
		sample[4][6] = .7;
		sample[5][0] = .06;
		sample[5][1] = .6;
		sample[5][2] = .44;
		sample[5][3] = .32;
		sample[5][4] = .8;
		sample[5][5] = 1;
		sample[5][6] = .55;
		sample[6][0] = .11;
		sample[6][1] = .44;
		sample[6][2] = .62;
		sample[6][3] = .33;
		sample[6][4] = .70;
		sample[6][5] = .55;
		sample[6][6] = 1;
		
		ret[0] = sample;
		ret[1] = names;
		
		return ret;
	}

}
