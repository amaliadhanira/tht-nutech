package com.ibretail.ibretaildemo.membership;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository <User, Long>{
	public List<User> findAll();
	Optional<User> findById(int id);
	public User save(User user);
	Optional<User> findByEmailAndPassword(String email, String password);
	Optional<User> findByEmail(String email);
}
