package kz.idc.rs.services.client.ac.requests;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.uri.UriBuilder;


import java.io.File;
import java.net.URI;

@ConfigurationProperties(ACConfiguration.PREFIX)
@Requires(property = ACConfiguration.PREFIX)
public class ACConfiguration {
    public static final String PREFIX = "ac-tools";
    private static final String ROOT_PATH_AUDIO = "audio";
    private static final String ROOT_PATH_CAMERA = "camera";

    @Property(name = "ac-tools.host")
    private String host;

    URI getAudioCards(String type) {

        String path = "get_devices";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_AUDIO)
                .path(File.separator)
                .path(path)
                .queryParam("type", type)
                .build();
    }

    URI getCameraCards() {

        String path = "get_devices";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_CAMERA)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI getVolume() {

        String path = "get_volume";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_AUDIO)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI setVolume(int volume) {

        String path = "set_volume";
        String volumeParam = "volume";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_AUDIO)
                .path(File.separator)
                .path(path)
                .queryParam(volumeParam, volume)
                .build();
    }

    URI isAvailableAudio() {

        String path = "is_available";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_AUDIO)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI isAvailableCamera() {

        String path = "is_available";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_CAMERA)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI play() {

        String path = "play";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_AUDIO)
                .path(File.separator)
                .path(path)
                .build();
    }

    URI stop(){
        String path = "stop";

        return UriBuilder.of(host)
                .path(File.separator)
                .path(ROOT_PATH_AUDIO)
                .path(File.separator)
                .path(path)
                .build();
    }
}