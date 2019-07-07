package com.jxtb.batch.writer;

import com.jxtb.batch.model.Person;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Created by jxtb on 2019/4/27.
 */
public class PersonWriter implements ItemWriter<Person> {

    @Override
    public void write(List<? extends Person> items) throws Exception {
        for(Person user : items){
            System.out.println(user);
        }
    }

}
