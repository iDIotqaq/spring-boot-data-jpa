package com.example.springboot.controller;

import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.service.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id){
            User user = userRepository.findOne(id);
            return user;
    }
    @GetMapping("/users")
    public List<User> getUsers(){
        List<User> users = userRepository.findAll();

        return users;
    }
    @GetMapping("/pages")
    public Page<User> getPages(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "2") Integer size){
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page,size,sort);
        return userRepository.findAll(pageable);
    }
//    @GetMapping("/page")
//    public Page<User> getpage(){
//        return userService.page(1,2);
//    }
    @PostMapping("/user")
    public User insertUser(User user){
        User save = userRepository.save(user);
        return save;
    }

}
