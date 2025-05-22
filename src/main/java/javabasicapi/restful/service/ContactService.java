package javabasicapi.restful.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;
import javabasicapi.restful.dto.ContactResponse;
import javabasicapi.restful.dto.CreateContactRequest;
import javabasicapi.restful.dto.SearchContactRequest;
import javabasicapi.restful.dto.UpdateContactRequest;
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

    @Transactional(readOnly = true)
    public ContactResponse getContactById(UUID contactId, User user) {
        Contact contact = contactRepository.findByIdAndUserId( contactId,user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phoneNumber(contact.getPhone())
                .email(contact.getEmail())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
    
    @Transactional
    public ContactResponse updateContact(UUID contactId, UpdateContactRequest request, User user) {
        Contact contact = contactRepository.findByIdAndUserId( contactId,user.getId())
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        if (request.getFirstName() != null) {
            contact.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            contact.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            contact.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            contact.setEmail(request.getEmail());
        }
        contact.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        contactRepository.save(contact);
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phoneNumber(contact.getPhone())
                .email(contact.getEmail())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
    
    @Transactional
    public void deleteContact(UUID contactId, User user) {
        Contact contact = contactRepository.findByIdAndUserId(contactId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        contactRepository.delete(contact);
    }
    
    @Transactional(readOnly = true)
    public Page<ContactResponse> getContacts(User user, SearchContactRequest search) {
        if (search.getPage() == null || search.getPage() <= 0) {
            search.setPage(1);
        } else {
            search.setPage(search.getPage() - 1);
        };
        if (search.getSize() == null || search.getSize() <= 0) {
            search.setSize(10);
        };
        Specification<Contact> spec = (root, query, builder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(search.getName())){
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%" + search.getName() + "%"),
                        builder.like(root.get("lastName"), "%" + search.getName() + "%")
                        ));
                    ;
            }
            if (Objects.nonNull(search.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%" + search.getPhone() + "%"));
            }
            if (Objects.nonNull(search.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + search.getEmail() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();  
        };

        Pageable pageable = PageRequest.of(search.getPage(), search.getSize());
        Page<Contact>contacts= contactRepository.findAll(spec, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream().map(contact -> ContactResponse.builder()
               .id(contact.getId())
               .firstName(contact.getFirstName())
               .lastName(contact.getLastName())
               .phoneNumber(contact.getPhone())
               .email(contact.getEmail())
               .createdAt(contact.getCreatedAt())
               .updatedAt(contact.getUpdatedAt())
              .build()).toList();
        
        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
