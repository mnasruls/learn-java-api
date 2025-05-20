package javabasicapi.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import javabasicapi.restful.dto.ContactResponse;
import javabasicapi.restful.dto.CreateContactRequest;
import javabasicapi.restful.entity.Contact;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.repository.ContactRepository;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ContactResponse createContact(CreateContactRequest request, User user) {
        validationService.validate(request);
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        var contactRes = contactRepository.save(contact);
        return ContactResponse.builder()
                .id(contactRes.getId())
                .firstName(contactRes.getFirstName())
                .lastName(contactRes.getLastName())
                .phoneNumber(contactRes.getPhone())
                .email(contactRes.getEmail())
                .createdAt(contactRes.getCreatedAt())
                .updatedAt(contactRes.getUpdatedAt())
                .build();
    }
}
