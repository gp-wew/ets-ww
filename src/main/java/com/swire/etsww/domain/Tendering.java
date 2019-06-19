package com.swire.etsww.domain;

import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 招标档案-实体
 *
 * @author Neal on 2019/6/19
 */
public class Tendering implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;//主键id
    private String tenderingNum;//招标编号
    private String purchasingUnit;//采购单位
    private String contacts;//联系人
    private String orderLevel;//采购订单级别
    private String type;//类别
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tenderingStartTime;//招标开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tenderingEndTime;//招标结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTime;//报价有效时间
    private String status;//状态
    private String projectName;//工程名称
    private String remarks;//备注

    public enum Type{
        MATER("00","采购物料");
        public final String code;
        public final String desc;

        Type(String code,String desc){
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
    public enum Status{
        DRAFTING("00","拟草中"),
        INAPPROVAL("01","审批中"),
        APPROVAL("02","批准");
        public final String code;
        public final String desc;

        Status(String code,String desc){
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
