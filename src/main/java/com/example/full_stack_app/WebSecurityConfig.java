package com.example.full_stack_app;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// LƯU Ý: Đảm bảo class này nằm trong thư mục của bạn: com.example.full_stack_app

@Configuration
public class WebSecurityConfig {

    private final DataSource dataSource;

    // Constructor Injection (Được khuyến nghị, khắc phục lỗi Autowiring trong IDE)
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // SỬA LỖI: Sử dụng NoOpPasswordEncoder để chấp nhận mật khẩu không mã hóa ('123456')
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // TẢI USER/ROLE TÙY CHỈNH TỪ BẢNG 'info'
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Truy vấn tìm người dùng (khớp với cột 'user')
        manager.setUsersByUsernameQuery("SELECT user as username, password, 1 as enabled FROM info WHERE user = ?");
        // Truy vấn tìm vai trò (khớp với cột 'role' và alias thành 'authority')
        manager.setAuthoritiesByUsernameQuery("SELECT user as username, role as authority FROM info WHERE user = ?");

        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}