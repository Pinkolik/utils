package pt.it.av.atnog.utils.ws.search;

import pt.it.av.atnog.utils.Http;
import pt.it.av.atnog.utils.json.JSONArray;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.json.JSONValue;

import java.util.Iterator;

/**
 * Searx search engine.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Searx extends WebSearchEngine {
  private static final String DEFAULT_URL = "http://searx.me/";

  /**
   *
   */
  public Searx() {
    super(DEFAULT_URL);
  }

  /**
   * @param url
   */
  public Searx(final String url) {
    super(url);
  }

  /**
   * @param url
   * @param maxResults
   */
  public Searx(final String url, final int maxResults) {
    super(url, maxResults);
  }

  @Override
  protected Iterator<Result> resultsIterator(final String q, final int skip) {
    return new SearxResultIterator(q, skip);
  }

  /**
   * Fast Searx search iterator.
   * <p>The result pages are consomed continuously.
   * Fetch one page of results and iterates over them, before fetching another result's page.
   * This way the network calls are spread throught time, improving latency to the user.</p>
   */
  private class SearxResultIterator implements Iterator<Result> {
    private final int skip;
    private final String q;
    private Iterator<JSONValue> it = null;
    private boolean done = false;

    public SearxResultIterator(final String q, final int skip) {
      this.q = q;
      this.skip = skip;

    }

    @Override
    public boolean hasNext() {
      if (it == null) {
        try {
          int pageno;
          if (skip == 0) {
            pageno = 1;
          } else {
            pageno = skip / 10;
          }

          System.err.println(url + "?format=json&pageno="
              + pageno + "&q=" + q);

          JSONObject json = Http.getJson(url + "?format=json&pageno="
              + pageno + "&q=" + q);

          if (json != null) {
            int numberResults = json.get("number_of_results").asInt();
            if (skip >= numberResults) {
              done = true;
            }

            JSONArray array = json.get("results").asArray();
            it = array.iterator();
            if (!it.hasNext()) {
              done = true;
            }
          } else {
            done = true;
          }
        } catch (Exception e) {
          e.printStackTrace();
          done = true;
        }
      } else {
        done = it.hasNext();
      }
      return !done;
    }

    @Override
    public Result next() {
      Result rv = null;
      if (!done) {
        JSONObject json = it.next().asObject();
        String name = json.get("title").asString(), uri = json.get("url").asString();
        if (json.contains("content")) {
          rv = new Result(name, json.get("content").asString(), uri);
        } else {
          rv = new Result(name, name, uri);
        }
      }
      return rv;
    }
  }
}
