package com.company.ai;

import com.company.math.LinearAlgebra;

import java.util.*;

public class HiddenMarkovModel {

    private final double[] initialProb;
    private final double[] evidence;
    private final double[][] transitionMatrix;

    public HiddenMarkovModel(double[] initialProb, double[][] transitionMatrix,
                             double[] evidence) {
        this.initialProb = initialProb;
        this.evidence = evidence;
        this.transitionMatrix = transitionMatrix;
    }


    public static double[] filtering(int timestep, double[][] transitionMatrix,
                                     double[] initial, List<double[][]> O) {
        double[] temp = LinearAlgebra.multiplyMV(transitionMatrix, initial);
        double[] vec = LinearAlgebra.multiplyDiagMV(O.get(timestep - 1), temp, false);
        double alpha = 1 / (vec[0] + vec[1]);
        return new double[]{vec[0] * alpha, vec[1] * alpha};
    }

    public static double[] smoothing(int k, int t, List<double[]> f_ks, double[][] transitionMatrix,
                                     List<double[][]> O) {
        double[] b = calculateB(k + 1, t, transitionMatrix, O);
        double[] temp = LinearAlgebra.multiplyVVPointwise(f_ks.get(k - 1), b);
        double alpha = 1 / (temp[0] + temp[1]);
        return new double[]{temp[0] * alpha, temp[1] * alpha};
    }

    static Map<Integer, double[]> bStash = new HashMap<>();

    private static double[] calculateB(int k, int t, double[][] transitionMatrix,
                                       List<double[][]> O) {
        if (k > t)
            return new double[]{1.0, 1.0};
        else if (bStash.containsKey(k)) {
            return bStash.get(k);
        } else {
            double[] temp = LinearAlgebra.multiplyMV(O.get(k - 1), calculateB(k + 1, t, transitionMatrix, O));
            double[] b = LinearAlgebra.multiplyMV(transitionMatrix, temp);
            bStash.put(k, b);
            return b;
        }
    }


    public static void main(String[] args) {
        int timestep = 3;

        double[][] O_1 = {{0.72, 0}, {0, 0.21}};
        double[][] O_2 = {{0.18, 0}, {0, 0.49}};
        double[][] O_3 = {{0.02, 0}, {0, 0.21}};
        List<double[][]> O = new ArrayList<>();
        O.add(O_1);
        O.add(O_2);
        O.add(O_3);
        double[] initial = {0.7, 0.3};
        double[][] transitionMatrix = {{0.8, 0.3}, {0.2, 0.7}};
        double[] vec = initial.clone();
        List<double[]> f_ks = new ArrayList<>();
        for (int i = 1; i <= timestep; i++) {
            vec = filtering(i, transitionMatrix, vec, O);
            LinearAlgebra.printVector(vec);
            f_ks.add(vec);
        }
        System.out.println();
        for (int i = 1; i <= timestep; i++) {
            vec = smoothing(i, timestep, f_ks, LinearAlgebra.transposeMatrix(transitionMatrix), O);
            LinearAlgebra.printVector(vec);
        }
    }
}
