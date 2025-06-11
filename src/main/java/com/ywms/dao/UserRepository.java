package com.ywms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUserId(int userId);

    /**
     * 根据角色ID查找第一个匹配的用户。
     * 这只是一个简单的示例，用于自动分配。
     * @param identityNumber 角色ID (例如 3 代表操作人员)
     * @return 第一个找到的用户
     */
    Optional<User> findFirstByIdentityNumber(int identityNumber);
}
