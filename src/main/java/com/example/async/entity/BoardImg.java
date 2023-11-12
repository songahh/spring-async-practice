package com.example.async.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "BOARD_IMG")
@AllArgsConstructor
@NoArgsConstructor
public class BoardImg {
    @Id
    @GeneratedValue
    private int id;
    private String originalFileName;
    private String imgUrl;

}
