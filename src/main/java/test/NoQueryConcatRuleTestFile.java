/*
package test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

class NoQueryConcatRuleTestFile {

    void checkQueries(EntityManager em, String status, String name) {
        // Compliant: Using parameters
        em.createQuery("SELECT u FROM User u WHERE u.status = :status")
                .setParameter("status", status);

        // Noncompliant@+1 [[sc=24;ec=61]] {{Use bind parameters instead of String concatenation to build this query.}}
        em.createQuery("SELECT u FROM User u WHERE u.name = '" + name + "'");

        // Noncompliant@+1
        em.createNativeQuery("SELECT * FROM users WHERE status = '" + status + "'");

        // Compliant: Simple addition (not a string)
        int sum = 1 + 2;

        // Compliant: Hardcoded string is fine
        em.createQuery("SELECT u FROM User u");
    }
}
*/
