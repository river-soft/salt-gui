package org.riversoft.salt.gui.calls.modules;

import com.google.gson.reflect.TypeToken;
import org.riversoft.salt.gui.calls.LocalCall;

import java.util.*;

/**
 * salt.modules.grains
 */
public class Grains {

    private Grains() { }

    private static LocalCall<List<String>> LS =
            new LocalCall<>("grains.ls", Optional.empty(),
            Optional.empty(), new TypeToken<List<String>>(){});

    public static LocalCall<Map<String, Object>> items(boolean sanitize) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("sanitize", sanitize);
        return new LocalCall<>("grains.items", Optional.empty(), Optional.of(args),
                new TypeToken<Map<String, Object>>(){});
    }

    public static LocalCall<List<String>> ls() {
        return LS;
    }

    public static LocalCall<Map<String, Object>> item(boolean sanitize,
            String... items) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("sanitize", sanitize);
        return new LocalCall<>("grains.item", Optional.of(Arrays.asList(items)),
                Optional.of(args), new TypeToken<Map<String, Object>>(){});
    }

    public static LocalCall<Boolean> hasValue(String key) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("key", key);
        return new LocalCall<>("grains.has_value", Optional.empty(), Optional.of(args),
                new TypeToken<Boolean>(){});
    }
}
