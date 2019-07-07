package com.jxtb.batch.process;

import com.jxtb.batch.model.PersonDo;
import com.jxtb.batch.item.PersonItem;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jxtb on 2019/7/3.
 */
public class PersonProcess implements ItemProcessor<PersonDo,PersonItem>{
    @Override
    public PersonItem process(PersonDo personDo) throws Exception {
        PersonItem item = new PersonItem();
        item.setName(personDo.getName());
        item.setAge(personDo.getAge());
        item.setNation(personDo.getNation());
        item.setAddress(personDo.getAddress());
        return item;
    }
}
