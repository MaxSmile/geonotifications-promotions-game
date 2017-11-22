package com.spindealsapp.database.service;

import com.spindealsapp.database.repository.CompanySqlRepository;
import com.spindealsapp.database.repository.specification.CompanyByIdSqlSpecification;
import com.spindealsapp.database.repository.specification.CompanySqlSpecification;
import com.spindealsapp.entity.Company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Volodymyr Kusenko on 22.11.2017.
 */

public class CompanyServiceLayer {

    private static CompanySqlRepository repository = new CompanySqlRepository();

    public static void add(Company company) {
        repository.add(company);
    }

    public static void add(Iterable<Company> companies) {
        repository.add(companies);
    }

    public static Company getCompany(String id) {
        List<Company> companyList = repository.query(new CompanyByIdSqlSpecification(id));
        return companyList.size() > 0 ? companyList.get(0) : new Company();
    }

    public static Map<String, Company> getCompanies() {
        List<Company> companyList = repository.query(new CompanySqlSpecification());
        Map<String, Company> companies = new HashMap<String, Company>();
        for (int i = 0; i < companyList.size(); i++) {
            companies.put(companyList.get(i).getId(), companyList.get(i));
        }

        return companies;
    }
}
