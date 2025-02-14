package kz.idc.rs.services.client.ac.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.audio.PlayDTO;
import kz.idc.dto.io.IODeviceDTO;


public interface ACToolsRequests {
    HttpRequest<?> getAudioCards(String type);
    HttpRequest<?> getCameras();
    HttpRequest<?> setVolume(int volume, IODeviceDTO ioDeviceDTO);
    HttpRequest<?> getVolume(IODeviceDTO ioDeviceDTO);
    HttpRequest<?> audioIsAvailable(IODeviceDTO io);
    HttpRequest<?> cameraIsAvailable(IODeviceDTO io);
    HttpRequest<?> play(PlayDTO playDTO);
    HttpRequest<?> stop();
}
