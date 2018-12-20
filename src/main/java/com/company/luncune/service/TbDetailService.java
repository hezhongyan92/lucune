package com.company.luncune.service;

import com.company.luncune.dao.TbDetailMapper;
import com.company.luncune.pojo.TbDetail;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
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
public class TbDetailService {
    @Autowired
    private TbDetailMapper tbDetailMapper;
    private boolean flag=false;

    public List<TbDetail> searchIndex(String str,int num) throws Exception{
        if (!flag){
            createIndex();
        }
       // QueryParser parser = new QueryParser("title", new StandardAnalyzer());
        QueryParser parser = new MultiFieldQueryParser(new String[]{"columnName","columnComment","tableName","tableComment"},new SmartChineseAnalyzer());
       //模糊查询，？匹配一个字符，*匹配多个
        Query query = parser.parse(str+"*");
        //常规查询，主要借助分词器查询
       // Query query = parser.parse(str);
        Path indexFile = Paths.get("E:\\search-learn\\data");
        Directory directory = FSDirectory.open(indexFile);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        TopDocs topDocs = indexSearcher.search(query, num);
        // 根据查询条件匹配出的记录
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<TbDetail> tbDetails = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取文档的ID
            int docId = scoreDoc.doc;
            // 通过ID获取文档
            Document doc = indexSearcher.doc(docId);
            TbDetail tbDetail = new TbDetail();
            tbDetail.setColumnName(doc.get("columnName"));
            tbDetail.setTableName(doc.get("tableName"));
            tbDetail.setColumnComment(doc.get("columnComment"));
            tbDetail.setTableComment(doc.get("tableComment"));
            tbDetails.add(tbDetail);
        }
        reader.close();
        return tbDetails;
    }
    public void createIndex() throws  Exception{
        List<TbDetail> tbDetailAll = tbDetailMapper.getTbDetailAll();
        List<Document> documents = writeDocument(tbDetailAll);
        Analyzer analyzer = new SmartChineseAnalyzer();
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
        flag = true;
    }
    private List<Document> writeDocument(List<TbDetail> tbDetailAll) throws  Exception{
        //获取数据源
        List<Document> docList = new ArrayList<>();
        for (TbDetail tbDetail : tbDetailAll) {
            Document document = new Document();
            // store:如果是yes，则说明存储到文档域中
            Field columnName = new TextField("columnName",tbDetail.getColumnName(), Field.Store.YES);
            Field tableName = new TextField("tableName",tbDetail.getTableName(),Field.Store.YES);
            Field columnConment = new TextField("columnComment", tbDetail.getColumnComment(), Field.Store.YES);
            Field tableConment = new TextField("tableComment", tbDetail.getTableComment(), Field.Store.YES);
            document.add(columnName);
            document.add(tableName);
            document.add(columnConment);
            document.add(tableConment);
            docList.add(document);
        }
        return docList;
    }
}
