package kz.idc.rs.services.settings;

import io.reactivex.Maybe;
import kz.idc.dto.IsSettingsSetDTO;
import kz.idc.dto.settings.ApiDTO;
import kz.idc.dto.sip.SipIgnoreAccDTO;

public interface SettingsService {
    Maybe<ApiDTO> getAllApiSettings();
    Maybe<SipIgnoreAccDTO> setSipConfigWithoutAcc(SipIgnoreAccDTO sipIgnoreAccDTO);
    Maybe<IsSettingsSetDTO> isSettingsSet();
}
