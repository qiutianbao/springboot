package com.jxtb.batch;

import com.jxtb.batch.bean.Person;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Created by jxtb on 2019/4/27.
 */
public class UserWriter implements ItemWriter<Person> {

    @Override
    public void write(List<? extends Person> items) throws Exception {
        for(Person user : items){
            System.out.println(user);
        }
    }

}
