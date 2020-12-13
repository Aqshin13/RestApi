package com.company.controller;

import com.company.dto.ResponseDTO;
import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.service.inter.UserServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    @Qualifier("userDao")
    private UserServiceInter userRepositoryCustom;

    @CrossOrigin
    @GetMapping(value = "/users")
    public ResponseEntity<ResponseDTO> getUsers(@RequestParam(value = "name", required = false) String name,
             @RequestParam(value = "surname", required = false) String surname
    ) {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = userRepositoryCustom.getAllUser(name, surname, null);
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            userDTOS.add(new UserDTO(u));
        }
        return ResponseEntity.ok(ResponseDTO.of(userDTOS));
    }

    @CrossOrigin
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<ResponseDTO> getUsers(@PathVariable("id") int id) {
        User user = userRepositoryCustom.getById(id);
        return ResponseEntity.ok(ResponseDTO.of(new UserDTO(user)));
    }

    @CrossOrigin
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("id") int id) {
        User user = userRepositoryCustom.getById(id);
        userRepositoryCustom.removeUser(id);

        return ResponseEntity.ok(ResponseDTO.of(new UserDTO(user), "Successfully deleted"));
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDTOS);
    }

    @CrossOrigin
    @PostMapping("/users")
    public ResponseEntity<ResponseDTO> addUser(@RequestBody UserDTO userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail("null");
        user.setPhone("12345");
        user.setSurname(userDto.getSurname());
        user.setPassword(userDto.getPassword());
        userRepositoryCustom.addUser(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        return ResponseEntity.ok(ResponseDTO.of(userDTO, "Successfully added"));
    }

}
