package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Image;

@Repository
public interface ImageDAO extends JpaRepository<Image, String>{

}
