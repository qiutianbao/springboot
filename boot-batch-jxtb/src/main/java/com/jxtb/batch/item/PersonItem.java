package com.jxtb.batch.item;

import com.jxtb.batch.common.utils.sdk.annotation.CChar;
import lombok.Data;

/**
 * Created by jxtb on 2019/7/5.
 */
@Data
public class PersonItem {
    @CChar(order = 1)
    public String name;
    @CChar(order = 2)
    public String age;
    @CChar(order = 3)
    public String nation;
    @CChar(order = 4)
    public String address;
}
