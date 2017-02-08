package org.riversoft.salt.gui.client.impl;

import org.riversoft.salt.gui.client.ConnectionFactory;
import org.riversoft.salt.gui.config.ClientConfig;
import org.riversoft.salt.gui.parser.JsonParser;

public class HttpClientConnectionFactory implements ConnectionFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> HttpClientConnection<T> create(String endpoint,
                                              JsonParser<T> parser, ClientConfig config) {
        return new HttpClientConnection<>(endpoint, parser, config);
    }

}

