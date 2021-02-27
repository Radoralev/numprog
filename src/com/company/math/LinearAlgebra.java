package com.company.math;

import java.util.Arrays;
import java.util.stream.IntStream;

public class LinearAlgebra {


    public static double[] multiplyMV(double[][] matrix, double[] vector) {
        return Arrays.stream(matrix)
                .mapToDouble(row ->
                        IntStream.range(0, row.length)
                                .mapToDouble(col -> row[col] * vector[col])
                                .sum()
                ).toArray();
    }

    public static void printVector(double[] vec) {
        System.out.print("[");
        for (double v : vec)
            System.out.print(v + ",\t");
        System.out.print("]\n");
    }

    public static void printMatrix(double[][] mat) {
        System.out.print("[");
        for (double[] v : mat) {
            printVector(v);
            System.out.print(",\n");
        }
        System.out.print("]\n");
    }


    public static double[][] transposeMatrix(double[][] x) {
        double[][] y = new double[x[0].length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                y[j][i] = x[i][j];
            }
        }
        return y;
    }

    public static double multiplyVV(double[] vec1, double[] vec2) {
        double product = 0;
        for (int i = 0; i < vec1.length; i++) {
            product += vec1[i] * vec2[i];
        }
        return product;
    }

    public static double[] multiplyVVPointwise(double[] vec1, double[] vec2) {
        double[] result = new double[vec1.length];
        for (int i = 0; i < vec1.length; i++) {
            result[i] = vec1[i] * vec2[i];
        }
        return result;
    }

    public static double vectorNorm(double[] vec) {
        return Math.sqrt(Arrays.stream(vec).map(i -> i * i).sum());
    }

    public static double[] multiplySV(double scalar, double[] vector) {
        return Arrays.stream(vector).map(i -> i * scalar).toArray();
    }

    public static double[] multiplyDiagMV(double[][] matrix, double[] vector, boolean inverse) {
        double[] x = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            x[i] = inverse ? (1 / matrix[i][i]) * vector[i] : matrix[i][i] * vector[i];
        }
        return x;
    }

    public static double[] addVV(double[] vec1, double[] vec2) {
        double[] product = new double[vec1.length];
        for (int i = 0; i < vec1.length; i++) {
            product[i] = vec1[i] + vec2[i];
        }
        return product;
    }

    public static double[] subVV(double[] vec1, double[] vec2) {
        double[] product = new double[vec1.length];
        for (int i = 0; i < vec1.length; i++) {
            product[i] = vec1[i] - vec2[i];
        }
        return product;
    }
}
