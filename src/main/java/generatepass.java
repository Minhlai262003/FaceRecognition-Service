import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class generatepass {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encoded = encoder.encode("manager_password");
        System.out.println(encoded);
    }
}
