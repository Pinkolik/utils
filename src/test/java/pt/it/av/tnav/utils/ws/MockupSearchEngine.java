package pt.it.av.tnav.utils.ws;

import pt.it.av.tnav.utils.ws.search.WebSearchEngine;

import java.util.*;

/**
 * Mockup for a instance of {@link WebSearchEngine}.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class MockupSearchEngine extends WebSearchEngine {
  HashMap<String, List<Result>> map;

  /**
   * Mockup search engine constructor.
   */
  public MockupSearchEngine() {
    super("", 3);
    map = new LinkedHashMap<>();

    List<Result> zero = new ArrayList<>();
    List<Result> one = new ArrayList<>();
    one.add(new Result("one", "one", "one"));
    List<Result> two = new ArrayList<>();
    two.add(new Result("one", "one", "one"));
    two.add(new Result("two", "two", "two"));
    List<Result> three = new ArrayList<>();
    three.add(new Result("one", "one", "one"));
    three.add(new Result("two", "two", "two"));
    three.add(new Result("three", "three", "three"));

    map.put("zero", zero);
    map.put("one", one);
    map.put("two", two);
    map.put("three", three);
  }

  @Override
  protected Iterator<Result> resultsIterator(final String q, final int skip, final int pageno) {
    Iterator<Result> it = null;
    if (map.containsKey(q)) {
      List<Result> temp = map.get(q);
      if (skip < temp.size())
        it = map.get(q).subList(skip, skip + 1).iterator();
    } else {
      it = map.get("zero").iterator();
    }
    return it;
  }
}
