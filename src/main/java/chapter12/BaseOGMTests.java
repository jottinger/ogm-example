package chapter12;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class BaseOGMTests {
    abstract String getPersistenceUnitName();
    EntityManagerFactory factory=null;
    public synchronized EntityManager getEntityManager() {
        if(factory==null) {
            factory= Persistence.createEntityManagerFactory(getPersistenceUnitName());
        }
        return factory.createEntityManager();
    }
}
