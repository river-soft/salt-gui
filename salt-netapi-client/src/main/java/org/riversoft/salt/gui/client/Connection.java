package org.riversoft.salt.gui.client;

import org.riversoft.salt.gui.exception.SaltException;

public interface Connection<T>  {


    /**
     * Send a GET request and parse the result into object of given {@link java.lang.reflect.Type}.
     *
     * @return object of type given by resultType
     * @throws SaltException if the request was not successful
     */
    T getResult() throws SaltException;

    /**
     * Send a POST request and parse the result into object of given {@link java.lang.reflect.Type}.
     *
     * @param data the data to send (in JSON format)
     * @return object of type given by resultType
     * @throws SaltException if the request was not successful
     */
    T getResult(String data) throws SaltException;

}