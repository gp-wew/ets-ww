package com.swire.etsww.repository;

import com.swire.etsww.domain.TenderingProjectDetails;
import org.springframework.data.ebean.repository.EbeanRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Neal on 2019/6/20
 * @ClassName TenderingProjectDetailsRepository
 */
public interface TenderingProjectDetailsRepository extends EbeanRepository<TenderingProjectDetails, String> {
    List<TenderingProjectDetails> findByTenderingProjectId(@Param("tenderingProjectId") int tenderingProjectId);
}
