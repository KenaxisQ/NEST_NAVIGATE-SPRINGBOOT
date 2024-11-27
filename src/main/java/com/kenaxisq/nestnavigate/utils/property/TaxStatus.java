package com.kenaxisq.nestnavigate.utils.property;

public enum TaxStatus {
    UP_TO_DATE("UP-TO-DATE"),
    PENDING("PENDING");

    private String status;
    TaxStatus(String status){
        this.status = status;
    }
}
