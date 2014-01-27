package chapter12;

import org.testng.annotations.Test;

import javax.persistence.EntityManager;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MongoTests extends BaseOGMTests {
    @Test
    public void testCR() {
        EntityManager em = getEntityManager();
        Person person = new Person("user 1", 1);
        em.persist(person);
        em.close();
        System.out.println(person);
        em=getEntityManager();
        Person p2=em.find(Person.class, person.getId());
        em.close();
        System.out.println(p2);
        assertNotNull(p2);
        assertEquals(p2, person);
    }

    @Override
    String getPersistenceUnitName() {
        return "chapter12-mongo";
    }
}
