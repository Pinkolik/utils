package pt.it.av.atnog.utils.json;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by mantunes on 3/30/15.
 */
public class JSONNumber extends JSONValue {
    protected final double n;

    public JSONNumber(double n) {
        this.n = n;
    }

    public double value() {
        return n;
    }

    @Override
    public void write(Writer w) throws IOException {
        w.append(String.valueOf(n));
    }
}
