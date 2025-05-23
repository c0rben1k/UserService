package by.algin.userservice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        // Создаём экземпляр BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Задаём пароль для шифрования
        String rawPassword = "11"; // Замените на желаемый пароль

        // Шифруем пароль
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Выводим зашифрованный пароль
        System.out.println("Зашифрованный пароль: " + encodedPassword);
    }
}