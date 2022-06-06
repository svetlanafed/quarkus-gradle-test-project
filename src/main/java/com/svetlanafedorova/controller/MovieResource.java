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

@Path("/movies")
public class MovieResource {

    public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie newMovie) {
        movies.add(newMovie);
        return Response.ok(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(
            @PathParam("id") Long id,
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
    public Response deleteMovie(
            @PathParam("id") Long id) {
        Optional<Movie> movieToDelete = movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();

        boolean deleted = movies.remove(movieToDelete.orElseThrow(() -> new NotFoundException("Movie not found")));

        return deleted ? Response.noContent().build() : Response.status(Status.BAD_REQUEST).build();
    }
}
