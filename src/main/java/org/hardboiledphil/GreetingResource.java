package org.hardboiledphil;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * curl -X POST -H "Content-Type: application/json" -d '{"bench":"bench2","chain":"chain2","chainNumber":2}' http://localhost:8080/hello/data/add
 */
@Slf4j
@Path("/hello")
public class GreetingResource {

    final static String WAITING_NAME = "data_monitor_in_waiting";
    final static String PROCESSING_NAME = "data_monitor_in_processing";

    @Inject
    DataWrapper dataWrapper;

    @GET
    @Path("/metrics")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getPrometheusMetrics() {

        // this.environment = environment; // could be used to metric by env
        // this.eventDate = Date.from(eventTimestamp);
        // this.eventTimestamp = eventTimestamp;
        // this.eventName = eventName; // status
        // this.eventReference = eventReference;
        // this.transactionType = transactionType; // type
        // this.transactionReference = transactionReference;
        // this.ilMessageId = ilMessageId;
        // this.chainPrefix = chainPrefix; // chain prefix
        // this.chainNumber = chainNumber; // chain number
        // this.messageVersion = messageVersion;
        log.info("What thread is this running on?");

        //
        val sb = new StringBuilder();
        if (dataWrapper.getDataThingMap().isEmpty()) {
            return Uni.createFrom().item(() -> "");
        };

        sb.append("HELP    \n")
                .append("TYPE    \n");

        val counts = dataWrapper.getDataThingMap()
                .stream()
                .map(dataThing -> dataThing.getBench()
                        + ","
                        + dataThing.getChain()
                        + ","
                        + dataThing.getChainNumber()
                        + ",")
                .collect(Collectors.groupingBy(
                        dataThingString -> dataThingString,
                        Collectors.counting()));

        counts.forEach((benchCombination, combinationCount) ->
                sb.append(WAITING_NAME)
                        .append("{")
                        .append(benchCombination)
                        .append("} ")
                        .append(combinationCount)
                        .append("\n"));

//                .append("{environment=\"{")
//                .append("}\",status=\"{")
//                .append("}\",type=\"{")
//                .append("}\",chainprefix=\"{")
//                .append("}\",chainnumber=\"{")
//                .append("}\",} ")
//                .append(count);
//        val sc = new StringBuilder("got this far");
        return Uni.createFrom().item(sb.toString());
    }

    @POST
    @Path("/data/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addData(final DataThing dataThing) {
        log.info("Adding item -> {}", dataThing);
        dataWrapper.addEntry(dataThing);
        return Response.ok().build();
    }

    @GET
    @Path("/data/remove/{bench}")
    public Response removeData(@PathParam("bench") final String benchName) {
        log.info("removeData called for {}", benchName);
        val dataThingMap = dataWrapper.getDataThingMap();
        val exists = dataThingMap.stream()
                .filter(dataThing -> dataThing.getBench().equals(benchName))
                .findFirst()
                .or(Optional::empty);
        if (exists.isPresent()) {
            log.info("Found {}", exists);
            exists.ifPresent(unwrapDataThing -> dataWrapper.removeEntry(unwrapDataThing));
            log.info("Request made to remove {} - successful", benchName);
            return Response.ok().build();
        } else {
            log.info("Request made to remove {} failed - doesn't exist", benchName);
            return Response.notModified().build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return Uni.createFrom().item("Hello from RESTEasy Reactive");
    }


}
