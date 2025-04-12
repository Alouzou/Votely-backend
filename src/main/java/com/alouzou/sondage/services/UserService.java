package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<UserDTO> listerUsers(Pageable pageable);
}
