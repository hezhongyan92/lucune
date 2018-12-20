package com.company.luncune;

import com.company.luncune.controller.LuncureController;
import com.company.luncune.service.TbDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuncuneApplicationTests {

    @Autowired
    private LuncureController luncureController;
    @Autowired
    private TbDetailService tbDetailService;
    @Test
    public void contextLoads() {
    }

   /* @Test
    public void  testLuncune(){
        try {
            luncureController.createIndex();
            luncureController.searchIndex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
   @Test
   public void testTbLuncune(){
       try {
           tbDetailService.createIndex();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}

