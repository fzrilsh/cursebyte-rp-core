package com.cursebyte.plugin.modules.government.pricing;

public final class RobustEmaModel {

    private final double alpha;
    private final double beta;
    private final double k;

    public RobustEmaModel(double alpha, double beta, double k) {
        this.alpha = alpha;
        this.beta = beta;
        this.k = k;
    }

    public Result update(double ema, double dev, double x) {
        double d = Math.abs(x - ema);
        double newDev = (1.0 - beta) * dev + beta * d;
        if (newDev < 1e-9) newDev = 1e-9;

        double lo = ema - k * newDev;
        double hi = ema + k * newDev;
        double xc = Math.max(lo, Math.min(hi, x));

        double newEma = (1.0 - alpha) * ema + alpha * xc;
        return new Result(newEma, newDev);
    }

    public static final class Result {
        public final double ema;
        public final double dev;

        public Result(double ema, double dev) {
            this.ema = ema;
            this.dev = dev;
        }
    }
}
