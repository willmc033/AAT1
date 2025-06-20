package org.example.model;

public class Agent {

    private int agentId;
    private String employeeNumber;
    private String approverName;
    private String workdayId;

    private User user;
    private ManagementUnit managementUnit;

    public Agent() {
    }


    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getWorkdayId() {
        return workdayId;
    }

    public void setWorkdayId(String workdayId) {
        this.workdayId = workdayId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ManagementUnit getManagementUnit() {
        return managementUnit;
    }

    public void setManagementUnit(ManagementUnit managementUnit) {
        this.managementUnit = managementUnit;
    }

    @Override
    public String toString() {
        if (user != null) {
            return user.getFullName() + " (" + employeeNumber + ")";
        }
        return "Agent " + employeeNumber;
    }
}