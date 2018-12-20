package com.company.luncune.controller;

import com.company.luncune.pojo.TbItem;
import com.company.luncune.service.LuncuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LuncureController {

    @Autowired
    private LuncuneService luncuneService;

    @RequestMapping(value = "/searchIndex",method = RequestMethod.POST)
    public List<TbItem> searchIndex(String str) throws Exception {
        //String strs = "手机";
        if (str==null){
            return null;
        }
        luncuneService.createIndex();
        return luncuneService.searchIndex(str,10);
    }
}
