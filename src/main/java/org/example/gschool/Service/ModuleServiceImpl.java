package org.example.gschool.Service;

import org.example.gschool.Dao.ModuleDao;
import org.example.gschool.Entity.Module;
import org.example.gschool.Entity.Professeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    @Transactional
    public void saveModule(Module module) {
        moduleDao.saveModule(module);
    }

    @Override
    public List<Module> getAllModules() {
        return moduleDao.findAll();
    }


    @Override
    @Transactional
    public List<Module> getModulesByFiliere(int filiereId) {
        return moduleDao.getModulesByFiliere(filiereId);
    }
}
