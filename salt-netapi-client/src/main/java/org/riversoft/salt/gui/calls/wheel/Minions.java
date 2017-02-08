package org.riversoft.salt.gui.calls.wheel;

import com.google.gson.reflect.TypeToken;
import org.riversoft.salt.gui.calls.WheelCall;

import java.util.List;
import java.util.Optional;

/**
 * salt.wheel.minions
 */
public class Minions {

    private Minions() {
    }

    private static final WheelCall<List<String>> CONNECTED =
            new WheelCall<>("minions.connected", Optional.empty(),
                    new TypeToken<List<String>>(){});

    public static WheelCall<List<String>> connected() {
        return CONNECTED;
    }
}
