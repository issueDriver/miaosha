package com.zuojie.dao;

import com.zuojie.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
/**
 * @zuthor:zuojie
 */
@Mapper
public interface UserDao {
    @Select("select * from user where id=#{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into user(id,name)values(#{id},#{name})")
    public boolean insert(User user);
}
