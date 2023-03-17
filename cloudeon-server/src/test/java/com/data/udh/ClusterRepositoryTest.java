package com.data.udh;
 
import com.data.udh.dao.ClusterInfoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClusterRepositoryTest {

    @Resource
    private ClusterInfoRepository clusterInfoRepository;
 
    @Test
    public void dod() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+clusterInfoRepository.findAll());
    }
}