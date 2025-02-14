package kz.idc.rs.services.client.wifi.requests;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.uri.UriBuilder;

import java.io.File;
import java.net.URI;

@ConfigurationProperties(WifiConfiguration.PREFIX)
@Requires(property = WifiConfiguration.PREFIX)
public class WifiConfiguration {

    public static final String PREFIX = "network";
    private static final String ROOT_PATH = "wifi";

    @Property(name = "network.host")
    private String host;

    URI getPoint(String device) {

        String path = "get_point";
        String param = "device";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .queryParam(param, device)
                .build();
    }

    URI getPoints(String device) {

        String path = "get_points";
        String param = "device";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .queryParam(param, device)
                .build();
    }

    URI getConnection() {

        String path = "get_connection";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI connect() {

        String path = "connect";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .build();
    }
}