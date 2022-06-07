package com.svetlanafedorova.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.svetlanafedorova.domain.Movie;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/movies")
@Tag(name = "MovieResource", description = "Movie REST APIs")
public class MovieResource {

    public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "getMovies",
            description = "Get all movies inside the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            summary = "countMovies",
            description = "Count amount of movies inside the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "createNewMovie",
            description = "Add new movie to the list"
    )
    @APIResponse(
            responseCode = "201",
            description = "Movie created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response createMovie(
            @RequestBody(
                    description = "Movie to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class))
            )
                    Movie newMovie) {
        movies.add(newMovie);
        return Response.status(Status.CREATED).entity(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "updateMovie",
            description = "Update the movie in the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Movie updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateMovie(
            @Parameter(
                    description = "Movie id",
                    required = true
            )
            @PathParam("id") Long id,
            @Parameter(
                    description = "Movie title",
                    required = true
            )
            @PathParam("title") String title) {
        movies = movies.stream().map(
                movie -> {
                    if (movie.getId().equals(id)) {
                        movie.setTitle(title);
                    }
                    return movie;
                }).collect(Collectors.toList());
        return Response.ok(movies).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "deleteMovie",
            description = "Delete the movie from the list"
    )
    @APIResponse(
            responseCode = "204",
            description = "Movie deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Movie not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteMovie(
            @PathParam("id") Long id) {
        Optional<Movie> movieToDelete = movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();

        boolean deleted = movies.remove(movieToDelete.orElseThrow(() -> new NotFoundException("Movie not found")));

        return deleted ? Response.noContent().build() : Response.status(Status.BAD_REQUEST).build();
    }
}
