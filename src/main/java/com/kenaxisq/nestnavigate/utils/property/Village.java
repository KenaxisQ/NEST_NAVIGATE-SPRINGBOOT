package com.kenaxisq.nestnavigate.utils.property;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Village {

    private final String villageCode;
    private final int mandalWiseSlNo;
    private final String villageName;

public String toJson(String mandalName) {
    ObjectMapper mapper = new ObjectMapper();
    try {
        return mapper.writeValueAsString(new VillageDetails(mandalName, "Srikakulam",villageName));
    } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "{}";
    }
}

private static class VillageDetails {
    private String mandal;
    private String revenueDivision;
    private String village;

    public VillageDetails(String mandal, String revenueDivision, String village) {
        this.mandal = mandal;
        this.revenueDivision = revenueDivision;
        this.village = village;
    }

    // Getters
    public String getMandal() {
        return mandal;
    }

    public String getRevenueDivision() {
        return revenueDivision;
    }

    public String getVillage() {
        return village;
    }
}

}