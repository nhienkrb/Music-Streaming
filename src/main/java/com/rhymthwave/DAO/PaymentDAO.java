package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Payment;

@Repository
public interface PaymentDAO extends JpaRepository<Payment, Long>{

}
