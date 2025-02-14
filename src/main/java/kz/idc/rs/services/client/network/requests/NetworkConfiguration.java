package kz.idc.rs.services.client.network.requests;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.uri.UriBuilder;

import java.io.File;
import java.net.URI;

@ConfigurationProperties(NetworkConfiguration.PREFIX)
@Requires(property = NetworkConfiguration.PREFIX)
public class NetworkConfiguration {
    public static final String PREFIX = "network";
    private static final String ROOT_PATH = "network";

    @Property(name = "network.host")
    private String host;

    URI getAllNetworkInterfaces(String networkType) {

        String path = "get_interfaces";
        String param = "type";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .queryParam(param, networkType)
                .build();
    }

    URI setDefaultNetworkInterface() {

        String path = "set_interface";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI setNetworkConfig() {

        String path = "set_config";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI getDefaultNetworkInterface() {

        String path = "get_current_interface";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI isDefaultRoute(String device) {

        String path = "is_default_route";
        String param = "device";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .queryParam(param, device)
                .path(path)
                .build();
    }

    URI addDefaultRoute() {

        String path = "add_default_route";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI getIpPath(String device) {

        String path = "get_ip";
        String param = "device";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .queryParam(param, device)
                .build();
    }

    URI getVPNIpPath(String device) {

        String path = "get_vpn_ip";
        String param = "device";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH)
                .path(File.separator)
                .path(path)
                .queryParam(param, device)
                .build();
    }
}