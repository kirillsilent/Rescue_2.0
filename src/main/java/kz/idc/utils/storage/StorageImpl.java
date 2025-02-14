package kz.idc.utils.storage;

import io.reactivex.Single;
import kz.idc.dto.IsSettingsSetDTO;
import kz.idc.dto.io.IODeviceDTO;
import kz.idc.dto.network.VPNEnabledDTO;
import kz.idc.dto.settings.SettingsDTO;
import kz.idc.dto.rescue.RescueDTO;
import kz.idc.dto.ServerAddressDTO;
import kz.idc.dto.sip.SipDTO;
import kz.idc.dto.sip.SipIgnoreAccDTO;
import kz.idc.dto.sip.acc.AccWithRegServerDTO;
import kz.idc.dto.sip.acc.AccountDTO;
import kz.idc.utils.file.$FileUtils;
import kz.idc.utils.file.FileUtils;
import kz.idc.utils.mapper.$Mapper;
import kz.idc.utils.mapper.Mapper;
import kz.idc.utils.settings.$Settings;
import kz.idc.dto.IncidentDTO;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class StorageImpl implements Storage {

    private final FileUtils fileUtils = $FileUtils.mk();
    private final Mapper mapper = $Mapper.mk();

    @Override
    public void createStorage() {
        // Создание папки "storage", если её нет
        if (!fileUtils.isStorageExists()) {
            System.out.println("Create storage");
            File file = fileUtils.createFileSettings();
            if (file != null) {
                mapper.writeJsonFile(file, $Settings.mk().initDefault());
            }
        } else {
            System.out.println("Storage exists");
            mapper.readFileSettings(fileUtils.getFileSettings());
        }

        // Создание папки "incident", если её нет
        File incidentFolder = new File("incident");
        if (!incidentFolder.exists()) {
            boolean created = incidentFolder.mkdir();
            if (created) {
                System.out.println("Incident folder created");
            } else {
                System.err.println("Failed to create incident folder");
            }
        }
    }

    @Override
    public void delete() {
        File settings = fileUtils.getFileSettings();
        if (settings.exists()) {
            if (settings.delete()) {
                System.out.println("File delete " + settings.getName());
            }
        }
    }

    @Override
    public Single<SettingsDTO> getSettings() {
        return Single.create(get ->
                get.onSuccess(mapper.readFileSettings(fileUtils.getFileSettings())));
    }

    @Override
    public Single<SettingsDTO> setSettings(SettingsDTO settings) {
        return Single.create(write -> {
            mapper.writeJsonFile(fileUtils.getFileSettings(), settings);
            write.onSuccess(settings);
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public Single<IODeviceDTO> setIO(IODeviceDTO io) {
        return getSettings().flatMap(settings -> update -> {
            List<IODeviceDTO> ioList = settings.getIo();
            Collections.replaceAll(ioList, findIO(ioList, io.getType()), io);
            settings.setIo(ioList);
            update.onSuccess(settings);
        }).flatMap(settings -> setSettings((SettingsDTO) settings))
                .flatMap(settings -> result -> result.onSuccess(findIO(settings.getIo(), io.getType())));
    }

    @Override
    public Single<IODeviceDTO> getIO(String type) {
        return getSettings().flatMap(settings -> get -> get.onSuccess(findIO(settings.getIo(), type)));
    }

    @Override
    public Single<RescueDTO> setRescueId(long rescueId) {
        return getSettings().flatMap(settingsDTO -> update -> {
            settingsDTO.setRescue(RescueDTO.create(rescueId));
            update.onSuccess(settingsDTO); })
                .flatMap(settings -> setSettings((SettingsDTO) settings))
                .flatMap(settings -> result -> result.onSuccess(settings.getRescue()));
    }

    @Override
    public Single<RescueDTO> getRescueId() {
        return getSettings().flatMap(settings -> get -> get.onSuccess(settings.getRescue()));
    }

    @Override
    public boolean isSettingsSetBool() {
        SettingsDTO settingsDTO = mapper.readFileSettings(fileUtils.getFileSettings());
        return isSettingsReady(settingsDTO);
    }

    @Override
    public Single<IsSettingsSetDTO> isSettingsSet(SettingsDTO settingsDTO) {
        return Single.create(isSet -> isSet.onSuccess(IsSettingsSetDTO.create(isSettingsReady(settingsDTO))));
    }

    private boolean isSettingsReady(SettingsDTO settingsDTO){
        if(settingsDTO.getRescue() == null){
            return false;
        }
        if(isStringEmpty(settingsDTO.getCentralServer().getHostname())
                || settingsDTO.getCentralServer().getPort() == 0){
            return false;
        }
        if(isStringEmpty(settingsDTO.getSip().getHostname())
                || settingsDTO.getSip().getPort() == 0
                || isStringEmpty(settingsDTO.getSip().getAccount().getAccount())
                || isStringEmpty(settingsDTO.getSip().getAccount().getPassword())
                || isStringEmpty(settingsDTO.getSip().getSipRegServer().getHostname())
                || settingsDTO.getSip().getSipRegServer().getPort() == 0
                || isStringEmpty(settingsDTO.getSip().getOperator())){
            return false;
        }
        List<IODeviceDTO> ioDevices = settingsDTO.getIo();
        for (IODeviceDTO io:
                ioDevices) {
            if(isStringEmpty(io.getDevice())){
                return false;
            }
        }
        return true;
    }

    private boolean isStringEmpty(String s){
        if(s == null){
            return true;
        }else return s.isBlank();
    }

    @Override
    public Single<AccountDTO> getSipAccount() {
        return getSip().flatMap(sip -> get -> get.onSuccess(sip.getAccount()));
    }

    @Override
    public Single<String> getServerAddress() {
        return getSettings().flatMap(settings -> get -> {
                    String address = settings.getCentralServer().getHostname() +
                            ":" +
                            settings.getCentralServer().getPort();
                    get.onSuccess(address);
                });
    }

    @Override
    public Single<ServerAddressDTO> setCentralServer(ServerAddressDTO centralServer) {
        return getSettings().flatMap(settings -> update -> {
            settings.setCentralServer(centralServer);
            update.onSuccess(settings);
        })
                .flatMap(settings -> setSettings((SettingsDTO) settings))
                .flatMap(settings -> result -> result.onSuccess(settings.getCentralServer()));
    }

    @Override
    public Single<SipIgnoreAccDTO> setSipConfig(SipIgnoreAccDTO sipIgnoreAccDTO) {
        return getSettings()
                .flatMap(settings -> update -> {
                    SipDTO sipConfig = settings.getSip();
                    sipConfig.setHostname(sipIgnoreAccDTO.getHostname());
                    sipConfig.setPort(sipIgnoreAccDTO.getPort());
                    sipConfig.setOperator(sipIgnoreAccDTO.getOperator());
                    settings.setSip(sipConfig);
                    update.onSuccess(settings);
                })
                .flatMap(settingsDTO -> setSettings((SettingsDTO) settingsDTO))
                .flatMap(settingsDTO -> result -> result.onSuccess(sipIgnoreAccDTO));
    }

    @Override
    public Single<SipDTO> getSip() {
        return getSettings().flatMap(settings ->
                get -> get.onSuccess(settings.getSip()));
    }

    @Override
    public Single<AccWithRegServerDTO> getSipAccountWithRegServer() {
        return getSettings().flatMap(settings -> create ->
                create.onSuccess(AccWithRegServerDTO.create(settings.getSip().getSipRegServer(),
                        settings.getSip().getAccount())));
    }


    @Override
    public Single<AccountDTO> setSipAccount(AccountDTO sipAccount) {
        return getSettings()
                .flatMap(settings -> update -> {
                    settings.getSip().getAccount().setAccount(sipAccount.getAccount());
                    settings.getSip().getAccount().setPassword(sipAccount.getPassword());
                    update.onSuccess(settings);
                })
                .flatMap(settings -> setSettings((SettingsDTO) settings))
                .flatMap(settings -> result -> result.onSuccess(sipAccount));
    }

    @Override
    public Single<AccWithRegServerDTO> setSipAccountWithRegServer(AccWithRegServerDTO accWithRegServerDTO) {
        return getSettings()
                .flatMap(settings -> update -> {
                    settings.getSip().setAccount(accWithRegServerDTO.getAccount());
                    settings.getSip().setSipRegServer(accWithRegServerDTO.getRegServer());
                    update.onSuccess(settings);
                })
                .flatMap(settings -> setSettings((SettingsDTO) settings))
                .flatMap(settings -> result -> result.onSuccess(accWithRegServerDTO));
    }

    @Override
    public Single<VPNEnabledDTO> setEnabledVPN(VPNEnabledDTO vpnEnabledDTO) {
        return getSettings()
                .flatMap(settings -> update -> {
                    settings.setVpnNetworkEnabled(vpnEnabledDTO.isVpnNetworkEnabled());
                    update.onSuccess(settings);
                })
                .flatMap(settings -> setSettings((SettingsDTO) settings))
                .flatMap(settings -> result -> result.onSuccess(vpnEnabledDTO));
    }

    @Override
    public Single<VPNEnabledDTO> getEnabledVPN() {
        return getSettings()
                .flatMap(settings -> get -> {
                    get.onSuccess(VPNEnabledDTO.create(settings.isVpnNetworkEnabled()));
                });
    }

    private IODeviceDTO findIO(List<IODeviceDTO> ioDevices, String type) {
        return ioDevices.stream()
                .filter(oldIO -> type.equals(oldIO.getType()))
                .findAny()
                .orElse(new IODeviceDTO());
    }

    @Override
    public Single<IncidentDTO> getIncident() {
        return Single.create(singleEmitter -> {
            File incidentFile = fileUtils.getFileIncident();
            // Проверяем, существует ли папка "incident"
            File incidentFolder = incidentFile.getParentFile();
            if (!incidentFolder.exists()) {
                boolean created = incidentFolder.mkdir();
                if (created) {
                    System.out.println("Incident folder created");
                } else {
                    System.err.println("Failed to create incident folder");
                }
            }
            singleEmitter.onSuccess(mapper.readFileIncident(incidentFile));
        });
    }

    @Override
    public Single<IncidentDTO> setIncident(IncidentDTO incident) {
        return Single.create(singleEmitter -> {
            File incidentFile = fileUtils.getFileIncident();
            mapper.writeJsonFile(incidentFile, incident);
            singleEmitter.onSuccess(incident);
        });
    }
}
