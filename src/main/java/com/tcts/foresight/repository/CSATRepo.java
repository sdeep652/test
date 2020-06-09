package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.CSATEntity;

@Repository
public interface CSATRepo extends JpaRepository<CSATEntity, Long> {

	List<CSATEntity> findByRating(Long rating);

	

}
