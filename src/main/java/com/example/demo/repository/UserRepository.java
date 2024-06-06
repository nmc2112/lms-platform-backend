package com.example.demo.repository;

import com.example.demo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.example.demo.entity")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Transactional
    @Modifying
    @Query(value = "delete from user where userClerkId is not null",nativeQuery = true)
    void deleteUsersFromClerk();

    List<User> findByRole(String role);

    @Query(value = "select * from user where role='teacher'",nativeQuery = true)
    public List<User> findAllTeacher();

}
