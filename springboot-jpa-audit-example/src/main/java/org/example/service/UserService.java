package org.example.service;

import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    /**
     * 插入
     *
     * @param username
     * @return
     */
    public UserEntity insert(String username){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userRepository.save(userEntity);
        return userEntity;
    }
}
