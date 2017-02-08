package org.riversoft.salt.gui.calls.modules;

import com.google.gson.reflect.TypeToken;
import org.riversoft.salt.gui.calls.LocalCall;

import java.util.Optional;

/**
 * salt.modules.timezone
 *
 * https://docs.saltstack.com/en/latest/ref/modules/all/salt.modules.timezone.html
 */
public class Timezone {

    private Timezone() { }

    public static LocalCall<String> getOffset() {
        return new LocalCall<>("timezone.get_offset", Optional.empty(), Optional.empty(),
                new TypeToken<String>(){});
    }
}
