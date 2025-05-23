package javabasicapi.restful.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javabasicapi.restful.dto.AddressResponse;
import javabasicapi.restful.dto.CreateAddressRequest;
import javabasicapi.restful.dto.UpdateAddressRequest;
import javabasicapi.restful.entity.Address;
import javabasicapi.restful.entity.Contact;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.repository.AddressRepository;
import javabasicapi.restful.repository.ContactRepository;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request, User user) {
        validationService.validate(request);
        var contact = contactRepository.findByIdAndUserId(UUID.fromString(request.getContactId()), user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);
        addressRepository.save(address);
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .contactId(address.getContact().getId())
                .build();
    }
    
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(UUID id, UUID contactId, User user) {
        var address = addressRepository.findByIdAndContactId(id, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

                if (address.getContact().getUser().getId() != user.getId()) {
            System.out.println("Address not found");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");   
        }

        return AddressResponse.builder()
               .id(address.getId())
               .street(address.getStreet())
               .city(address.getCity())
               .province(address.getProvince())
               .country(address.getCountry())
               .postalCode(address.getPostalCode())
               .contactId(address.getContact().getId())
               .build();
    }

    @Transactional
    public void deleteAddressById(UUID id, UUID contactId, User user) {
        var address = addressRepository.findByIdAndContactId(id, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));
        if (address.getContact().getUser().getId() != user.getId()) {
            System.out.println("Address not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }
        addressRepository.deleteById(id);
    }
    
    @Transactional
    public AddressResponse updateAddress(UUID id, UUID contactId, UpdateAddressRequest request, User user) {

        var address = addressRepository.findByIdAndContactId(id, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));
        if (address.getContact().getUser().getId() != user.getId()) {
            System.out.println("Address not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }
        if (request.getStreet() != null) {
            address.setStreet(request.getStreet());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getProvince() != null) {
            address.setProvince(request.getProvince());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }
        addressRepository.save(address);
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .contactId(address.getContact().getId())
                .build();
    }
    
    @Transactional(readOnly = true)
    public List<AddressResponse> listAddresses(User user, UUID contactId) {
        Contact contact = contactRepository.findByIdAndUserId(contactId, user.getId())
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        List<Address> addresses = addressRepository.findByContact(contact);
        return addresses.stream().map(address -> AddressResponse.builder()
              .id(address.getId())
              .street(address.getStreet())
              .city(address.getCity())
              .province(address.getProvince())
              .country(address.getCountry())
              .postalCode(address.getPostalCode())
              .contactId(address.getContact().getId())
              .build()).toList();
    }
}
