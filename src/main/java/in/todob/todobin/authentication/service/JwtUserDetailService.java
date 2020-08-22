package in.todob.todobin.authentication.service;

import in.todob.todobin.authentication.model.AppUser;
import in.todob.todobin.authentication.model.UserPrincipal;
import in.todob.todobin.authentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findAppUserByUsername(username)
                                        .orElseThrow(() -> new UsernameNotFoundException("Could not find user.s"));

        return UserPrincipal.create(appUser);
    }
}
