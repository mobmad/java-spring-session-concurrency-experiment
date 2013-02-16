package no.mobmad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Scope("request")
@Component
@Path("/")
public class CarResource
{

    @Autowired
    CarService carService;

    @Autowired
    HttpSession session;

    @GET
    @Path("cars")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCars(@QueryParam("mode") String mode,
                            @QueryParam("responseDelay") int responseDelay,
                            @QueryParam("_") String rid)
    {
        Logger logger = new Logger(session.getId(), rid);
        List<Car> cars = carService.getCars(mode, responseDelay, logger);

        return Response.ok(cars).build();
    }

    @POST
    @Path("session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@Context HttpServletRequest request)
    {
        invalidateSession(request);
        request.getSession(true);
        return Response.ok().entity("{}").build();
    }

    @DELETE
    @Path("session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSession(@Context HttpServletRequest request)
    {
        invalidateSession(request);
        return Response.ok().entity("{}").build();
    }

    private void invalidateSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate();
        }
    }
}
