/*
package test;

import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import java.util.List;

class Company {

    // Noncompliant@+1 {{Collections should use FetchType.LAZY to avoid N+1 performance issues.}}
    @OneToMany(fetch = FetchType.EAGER)
    private List<Employee> employees;

    // Noncompliant@+1 {{Collections should use FetchType.LAZY to avoid N+1 performance issues.}}
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Project> projects;

    // Compliant: Default fetch for OneToMany is LAZY
    @OneToMany
    private List<Department> departments;

    // Compliant: Explicit LAZY is the best practice
    @OneToMany(fetch = FetchType.LAZY)
    private List<Asset> assets;

    // Compliant: ManyToOne defaults to EAGER and is usually acceptable for single objects
    @ManyToOne(fetch = FetchType.EAGER)
    private Company parentCompany;
}

class Employee {}
class Project {}
class Department {}
class Asset {}
*/
