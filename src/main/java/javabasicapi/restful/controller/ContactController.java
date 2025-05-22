package javabasicapi.restful.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javabasicapi.restful.dto.ContactResponse;
import javabasicapi.restful.dto.CreateContactRequest;
import javabasicapi.restful.dto.PaginationResponse;
import javabasicapi.restful.dto.SearchContactRequest;
import javabasicapi.restful.dto.UpdateContactRequest;
import javabasicapi.restful.dto.WebResponse;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.service.ContactService;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(
        path= "api/contacts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
        public WebResponse<ContactResponse> createContact(
            @RequestBody CreateContactRequest request, User user
        ) {
            ContactResponse response = contactService.createContact(request, user);
            return WebResponse.<ContactResponse>builder().data(response).build();
        }

    @GetMapping(
        path = "api/contacts/{contactId}",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> getContact(User user, @PathVariable("contactId") UUID contactId) {
        ContactResponse response = contactService.getContactById(contactId, user);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }
    
    @PutMapping(
        path = "api/contacts/{contactId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> updateContact(
        @RequestBody UpdateContactRequest request,
        @PathVariable("contactId") UUID contactId,
        User user
    ) {
        ContactResponse response = contactService.updateContact(contactId, request, user);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }

    @DeleteMapping(
        path = "api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteContact(
        @PathVariable("contactId") UUID contactId,
        User user
    ) {
        contactService.deleteContact(contactId, user);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
        path = "api/contacts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>> getContacts(
            User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        SearchContactRequest request = SearchContactRequest.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .page(page)
                .size(size)
                .build();
        Page<ContactResponse> response = contactService.getContacts(user, request);
        return WebResponse.<List<ContactResponse>>builder()
                .data(response.getContent())
        .pagination(
            PaginationResponse.builder()
                    .currentPage(page)
                    .totalPages(response.getTotalPages())
                    .size(size)
                    .totalData(response.getTotalElements())
                    .build()
        ).build();
    }
}
