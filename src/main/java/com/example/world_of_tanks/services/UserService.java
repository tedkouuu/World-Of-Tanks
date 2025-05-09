package com.example.world_of_tanks.services;

import com.example.world_of_tanks.models.UserEntity;
import com.example.world_of_tanks.models.UserRoleEntity;
import com.example.world_of_tanks.models.dto.DeleteUserDTO;
import com.example.world_of_tanks.models.dto.EditUserDTO;
import com.example.world_of_tanks.models.dto.RegisterDTO;
import com.example.world_of_tanks.models.enums.UserRoleEnum;
import com.example.world_of_tanks.repositories.UserRepository;
import com.example.world_of_tanks.repositories.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
//    private final EmailService emailService;
    private final ModelMapper modelMapper;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
//        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }


    public void register(RegisterDTO registerDTO, Locale preferredLocale) {

        UserRoleEnum userRoleEnum = registerDTO.getRole();

        UserRoleEntity role = this.userRoleRepository.findByUserRole(userRoleEnum);

        UserEntity user = modelMapper.map(registerDTO, UserEntity.class);

        user.setRoles(List.of(role));

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        this.userRepository.save(user);

//        emailService.sendRegistrationEmail(registerDTO.getEmail(), registerDTO.getFullName(),
//                preferredLocale);

    }

    public void editUser(EditUserDTO editUserDTO) {

        Optional<UserEntity> oldUser = this.userRepository.findByUsername(editUserDTO.getOldUsername());

        UserEntity entityToEdit = oldUser.get();

        entityToEdit.setFullName(editUserDTO.getFullName()).setPassword(passwordEncoder.encode(editUserDTO.getPassword())).setUsername(editUserDTO.getNewUsername());

        this.userRepository.save(entityToEdit);


    }

    public void deleteUser(DeleteUserDTO deleteUserDTO) {

        Optional<UserEntity> user = this.userRepository.findByUsername(deleteUserDTO.getUsername());

        this.userRepository.delete(user.get());

    }
}

/*
        Automatic login

        public void login(String username) {

        UserEntity newUser = modelMapper.map(userRegisterDTO,UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        UserRoleEntity role = this.userRoleRepository.findByUserRole(userRoleEnum);

        user.setRoles(List.of(role));

        this.userRepository.save(user);

        emailService.sendRegistrationEmail(registerDTO.getEmail(), registerDTO.getFullName());
    }


    private void login(UserEntity userEntity) {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(userEntity.getUsername());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(auth);
*/


