package org.riversoft.salt.gui.client;

import org.riversoft.salt.gui.config.ClientConfig;
import org.riversoft.salt.gui.parser.JsonParser;

public interface ConnectionFactory {

    /**
     * Create a new {@link Connection} for a given endpoint and configuration.
     *
     * @param <T> type of the result as returned by the parser
     * @param endpoint the API endpoint
     * @param parser the parser used for parsing the result
     * @param config the configuration
     * @return object representing a connection to the API
     */
    <T> Connection<T> create(String endpoint, JsonParser<T> parser, ClientConfig config);

}