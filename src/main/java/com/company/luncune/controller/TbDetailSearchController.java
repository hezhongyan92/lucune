package com.company.luncune.controller;

import com.company.luncune.pojo.TbDetail;
import com.company.luncune.service.TbDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TbDetailSearchController {
    @Autowired
    private TbDetailService tbDetailService;

    @RequestMapping(value = "/tbDetails",method = RequestMethod.POST)
    public List<TbDetail> getTbDetails(String str){
        if(str==null||str.length()==0){
            return null;
        }
        List<TbDetail> tbDetails=null;
        try {
            tbDetails = tbDetailService.searchIndex(str,10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbDetails;
    }
}
