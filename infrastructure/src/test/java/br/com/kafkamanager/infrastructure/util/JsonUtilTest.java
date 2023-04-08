package br.com.kafkamanager.infrastructure.util;

import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

    private static final String FILENAME = "test.json";

    @Test
    void shouldReadJsonFile() throws IOException {
        TestObject testObject = new TestObject("John", "Doe", 25);

        JsonUtil.writeJsonFile(testObject, FILENAME);

        TestObject readObject = JsonUtil.readJsonFile(FILENAME, TestObject.class);

        //Files.deleteIfExists(Paths.get(FILENAME));

        Assertions.assertEquals(testObject, readObject);
    }

    private static class TestObject {

        private final String firstName;
        private final String lastName;
        private final int age;

        public TestObject(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TestObject that = (TestObject) o;
            return age == that.age &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName, age);
        }
    }
}
