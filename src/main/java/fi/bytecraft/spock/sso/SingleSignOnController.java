package fi.bytecraft.spock.sso;

import fi.bytecraft.spock.sso.domain.LoginAction;
import fi.bytecraft.spock.sso.domain.LoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sso")
public class SingleSignOnController {

    private SingleSignOnService service;

    public SingleSignOnController(SingleSignOnService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginAction> login(
            @RequestBody LoginRequestDTO loginInfo,
            HttpServletRequest request
    ) throws LoginException {
        String ip = request.getRemoteAddr();

        return new ResponseEntity<>(
                service.login(loginInfo.getEmail(), loginInfo.getPassword(), ip), HttpStatus.OK
        );
    }

}
