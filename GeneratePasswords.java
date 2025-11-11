import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswords {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== FoodShare Password Hashes ===");
        System.out.println("Admin (admin123): " + encoder.encode("admin123"));
        System.out.println("Donor (donor123): " + encoder.encode("donor123"));  
        System.out.println("Receiver (recv123): " + encoder.encode("recv123"));
    }
}