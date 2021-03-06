package com.example.springboot.repository;

import com.example.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    User findByEmail(String Email);
    void deleteByEmailAndAndLastName(String Email,String LastName);
    @Modifying
    @Query("update User set lastName=:lastName where id=:id")
    int updateLastNameByid(@Param("id") Integer id,@Param("lastName") String lastName);

    @Query("select u from User u where u.lastName=:LastName")
    User searchUserByLastName(@Param("LastName") String LastName);

    @Modifying
    @Query("delete from User where lastName=:LastName ")
    void deleteUserByLastName(@Param("LastName") String LastName);

    List<User> findByLastNameAndEmailNotNullOrderByIdAsc(String LastName);



}
