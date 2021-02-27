package com.company.numprog;

import com.company.math.LinearAlgebra;

import java.util.Arrays;

public class EigenvalueSolver {


    private final double[][] M;
    private boolean shifted = false;
    private static final double EPSILON = 0.001;
    private double eigenVal;
    private double[] eigenVector;
    private double mu;

    public EigenvalueSolver(double[][] matrix) {
        this.M = matrix;
    }

    public void shift(double mu) {
        for (int i = 0; i < M.length; i++)
            M[i][i] -= mu;
        this.mu = mu;
        shifted = true;
    }

    private boolean convergenceCriterium(double[] w, double lambda, double[] x_prev) {
        double[] vec = LinearAlgebra.subVV(w, LinearAlgebra.multiplySV(lambda, x_prev));
        return LinearAlgebra.vectorNorm(vec) <= (EPSILON * LinearAlgebra.vectorNorm(w));
    }

    public void powerIteration(double[] startVector) {
        double[] x_prev = startVector.clone();
        double[] w;
        double lambda = 0;
        int iterations = 0;
        do {
            System.out.println("i = " + iterations);
            w = LinearAlgebra.multiplyMV(M, x_prev);
            System.out.println("w = " + Arrays.toString(w));
            lambda = LinearAlgebra.multiplyVV(w, x_prev);
            System.out.println("lambda = " + lambda);
            double norm = LinearAlgebra.vectorNorm(w);
            System.out.println("norm = " + norm);
            x_prev = LinearAlgebra.multiplySV(1 / norm, w);
            System.out.println("x = " + Arrays.toString(x_prev));
            System.out.println();
            iterations++;
        } while (!convergenceCriterium(w, lambda, x_prev));
        if (shifted)
            lambda += mu;
        this.eigenVal = lambda;
        this.eigenVector = x_prev;
        System.out.println("lambda = " + lambda);
    }

    public static void main(String[] args) {
        double[][] A = {
                {3, 1}, {1, 3}
        };
        double[] x0 = {1, 0};
        EigenvalueSolver solver = new EigenvalueSolver(A);
        solver.shift(1.5);
        solver.powerIteration(x0);
    }
}
