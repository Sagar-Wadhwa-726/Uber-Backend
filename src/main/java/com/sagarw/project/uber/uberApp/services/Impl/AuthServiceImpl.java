package com.sagarw.project.uber.uberApp.services.Impl;

import com.sagarw.project.uber.uberApp.dto.DriverDto;
import com.sagarw.project.uber.uberApp.dto.SignupDto;
import com.sagarw.project.uber.uberApp.dto.UserDto;
import com.sagarw.project.uber.uberApp.entities.Rider;
import com.sagarw.project.uber.uberApp.entities.User;
import com.sagarw.project.uber.uberApp.entities.enums.Role;
import com.sagarw.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.sagarw.project.uber.uberApp.repositories.UserRepository;
import com.sagarw.project.uber.uberApp.services.AuthService;
import com.sagarw.project.uber.uberApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;

    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    public UserDto signup(SignupDto signupDto) {

        userRepository.findByEmail(signupDto.getEmail()).ifPresent(user ->{
            throw new RuntimeConflictException("Can't signup, user already exists with this e-mail !");
        });

        User mappedUser = modelMapper.map(signupDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);

        /*Everything which is associated to the user has to be also created over here
        * Create user related entities*/

        // RIDER needs to be created
        riderService.createNewRider(savedUser);

        // TODO :: WALLET needs to be created, add wallet related services
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
