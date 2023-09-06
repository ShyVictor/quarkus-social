package io.github.shyvictor.quarkussocial.rest;

import io.github.shyvictor.quarkussocial.domain.model.User;
import io.github.shyvictor.quarkussocial.rest.dto.CreateUserRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @POST @Transactional
    public Response createUser(CreateUserRequest userRequest){
        validateCreateUserRequest(userRequest);
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        user.persist();
        return Response.ok(user).build();
    }

    private void validateCreateUserRequest(CreateUserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getAge() == null)
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());

    }

}
