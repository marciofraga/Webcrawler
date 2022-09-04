package com.overmind.crawler.service.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class AbstractCrawler {

    public void visit(String url, String path) throws IOException {
        Document doc = Jsoup.connect(url).get();

        extractData(doc, path);
    }

    public abstract void extractData(Document doc, String path);
}
