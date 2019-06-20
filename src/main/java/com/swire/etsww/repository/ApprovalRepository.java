package com.swire.etsww.repository;

import com.swire.etsww.domain.Approval;
import org.springframework.data.ebean.repository.EbeanRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Neal on 2019/6/20
 * @ClassName ApprovalRepository
 */
@Repository
public interface ApprovalRepository extends EbeanRepository<Approval,String> {
    List<Approval> findByTenderingNum(@Param("tenderingNum")String tenderingNum);
}
