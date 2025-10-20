package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User user) {
        // Logic to save user to the database
        if (user == null) {
            return null;
        }
        return this.userRepository.save(user);
    }

    public void deleteUser(Long id) {
        // Logic to delete user from the database
        if (id.longValue() > 0) {
            this.userRepository.deleteById(id);
        }
    }

    public User getDetailUser(Long id) {
        // Logic to get user detail from the database
        return this.userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long userId, User userUpdate) {
        // Logic to update user in the database
        // This is a placeholder implementation; actual implementation would parse
        // userUpdate and apply changes
        User existingUser = this.userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            // Apply updates to existingUser based on userUpdate string
            // For simplicity, let's assume userUpdate is just a new name
            existingUser.setEmail(userUpdate.getEmail());
            existingUser.setName(userUpdate.getName());
            existingUser.setPassword(userUpdate.getPassword());
            return this.userRepository.save(existingUser);
        }
        return null;
    }

    public User getUserByUserName(String email) {
        return this.userRepository.findByEmail(email);
    }
}
