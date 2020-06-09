package com.tcts.foresight.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.CategoryDetailsEntity;
import com.tcts.foresight.entity.SubCatDetailsEntity;


public interface CategoryDetailsRepo extends  JpaRepository<CategoryDetailsEntity, Long>,CrudRepository<CategoryDetailsEntity, Long>  {

	@Query("SELECT cat.name FROM CategoryDetailsEntity cat where cat.name = :name AND cat.module =:module")
	String findNameByModule(@Param("name") String name, @Param("module") String module);
	
	@Query("SELECT cat.id FROM CategoryDetailsEntity cat where cat.name = :name AND cat.module = :module")
	Long findIdByName(@Param("name") String name, @Param("module") String module);
	
	void save(SubCatDetailsEntity subCategoryDetails);

	@Query(value = "select c.* subcat_name from subcategory_details sc, category_details c, category_subcategory scm where sc.id=scm.subcategory_id and c.id=scm.category_id and sc.name = ?1 and c.name=?2 and c.module=?3",
            nativeQuery = true)
    List<CategoryDetailsEntity> queryBy(String subCatName, String catName, String module);
	
	
	@Query("SELECT cat.name FROM CategoryDetailsEntity cat where cat.name = :name AND cat.module =:module")
	ArrayList<String> findNameByModule1(@Param("name") String name, @Param("module") String module);
	
	@Query(value = "SELECT name FROM category_details where id = :id", nativeQuery = true)
	String findByCatId(@Param("id") Long id); 

	
}
