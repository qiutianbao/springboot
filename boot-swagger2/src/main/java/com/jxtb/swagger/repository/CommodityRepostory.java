package com.jxtb.swagger.repository;

import com.jxtb.swagger.entity.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jxtb on 2019/1/31.
 */
public interface CommodityRepostory extends JpaRepository<Commodity,Integer> {
    Commodity findById(int id);
}
