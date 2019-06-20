package com.swire.etsww.repository;

import com.swire.etsww.domain.Tendering;
import org.springframework.data.ebean.repository.EbeanRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Neal on 2019/6/20
 * @ClassName TenderingRepository
 */
public interface TenderingRepository extends EbeanRepository<Tendering, String> {
    Tendering findByStatus(@Param("status") String status);
}
