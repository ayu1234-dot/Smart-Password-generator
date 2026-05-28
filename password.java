import java.security.SecureRandom;
import java.util.*;

/**
 * SmartPasswordGenerator - A secure password generation and evaluation system
 * This application generates strong passwords based on user-specific information
 * and evaluates their strength using multiple security criteria.
 */

public class password {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordGeneratorApp app = new PasswordGeneratorApp();
        
        System.out.println("\n========================================");
        System.out.println("   SMART PASSWORD GENERATOR v2.0");
        System.out.println("   With User-Specific Data Integration");
        System.out.println("========================================\n");
        
        boolean running = true;
        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Generate a secure password");
            System.out.println("2. Evaluate password strength");
            System.out.println("3. Generate customized password");
            System.out.println("4. Generate password with user details (Name, PAN, DOB)");
            System.out.println("5. Run test with multiple users");
            System.out.println("6. Exit");
            System.out.print("\nEnter your choice (1-6): ");
            
            int choice = getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    app.generateDefaultPassword();
                    break;
                case 2:
                    app.evaluatePassword(scanner);
                    break;
                case 3:
                    app.generateCustomPassword(scanner);
                    break;
                case 4:
                    app.generatePasswordWithUserDetails(scanner);
                    break;
                case 5:
                    app.runTestWithMultipleUsers();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nThank you for using Smart Password Generator!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }
    
    private static int getIntInput(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}

/**
 * PasswordGeneratorApp - Main application controller
 * Manages user interactions and coordinates password generation and evaluation
 */
class PasswordGeneratorApp {
    private PasswordGenerator generator;
    private PasswordEvaluator evaluator;
    
    public PasswordGeneratorApp() {
        this.generator = new PasswordGenerator();
        this.evaluator = new PasswordEvaluator();
    }
    
    /**
     * Generates a default secure password with all character types
     */
    public void generateDefaultPassword() {
        System.out.println("\n--- Generating Default Secure Password ---");
        PasswordConfig config = new PasswordConfig(16, true, true, true, true);
        String password = generator.generatePassword(config);
        
        System.out.println("\nGenerated Password: " + password);
        System.out.println("Password Length: " + password.length());
        
        PasswordStrength strength = evaluator.evaluatePassword(password);
        displayStrengthReport(strength);
    }
    
    /**
     * Allows user to evaluate an existing password
     */
    public void evaluatePassword(Scanner scanner) {
        System.out.println("\n--- Password Strength Evaluator ---");
        System.out.print("Enter the password to evaluate: ");
        String password = scanner.nextLine();
        
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty!");
            return;
        }
        
