package com.tcts.foresight.repository.problem;

import javax.transaction.Transactional;

import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.problem.KnownErrorEntity;


@Repository
@Transactional
public interface KnownErrorRepo extends JpaRepository<KnownErrorEntity, Long>,CrudRepository<KnownErrorEntity, Long> {


	public void deleteByProblemID(String problemID);
}
