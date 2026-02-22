package com.roima.hrms.user.repository;

import com.roima.hrms.user.entity.Role;
import com.roima.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
     boolean existsByEmail(String email);
     List<User> findByManagerId(Long managerId);
     List<User> findByNameContainingIgnoreCase(String name);
     List<User> findByManager_IdIsNull();

     List<User> findByRole_Role(String role);

     /**
      * Find all employees whose birthday is today
      */
     @Query("SELECT u FROM User u WHERE MONTH(u.birth_date) = MONTH(:date) AND DAY(u.birth_date) = DAY(:date)")
     List<User> findByBirthdayToday(@Param("date") LocalDate date);

     /**
      * Find all employees whose work anniversary is today
      */
     @Query("SELECT u FROM User u WHERE MONTH(u.joining_date) = MONTH(:date) AND DAY(u.joining_date) = DAY(:date)")
     List<User> findByAnniversaryToday(@Param("date") LocalDate date);
}
