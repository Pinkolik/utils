package pt.it.av.atnog.utils.bla;

import pt.it.av.atnog.utils.structures.tuple.Octuple;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by mantunes on 1/23/15.
 */
public class Naive {
    public static double det(Matrix M) {
        double rv = 0.0;
        if (M.columns == 1 && M.rows == 1)
            rv = M.data[0];
        else if (M.columns == 2 && M.rows == 2)
            rv = M.data[0] * M.data[3] - M.data[2] * M.data[1];
        else if (M.columns == 3 && M.rows == 3)
            rv = (M.data[0] * M.data[4] * M.data[8] + M.data[1] * M.data[5] * M.data[6] + M.data[2] * M.data[3] * M.data[7]) - (M.data[2] * M.data[4] * M.data[6] + M.data[1] * M.data[3] * M.data[8] + M.data[0] * M.data[5] * M.data[7]);
        else {
            for (int j1 = 0; j1 < M.rows; j1++) {
                Matrix T = new Matrix(M.rows - 1, M.columns - 1);
                for (int i = 1; i < M.rows; i++) {
                    int j2 = 0;
                    for (int j = 0; j < M.rows; j++) {
                        if (j == j1)
                            continue;
                        T.data[(i - 1) * T.columns + j2] = M.data[i * M.columns + j];
                        j2++;
                    }
                }
                rv += Math.pow(-1.0, 1.0 + j1 + 1.0) * M.data[0 * M.columns + j1] * det(T);
            }
        }
        return rv;
    }

    public static Matrix transpose(Matrix M) {
        Matrix C = new Matrix(M.columns, M.rows);
        for (int n = 0, total = M.data.length; n < total; n++) {
            int r = n / M.columns, c = n % M.columns;
            C.data[c * C.columns + r] = M.data[n];
        }
        return C;
    }

    public static Matrix mul(Matrix A, Matrix B) {
        Matrix C = new Matrix(A.rows, B.columns);
        for (int i = 0; i < A.rows; i++)
            for (int k = 0; k < B.rows; k++)
                for (int j = 0; j < B.columns; j++)
                    C.data[i * C.columns + j] += A.data[i * A.columns + k] * B.data[k * B.columns + j];
        return C;
    }

    public Matrix mul(Matrix A, Matrix B, int blk) {
        Matrix C = new Matrix(A.rows, B.columns);
        double b_data[] = new double[blk * blk];
        Deque<Octuple<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> stack = new ArrayDeque<>();
        stack.push(new Octuple(0, A.rows, 0, A.columns, 0, B.rows, 0, B.columns));
        while (!stack.isEmpty()) {
            Octuple<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> o = stack.pop();
            int a_rb = o.a, a_re = o.b, a_cb = o.c, a_ce = o.d, b_rb = o.e, b_re = o.f, b_cb = o.g, b_ce = o.h;
            if ((a_re - a_rb) <= blk && (a_ce - a_cb) <= blk) {
                for (int i = b_rb, bc = 0; i < b_re; i++, bc++)
                    for (int j = b_cb, br = 0; j < b_ce; j++, br++)
                        b_data[br * blk + bc] = B.data[i * A.columns + j];
                for (int i = a_rb; i < a_re; i++) {
                    int ai = i * A.columns;
                    for (int j = b_cb, br = 0; j < b_ce; j++, br++) {
                        double cij = 0;
                        int bi = br * blk;
                        for (int k = b_rb, bc = 0; k < b_re; k++, bc++)
                            cij += A.data[ai + k] * b_data[bi + bc];
                        C.data[i * C.columns + j] += cij;
                    }
                }
            } else if ((a_re - a_rb) >= (a_ce - a_cb)) {
                int a_rm = (a_rb + a_re) / 2, b_cm = (b_cb + b_ce) / 2;
                stack.push(new Octuple(a_rb, a_rm, a_cb, a_ce, b_rb, b_re, b_cb, b_cm));
                stack.push(new Octuple(a_rb, a_rm, a_cb, a_ce, b_rb, b_re, b_cm, b_ce));
                stack.push(new Octuple(a_rm, a_re, a_cb, a_ce, b_rb, b_re, b_cb, b_cm));
                stack.push(new Octuple(a_rm, a_re, a_cb, a_ce, b_rb, b_re, b_cm, b_ce));
            } else {
                int a_cm = (a_cb + a_ce) / 2, b_rm = (b_rb + b_re) / 2;
                stack.push(new Octuple(a_rb, a_re, a_cb, a_cm, b_rb, b_rm, b_cb, b_ce));
                stack.push(new Octuple(a_rb, a_re, a_cm, a_ce, b_rm, b_re, b_cb, b_ce));
            }
        }
        return C;
    }
}