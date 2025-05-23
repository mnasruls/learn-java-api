package javabasicapi.restful.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javabasicapi.restful.entity.Address;
import javabasicapi.restful.entity.Contact;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address>{
    Optional<Address> findByIdAndContactId(UUID id, UUID contactId);
    List<Address> findByContact(Contact contact);
}
