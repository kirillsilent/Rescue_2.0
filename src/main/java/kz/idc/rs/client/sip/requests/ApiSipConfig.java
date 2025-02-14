package kz.idc.rs.client.sip.requests;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.uri.UriBuilder;

import java.io.File;
import java.net.URI;

@ConfigurationProperties(ApiSipConfig.PREFIX)
@Requires(property = ApiSipConfig.PREFIX)
public class ApiSipConfig {
    public static final String PREFIX = "siphone";

    @Property(name = "siphone.host")
    private String host;

    public URI call() {

        String path = "call";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }

}