        PasswordStrength strength = evaluator.evaluatePassword(password);
        displayStrengthReport(strength);
    }
    
    /**
     * Generates a password with user-specific customization
     */
    public void generateCustomPassword(Scanner scanner) {
        System.out.println("\n--- Customized Password Generator ---");
        
        System.out.print("Enter desired password length (8-32): ");
        int length = getValidLength(scanner, 8, 32);
        
        System.out.print("Include lowercase letters? (y/n): ");
        boolean useLowercase = getYesNo(scanner);
        
        System.out.print("Include uppercase letters? (y/n): ");
        boolean useUppercase = getYesNo(scanner);
        
        System.out.print("Include numbers? (y/n): ");
        boolean useNumbers = getYesNo(scanner);
        
        System.out.print("Include special characters? (y/n): ");
        boolean useSpecial = getYesNo(scanner);
        
        // Validate at least one option is selected
        if (!useLowercase && !useUppercase && !useNumbers && !useSpecial) {
            System.out.println("Error: Please select at least one character type!");
            return;
        }
        
        // Optional: Get user-specific information
        System.out.print("Include user-specific word/prefix? (optional, press Enter to skip): ");
        String userInfo = scanner.nextLine().trim();
        
        PasswordConfig config = new PasswordConfig(length, useLowercase, useUppercase, useNumbers, useSpecial);
        String password = generator.generatePassword(config, userInfo);
        
        System.out.println("\n--- Generated Custom Password ---");
        System.out.println("Password: " + password);
        System.out.println("Length: " + password.length());
        
        PasswordStrength strength = evaluator.evaluatePassword(password);
        displayStrengthReport(strength);
    }
    
    /**
     * Generates password with user-specific information (Name, PAN, DOB)
     */
    public void generatePasswordWithUserDetails(Scanner scanner) {
        System.out.println("\n--- Password Generation with User Details ---");
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter your PAN (e.g., AAAPA1234A): ");
        String pan = scanner.nextLine().trim();
        
        System.out.print("Enter your Date of Birth (DD/MM/YYYY format): ");
        String dob = scanner.nextLine().trim();
        
        // Validate inputs
        if (name.isEmpty() || pan.isEmpty() || dob.isEmpty()) {
            System.out.println("Error: All fields are required!");
            return;
        }
        
        User user = new User(name, pan, dob);
        
        // Display user details
        System.out.println("\n--- User Profile ---");
        System.out.println("Name: " + user.getName());
        System.out.println("PAN: " + user.getPan());
        System.out.println("Date of Birth: " + user.getDateOfBirth());
        
        // Generate password using user data
        System.out.print("\nEnter desired password length (8-32): ");
        int length = getValidLength(scanner, 8, 32);
        
        PasswordConfig config = new PasswordConfig(length, true, true, true, true);
        String password = generator.generatePasswordFromUser(config, user);
        
        System.out.println("\n--- Generated Password ---");
        System.out.println("Password: " + password);
        System.out.println("Length: " + password.length());
        
        PasswordStrength strength = evaluator.evaluatePassword(password);
        classifyPasswordStrength(strength); // Simplified classification (Weak, Medium, Strong)
        displayStrengthReport(strength);
    }
    
    /**
     * Runs test with multiple sample users
     */
    public void runTestWithMultipleUsers() {
        System.out.println("\n========================================");
        System.out.println("   TESTING WITH MULTIPLE USERS");
        System.out.println("========================================\n");
        
        // Create test users
        User[] testUsers = {
            new User("Rajesh Kumar", "AAAPK1234K", "15/03/1990"),
            new User("Priya Sharma", "BBPRS5678S", "22/07/1995"),
            new User("Amit Patel", "CCAPK9012K", "10/11/1988"),
            new User("Neha Verma", "DDNEV3456V", "05/08/1992"),
            new User("Vikram Singh", "EEVSI7890I", "30/01/1987")
        };
        
        PasswordConfig standardConfig = new PasswordConfig(14, true, true, true, true);
        
        for (int i = 1; i <= testUsers.length; i++) {
            User user = testUsers[i - 1];
            displayUserTestResult(i, user, standardConfig);
        }
        
        System.out.println("\n========================================");
        System.out.println("   TEST SUMMARY");
        System.out.println("========================================");
        System.out.println("Total Users Tested: " + testUsers.length);
        System.out.println("All passwords generated successfully!");
    }
    
    /**
     * Displays test results for a single user
     */
    private void displayUserTestResult(int userNum, User user, PasswordConfig config) {
        System.out.println("--- User " + userNum + " ---");
        System.out.println("Name: " + user.getName());
        System.out.println("PAN: " + user.getPan());
        System.out.println("DOB: " + user.getDateOfBirth());
        
        PasswordGenerator generator = new PasswordGenerator();
        String password = generator.generatePasswordFromUser(config, user);
        PasswordStrength strength = evaluator.evaluatePassword(password);
        
        System.out.println("Generated Password: " + password);
        System.out.println("Password Length: " + password.length());
        System.out.println("Strength Level: " + strength.getStrengthLevel());
        System.out.println("Strength Score: " + strength.getScore() + "/100");
        classifyPasswordStrength(strength);
        System.out.println();
    }
    
    /**
     * Classifies password as Weak, Medium, or Strong (simplified version)
     */
    private void classifyPasswordStrength(PasswordStrength strength) {
        String classification;
        if (strength.getScore() >= 60) {
            classification = "STRONG";
        } else if (strength.getScore() >= 40) {
            classification = "MEDIUM";
        } else {
            classification = "WEAK";
        }
        System.out.println("Classification: " + classification);
    }
    
    /**
     * Displays a detailed strength report for the password
     */
    private void displayStrengthReport(PasswordStrength strength) {
        System.out.println("\n--- Strength Analysis ---");
        System.out.println("Strength Level: " + strength.getStrengthLevel());
        System.out.println("Score: " + strength.getScore() + "/100");
        System.out.println("Has Lowercase: " + strength.hasLowercase());
        System.out.println("Has Uppercase: " + strength.hasUppercase());
        System.out.println("Has Numbers: " + strength.hasNumbers());
        System.out.println("Has Special Characters: " + strength.hasSpecialCharacters());
        System.out.println("Length: " + strength.getLength());
        System.out.println("\nRecommendations: " + strength.getRecommendations());
    }
    
    private int getValidLength(Scanner scanner, int min, int max) {
        try {
            int length = scanner.nextInt();
            if (length >= min && length <= max) {
                return length;
            } else {
                System.out.println("Length must be between " + min + " and " + max + ". Using default: 12");
                return 12;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Invalid input. Using default length: 12");
            return 12;
        }
    }
    
    private boolean getYesNo(Scanner scanner) {
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
}

/**
 * PasswordConfig - Configuration class for password generation parameters
 * Demonstrates the builder pattern and encapsulation
 */
class PasswordConfig {
    private int length;
    private boolean useLowercase;
    private boolean useUppercase;
    private boolean useNumbers;
    private boolean useSpecialCharacters;
    
    public PasswordConfig(int length, boolean useLowercase, boolean useUppercase, 
                         boolean useNumbers, boolean useSpecialCharacters) {
        this.length = length;
        this.useLowercase = useLowercase;
        this.useUppercase = useUppercase;
        this.useNumbers = useNumbers;
        this.useSpecialCharacters = useSpecialCharacters;
    }
    
    // Getters
    public int getLength() { return length; }
    public boolean isUseLowercase() { return useLowercase; }
    public boolean isUseUppercase() { return useUppercase; }
    public boolean isUseNumbers() { return useNumbers; }
    public boolean isUseSpecialCharacters() { return useSpecialCharacters; }
}

/**
 * PasswordGenerator - Core password generation engine
 * Uses SecureRandom for cryptographically strong random number generation
 */
class PasswordGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    
    private SecureRandom random;
    
    public PasswordGenerator() {
        this.random = new SecureRandom();
    }
    
    /**
     * Generates a password based on configuration
     */
    public String generatePassword(PasswordConfig config) {
        return generatePassword(config, "");
    }
    
    /**
     * Generates a password with user-specific information
     */
    public String generatePassword(PasswordConfig config, String userInfo) {
        StringBuilder characterPool = new StringBuilder();
        StringBuilder password = new StringBuilder();
        
        // Build character pool
        if (config.isUseLowercase()) characterPool.append(LOWERCASE);
        if (config.isUseUppercase()) characterPool.append(UPPERCASE);
        if (config.isUseNumbers()) characterPool.append(NUMBERS);
        if (config.isUseSpecialCharacters()) characterPool.append(SPECIAL_CHARS);
        
        // Add user info if provided (limited to avoid excessive length)
        String prefix = "";
        if (!userInfo.isEmpty()) {
            prefix = userInfo.substring(0, Math.min(3, userInfo.length())).toUpperCase();
        }
        
        // Generate password
        int targetLength = config.getLength() - prefix.length();
        for (int i = 0; i < targetLength; i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }
        
        // Shuffle to ensure randomness
        String result = prefix + password.toString();
        return shuffleString(result);
    }
    
    /**
     * Generates a password from user-specific data (Name, PAN, DOB)
     * Incorporates user information into the password generation
     */
    public String generatePasswordFromUser(PasswordConfig config, User user) {
        StringBuilder characterPool = new StringBuilder();
        StringBuilder password = new StringBuilder();
        
        // Build character pool
        if (config.isUseLowercase()) characterPool.append(LOWERCASE);
        if (config.isUseUppercase()) characterPool.append(UPPERCASE);
        if (config.isUseNumbers()) characterPool.append(NUMBERS);
        if (config.isUseSpecialCharacters()) characterPool.append(SPECIAL_CHARS);
        
        // Extract components from user data
        String nameInitials = extractInitials(user.getName());
        String panChars = user.getPan().substring(0, Math.min(3, user.getPan().length()));
        String dobChars = extractDOBNumbers(user.getDateOfBirth());
        
        // Create a seed from user data
        String userSeed = nameInitials + panChars + dobChars;
        
        // Generate password combining user data with random characters
        int seedLength = Math.min(4, userSeed.length());
        String seed = userSeed.substring(0, seedLength).toUpperCase();
        
        // Generate random portion
        int randomLength = config.getLength() - seed.length();
        for (int i = 0; i < randomLength; i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }
        
        // Combine seed and random password, then shuffle
        String result = seed + password.toString();
        return shuffleString(result);
    }
    
    /**
     * Extracts initials from a name
     */
    private String extractInitials(String name) {
        StringBuilder initials = new StringBuilder();
        String[] parts = name.split(" ");
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }
        return initials.toString();
    }
    
    /**
     * Extracts numbers from DOB (DD/MM/YYYY format)
     */
    private String extractDOBNumbers(String dob) {
        // Extract only digits from DOB
        return dob.replaceAll("[^0-9]", "");
    }
    
    /**
     * Shuffles a string for better randomization
     */
    private String shuffleString(String str) {
        char[] chars = str.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}

