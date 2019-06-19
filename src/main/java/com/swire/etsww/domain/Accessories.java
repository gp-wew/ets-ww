package com.swire.etsww.domain;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 附件表-实体
 *
 * @author Neal on 2019/6/19
 */
@Data
@Entity
@Table(name = "tb_info_accessories")
public class Accessories implements Serializable, Persistable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;//主键id
    private String tenderingNum;//招标编号
    private String accessoriesName;//附件名称
    private String accessoriesAddress;//附件地址

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return null == getId();
    }
}
