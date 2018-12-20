package com.company.luncune.controller;

import com.company.luncune.dao.TbItemMapper;
import com.company.luncune.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemSereachController {
    @Autowired
    private TbItemMapper tbItemMapper;
    @RequestMapping("/selectAll")
    public List<TbItem> selectAll() {
        List<TbItem> tbItems = tbItemMapper.selectAll();
        return tbItems;
    }
}
