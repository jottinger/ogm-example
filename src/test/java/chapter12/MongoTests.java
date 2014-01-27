package chapter12;

import org.testng.annotations.Test;

import javax.persistence.EntityManager;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class MongoTests extends BaseOGMTests {
    @Test
    public void testCR() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Person person = new Person("user 1", 1);
        em.persist(person);
        em.getTransaction().commit();
        em.close();
        System.out.println(person);
        em=getEntityManager();
        Person p2=em.find(Person.class, person.getId());
        em.close();
        System.out.println(p2);
        assertNotNull(p2);
        assertEquals(p2, person);
    }

    @Test
    public void testUpdate() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Person person = new Person("user 2", 1);
        em.persist(person);
        em.getTransaction().commit();
        em.close();

        em=getEntityManager();
        em.getTransaction().begin();
        Person p2=em.find(Person.class, person.getId());
        p2.setBalance(2);
        em.getTransaction().commit();
        em.close();

        em=getEntityManager();
        em.getTransaction().begin();
        Person p3=em.find(Person.class, person.getId());
        em.getTransaction().commit();
        em.close();

        assertEquals(p3, p2);
    }

    @Test
    public void testDelete() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Person person = new Person("user 3", 1);
        em.persist(person);
        em.getTransaction().commit();
        em.close();

        em=getEntityManager();
        em.getTransaction().begin();
        Person p2=em.find(Person.class, person.getId());
        em.remove(p2);
        em.getTransaction().commit();
        em.close();

        em=getEntityManager();
        em.getTransaction().begin();
        Person p3=em.find(Person.class, person.getId());
        em.getTransaction().commit();
        em.close();

        assertNull(p3);
    }

    @Override
    String getPersistenceUnitName() {
        return "chapter12-mongo";
    }
}
