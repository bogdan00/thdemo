package com.example.thdemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.*;

@Data
@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "person_id")
    UUID person_id;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;
}
