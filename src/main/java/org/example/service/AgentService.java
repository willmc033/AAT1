package org.example.service;

import org.example.dao.AgentDao;
import org.example.model.Agent;
import java.util.List;

public class AgentService {
    private final AgentDao agentDao = new AgentDao();

    public List<Agent> findAgentsByMu(int muId) {
        return agentDao.findByMu(muId);
    }
}