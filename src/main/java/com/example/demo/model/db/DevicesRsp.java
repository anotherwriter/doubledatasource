package com.example.demo.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class DevicesRsp {
    private List<DeviceInfo> guids;

    @Data
    @AllArgsConstructor
    public static class DeviceInfo {

        private String desc;

        private int state;
    }
}
