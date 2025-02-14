package kz.idc.rs.services.client.wifi.requests;

import io.micronaut.http.HttpRequest;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.wifi.ConnectToPointDTO;
import kz.idc.dto.wifi.PointIsConnectedDTO;

public interface WifiToolsRequests {
    HttpRequest<?> getPoint(IODeviceDTO io);
    HttpRequest<?> getPoints(IODeviceDTO io);
    HttpRequest<?> getConnection(PointIsConnectedDTO pointIsConnected);
    HttpRequest<?> connect(ConnectToPointDTO connectToPoint);
}
