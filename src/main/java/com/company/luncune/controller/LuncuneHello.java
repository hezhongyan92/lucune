package com.company.luncune.controller;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class LuncuneHello {

    public static void main(String[] args) throws Exception {
        //定义分词器
        StandardAnalyzer analyzer = new StandardAnalyzer();

        //创建索引
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);
        addDoc(w,"Lucene in Action","193398817");
        addDoc(w,"Lucene for Dummies","55320055Z");
        addDoc(w,"Managing Gigabytes","55063554A");
        addDoc(w,"The Art of Computer Science","9900333X");
        w.close();
        String querystr = args.length > 0? args[0] : "lucene";

        Query q = new QueryParser("title", analyzer).parse(querystr);

        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, null);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0; i<hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t"+ d.get("title"));
        }

        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }

}
