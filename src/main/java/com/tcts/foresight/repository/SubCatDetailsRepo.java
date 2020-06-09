package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.SubCatDetailsEntity;

public interface SubCatDetailsRepo
		extends JpaRepository<SubCatDetailsEntity, Long>, CrudRepository<SubCatDetailsEntity, Long> {

	@Query(value = "select sc.* subcat_name from subcategory_details sc, category_details c, category_subcategory scm where sc.id=scm.subcategory_id and c.id=scm.category_id and sc.name = ?1 and c.name=?2 and c.module=?3",
            nativeQuery = true)
    List<SubCatDetailsEntity> queryBy(String subCatName, String catName, String module);
	
	@Query(value = "select sc.* subcat_name from subcategory_details sc, category_details c, category_subcategory scm where sc.id=scm.subcategory_id and c.id=scm.category_id  and c.module=:module",
            nativeQuery = true)
    List<SubCatDetailsEntity> fetchAllSubCategories(@Param("module") String module);
	
	@Query(value = "select sc.* subcat_name from subcategory_details sc, category_details c, category_subcategory scm where c.module=:module and scm.category_id=:id"
			+ " AND c.id=scm.category_id AND sc.id=scm.subcategory_id",
            nativeQuery = true)
    List<SubCatDetailsEntity> fetchAllMulSubCategories(@Param("module") String module,@Param("id")Long id);
	
	SubCatDetailsEntity findByName(String subCatName);
	
	List<SubCatDetailsEntity> findByNameAndCategoryList_Module(String subCatName,String module);
	
	@Query("select sc from SubCatDetailsEntity sc left join sc.categoryList cat where sc.name =:subCatName and cat.id in (:categoryIdList)")
	@EntityGraph(attributePaths = {"categoryList"})
	SubCatDetailsEntity getByName(@Param("subCatName") String subCatName , @Param("categoryIdList") List<Long> categoryIdList);
	
	@Query("select sc from SubCatDetailsEntity sc left join sc.categoryList cat where cat.id =:categoryId and cat.module =:module")
	List<SubCatSummary> getSubCatByCatId(@Param("categoryId") Long categoryId,@Param("module") String module);
	
	@Query(value = "select name from subcategory_details  where id =:id ",nativeQuery = true )
	String findSubCatById(@Param("id") Long id); 

	List<SubCatDetailsEntity> findDistinctBycategoryList_Module(String module);
	
}