/**
 * PasswordStrength - Evaluates and stores password strength metrics
 * Implements comprehensive security analysis
 */
class PasswordStrength {
    private String password;
    private int score;
    private String strengthLevel;
    private boolean hasLowercase;
    private boolean hasUppercase;
    private boolean hasNumbers;
    private boolean hasSpecialCharacters;
    
    public PasswordStrength(String password) {
        this.password = password;
        this.score = 0;
        this.strengthLevel = "Weak";
        analyzePassword();
    }
    
    /**
     * Analyzes the password and calculates strength score
     */
    private void analyzePassword() {
        score = 0;
        
        // Check character types
        hasLowercase = password.matches(".*[a-z].*");
        hasUppercase = password.matches(".*[A-Z].*");
        hasNumbers = password.matches(".*[0-9].*");
        hasSpecialCharacters = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*");
        
        // Length score (max 30 points)
        int length = password.length();
        score += Math.min(30, length * 2);
        
        // Character variety (max 40 points)
        if (hasLowercase) score += 10;
        if (hasUppercase) score += 10;
        if (hasNumbers) score += 10;
        if (hasSpecialCharacters) score += 10;
        
        // Bonus for good length (max 20 points)
        if (length >= 12) score += 10;
        if (length >= 16) score += 10;
        
        // Check for sequential patterns (penalty)
        if (hasSequentialPattern()) score -= 10;
        
        // Ensure score is within range
        score = Math.max(0, Math.min(100, score));
        
        // Determine strength level
        if (score >= 80) {
            strengthLevel = "Very Strong";
        } else if (score >= 60) {
            strengthLevel = "Strong";
        } else if (score >= 40) {
            strengthLevel = "Moderate";
        } else if (score >= 20) {
            strengthLevel = "Weak";
        } else {
            strengthLevel = "Very Weak";
        }
    }
    
