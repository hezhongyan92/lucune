package com.company.luncune.service;

import com.company.luncune.dao.TbItemMapper;
import com.company.luncune.pojo.TbItem;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LuncuneService {
    @Autowired
    private TbItemMapper tbItemMapper;

    public List<TbItem> searchIndex(String str,int num) throws Exception{
            QueryParser parser = new QueryParser("title", new StandardAnalyzer());
            Query query = parser.parse(str);
            Path indexFile = Paths.get("E:\\search-learn\\data");
            Directory directory = FSDirectory.open(indexFile);
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TopDocs topDocs = indexSearcher.search(query, num);
            // 根据查询条件匹配出的记录
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<TbItem> tbItems = new ArrayList<>();
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 获取文档的ID
                int docId = scoreDoc.doc;
                // 通过ID获取文档
                Document doc = indexSearcher.doc(docId);
                TbItem tbItem = new TbItem();
                tbItem.setId(Long.parseLong( doc.get("id")));
                tbItem.setTitle(doc.get("title"));
                tbItem.setSellPoint(doc.get("sellPoint"));
                tbItem.setBarcode(doc.get("barcode"));
                tbItem.setImage(doc.get("image"));
                tbItem.setId(Long.parseLong( doc.get("cid")));
                tbItems.add(tbItem);
            }
            reader.close();
            return tbItems;
        }
        public void createIndex() throws  Exception{
            List<TbItem> tbItems = tbItemMapper.selectAll();
            List<Document> documents = writeDocument(tbItems);
            Analyzer analyzer = new StandardAnalyzer();
            //创建索引库
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            Path indexFile = Paths.get("E:\\search-learn\\data");
            Directory directory = FSDirectory.open(indexFile);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            for (Document document : documents) {
                indexWriter.addDocument(document);
            }
            // 关闭indexWriter
            indexWriter.close();
        }
        private List<Document> writeDocument(List<TbItem> tbItems) throws  Exception{
            //获取数据源
            List<Document> docList = new ArrayList<>();
            for (TbItem product : tbItems) {
                Document document = new Document();
                // store:如果是yes，则说明存储到文档域中
                Field id = new TextField("id", product.getId().toString(), Field.Store.YES);
                Field title = new TextField("title", product.getTitle(), Field.Store.YES);
                Field sellPoint = new TextField("sellPoint", product.getSellPoint(), Field.Store.YES);
                Field barcode = new TextField("barcode", product.getBarcode(), Field.Store.YES);
                Field image = new TextField("image", product.getImage(), Field.Store.YES);
                Field price = new TextField("price", product.getPrice().toString(), Field.Store.YES);
                Field cid = new TextField("cid", product.getCid().toString(), Field.Store.YES);
                Field num = new TextField("num", product.getNum()+"", Field.Store.YES);
                Field status = new TextField("status", product.getStatus()+"", Field.Store.YES);
                Field created = new TextField("created", product.getCreated().toString(), Field.Store.YES);
                Field updated = new TextField("updated", product.getUpdated().toString(), Field.Store.YES);
                // 将field域设置到Document对象中
                document.add(id);
                document.add(title);
                document.add(sellPoint);
                document.add(barcode);
                document.add(image);
                document.add(price);
                document.add(cid);
                document.add(num);
                document.add(status);
                document.add(created);
                document.add(updated);
                docList.add(document);
            }
            return docList;
    }
}
