package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.RoleRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        List<Role> roles = roleRepository.findAllByUsersId(user.getId());
        Collection<SimpleGrantedAuthority> authorities = roles
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName()))
                .toList();

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
