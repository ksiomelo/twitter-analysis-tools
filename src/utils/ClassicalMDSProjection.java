/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
@inproceedings{paulovich2007pex,
author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
Minghim},
title = {The Projection Explorer: A Flexible Tool for Projection-based 
Multidimensional Visualization},
booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
Computer Graphics and Image Processing (SIBGRAPI 2007)},
year = {2007},
isbn = {0-7695-2996-8},
pages = {27--34},
doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
publisher = {IEEE Computer Society},
address = {Washington, DC, USA},
}
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package utils;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Cassio
 */
public class ClassicalMDSProjection  {


   // @Override
    public DoubleMatrix2D project(DoubleMatrix2D distancem) {
        //1. Compute the matrix of squared dissimilarities D^2.
    	int thecount = distancem.columns(); // size?
        DoubleMatrix2D D = new DenseDoubleMatrix2D(thecount,thecount);
    	
    	
        for (int i = 0; i < thecount - 1; i++) {
            for (int k = i + 1; k < thecount; k++) {
            	D.setQuick(i, k, distancem.get(i, k)  * distancem.get(i, k));
            	D.setQuick(k, i, distancem.get(i, k)  * distancem.get(i, k));
            }
        }

        //2. Apply double centering to this matrix: B = -1/2 * J * D^2 * J
        //      J = I - n^-1 * 1 * 1T
        DoubleMatrix2D J = DoubleFactory2D.dense.identity(thecount);
        double value = 1.0 / thecount;

        for (int i = 0; i < thecount; i++) {
            for (int k = 0; k < thecount; k++) {
                J.setQuick(i, k, J.getQuick(i, k) - value);
            }
        }

        DoubleMatrix2D b = J.zMult(D, null, -0.5, 1.0, false, false).zMult(J, null, 1.0, 1.0, false, false);

        //3. Compute the eigendecomposition of B = Q+A+.
        EigenvalueDecomposition dec = new EigenvalueDecomposition(b);
        DoubleMatrix2D eigenvectors = dec.getV();
        DoubleMatrix1D eigenvalues = dec.getRealEigenvalues();

        //4. Let the matrix of the first m eigenvalues greater than zero be A+
        //and Q+ the first m columns of Q. Then, the coordinate matrix of
        //classical scaling is given by X = Q+A^1/2+ .
        DenseDoubleMatrix2D Q = new DenseDoubleMatrix2D(thecount, 2);
        DenseDoubleMatrix2D A = new DenseDoubleMatrix2D(2, 2);

        for (int i = eigenvalues.size() - 1, k = 0; i >= 0 && k < 2; i--) {
            if (eigenvalues.get(i) > EPSILON) {
                for (int n = 0; n < Q.rows(); n++) {
                    Q.setQuick(n, k, -1*eigenvectors.getQuick(n, i));
                }

                A.setQuick(k, k, Math.sqrt(eigenvalues.getQuick(i)));
                k++;
            }
        }

        DoubleMatrix2D result = Q.zMult(A, null, 1.0, 1.0, false, false);
        return result;

//        float[][] projection = new float[result.rows()][];
//
//        for (int i = 0; i < projection.length; i++) {
//            projection[i] = new float[2];
//
//            projection[i][0] = (float) result.getQuick(i, 0);
//            projection[i][1] = (float) result.getQuick(i, 1);
//        }
//
//        AbstractMatrix proj = new DenseMatrix();
//        ArrayList<String> attributes = new ArrayList<String>();
//        attributes.add("x");
//        attributes.add("y");
//        proj.setAttributes(attributes);
//
//        ArrayList<Integer> ids = dmat.getIds();
//        float[] cdata = dmat.getClassData();
//
//        for (int i = 0; i < projection.length; i++) {
//            AbstractVector vector = new DenseVector(projection[i], ids.get(i), cdata[i]);
//            proj.addRow(vector);
//        }
//
//        return proj;
    }

    private static final float EPSILON = Float.MIN_VALUE;
    
    public static void main(String[] args) {
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
		
		ClassicalMDSProjection mds = new ClassicalMDSProjection();
		
		DoubleMatrix2D matrix = new DenseDoubleMatrix2D(sample);
		
		DoubleMatrix2D res = mds.project(matrix);
		System.out.println(res.toString());
	}
}
