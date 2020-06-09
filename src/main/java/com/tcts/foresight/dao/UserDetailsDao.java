//package com.tcts.foresight.dao;
//
//import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
//import org.jdbi.v3.sqlobject.customizer.Bind;
//import org.jdbi.v3.sqlobject.statement.SqlQuery;
//import org.jdbi.v3.sqlobject.statement.SqlUpdate;
//
//import com.tcts.foresight.pojo.UserDetailsDTO;
//
//public interface UserDetailsDao {
//
//
//
//
//
//	@SqlUpdate("delete from user_details where username= ?")
//	boolean deleteUserwithUsername(String username);
//	
//
//	@SqlQuery("SELECT user_theme FROM user_details WHERE lower(email)= :username")
//	@RegisterBeanMapper(UserDetailsDTO.class)
//	String getUserTheme(@Bind("email") String username);
//
//	@SqlUpdate("update public.user_details set isLogin='N' where lower(email)=:username ")
//	boolean setUserLogout(@Bind("email")String username);
//
//	@SqlUpdate("update public.user_details set user_theme=:theme where lower(email)=:username")
//	boolean updateTheme(@Bind("theme")String theme,@Bind("email")String username);
//
//}
//
//
