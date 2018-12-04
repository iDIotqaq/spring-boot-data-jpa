package com.example.springboot.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: zxx
 * @Date: 2018/11/29 20:35
 * @Version 1.0
 */
@Entity
@Table(name="tbl_user")
@Data
public class User {
    /**
     * @Id 表示主键
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 表示自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="last_name",length = 50)
    private String lastName;
    private String email;
}
