package kz.idc.rs.client.sip.requests;

import io.micronaut.http.HttpRequest;

public interface ApiSipRequests {
    HttpRequest<?> call();
}
