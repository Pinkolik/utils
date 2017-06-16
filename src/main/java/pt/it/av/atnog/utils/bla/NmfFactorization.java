package pt.it.av.atnog.utils.bla;

import pt.it.av.atnog.utils.ArrayUtils;

/**
 * Non negative matrix factorization.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 */
public class NmfFactorization {

  /**
   * @param k
   * @param n
   * @param e
   * @return
   */
  public static Matrix[] nmf_mu(Matrix v, int k, int n, double e) {
    double max = v.max(), eps = Math.ulp(1.0);
    Matrix w = Matrix.random(v.rows, k, 0.0, max);
    Matrix h = Matrix.random(k, v.cols, 0.0, max);

    // compute WH matrix
    Matrix wh = w.mul(h);
    double cost = ArrayUtils.euclideanDistance(v.data, 0, wh.data, 0, v.data.length);

    for (int i = 0; i < n && cost > e; i++) {
      // update feature matrix.
      Matrix wt = w.transpose();
      Matrix hn = wt.mul(v);
      Matrix hd = wt.mul(wh);

      ArrayUtils.add(hd.data, 0, eps, hd.data, 0, hd.data.length);
      //ArrayUtils.add(hd.data, 0, Double.MIN_VALUE, hd.data, 0, hd.data.length);

      //h.smultEq(hn.smultEq(hd.sinvEq()));
      //ArrayUtils.div(hn.data, 0, hd.data, 0, temp1, 0, k * v.cols);
      //ArrayUtils.mul(h.data, 0, temp1, 0, h.data, 0, k * v.cols);
      ArrayUtils.mulDiv(h.data, 0, hn.data, 0, hd.data, 0, h.data, 0, k * v.cols);

      // update weights matrix
      Matrix ht = h.transpose();
      Matrix wn = v.mul(ht);
      Matrix wd = w.mul(h).mul(ht);

      ArrayUtils.add(wd.data, 0, eps, wd.data, 0, wd.data.length);
      //ArrayUtils.add(wd.data, 0, Double.MIN_VALUE, wd.data, 0, wd.data.length);

      //w.smultEq(wn.smultEq(wd.sinvEq()));
      //ArrayUtils.div(wn.data, 0, wd.data, 0, temp1, 0, v.rows * k);
      //ArrayUtils.mul(w.data, 0, temp1, 0, w.data, 0, v.rows * k);
      ArrayUtils.mulDiv(w.data, 0, wn.data, 0, wd.data, 0, w.data, 0, k * v.rows);

      // compute WH matrix
      wh = w.mul(h);
      cost = ArrayUtils.euclideanDistance(v.data, 0, wh.data, 0, v.data.length);
    }
    return new Matrix[]{w, h};
  }
}
