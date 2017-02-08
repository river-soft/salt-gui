package org.riversoft.salt.gui.datatypes;

import java.util.List;

/**
 * Representation of a scheduled job (not finished yet).
 */
public class ScheduledJob {

    private String jid;
    private List<String> minions;

    public String getJid() {
        return jid;
    }

    public List<String> getMinions() {
        return minions;
    }
}
