package chapter12;

public class InfinispanTests extends BaseOGMTests {
    @Override
    String getPersistenceUnitName() {
        return "chapter12-ispn";
    }
}
