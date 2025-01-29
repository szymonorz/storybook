package pl.szyorz.storybook.entity.role;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {
    List<Role> findByNameIn(Collection<String> nameList);
    List<Role> findAllByUsersId(UUID userId);
}
