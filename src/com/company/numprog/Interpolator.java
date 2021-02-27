package com.company.numprog;

import java.util.HashMap;
import java.util.Map;

public class Interpolator {
    private final HashMap<Double, Double> samples;
    private InterpolationStrategy strategy;

    public Interpolator() {
        samples = new HashMap<>();
        strategy = null;
    }

    public void setStrategy(InterpolationStrategy newStrategy) {
        this.strategy = newStrategy;
    }


    public void addSample(double x, double y) {
        var previous = samples.put(x, y);
        if (previous != null) {
            System.out.println("There was a number in the map.");
        }
    }

    public void addAllSamples(HashMap<Double, Double> samplesNew) {
        samples.entrySet().stream()
                .forEach(entry -> addSample(entry.getKey(), entry.getValue()));
    }


    public double predict(double x) {
        System.out.println("Interpolating...");
        if (strategy == null) {
            System.out.println("Interpolation failed. Please set strategy.");
            return 0;
        }
        double y = 0;
        switch (strategy) {
            case LAGRANGE -> y = samples.getOrDefault(x, 0.0);
            case AITKEN_NEVILLE -> y = aitken_neville(x);
            case NEWTON -> newton();
            case SPLINES -> splines();
            case FFT -> fast_fourier_transform();
            case DFT -> discrete_fourier_transform();
            case IDFT -> inverse_fourier_transform();
        }
        return y;
    }


    private void inverse_fourier_transform() {
    }

    private void discrete_fourier_transform() {

    }

    private void fast_fourier_transform() {

    }

    private void splines() {

    }

    private void newton() {
    }


    private double aitken_neville(double x_input) {
        int n = samples.entrySet().size();

        double[] y = samples.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .mapToDouble(Map.Entry::getValue)
                .toArray();
        double[] x = samples.keySet().stream()
                .mapToDouble(i -> i)
                .sorted().toArray();

        double[][] p = new double[n][n];

        for (int i = 0; i < n; i++)
            p[i][0] = y[i];

        for (int k = 1; k < n; k++)
            for (int i = 0; i < n - k; i++) {
                System.out.printf("p[%d][%d] = (x[%d] - x) / x[%d] * p[%d][%d] + (x-x[%d]) / (x[%d] - x[%d])*p[%d][%d] = ", i, k, i + k, i + k, i, k - 1, i, i + k, i, i + 1, k - 1);
                p[i][k] = (x[i + k] - x_input) / (x[i + k]) * p[i][k - 1];
                p[i][k] += (x_input - x[i]) / (x[i + k] - x[i]) * p[i + 1][k - 1];
                System.out.print(p[i][k] + "\n");
            }


        return p[0][n - 1];
    }

    public static void main(String[] args) {
        Interpolator interpolator = new Interpolator();
        interpolator.setStrategy(InterpolationStrategy.AITKEN_NEVILLE);

        interpolator.addSample(0, 0);
        interpolator.addSample(1, 1);
        interpolator.addSample(2, -4);
        //interpolator.addSample(1.5, 0);
        System.out.println("p[0] = y0 =" + 0);
        System.out.println("p[1] = y1 =" + 1);
        System.out.println("p[2] = y2 =" + -4);

        System.out.println("Predicted: " + interpolator.predict(-1));
    }

}
