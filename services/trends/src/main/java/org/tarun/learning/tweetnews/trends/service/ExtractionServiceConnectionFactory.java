package org.tarun.learning.tweetnews.trends.service;

import org.springframework.stereotype.Component;


public class ExtractionServiceConnectionFactory implements ServiceConnectionFactory {

    private String host;
    private int port;
    private String protocol;

    public  ExtractionServiceConnectionFactory() {}

    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    @Override
    public ServiceConnection getConnection() {
        return new ServiceConnection(host,port, protocol);
    }
}
