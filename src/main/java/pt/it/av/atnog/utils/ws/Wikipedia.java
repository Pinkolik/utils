package pt.it.av.atnog.utils.ws;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pt.it.av.atnog.utils.HTTP;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO: improve the continue interface
public class Wikipedia implements SearchEngine {
    @Override
    public List<String> search(final String q) {
        String qURL = URLEncoder.encode(q);
        List<String> rv = new ArrayList<>();
        boolean done = false;
        String cont = "";
        while (!done) {
            try {
                JSONObject json = HTTP.getJSON("http://en.wikipedia.org/w/api.php?format=json&action=query" +
                        "&rawcontinue=&continue=" + cont + "&titles=" + qURL);
                try {
                    cont = json.get("query-continue").asObject().get("extracts").asObject().get("continue").toString();
                } catch (Exception e) {
                    //e.printStackTrace();
                    done = true;
                }
                json = json.get("query").asObject().get("pages").asObject();
                for (String name : json.names()) {
                    try {
                        long pageid = (long) json.get(name).asObject().get("pageid").asNumber();
                        Document doc = Jsoup.parse(HTTP.get("http://en.wikipedia.org/?curid=" + pageid));
                        rv.add(doc.body().text());
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return rv;
    }


    @Override
    public List<String> snippets(final String q) {
        String qURL = URLEncoder.encode(q);
        List<String> rv = new ArrayList<>();
        boolean done = false;
        String cont = "";
        while (!done) {
            try {
                JSONObject json = HTTP.getJSON("http://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&" +
                        "exintro=&explaintext=&rawcontinue=&excontinue=" + cont + "&titles=" + qURL);
                try {
                    cont = json.get("query-continue").asObject().get("extracts").asObject().get("excontinue").toString();
                } catch (Exception e) {
                    //e.printStackTrace();
                    done = true;
                }
                json = json.get("query").asObject().get("pages").asObject();
                for (String name : json.names()) {
                    try {
                        rv.add(json.get(name).asObject().get("extract").asString());
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                //e.printStackTrace();
                done = true;
            }
        }
        return rv;
    }

    @Override
    public Iterator<String> searchIt(String q) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public Iterator<String> snippetsIt(String q) throws UnsupportedEncodingException {
        return null;
    }
}
