package io.github.shyvictor.quarkussocial.rest;

import io.github.shyvictor.quarkussocial.domain.model.User;
import io.github.shyvictor.quarkussocial.domain.repository.UserRepository;
import io.github.shyvictor.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;
    public UserResource(UserRepository repository){

    }
    @POST @Transactional
    public Response createUser(CreateUserRequest userRequest){
        validateCreateUserRequest(userRequest);
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        userRepository.persist(user);
        return Response.ok(user).build();
    }
    @GET
    public Response listAllUsers(){
        final PanacheQuery<User> panacheQuery = userRepository.findAll();
        return Response.ok(panacheQuery.list()).build();
    }

    @DELETE @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id){
        User user = userRepository.findById(id);
        validateUserExistence(user);
        userRepository.delete(user);
        return Response.ok().build();
    }

    @PUT @Path("{id}") @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest) {
        final User user = userRepository.findById(id);
        validateUserExistence(user);
        validateCreateUserRequest(createUserRequest);
        user.setAge(createUserRequest.getAge());
        user.setName(createUserRequest.getName());
        return Response.ok().build();
    }

    private void validateUserExistence(User user){
        if (user == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
    }
    private void validateCreateUserRequest(CreateUserRequest createUserRequest) {
        if (createUserRequest.getName() == null || createUserRequest.getAge() == null)
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());

    }


}
