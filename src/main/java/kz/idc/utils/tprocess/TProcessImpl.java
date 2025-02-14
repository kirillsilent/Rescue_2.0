package kz.idc.utils.tprocess;

import kz.idc.rs.services.client.sip.SipStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TProcessImpl implements TProcess {
    @Override
    public String restartSipService() {
        try {
            Process p = startProcess(new String[]{"systemctl", "is-active", "sowa_sip.service"});
            String error = readInputStream(p.getErrorStream());
            String term = readInputStream(p.getInputStream());
            if ((error) != null) {
                throw new RuntimeException(error);
            } else {
                if(term != null && term.contains(SipStatus.SIP_FAILED.STATUS)){
                    throw new RuntimeException(term);
                }
                startProcess(new String[]{"sudo", "systemctl", "restart", "sowa_sip.service"});
            }
            return SipStatus.SIP_RESTARTED.STATUS;
        } catch (Exception ex) {
            return SipStatus.SIP_RESTART_ERROR.STATUS;
        }
    }

    @Override
    public String stop() {
        try {
            Process p = startProcess(new String[]{"systemctl", "is-active", "sowa_sip.service"});
            String error = readInputStream(p.getErrorStream());
            String term = readInputStream(p.getInputStream());
            if ((error) != null) {
                throw new RuntimeException(error);
            } else {
                if(term != null && term.contains(SipStatus.SIP_FAILED.STATUS)){
                    throw new RuntimeException(term);
                }
                startProcess(new String[]{"sudo", "systemctl", "stop", "sowa_sip.service"});
            }
            return SipStatus.SIP_STOP.STATUS;
        } catch (Exception ex) {
            return SipStatus.SIP_STOP_ERROR.STATUS;
        }
    }

    private Process startProcess(String[] args) {
        try {
            return new ProcessBuilder(args).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        if ((line = reader.readLine()) != null) {
            return line;
        }
        return null;
    }
}
