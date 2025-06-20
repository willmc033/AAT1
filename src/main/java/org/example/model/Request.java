package org.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Request {

    private String requestId;
    private Agent agent;
    private RequestType requestType;
    private LocalDate requestDate;
    private Status status;
    private LocalDateTime submissionTimestamp;
    private User updatedByUser;
    private LocalDateTime lastUpdateTimestamp;
    private String escalatedApproverName;

    public enum RequestType {
        vacation, PDO, BDL
    }

    public enum Status {
        submitted, approved, cancelled, cancel_review, completed, not_in_wd
    }

    public Request() {
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getSubmissionTimestamp() {
        return submissionTimestamp;
    }

    public void setSubmissionTimestamp(LocalDateTime submissionTimestamp) {
        this.submissionTimestamp = submissionTimestamp;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getEscalatedApproverName() {
        return escalatedApproverName;
    }

    public void setEscalatedApproverName(String escalatedApproverName) {
        this.escalatedApproverName = escalatedApproverName;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId='" + requestId + '\'' +
                ", agent=" + (agent != null ? agent.getUser().getFullName() : "null") +
                ", requestDate=" + requestDate +
                ", status=" + status +
                '}';
    }
}