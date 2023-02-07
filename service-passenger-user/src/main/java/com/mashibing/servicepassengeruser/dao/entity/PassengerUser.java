package com.mashibing.servicepassengeruser.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
//@TableName("PassengerUser")
public class PassengerUser {
    /**
     *   `id` int NOT NULL,
     *   `gmt_create` datetime DEFAULT NULL,
     *   `gmt_modified` datetime DEFAULT NULL,
     *   `passengerPhone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
     *   `passengerName` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
     *   `passengerGender` int DEFAULT NULL,
     *   `state` int DEFAULT NULL,
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private String passengerPhone;
    private String passengerName;
    /**
     * 0 女
     * 1 男
     */
    private byte passengerGender;
    /**
     * 0 有效
     * 1 无效
     */
    private byte state;
}
