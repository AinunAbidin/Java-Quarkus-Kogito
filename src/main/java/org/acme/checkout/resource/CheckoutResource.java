package org.acme.checkout.resource;

import java.util.Map;

import org.acme.checkout.CheckoutData;
import org.kie.kogito.Model;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/checkout")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CheckoutResource {

    @Inject
    @Named("simple_checkout")
    Process<? extends Model> simpleCheckout;

    @POST
    public Response checkout(CheckoutData data) {
        try {
            Model model = simpleCheckout.createModel();
            model.fromMap(Map.of("data", data));
            ProcessInstance<? extends Model> instance = simpleCheckout.createInstance(model);
            instance.start();
            CheckoutData result = (CheckoutData) instance.variables().toMap().get("data");
            return Response.ok(result).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        }
    }
}
