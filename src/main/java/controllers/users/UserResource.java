package controllers.users;

import repositories.users.UserRepository;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.RolesAllowed;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;

    @Inject
    UserRepository userRepository;

    @GET
    @Path("users")
    @RolesAllowed("admin")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @POST
    @Path("users")
    @Transactional
    @RolesAllowed("admin")
    public Response create(CreateUserRequest request) {
        User user = userRepository.create(request);
        return Response.ok(user).status(201).build();
    }

    @DELETE
    @Path("users")
    @Transactional
    @RolesAllowed("admin")
    public Response delete(DeleteUserRequest request) {
        User user = userRepository.delete(request);
        return Response.ok(user).status(201).build();
    }

    @GET
    @Path("users/{user_id}")
    @RolesAllowed("admin")
    public User getUser(Integer user_id) {
        User user = userRepository.getById(user_id);
        if (user == null) {
            throw new WebApplicationException("User does not exist.", 404);
        }
        return user;
    }

    @GET
    @Path("me")
    public Response getMe() {
        User user = userRepository.getByUsername(securityIdentity.getPrincipal().getName());
        if (user != null) {
            return Response.ok(user).status(200).build();
        } else {
            Integer tableNumber = Optional.ofNullable(jwt.getClaim("table_number"))
                    .map(claim -> {
                        try {
                            return Integer.parseInt(claim.toString());
                        } catch (NumberFormatException e) {
                            return 99;
                        }
                    })
                    .orElse(99);
            return Response.ok(
                    userRepository.create(
                            new CreateUserRequest(
                                    securityIdentity.getPrincipal().getName(),
                                    jwt.getClaim("given_name").toString(),
                                    jwt.getClaim("family_name").toString(),
                                    tableNumber
                            )
                    )
            ).status(201).build();
        }
    }
}