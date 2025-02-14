package kz.idc.rs.services.client.network;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.*;
import kz.idc.dto.StatusDTO;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.io.IOType;
import kz.idc.dto.network.*;
import kz.idc.error.$Error;
import kz.idc.rs.services.client.network.requests.$NetworkToolsRequests;
import kz.idc.rs.services.client.network.requests.NetworkConfiguration;
import kz.idc.rs.services.client.network.requests.NetworkToolsRequests;
import kz.idc.utils.storage.$Storage;
import kz.idc.utils.storage.Storage;


public class NetworkToolsImpl implements NetworkTools {

    private final RxHttpClient httpClient;
    private final NetworkToolsRequests networkToolsRequests;
    private final Storage storage = $Storage.mk();

    public NetworkToolsImpl(RxHttpClient httpClient,
                            NetworkConfiguration configuration) {
        this.httpClient = httpClient;
        networkToolsRequests = $NetworkToolsRequests.mk(configuration);
    }

    @Override
    public Maybe<Object> getNetworkInterfaces(String type) {
        return httpClient.retrieve(networkToolsRequests.getNetworkInterfaces(type), NetworkDevicesDTO.class)
                .firstElement()
                .flatMap(io -> result -> result.onSuccess(io))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> setNetworkConfig(NetworkConfigDTO networkConfigDTO) {
        Flowable<?> flowable = httpClient.exchange(networkToolsRequests.setNetworkConfig(networkConfigDTO), HttpResponse.class);
        return httpClient.exchange(networkToolsRequests.setNetworkConfig(networkConfigDTO), HttpResponse.class)
                .firstElement()
                .retryWhen(retryHandler -> retryHandler.flatMap(nothing -> flowable))
                .flatMap(resp -> result -> result.onSuccess(resp))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> setNetworkInterface(IODeviceDTO io) {
        return httpClient.retrieve(networkToolsRequests.setNetworkInterface(io), NetworkConfigDTO.class)
                .firstElement()
                .flatMap(network -> storage.setIO(io).toMaybe()
                        .flatMap(iface -> result -> result.onSuccess(network)))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> getCurrentNetworkInterface() {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(networkToolsRequests.getCurrentNetworkInterface(io), NetworkConfigDTO.class)
                        .firstElement()
                        .flatMap(networkConfig -> result -> result.onSuccess(networkConfig)))
                .onErrorResumeNext(t -> result -> result.onSuccess($Error.mk().createErrorNetworkModule(t.getMessage())));
    }

    @Override
    public Maybe<Object> getStatusNetworkInterface() {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(networkToolsRequests.getCurrentNetworkInterface(io), NetworkConfigDTO.class)
                        .firstElement()
                        .flatMap(networkConfig -> result -> {
                            if (networkConfig.getIp() != null) {
                                result.onSuccess(StatusDTO.create(true));
                            } else {
                                result.onSuccess(StatusDTO.create(false));
                            }
                        }))
                .onErrorResumeNext(t -> result -> result.onSuccess(StatusDTO.create(false)));
    }

    @Override
    public Maybe<Object> isDefaultRoute() {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.retrieve(networkToolsRequests.isDefaultRoute(io), NetworkRouteDTO.class)
                        .firstElement()
                        .retryWhen(retryHandler -> retryHandler
                                .flatMap(nothing -> httpClient.retrieve(networkToolsRequests.isDefaultRoute(io), NetworkRouteDTO.class)))
                        .flatMap(networkRoute -> result -> result.onSuccess(networkRoute)));

    }

    @Override
    public Maybe<Object> addDefaultRoute() {
        return storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> httpClient.exchange(networkToolsRequests.addDefaultRoute(io), HttpResponse.class)
                        .firstElement()
                        .retryWhen(retry -> retry.flatMap(nothing -> {
                            System.out.println("Error add route");
                            return httpClient.retrieve(networkToolsRequests.addDefaultRoute(io));
                        })));
    }

    @Override
    public Maybe<Object> getIpFromInterface(boolean isVPNEnabled) {
        Maybe<Object> maybe =  storage.getIO(IOType.NETWORK.DEVICE).toMaybe()
                .flatMap(io -> get -> {
                    if (io.getDevice() == null) {
                        get.onError(new Throwable());
                    } else {
                        get.onSuccess(io);
                    }
                });
        if(!isVPNEnabled){
        return maybe.flatMap(io -> httpClient.retrieve(networkToolsRequests.getIpFromInterface((IODeviceDTO) io), NetworkIPMacDTO.class)
                        .firstElement());
        }else {
            return maybe.flatMap(io -> httpClient.retrieve(networkToolsRequests.getIpFromVPNInterface((IODeviceDTO) io), NetworkIPMacDTO.class)
                    .firstElement());
        }
    }

    @Override
    public Maybe<VPNEnabledDTO> setWorkOnVPN(VPNEnabledDTO vpnEnabledDTO) {
        return storage.setEnabledVPN(vpnEnabledDTO).toMaybe();
    }

    @Override
    public Maybe<VPNEnabledDTO> getStateWorkOnVPN() {
        return storage.getEnabledVPN().toMaybe();
    }
}
