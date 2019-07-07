package com.jxtb.batch.reader;

import com.jxtb.batch.model.PersonDo;
import com.jxtb.batch.common.reader.KeyBasedStreamReader;
import com.jxtb.batch.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxtb on 2019/7/3.
 */
@Slf4j
public class PersonReader extends KeyBasedStreamReader<PersonDo>{

    @Value("#{jobParameters['date']}")
    private String batchDate;
//    @Autowired
//    private PersonMapper personMapper;

    @Override
    protected List<String> loadKeys() {
        log.info("person,batchDate:{}", batchDate);
       // List<Long> keys = personMapper.selectPersonKey(batchDate);
        List<Long> keys = new ArrayList<>();
        log.info("key size: " + (keys == null ? 0 : keys.size()));
        return convert(keys);
    }

    @Override
    protected PersonDo loadItemByKey(String key) {
        Long id = Long.parseLong(key);
//        PersonDo person = personMapper.selectByPrimaryKey(id);
        PersonDo person = null;
        return person;
    }
}
