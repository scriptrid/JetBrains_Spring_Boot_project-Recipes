package recipes.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.exceptions.UserAlreadyExistsException;
import recipes.model.UserDetailsImpl;
import recipes.model.dto.CreateUserDto;
import recipes.model.entity.UserEntity;
import recipes.repository.UsersRepository;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addUser(CreateUserDto dto) {
        if (usersRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new UserAlreadyExistsException();
        }
        UserEntity entity = toEntity(dto);
        usersRepository.save(entity);
    }

    private UserEntity toEntity(CreateUserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setEmail(dto.email());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        return entity;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity entity = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("NotFound: " + email));
        return new UserDetailsImpl(entity);
    }

    public UserEntity getUser(UserDetails details) {
        return usersRepository.findByEmail(details.getUsername()).orElseThrow();
    }
}
