package com.company.luncune.dao;

import com.company.luncune.pojo.TbDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TbDetailMapper {

   /* @Select("SELECT a.column_name,a.column_comment,b.table_name,b.table_comment FROM " +
            "(select table_name,column_name,column_comment from information_schema.COLUMNS where table_schema = 'sell') a," +
            "(select table_name,table_comment from information_schema.TABLES where table_schema = 'sell') b " +
            "WHERE a.table_name=b.table_name")*/
    List<TbDetail> getTbDetailAll();
}
