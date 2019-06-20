package com.swire.etsww.repository;

import com.swire.etsww.domain.Accessories;
import org.springframework.data.ebean.repository.EbeanRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Neal on 2019/6/20
 * @ClassName AccessoriesRepository
 */
@Repository
public interface AccessoriesRepository extends EbeanRepository<Accessories,String> {
    List<Accessories> findByTenderingNum(@Param("tenderingNum") String tenderingNum);
}
