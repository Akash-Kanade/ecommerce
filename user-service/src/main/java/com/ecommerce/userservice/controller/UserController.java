package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserRequestDTO;
import com.ecommerce.userservice.dto.UserResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping("getall")
    public ResponseEntity<List<UserResponseDTO>> getall(){
    var list = userService.getAllUser();
    return ResponseEntity.ok().body(list);
    }

    @GetMapping("getuser/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id)
    {
        return  ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable long id, @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO response = userService.updateUser(userRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("update-property/{id}")
    public ResponseEntity<UserResponseDTO> updateProperty(@PathVariable long id, @RequestBody UserRequestDTO userRequestDTO){
        return  ResponseEntity.ok().body(userService.updateUserProperty(id, null, null));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        String response = userService.deleteUserById(id);
        return ResponseEntity.ok().body(response);
    }



}