    /**
     * Detects sequential patterns like "abc" or "123"
     */
    private boolean hasSequentialPattern() {
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);
            
            if ((c2 == c1 + 1 && c3 == c2 + 1) || 
                (c2 == c1 - 1 && c3 == c2 - 1)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Provides recommendations for improving password strength
     */
    public String getRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        if (password.length() < 12) {
            recommendations.add("Increase password length to at least 12 characters");
        }
        if (!hasLowercase) {
            recommendations.add("Add lowercase letters");
        }
        if (!hasUppercase) {
            recommendations.add("Add uppercase letters");
        }
        if (!hasNumbers) {
            recommendations.add("Add numbers");
        }
        if (!hasSpecialCharacters) {
            recommendations.add("Add special characters (!@#$%^&* etc)");
        }
        
        if (recommendations.isEmpty()) {
            return "Your password is strong! Keep it secure and unique.";
        }
        return String.join(", ", recommendations);
    }
    
    // Getters
    public int getScore() { return score; }
    public String getStrengthLevel() { return strengthLevel; }
    public int getLength() { return password.length(); }
    public boolean hasLowercase() { return hasLowercase; }
    public boolean hasUppercase() { return hasUppercase; }
    public boolean hasNumbers() { return hasNumbers; }
    public boolean hasSpecialCharacters() { return hasSpecialCharacters; }
}

/**
 * User - Class to store user-specific information
 * Encapsulates user details: Name, PAN, and Date of Birth
 */
class User {
    private String name;
    private String pan;
    private String dateOfBirth;
    
    public User(String name, String pan, String dateOfBirth) {
        this.name = name;
        this.pan = pan;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getPan() {
        return pan;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPan(String pan) {
        this.pan = pan;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pan='" + pan + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}

/**
 * PasswordEvaluator - Delegates password evaluation to PasswordStrength
 * Provides a clean interface for strength evaluation
 */
class PasswordEvaluator {
    /**
     * Evaluates a password and returns its strength metrics
     */
    public PasswordStrength evaluatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return new PasswordStrength(password);
    }
}

