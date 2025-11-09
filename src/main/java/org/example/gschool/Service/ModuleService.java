package org.example.gschool.Service;

import org.example.gschool.Entity.Module;

import java.util.List;

public interface ModuleService {
    void saveModule(Module module);
    List<Module> getModulesByFiliere(int filiereId);
    List<Module> getAllModules();

}
