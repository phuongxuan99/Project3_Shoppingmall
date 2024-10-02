package com.example.project3.user.repo;


import com.example.project3.user.entity.UserUpgrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUpgradeRepo extends JpaRepository<UserUpgrade, Long> {
}
