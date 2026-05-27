package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.RegisterUserDTO;
import com.ecommerce.userservice.dto.UserRequestDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.InvalidUserException;
import com.ecommerce.userservice.exception.UserAlreadyExistException;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository repo, UserMapper userMapper, PasswordEncoder encoder) {
        this.repository = repo;
        this.userMapper = userMapper;
        this.passwordEncoder = encoder;
    }

    @Override
    public UserResponseDTO registerUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistException {
        if (repository.existsUserByEmail(registerUserDTO.getEmail()))
            throw new UserAlreadyExistException("User with email: " + registerUserDTO.getEmail() + " already exist");

        User user = userMapper.toEntity(registerUserDTO);
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));

        // defaults
        user.setActive(true);
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        // save
        User savedUser = repository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUser() {
        var userList = repository.findAll();
        List<UserResponseDTO> userResponseDTOList = userList.stream().map(this.userMapper::toResponse).toList();
        return userResponseDTOList;
    }

    @Override
    public UserResponseDTO getUserById(long id) {
        User u = fetchById(id);
        return this.userMapper.toResponse(u);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(UserRequestDTO userDTO) {
        User user = repository.findUserByEmail(userDTO.getEmail()).orElseThrow(()-> new InvalidUserException("Invalid User"));
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setMobileNo(userDTO.getMobileNo());
        return userMapper.toResponse(this.repository.save(user));
    }

    @Override
    public UserResponseDTO updateUserProperty(long id, String property, String value) {
        return null;
    }

    @Override
    public String deleteUserById(long id) {
        User u = fetchById(id);
        repository.delete(u);
        // placeholder to delete cart-data for a user.
        return "User with id:+"+ id +"has been deleted";
    }

    private User fetchById(long id)
    {
        return repository.findById(id).orElseThrow(() -> new InvalidUserException("Invalid User Id"));
    }
}
