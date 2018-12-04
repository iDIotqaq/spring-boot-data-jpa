package com.example.springboot;

import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDataJpaApplicationTests {
    @Autowired
    UserService userService;
    @PersistenceContext
    EntityManager em;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void findOne() {
        User user= userService.findOne(2);
        System.out.println(user);
    }
    @Test
    public void findAll(){
        List<User> users = userService.findAll();
        System.out.println(users);
    }
    @Test
    public void findAllSort(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<User> users = userService.findAllSort(sort);
        System.out.println(users);
    }
    @Test
    public void findByEmail(){
        User user = userService.findByEmail("aa");
        System.out.println(user);
    }
    @Test
    public void searchUserByLastName(){
        User user = userService.searchUserByLastName("zhangsan");
        System.out.println(user);
    }
    @Test
    public void updateUser(){
        User user= userService.findOne(4);
        user.setLastName("gouye");
        User result = userService.updateUser(user);
        System.out.println(result);
    }
    @Test
    public void updateLastNameByid(){
        int count = userService.updateLastNameByid(5,"tanghaoyu");
        System.out.println(count);
    }
    @Test
    public void insertUser(){
        User user= new User();
        user.setEmail("cc");
        user.setLastName("huohua");
        User result = userService.updateUser(user);
        System.out.println(result);
    }
    @Test
    public void deleteById(){
        userService.deleteById(18);
    }
    @Test
    public void deleteByEmailAndLastName(){
        userService.deleteByEmailAndLastName("ff","ff");

    }
    @Test
    public void deleteUserByLastName(){
        userService.deleteUserByLastName("wcx");
    }
    @Test
    public void findByLastNameAndEmailNotNullOrderByIdDesc(){
        List<User> users = userService.findByLastNameAndEmailNotNullOrderByIdAsc("yangye");
        System.out.println(users);
    }
    @Test
    public void findOneC(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> c = cb.createQuery(User.class);
        Root<User> user = c.from(User.class);
        c.select(user).where(cb.equal(user.get("email"),"aa"));
        User result =em.createQuery(c).getSingleResult();
        System.out.println(result);
    }
    @Test
    public void page(){
        String map = "{\"page\" : 1,\"pageSize\" : 2," +
                " \"filter\":{ \"filters\":[{ \"field\" : \"email\", \"value\":\"aa\"}]}}";
        Map searchParameters = new HashMap();
        try {
            searchParameters =objectMapper.readValue(map,new TypeReference<Map>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
       Map result = userService.getPage(searchParameters);
        System.out.println(result.get("total"));
        System.out.println(result.get("users"));
    }

}
