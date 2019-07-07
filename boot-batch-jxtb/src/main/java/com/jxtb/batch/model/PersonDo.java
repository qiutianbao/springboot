package com.jxtb.batch.model;

import lombok.Data;

/**
 * Created by jxtb on 2019/4/22.
 */
@Data
public class PersonDo {
    private int id;
    private String name;
    private String age;
    private String nation;
    private String address;

    public PersonDo(int id, String name, String age, String nation, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nation = nation;
        this.address = address;
    }
}
