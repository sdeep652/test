package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.CSATFeedabckEntity;

@Repository
public interface CSATFeedbackRepo extends JpaRepository<CSATFeedabckEntity, String> {

	CSATFeedabckEntity findByTicketid(String ticketid);

}
