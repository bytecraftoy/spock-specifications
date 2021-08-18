package fi.bytecraft.spock.sso;

import fi.bytecraft.spock.sso.domain.UserLoginInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLoginInfoRepository extends CrudRepository<UserLoginInfo, Long> {

    Optional<UserLoginInfo> findByEmail(String email);

}
