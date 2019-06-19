package com.swire.etsww.domain;

import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 审批表-实体
 *
 * @author Neal on 2019/6/19
 */
public class Approval implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;//主键id
    private String tenderingNum;//招标编号
    private int approvalId;//审批编号
    private String approver;//审批人
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;//审批时间
    private String approvalType;//审批类型
    private String annotation;//批注

    public enum ApprovalType{
        SUB("00","提交"),
        APPROVAL("01","批准");
        public final String code;
        public final String desc;

        ApprovalType(String code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return null == getId();
    }
}
