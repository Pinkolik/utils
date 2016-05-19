package pt.it.av.atnog.utils.bla;

import pt.it.av.atnog.utils.MathUtils;

/**
 * This class represent a Vector used in linear algebra.
 * Implements several functions used in linear algebra, machine learning and matrix algebra.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Vector {
    private static int T = 256;
    private static int B = 128;
    protected int bIdx, length;
    protected double data[];

    public Vector(double data[], int bIdx, int length) {
        this.data = data;
        this.bIdx = bIdx;
        this.length = length;
    }

    public Vector(double data[]) {
        this(data, 0, data.length);
    }

    public Vector(int length) {
        this(new double[length], 0, length);
    }

    public static Vector ones(int length) {
        Vector c = new Vector(length);
        c.set(1.0);
        return c;
    }

    public static Vector rand(int length) {
        Vector b = new Vector(length);
        for (int n = 0; n < length; n++)
            b.data[n + b.bIdx] = MathUtils.randomBetween(0, 10);
        return b;
    }

    public int size() {
        return length;
    }

    public void set(int i, double scalar) {
        data[bIdx + i] = scalar;
    }

    public void set(double scalar) {
        for (int i = 0; i < length; i++)
            data[bIdx + i] = scalar;
    }

    public double get(int i) {
        return data[bIdx + i];
    }

    public double sum() {
        double rv = 0.0;
        for (int i = 0; i < length; i++)
            rv += data[bIdx + i];
        return rv;
    }

    public Vector add(double scalar) {
        Vector c = new Vector(length);
        ArraysOps.add(this.data, this.bIdx, scalar, c.data, c.bIdx, length);
        return c;
    }

    public Vector uAdd(double scalar) {
        ArraysOps.add(this.data, this.bIdx, scalar, this.data, this.bIdx, length);
        return this;
    }

    public Vector add(Vector b) {
        Vector c = new Vector(length);
        ArraysOps.add(this.data, this.bIdx, b.data, b.bIdx, c.data, c.bIdx, length);
        return c;
    }

    public Vector uAdd(Vector b) {
        ArraysOps.add(this.data, this.bIdx, b.data, b.bIdx, this.data, this.bIdx, length);
        return this;
    }

    public Vector sub(double scalar) {
        Vector c = new Vector(length);
        ArraysOps.sub(this.data, this.bIdx, scalar, c.data, c.bIdx, length);
        return c;
    }

    public Vector uSub(double scalar) {
        ArraysOps.sub(this.data, this.bIdx, scalar, this.data, this.bIdx, length);
        return this;
    }

    public Vector sub(Vector b) {
        Vector c = new Vector(length);
        ArraysOps.sub(this.data, this.bIdx, b.data, b.bIdx, c.data, c.bIdx, length);
        return c;
    }

    public Vector uSub(Vector b) {
        ArraysOps.sub(this.data, this.bIdx, b.data, b.bIdx, this.data, this.bIdx, length);
        return this;
    }

    public Vector mul(double scalar) {
        Vector c = new Vector(length);
        ArraysOps.mul(this.data, this.bIdx, scalar, c.data, c.bIdx, length);
        return c;
    }

    public Vector uMul(double scalar) {
        ArraysOps.mul(this.data, this.bIdx, scalar, this.data, this.bIdx, length);
        return this;
    }

    public Vector mul(Vector b) {
        Vector c = new Vector(length);
        ArraysOps.mul(this.data, this.bIdx, b.data, b.bIdx, c.data, c.bIdx, length);
        return c;
    }

    public Vector uMul(Vector b) {
        ArraysOps.mul(this.data, this.bIdx, b.data, b.bIdx, this.data, this.bIdx, length);
        return this;
    }

    public Vector div(double scalar) {
        Vector c = new Vector(length);
        ArraysOps.div(this.data, this.bIdx, scalar, c.data, c.bIdx, length);
        return c;
    }

    public Vector uDiv(double scalar) {
        ArraysOps.div(this.data, this.bIdx, scalar, this.data, this.bIdx, length);
        return this;
    }

    public Vector div(Vector b) {
        Vector c = new Vector(length);
        ArraysOps.div(this.data, this.bIdx, b.data, b.bIdx, c.data, c.bIdx, length);
        return c;
    }

    public Vector uDiv(Vector b) {
        ArraysOps.div(this.data, this.bIdx, b.data, b.bIdx, this.data, this.bIdx, length);
        return this;
    }

    public Vector power(double b) {
        Vector c = new Vector(length);
        ArraysOps.pow(this.data, this.bIdx, b, c.data, c.bIdx, length);
        return c;
    }

    public Vector uPower(double b) {
        ArraysOps.pow(this.data, this.bIdx, b, this.data, this.bIdx, length);
        return this;
    }

    public double innerProduct(Vector b) {
        double c = 0.0;
        for (int i = 0; i < length; i++)
            c += data[bIdx + i] * b.data[b.bIdx + i];
        return c;
    }

    public Matrix outerProduct(Vector b) {
        Matrix C = new Matrix(length, b.length);
        int k = 0;
        for (int i = 0; i < length; i++)
            for (int j = 0; j < b.length; j++) {
                C.data[k] = data[bIdx + i] * b.data[b.bIdx + j];
                k++;
            }
        return C;
    }

    public double norm(int p) {
        double norm = 0.0;
        for (int i = 0; i < length; i++)
            norm = MathUtils.norm(norm, data[bIdx + i], p);
        return norm;
    }

    public double minkowskiDistance(Vector b, int p) {
        return ArraysOps.minkowskiDistance(data, bIdx, b.data, b.bIdx, length, p);
    }

    public double euclideanDistance(Vector b) {
        return ArraysOps.euclideanDistance(data, bIdx, b.data, b.bIdx, length);
    }

    public double manhattanDistance(Vector b) {
        return ArraysOps.manhattanDistance(data, bIdx, b.data, b.bIdx, length);
    }

    public double cosine(Vector b) {
        double rv = 0.0, dp = innerProduct(b);
        if (dp > 0)
            rv = dp / (norm(2) * b.norm(2));
        return rv;
    }

    public double kld(Vector b) {
        double rv = 0.0;
        for (int i = 0; i < length; i++) {
            if (b.data[b.bIdx + i] != 0.0 && data[bIdx + i] != 0.0)
                rv += data[bIdx + i] * Math.log(data[bIdx + i] / b.data[b.bIdx + i]);
            else if (b.data[b.bIdx + i] == 0.0)
                rv += data[bIdx + i];
        }
        return rv;
    }

    public int elbow() {
        int min = 0;
        double minDist = Math.sqrt(Math.pow(0.0 - data[bIdx], 2.0) + Math.pow(0.0 - 0.0, 2.0));
        for (int i = 1; i < length; i++) {
            double tmpDist = Math.sqrt(Math.pow(0.0 - data[bIdx + i], 2.0) + Math.pow(0.0 - i, 2.0));
            if (tmpDist < minDist) {
                minDist = tmpDist;
                min = i;
            }
        }
        return min;
    }

    double mean() {
        double m = 0;
        int t = 1;
        for (int i = 0; i < length; i++) {
            m += (data[bIdx + i] - m) / t;
            ++t;
        }
        return m;
    }

    double var() {
        double m = mean(), v = 0.0;
        for (int i = 0; i < length; i++)
            v += Math.pow(data[bIdx + i] - m, 2.0);
        return v / (length - 1);
    }

    public double std() {
        return Math.sqrt(var());
    }

    public double cov(Vector Y) {
        double c = 0.0, mx = 0.0, my = 0.0;
        int t = 1;
        for (int i = 0; i < length; i++) {
            mx += (data[bIdx + i] - mx) / t;
            my += (Y.data[Y.bIdx + i] - my) / t;
            ++t;
        }
        for (int i = 0; i < length; i++)
            c += (data[bIdx + i] - mx) * (Y.data[Y.bIdx + i] - my);
        return c / (length - 1);
    }

    public double corr(Vector Y) {
        double r = 0.0, mx = 0.0, my = 0.0, sx = 0.0, sy = 0.0;
        int t = 1;
        for (int i = 0; i < length; i++) {
            mx += (data[bIdx + i] - mx) / t;
            my += (Y.data[Y.bIdx + i] - my) / t;
            ++t;
        }
        for (int i = 0; i < length; i++) {
            sx += Math.pow(data[bIdx + i] - mx, 2.0);
            sy += Math.pow(Y.data[Y.bIdx + i] - my, 2.0);
        }
        sx = Math.sqrt(sx / (length - 1));
        sy = Math.sqrt(sy / (length - 1));
        for (int i = 0; i < length; i++)
            r += ((data[bIdx + i] - mx) / sx) * ((Y.data[Y.bIdx + i] - my) / sy);
        return r / (length - 1);
    }

    public boolean equals(Vector b, double eps) {
        boolean rv = false;
        return rv;
    }

    @Override
    public boolean equals(Object o) {
        boolean rv = false;
        if (o != null) {
            if (o == this)
                rv = true;
            else if (o instanceof Vector) {
                Vector b = (Vector) o;
                if (length == b.length) {
                    rv = true;
                    for (int i = 0; i < length && rv == true; i++)
                        if (Double.compare(data[bIdx + i], b.data[b.bIdx + i]) != 0)
                            rv = false;
                }
            }
        }
        return rv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < length - 1; i++)
            sb.append(data[bIdx + i] + ", ");
        sb.append(data[bIdx + length - 1] + "]");
        return sb.toString();
    }
}
