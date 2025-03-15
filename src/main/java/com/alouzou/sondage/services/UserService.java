package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User createUser(String username, String email, String password, RoleName roleName);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    boolean deleteUser(Long id);
    Optional<User> getUserById(Long id);

    User modifyUser(Long id, UserDTO userDTO);
}
