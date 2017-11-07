package com.platform.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.Data;

/**
 * 所有序列化model集成该类
 * 
 * @author 王龙
 * @date 2017年10月26日 下午4:40:04
 */
@MappedSuperclass
@Data
public abstract class AbstractBaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 乐观锁. */
    @Version
    private Integer version;

    /** 创建时间，默认当前时间. */
    @Column(updatable = false, insertable = false, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Override
    public String toString() {
        return "BaseEntity{" + "id=" + id + ", version=" + version + ", createTime=" + createTime + '}';
    }
}