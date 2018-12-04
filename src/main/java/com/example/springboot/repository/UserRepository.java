package com.example.springboot.repository;

import com.example.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: zxx
 * @Date: 2018/11/29 20:41
 * @Version 1.0
 */
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    User findByEmail(String Email);

    @Modifying
    @Query("update User set lastName=:lastName where id=:id")
    int updateLastNameByid(@Param("id") Integer id,@Param("lastName") String lastName);

    List<User> findByLastNameAndEmailNotNullOrderByIdAsc(String LastName);


}
