package com.jxtb.jpa;

import com.jxtb.jpa.dao.CityRepository;
import com.jxtb.jpa.entity.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by jxtb on 2019/7/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaApplication.class)
public class CityTest {
    @Autowired
    private CityRepository cityRepository;

    @Test
    public void getCity(){
        List<City> list = cityRepository.findAll();
        if(list != null){
           for(int i = 0; i < list.size(); i ++){
               City city = list.get(i);
               System.out.println(city.getCityId() + "<====>" + city.getCityName() + "<===>" + city.getCityIntroduce());
           }
        }
    }

}
