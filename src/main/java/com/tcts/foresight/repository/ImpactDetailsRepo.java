package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


import com.tcts.foresight.entity.ImpactDetailsEntity;

public interface ImpactDetailsRepo extends  JpaRepository<ImpactDetailsEntity, Long>,CrudRepository<ImpactDetailsEntity, Long>  
{

}
