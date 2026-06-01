package com.account.transfer.application.dto;

import java.time.LocalDateTime;

public class TransactionSearchRequest {
    private String clientId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }   
}
