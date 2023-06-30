package com.example.rentacaruserservice.controller;

import com.example.rentacaruserservice.dto.*;
import com.example.rentacaruserservice.mapper.UserMapper;
import com.example.rentacaruserservice.security.CheckSecurity;
import com.example.rentacaruserservice.service.UserServiceSpecification;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserServiceSpecification userServiceSpecification;
    private UserMapper userMapper;

    public UserController(UserServiceSpecification userServiceSpecification, UserMapper userMapper) {
        this.userServiceSpecification = userServiceSpecification;
        this.userMapper = userMapper;
    }

    @ApiOperation(value = "Get all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping("/all")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     @ApiIgnore Pageable pageable) {

        return new ResponseEntity<>(userServiceSpecification.findAll(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all clients")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping("/all/clients")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Page<ClientDto>> getAllClients(@RequestHeader("Authorization") String authorization,
                                                     @ApiIgnore Pageable pageable) {

        return new ResponseEntity<>(userServiceSpecification.findAllClients(pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Register client")
    @PostMapping("/registration/client")
    public ResponseEntity<UserDto> registerClient(@RequestBody @Valid ClientCreateDto clientCreateDto) {
        return new ResponseEntity<>(userServiceSpecification.createClient(clientCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Register manager")
    @PostMapping("/registration/manager")
    public ResponseEntity<UserDto> registerClient(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        return new ResponseEntity<>(userServiceSpecification.createManager(managerCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Client Update")
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable("id") Long id,
                                            @RequestBody @Valid ClientCreateDto clientCreateDto) {
        return new ResponseEntity<>(userServiceSpecification.updateClient(id, clientCreateDto), HttpStatus.OK);
    }
    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        TokenResponseDto token = userServiceSpecification.login(tokenRequestDto);
        if (token.getToken() != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @ApiOperation(value = "Discount")
    @GetMapping("/{id}/discount")
    public ResponseEntity<ClientStatusDto> getDiscount(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userServiceSpecification.findDiscount(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Ban user")
    @PutMapping("/{id}/ban")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<UserDto> banUser(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) {
        return new ResponseEntity<>(userServiceSpecification.banUser(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Unban user")
    @PutMapping("/{id}/unban")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<UserDto> unbanUser(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) {
        return new ResponseEntity<>(userServiceSpecification.unbanUser(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Password Update")
    @PutMapping("/{id}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable("id") Long id,
                                                  @RequestBody @Valid PasswordUserDto passwordClientDto) {
        return new ResponseEntity<>(userServiceSpecification.updatePassword(id, passwordClientDto), HttpStatus.OK);
    }
}
