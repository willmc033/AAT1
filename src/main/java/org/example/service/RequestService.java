package org.example.service;

import org.example.dao.RequestDao;
import org.example.model.Agent;
import org.example.model.Request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class RequestService {


    private final RequestDao requestDao = new RequestDao();


    public List<Request> findRequests(Request.Status status, LocalDate startDate, String agentSearchText, String lineOfWork) {
        try {
            return requestDao.findByFilters(status, startDate, agentSearchText, lineOfWork);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public void createNewRequest(Agent agent, LocalDate date, Request.RequestType type) {

        Request newRequest = new Request();
        newRequest.setRequestId("REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        newRequest.setAgent(agent);
        newRequest.setRequestDate(date);
        newRequest.setRequestType(type);
        newRequest.setStatus(Request.Status.submitted);

        newRequest.setSubmissionTimestamp(LocalDateTime.now());
        requestDao.save(newRequest);

        System.out.println("Lógica de servicio: Creando nueva solicitud para la fecha " + date);
    }

    public void approveRequests(List<String> requestIds) {
        System.out.println("Lógica de servicio: Aprobando " + requestIds.size() + " solicitudes.");

    }
    public void cancelRequests(List<String> requestIds) {
        if (requestIds == null || requestIds.isEmpty()) {
            return;
        }

        for (String id : requestIds) {
            Request request = requestDao.findById(id);
            if (request != null) {
                if (request.getStatus() == Request.Status.submitted) {
                    requestDao.updateStatus(id, Request.Status.cancelled);
                } else {
                    requestDao.updateStatus(id, Request.Status.cancel_review);
                }
            }
        }
    }
}