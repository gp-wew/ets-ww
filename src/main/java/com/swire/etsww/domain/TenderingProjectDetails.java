package com.swire.etsww.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 招标项目明细-实体
 *
 * @author Neal on 2019/6/19
 */
public class TenderingProjectDetails  implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;//主键id
    private int tenderingProjectId;//招标项目id
    private int tenderingProjectDetailsId;//招标项目明细id
    private String tenderingProjectDetailsName;//招标项目明细name

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return null == getId();
    }
}
