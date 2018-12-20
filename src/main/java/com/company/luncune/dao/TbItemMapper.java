package com.company.luncune.dao;

import com.company.luncune.pojo.TbItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TbItemMapper {
    @Select("select * from tb_item")
    List<TbItem> selectAll();
}
