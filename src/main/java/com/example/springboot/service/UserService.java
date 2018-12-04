package com.example.springboot.service;

import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zxx
 * @Date: 2018/12/4 10:27
 * @Version 1.0
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    /**
     * 查询
     * */
    public User findOne(Integer id){
        return userRepository.findOne(id);
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public List<User> findAllSort(Sort sort){
       return userRepository.findAll(sort);
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User searchUserByLastName(String LastName){
       return  userRepository.searchUserByLastName(LastName);
    }
    /**
     * 更新和新增
     * @param user
     * @return
     */
    public User updateUser(User user){
       User result = userRepository.saveAndFlush(user);
       return result;
    }


    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int updateLastNameByid(Integer id,String lastName){
        return userRepository.updateLastNameByid(id,lastName);
    }
    public List<User> findByLastNameAndEmailNotNullOrderByIdAsc(String LastName){
        return userRepository.findByLastNameAndEmailNotNullOrderByIdAsc(LastName);
    }

    /**
     * 删除
     * @param Email
     * @param LastName
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteByEmailAndLastName(String Email,String LastName){
       userRepository.deleteByEmailAndAndLastName(Email,LastName);
    }
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteById(Integer id){
        userRepository.delete(id);
    }
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteUserByLastName(String LastName){
        userRepository.deleteUserByLastName(LastName);
    }
    /**
     * 分页
     * @param searchParameters
     * @return
     */
    public Map getPage( Map searchParameters){
        //初始化数据
        Map map = new HashMap();
        int page=0;
        int size=2;
        Page<User> pageList = null;
        PageRequest pageable;
        //判断数据是否为空，不为空则赋值
        if (searchParameters != null && searchParameters.size() > 0 && searchParameters.get("page") != null) {
            page = Integer.parseInt(searchParameters.get("page").toString()) - 1;
        }
        if (searchParameters != null && searchParameters.size() > 0 && searchParameters.get("pageSize") != null) {
            size = Integer.parseInt(searchParameters.get("pageSize").toString());
        }
        //设置页数最大最小值
        if (size < 1) {
            size = 1;
        }
        if (size > 100){
            size = 100;
        }
        //判断是否传入sort排序
        List<Map> orderMaps = (List<Map>) searchParameters.get("sort");
        List<Order> orders = new ArrayList<Order>();
        if (orderMaps != null) {
            for (Map m : orderMaps) {
                if (m.get("field") == null){
                    continue;
                }
                String field = m.get("field").toString();
                if (!StringUtils.isEmpty(field)) {
                    String dir = m.get("dir").toString();
                    if ("DESC".equalsIgnoreCase(dir)) {
                        orders.add(new Order(Direction.DESC, field));
                    } else {
                        orders.add(new Order(Direction.ASC, field));
                    }
                }
            }
        }
        //生成pageable
        if (orders.size() > 0) {
            pageable = new PageRequest(page, size, new Sort(orders));
        } else {
            Sort sort = new Sort(Direction.ASC, "id");
            pageable = new PageRequest(page, size, sort);
        }
        //获取查询条件
        Map filter = (Map) searchParameters.get("filter");
        //判断是否有条件
        if (filter != null) {
            List<Map> filters = (List<Map>) filter.get("filters");
            Specification<User> specification = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    //添加条件到语句
                    for (Map m:filters) {
                        String field = m.get("field").toString().trim();
                        String value = m.get("value").toString().trim();
                        if (value != null && value.length() > 0) {
                            if ("email".equalsIgnoreCase(field)) {
                                predicates.add(cb.like(root.<String>get(field), value + "%"));
                            }else if("lastName".equalsIgnoreCase(field)){
                                predicates.add(cb.like(root.<String>get(field), value + "%"));
                            }
                        }
                    }
                    return cb.and(predicates.toArray(new Predicate[0]));
                }

            };
            pageList = userRepository.findAll(specification,pageable);
        }else{
            Specification<User> specification = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    // 查询出未删除的
                    //list.add(cb.equal(root.<Integer>get("state"), 1));
                    return cb.and(predicates.toArray(new Predicate[0]));
                }
            };
            pageList = userRepository.findAll(specification,pageable);
        }
        map.put("total",pageList.getTotalElements());
        List<User> list = pageList.getContent();
        map.put("users",list);
        return map;
    }


}

