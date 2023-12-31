package io.github.shyvictor.quarkussocial.rest;

import io.github.shyvictor.quarkussocial.domain.model.User;
import io.github.shyvictor.quarkussocial.domain.repository.UserRepository;
import io.github.shyvictor.quarkussocial.rest.dto.CreateUserRequest;
import io.github.shyvictor.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository userRepository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        validateCreateUserRequest(userRequest);
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED.getStatusCode()).
                entity(user).build();
    }

    @GET
    public Response listAllUsers() {
        final PanacheQuery<User> panacheQuery = userRepository.findAll();
        return Response.ok(panacheQuery.list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        validateUserExistence(user);
        userRepository.delete(user);
        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest) {
        final User user = userRepository.findById(id);
        validateUserExistence(user);
        validateCreateUserRequest(createUserRequest);
        user.setAge(createUserRequest.getAge());
        user.setName(createUserRequest.getName());
        return Response.ok().build();
    }

    private void validateUserExistence(User user) {
        if (user == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
    }

    private void validateCreateUserRequest(CreateUserRequest createUserRequest) {
        final Set<ConstraintViolation<CreateUserRequest>> requestViolations = validator.validate(createUserRequest);
        if (!requestViolations.isEmpty()) {
            final ResponseError responseError = ResponseError.createFromValidation(requestViolations);
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(responseError).build());
        }
    }
}