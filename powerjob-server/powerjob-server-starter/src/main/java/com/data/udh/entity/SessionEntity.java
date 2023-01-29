package com.data.udh.entity;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "udh_session")
@Data
public class SessionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    /**
     *
     */
    private Integer userId;
    /**
     *
     */
    private String ip;
    /**
     *
     */
    private Date lastLoginTime;

}
