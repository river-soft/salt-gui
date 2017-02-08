package org.riversoft.salt.gui.calls.modules;

import com.google.gson.reflect.TypeToken;
import org.riversoft.salt.gui.calls.LocalCall;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * salt.modules.system
 *
 * https://docs.saltstack.com/en/latest/ref/modules/all/salt.modules.system.html
 */
public class System {

    private System() { }

    public static LocalCall<String> reboot(Optional<Integer> at_time) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        at_time.ifPresent(t -> {
            args.put("at_time", t);
        });
        return new LocalCall<>("system.reboot", Optional.empty(), Optional.of(args),
                new TypeToken<String>(){});
    }
}
