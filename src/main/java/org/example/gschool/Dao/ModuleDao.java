package org.example.gschool.Dao;

import org.example.gschool.Entity.Module;

import java.util.List;

public interface ModuleDao {
    void saveModule(Module module);
    List<Module> getModulesByFiliere(int filiereId);
    List<Module> findAll();

}
