package org.riversoft.salt.gui.client.impl;

import org.riversoft.salt.gui.client.ConnectionFactory;
import org.riversoft.salt.gui.config.ClientConfig;
import org.riversoft.salt.gui.parser.JsonParser;

/**
 * Implementation of a factory for connections using JDK's HttpURLConnection.
 *
 * @see JDKConnection
 */
public class JDKConnectionFactory implements ConnectionFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> JDKConnection<T> create(String endpoint, JsonParser<T> parser,
                                       ClientConfig config) {
        return new JDKConnection<>(endpoint, parser, config);
    }
}

