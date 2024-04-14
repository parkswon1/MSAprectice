package com.jumtospring.rest.service.UserServiceLogic;

import com.jumtospring.rest.entity.User;
import com.jumtospring.rest.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class UserServiceLogicTest {

    @Autowired
    private UserService userService;

    private User kim;
    private User Lee;

    @BeforeEach
    public void startUp(){
        this.kim = new User("Kim", "Kim@naver.com");
        this.Lee = new User("Lee", "Lee@naver.com");
        this.userService.register(kim);
        this.userService.register(Lee);
    }

    @Test
    public void registerTest(){
        User sample = User.sample();
        assertThat(this.userService.register(sample)).isEqualTo(sample.getId());
        assertThat(this.userService.findAll().size()).isEqualTo(3);
        this.userService.remove(sample.getId());
    }

    @Test
    public void find(){
        assertThat(this.userService.find(Lee.getId())).isNotNull();
        assertThat(this.userService.find(kim.getId()).getEmail()).isEqualTo(Lee.getEmail());
    }

    @AfterEach
    public void cleanUp(){
        this.userService.remove(kim.getId());
        this.userService.remove(Lee.getId());
    }
}
