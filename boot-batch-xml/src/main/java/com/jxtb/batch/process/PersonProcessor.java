package com.jxtb.batch.process;

import com.jxtb.batch.bean.Person;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jxtb on 2019/4/27.
 */
public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person item) throws Exception {
//        if (Integer.parseInt(item.getAge()) % 2 == 0) {
//            return item;
//        }
        return item;
    }
}
