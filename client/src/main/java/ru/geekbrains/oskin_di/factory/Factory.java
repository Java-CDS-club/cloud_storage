package ru.geekbrains.oskin_di.factory;

import ru.geekbrains.oskin_di.core.NettyClient;
import ru.geekbrains.oskin_di.service.NetworkService;
import ru.geekbrains.oskin_di.service.impl.NetworkServiceImpl;

public class Factory {

    public static NettyClient getClientService() {
        return NettyClient.getInstance();
    }

    public static NetworkService getNetworkService() {
        return NetworkServiceImpl.getInstance();
    }

}
