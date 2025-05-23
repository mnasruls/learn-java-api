package javabasicapi.restful.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javabasicapi.restful.dto.AddressResponse;
import javabasicapi.restful.dto.CreateAddressRequest;
import javabasicapi.restful.dto.UpdateAddressRequest;
import javabasicapi.restful.dto.WebResponse;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.service.AddressService;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping(
        path = "api/addresses",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> createAddress(
                @RequestBody CreateAddressRequest request,
                User user
    ) {
        AddressResponse addressResponse = addressService.createAddress(request, user);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }
    
    @GetMapping(
            path = "api/addresses/{contactId}/{addressId}",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> getAddress(
        @PathVariable("addressId") UUID addressId,
        @PathVariable("contactId") UUID contactId,
        User user
    ) {
        AddressResponse addressResponse = addressService.getAddressById(addressId, contactId, user);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @PutMapping(
        path = "api/addresses/{contactId}/{addressId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> updateAddress(
        @PathVariable("addressId") UUID addressId,
        @PathVariable("contactId") UUID contactId,
        @RequestBody UpdateAddressRequest request,
        User user
    )
    {
        AddressResponse addressResponse = addressService.updateAddress(addressId, contactId, request, user);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @DeleteMapping(
        path = "api/addresses/{contactId}/{addressId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteAddress(
        @PathVariable("addressId") UUID addressId,
        @PathVariable("contactId") UUID contactId,
        User user
    )
    {
        addressService.deleteAddressById(addressId, contactId, user);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
        path = "api/addresses/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> getAddress(
        @PathVariable("contactId") UUID contactId,
        User user
    )
    {
        List<AddressResponse> addressResponse = addressService.listAddresses( user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(addressResponse).build();
    }
}
