package kz.idc.utils.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kz.idc.dto.IncidentDTO;
import kz.idc.dto.settings.SettingsDTO;

import java.io.File;
import java.io.IOException;

public class MapperImpl implements Mapper {

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    public MapperImpl() {
        mObjectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Включение форматирования JSON по умолчанию
    }

    @Override
    public void writeJsonFile(File file, Object o) {
        try {
            mObjectMapper.writeValue(file, o);
        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    @Override
    public IncidentDTO readFileIncident(File file) {
        try {
            return mObjectMapper.readValue(file, IncidentDTO.class);
        } catch (IOException e) {
            System.out.println("Error reading IncidentDTO JSON: " + e.getMessage());
        }
        return null;
    }

    @Override
    public SettingsDTO readFileSettings(File file) {
        try {
            return mObjectMapper.readValue(file, SettingsDTO.class);
        } catch (IOException e) {
            System.out.println("Error reading SettingsDTO JSON: " + e.getMessage());
        }
        return null;
    }
}
