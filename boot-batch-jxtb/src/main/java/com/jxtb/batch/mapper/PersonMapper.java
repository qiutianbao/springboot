package com.jxtb.batch.mapper;

import com.jxtb.batch.model.PersonDo;

import java.util.List;

/**
 * Created by jxtb on 2019/7/5.
 */
public interface PersonMapper {
    List<Long> selectPersonKey(String batchDate);

    PersonDo selectByPrimaryKey(Long id);
}
