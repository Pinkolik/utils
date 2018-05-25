package pt.it.av.atnog.utils.json;

import java.io.IOException;
import java.io.Writer;

/**
 * JSON Null.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class JSONNull implements JSONValue {

    @Override
    public void write(Writer w) throws IOException {
        w.append("null");
    }

    @Override
    public boolean equals(Object o) {
        boolean rv = false;
        if (o != null) {
            if (o == this)
                rv = true;
            else if (o instanceof JSONNull) {
                rv = true;
            }
        }
        return rv;
    }
}
