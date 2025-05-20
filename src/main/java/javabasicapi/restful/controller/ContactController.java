package javabasicapi.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javabasicapi.restful.dto.ContactResponse;
import javabasicapi.restful.dto.CreateContactRequest;
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
}
