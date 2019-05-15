package com.jxtb.batch.reader;

import com.jxtb.batch.bean.Person;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Created by jxtb on 2019/4/27.
 */
public class PersonFieldSetMapper implements FieldSetMapper<Person> {
    @Override
    public Person mapFieldSet(FieldSet fieldSet) throws BindException {
        return new Person(
                fieldSet.readInt("ID"),
                fieldSet.readString("NAME"),
                fieldSet.readString("AGE"),
                fieldSet.readString("NATION"),
                fieldSet.readString("ADDRESS")
        );
    }

}
