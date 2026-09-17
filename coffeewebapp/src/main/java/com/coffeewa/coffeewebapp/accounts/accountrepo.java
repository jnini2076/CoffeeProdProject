package com.coffeewa.coffeewebapp.accounts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;





@Repository
public interface accountrepo extends JpaRepository<account, Long> {

    Optional<account> findByUsernameAndPassword(String username, String password);
    Optional<account> findByUsernameIgnoreCase(String username);

    Optional<account> findByPhonenumber(String phonenumber);

    Optional<account> findByPhonenumberAndUsername(String username, String phonenumber);

    Optional<AccountVerifiedDTO> findByUsernameAndVerifiedTrue(String username);





}
