package pl.szyorz.storybook.entity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.szyorz.storybook.entity.role.Role;
import pl.szyorz.storybook.entity.role.RoleRepository;
import pl.szyorz.storybook.entity.user.exception.DoesntExistException;

import java.util.*;

@Service
@AllArgsConstructor
public class DetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new DoesntExistException("User not found");
        }
        User user = userOptional.get();
        List<Role> _roles = roleRepository.findAllByUsersId(user.getId());
        String[] roles = _roles
                .stream()
                .map(Role::getName)
                .toArray(String[]::new);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role: _roles) {
            role.getPrivileges().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.name())));
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .roles(roles)
                .authorities(authorities)
                .build();
    }
}
