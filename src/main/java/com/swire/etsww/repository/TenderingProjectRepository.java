package com.swire.etsww.repository;

import com.swire.etsww.domain.TenderingProject;
import org.springframework.data.ebean.repository.EbeanRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Neal on 2019/6/20
 * @ClassName TenderingProjectRepository
 */
public interface TenderingProjectRepository extends EbeanRepository<TenderingProject, String> {
    TenderingProject findByTenderingNum(@Param("tenderingNum") String tenderingNum);
}
