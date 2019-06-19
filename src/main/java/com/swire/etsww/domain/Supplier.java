package com.swire.etsww.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 供应商-实体
 *
 * @author Neal on 2019/6/19
 */
public class Supplier implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;//主键id
    private String tenderingNum;//招标编号
    private int supplierId;//供应商id
    private String supplier_name;//供应商name
    private String telephone;//电话
    private String mailbox;//邮箱

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return null == getId();
    }
}
