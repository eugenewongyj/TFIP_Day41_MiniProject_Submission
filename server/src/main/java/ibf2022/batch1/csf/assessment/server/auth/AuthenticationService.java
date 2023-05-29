package ibf2022.batch1.csf.assessment.server.auth;

import java.util.Optional;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ibf2022.batch1.csf.assessment.server.configs.JwtService;
import ibf2022.batch1.csf.assessment.server.person.Person;
import ibf2022.batch1.csf.assessment.server.person.PersonRepository;
import ibf2022.batch1.csf.assessment.server.person.Role;
import ibf2022.batch1.csf.assessment.server.services.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    public AuthenticationResponse signup(AuthenticationRequest request) throws Exception  {
        Optional<Person> person = personRepository.getPersonByEmail(request.getEmail());

        if (person.isPresent()) {
            throw new Exception("Email already exists");
        }

        Person newPerson = Person.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        personRepository.addPerson(newPerson);
        String jwtToken = jwtService.generateToken(newPerson);

        // Send Email
        emailService.sendRegistrationEmail(request.getEmail());
        return AuthenticationResponse.builder().token(jwtToken).email(newPerson.getEmail()).expiresIn("3600").build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword())
        );
        Person person = personRepository.getPersonByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder().token(jwtToken).email(person.getEmail()).expiresIn("3600").build();
    }
    
}
