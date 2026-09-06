package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf9.servicehub.dto.RegisterRequestDTO;
import gr.aueb.cf9.servicehub.dto.UserResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.User;
import gr.aueb.cf9.servicehub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       Mapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EntityAlreadyExistsException("Email already in use: " + dto.email());
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());

        User saved = userRepository.save(user);
        return mapper.toResponseDTO(saved);
    }
}