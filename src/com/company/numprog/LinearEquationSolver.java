package com.company.numprog;

import com.company.math.LinearAlgebra;

import java.util.Arrays;

public class LinearEquationSolver {
    private final double[][] M;
    private final double EPSILON = 0.0000000001;

    public LinearEquationSolver(double[][] M) {
        this.M = M;
    }

    private double[] calcResidium(final double[] b, final double[] xi) {
        return LinearAlgebra.subVV(b, LinearAlgebra.multiplyMV(M, xi));
    }

    private boolean convergenceCheck(double[] x_1, double[] x_2) {
        for (int i = 0; i < x_1.length; i++)
            if (Math.abs(x_1[i] - x_2[i]) > EPSILON)
                return false;

        return true;
    }

    /**
     * Calculates an iteration with the richardson ansatz.
     *
     * @param b  Vector b .
     * @param x0 Previous value for iterative solution.
     * @return Next value of iterative solution.
     */
    private double[] richardsonIteration(final double[] b, final double[] x0) {
        double[] r = calcResidium(b, x0);
        return LinearAlgebra.addVV(x0, r);
    }

    /**
     * Calculates an iteration with the jacobi ansatz.
     *
     * @param b  Vector b .
     * @param x0 Previous value for iterative solution.
     * @return Next value of iterative solution.
     */
    private double[] jacobiIteration(final double[] b, final double[] x0) {
        double[] r = calcResidium(b, x0);

        double[] y = LinearAlgebra.multiplyDiagMV(M, r, true);
        return LinearAlgebra.addVV(x0, y);
    }

    private double[] steepestDescent(final double[] b, final double[] x0) {
        double[] r = calcResidium(b, x0);
        System.out.println("r = " + Arrays.toString(r));
        double stepSize =
                LinearAlgebra.multiplyVV(r, r) / LinearAlgebra.multiplyVV(r, LinearAlgebra.multiplyMV(M, r));
        System.out.println("alpha = " + stepSize);
        double[] result = LinearAlgebra.multiplySV(stepSize, r);
        result = LinearAlgebra.addVV(result, x0);
        return result;
    }

    private double[] gaussSeidelIteration(final double[] b, final double[] x0) {
        int n = b.length;

        double[] x_next = new double[b.length];
        //Iterate over matrix rows
        for (int k = 0; k < n; k++) {
            System.out.println("k = " + k);
            double r_k, y_k, sum_future = 0, sum_present = 0;
            //iterate over already calculated values for x
            for (int j = 0; j < k; j++)
                sum_future += M[k][j] * x_next[j];
            //iterate over values for x from previous Gauss-Seidel iteration
            for (int j = k; j < n; j++)
                sum_present += M[k][j] * x0[j];
            //x[i][k] = x[i-1][k] - inv(M[k][k]) * (b[k] - sum_future(0,k) - sum_present(k,n))
            r_k = b[k] - sum_future - sum_present;
            System.out.println("r[i][k] = " + r_k);
            y_k = (1 / M[k][k]) * r_k;
            System.out.println("y[i][k] = " + y_k);
            x_next[k] = x0[k] + y_k;
            System.out.println("x[i][k] = " + x_next[k]);
        }
        return x_next;
    }

    /**
     * Solves Mx = b . Uses starting value x0 for an iterative solution.
     *
     * @param b  Vector b .
     * @param x0 Starting value for iterative solution.
     * @return The solution x of the equation Mx=b .
     */
    public double[] solve(final double[] b, final double[] x0) {
        double[] x_last = x0.clone();
        double[] x_next = new double[0];
        for (int i = 0; ; i++) {
            System.out.println("i = " + i);
            //x_next = richardsonIteration(b, x_last);
            //x_next = jacobiIteration(b, x_last);
            //x_next = gaussSeidelIteration(b, x_last);
            x_next = steepestDescent(b, x_last);
            System.out.println("x[" + (i + 1) + "]=" + Arrays.toString(x_next));
            if (convergenceCheck(x_last, x_next))
                break;
            x_last = x_next;
            System.out.println();
        }
        return x_next;
    }

    public static void main(String[] args) {
        double[][] M = {
                {2, -1, 1}, {-1, 2, -1}, {1, -1, -2}
        };
        double[][] M2 = {
                {2, -2}, {-2, 6}
        };
        LinearEquationSolver lge = new LinearEquationSolver(M2);
        double[] b = {0, -4};
        double[] x0 = {0, 0};
        double[] result = lge.solve(b, x0);

        Arrays.stream(result).forEach(System.out::println);
    }

}
