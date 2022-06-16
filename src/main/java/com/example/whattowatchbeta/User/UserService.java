package com.example.whattowatchbeta.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(UserEntity user){
        userRepository.save(user);
    }

    public UserEntity changeUserDetails(UserEntity user, Optional<UserEntity> userCheck){
        return UserEntity.builder()
                .id(userCheck.get().getId())
                .email(user.getEmail())
                .name(user.getName())
                .subscribe(user.isSubscribe())
                .build();
    }

    public List<UserEntity> getSubscribers(){
        return userRepository.getAllBySubscribeIsTrue();
    }

    public Optional<UserEntity> getUserByEmail(String email){
       return  userRepository.findUserEntityByEmail(email);
    }

    public void updateUser(UserEntity user,Optional<UserEntity> userCheck)  {
        userRepository.save(changeUserDetails(user,userCheck));
    }

}