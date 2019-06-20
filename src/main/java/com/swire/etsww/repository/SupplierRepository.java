package com.swire.etsww.repository;

import com.swire.etsww.domain.Supplier;
import org.springframework.data.ebean.repository.EbeanRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Neal on 2019/6/20
 * @ClassName SupplierRepository
 */
public interface SupplierRepository extends EbeanRepository<Supplier, String> {
    List<Supplier> findByTenderingNum(@Param("tenderingNum") String tenderingNum);
}
