//package com.tcts.foresight.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//
//import com.tcts.foresight.entity.ParentChildEntity;
//
//public interface ParentChildRepo extends CrudRepository<ParentChildEntity,Integer> {
//
//	
//	/*
//	 * @Query(value =
//	 * "SELECT child_ticketid FROM parentchild_mapping  where parent_ticketid  IN(:nav)"
//	 * ,nativeQuery = true) List<String> fetchChilds(@Param("nav")String
//	 * selectedId);
//	 */
//	
//	@Query(value = "SELECT child_ticketid FROM parentchild_mapping  where parent_ticketid  IN(:child)",nativeQuery = true)
//	List<String> fetchChilds(@Param("child")String selectedId);
//
//	@Query(value = "SELECT parent_ticketid FROM parentchild_mapping  where child_ticketid  IN(:parent)",nativeQuery = true)
//	List<String> fetchParents(@Param("parent")String selectedId);
//	
//
//}
