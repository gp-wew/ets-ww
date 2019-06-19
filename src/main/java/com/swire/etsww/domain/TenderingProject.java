package com.swire.etsww.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 招标项目-实体
 *
 * @author Neal on 2019/6/19
 */
public class TenderingProject implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;//主键id
    private String tenderingNum;//招标编号
    private int tenderingProjectId;//招标项目id
    private String tenderingProjectName;//招标项目name

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return null == getId();
    }
}
