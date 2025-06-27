package com.ywms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUserId(int userId);

    
    Optional<User> findFirstByIdentityNumber(int identityNumber);

    @Query(value = "SELECT * FROM user WHERE identity_number = ?1 ORDER BY RAND() LIMIT 1",
            nativeQuery = true)
    Optional<User> findRandomByIdentityNumberNative(int identityNumber);

    @Query(value = "SELECT * FROM user WHERE departmentB = ?1 ORDER BY RAND() LIMIT 1",
            nativeQuery = true)
    Optional<User> findRandomApproverByIdentityNumberNative(int departmentB);


}
