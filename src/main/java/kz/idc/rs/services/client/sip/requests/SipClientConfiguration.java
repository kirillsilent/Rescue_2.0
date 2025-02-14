package kz.idc.rs.services.client.sip.requests;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.uri.UriBuilder;

import java.io.File;
import java.net.URI;

@ConfigurationProperties(SipClientConfiguration.PREFIX)
@Requires(property = SipClientConfiguration.PREFIX)
public class SipClientConfiguration {

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

    public URI getSipStatus() {

        String path = "status";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }

    public URI updateSipClient() {

        String path = "account/update";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }

    public URI updateSipConfig() {

        String path = "config/update";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }

    public URI updateIO() {

        String path = "io/update";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }

    public URI startEmergency() {

        String path = "emergency/start";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }

    public URI stopEmergency() {

        String path = "emergency/stop";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(PREFIX)
                .path(File.separator)
                .path(path)
                .build();
    }
}
