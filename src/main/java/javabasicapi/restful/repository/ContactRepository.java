package javabasicapi.restful.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javabasicapi.restful.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID>, JpaSpecificationExecutor<Contact>{
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByUserId(UUID userId);
    Optional<Contact> findByPhone(String phone);
    Optional<Contact> findByIdAndUserId(UUID id, UUID userId);
}
