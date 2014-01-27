package chapter12;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class MongoTests extends BaseOGMTests {

    @AfterMethod
    public void clearDB() {
        try {
            Mongo mongo = new Mongo();
            DB db = mongo.getDB("chapter12");
            db.dropDatabase();
            mongo.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCR() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Person person = new Person("user 1", 1);
        em.persist(person);
        em.getTransaction().commit();
        em.close();
        System.out.println(person);
        em = getEntityManager();
        Person p2 = em.find(Person.class, person.getId());
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

        em = getEntityManager();
        em.getTransaction().begin();
        Person p2 = em.find(Person.class, person.getId());
        p2.setBalance(2);
        em.getTransaction().commit();
        em.close();

        em = getEntityManager();
        em.getTransaction().begin();
        Person p3 = em.find(Person.class, person.getId());
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

        em = getEntityManager();
        em.getTransaction().begin();
        Person p2 = em.find(Person.class, person.getId());
        em.remove(p2);
        em.getTransaction().commit();
        em.close();

        em = getEntityManager();
        em.getTransaction().begin();
        Person p3 = em.find(Person.class, person.getId());
        em.getTransaction().commit();
        em.close();

        assertNull(p3);
    }

    @Test
    public void testQuery() {
        Map<Integer, Person> people = new HashMap<>();
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        for (int i = 4; i < 7; i++) {
            people.put(i, new Person("user " + i, i));
            em.persist(people.get(i));
        }

        em.getTransaction().commit();
        em.close();

        em = getEntityManager();
        em.getTransaction().begin();
        Session session = em.unwrap(Session.class);
        Query q = session.createQuery("from Person p where p.balance = :balance");
        q.setInteger("balance", 4);
        Person p = (Person) q.uniqueResult();
        assertEquals(p, people.get(4));
        em.getTransaction().commit();
        em.close();

        em = getEntityManager();
        em.getTransaction().begin();
        session = em.unwrap(Session.class);
        q = session.createQuery("from Person p where p.balance > :balance");
        q.setInteger("balance", 4);
        List<Person> peopleList = q.list();
        assertEquals(peopleList.size(), 2);
        em.getTransaction().commit();
        em.close();

        em = getEntityManager();
        em.getTransaction().begin();
        session = em.unwrap(Session.class);
        q = session.createQuery("from Person p where p.balance = :balance and p.name=:name");
        q.setInteger("balance", 4);
        q.setString("name", "user 4");
        p = (Person) q.uniqueResult();
        assertEquals(p, people.get(4));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    String getPersistenceUnitName() {
        return "chapter12-mongo";
    }
}
