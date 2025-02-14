package kz.idc.rs.services.client.network.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.network.NetworkConfigDTO;

public interface NetworkToolsRequests {
    HttpRequest<?> getIpFromInterface(IODeviceDTO ioDeviceDTO);
    HttpRequest<?> getIpFromVPNInterface(IODeviceDTO ioDeviceDTO);
    HttpRequest<?> getNetworkInterfaces(String type);
    HttpRequest<?> setNetworkConfig(NetworkConfigDTO networkConfig);
    HttpRequest<?> setNetworkInterface(IODeviceDTO io);
    HttpRequest<?> getCurrentNetworkInterface(IODeviceDTO io);
    HttpRequest<?> isDefaultRoute(IODeviceDTO io);
    HttpRequest<?> addDefaultRoute(IODeviceDTO io);
}
