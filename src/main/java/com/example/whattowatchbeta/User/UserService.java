package com.example.whattowatchbeta.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(UserEntity user){
        Optional<UserEntity> userCheck= userRepository.findUserEntityByEmail(user.getEmail());
        if (userCheck.isPresent())
            userRepository.save(changeUserDetails(user, userCheck));
         else
            userRepository.save(user);

    }

    public UserEntity changeUserDetails(UserEntity user, Optional<UserEntity> userCheck){
        UserEntity userEntity = UserEntity.builder()
                .id(userCheck.get().getId())
                .email(user.getEmail())
                .name(user.getName())
                .subscribe(user.isSubscribe())
                .build();
        return userEntity;
    }


    public List<UserEntity> getSubscribers(){
        return userRepository.getAllBySubscribeIsTrue();
    }


    public Optional<UserEntity> getUserByEmail(String email){
       return  userRepository.findUserEntityByEmail(email);
    }

}