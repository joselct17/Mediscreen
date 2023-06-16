package com.mediscreen.Mediscreen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "patient_id")
    private  Integer id;

    private String name;

    private String lastName;

    private String birthDate;

    private String sex;

    private String address;

    private String phone;


}
