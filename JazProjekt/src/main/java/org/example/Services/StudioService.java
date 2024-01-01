package org.example.Services;

import org.example.Model.Studio;
import org.example.Repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudioService {
    private final CompanyRepository companyRepository;
    @Autowired
    public StudioService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Studio> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Studio> getCompanyById(int id) {
        return companyRepository.findById(id);
    }

    public Studio SaveCompany(Studio studio) {
        return companyRepository.save(studio);
    }


    public void deleteCompany(int id) {
        companyRepository.deleteById(id);
    }
}
