package org.example.service;

import org.example.dao.ManagementUnitDao;
import org.example.model.ManagementUnit;
import java.util.List;

public class ManagementUnitService {
    private final ManagementUnitDao muDao = new ManagementUnitDao();

    public List<ManagementUnit> findAll() {
        return muDao.findAll();
    }
}