package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.TemplateEntity;

@Repository
public interface TemplateRepo extends JpaRepository<TemplateEntity, Long> {

	List<TemplateEntity> findByModule(String module);

	

	

	

}
