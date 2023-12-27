package com.rhymthwave.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentID")
    private Long paymentID;

    @Column(name = "paymentName",columnDefinition = "nvarchar(100)")
    private String paymentName;

    @Column(name = "paymentType",columnDefinition = "nvarchar(100)")
    private String paymentType="ATM";
    
    @Column(name = "CREATEDATE")
    private Date CREATEDATE=new Date();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "EMAIL")
    private Account account;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "SUBCRIPTIONID")
    private Subscription subscription;


}
