package ru.geekbrains.oskin_di.service.impl;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.core.NettyClient;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.Callback;
import ru.geekbrains.oskin_di.service.NetworkService;

public class NetworkServiceImpl implements NetworkService {

    private final NettyClient client = Factory.getClientService();

    private static NetworkServiceImpl instance;

    public static NetworkServiceImpl getInstance() {
        if (instance == null) {
            instance = new NetworkServiceImpl();
        }
        return instance;
    }

    @Override
    public void sendCommand(Command command, Callback callback) {
        client.sendCommand(command, callback);
    }

    @Override
    public void openConnection() {
        client.startClient();
    }

    @Override
    public void closeConnection() {
        client.stopClient();
    }
}
