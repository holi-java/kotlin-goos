package test;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapCollisionTest {
    class Key {
        private int key;

        public Key(int key) {

            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            return key == ((Key) obj).key;
        }

        public int hashCode() {
            return 0;
        }

    }

    @Test
    public void bucket() throws Throwable {
        Map<Key, Object> context = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            context.put(new Key(i), null);
        }
        Field table = context.getClass().getDeclaredField("table");
        table.setAccessible(true);
        Object tables = table.get(context);
        context.get(new Key(1));
    }
}
