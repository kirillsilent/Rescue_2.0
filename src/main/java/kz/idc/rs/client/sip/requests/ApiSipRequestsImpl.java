package kz.idc.rs.client.sip.requests;

import io.micronaut.http.HttpRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiSipRequestsImpl implements ApiSipRequests {

    private final ApiSipConfig apiSipConfig;

    @Override
    public HttpRequest<?> call() {
        return HttpRequest.GET(apiSipConfig.call());
    }
}
