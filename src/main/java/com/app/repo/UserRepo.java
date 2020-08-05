package com.app.repo;

import com.app.model.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findOneByUserId(String userId);
    Optional<User> findOneByUserIdAndPassword(String userId, String password);
    /*
    public List<User> findAll();
    public Page<User> findAll(Pageable p);
    User save(User c);
    void delete(User c);
    void delete(Integer id);
    boolean exists( Integer id);
    */
}

