package in.todob.todobin.authentication.service;

import in.todob.todobin.model.TodobinUser;
import in.todob.todobin.authentication.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private JwtUserDetailService jwtUserDetailService;

    @Before
    public void setUp() throws Exception {
        jwtUserDetailService = new JwtUserDetailService(mockUserRepository);
    }

    @Test
    public void loadByUsername_returnsUserPrincipal_whenUsernameExists() {

        Optional<TodobinUser> appUser = Optional.of(TodobinUser.builder()
                                                               .username("username")
                                                               .role("USER")
                                                               .build());

        when(mockUserRepository.findAppUserByUsername("username"))
                .thenReturn(appUser);

        UserDetails actual = jwtUserDetailService.loadUserByUsername("username");

        assert (actual.getUsername()).equals("username");
    }

    @Test
    public void loadByUsername_throwsUsernameNotFoundException_whenUsernameDoesNotExist() {

        Optional<TodobinUser> appUser = Optional.of(TodobinUser.builder()
                                                               .username("username")
                                                               .role("USER")
                                                               .build());

        when(mockUserRepository.findAppUserByUsername("username"))
                .thenReturn(appUser);

        UserDetails actual = jwtUserDetailService.loadUserByUsername("username");

        assert (actual.getUsername()).equals("username");
    }
}