package com.mbds.vamp.dashboardapp.utils;

import java.net.URISyntaxException;
import com.github.nkzawa.socketio.client.IO;

/**
 * Created by Yasmine on 15/03/2018.
 */

public class SocketManager {
    private com.github.nkzawa.socketio.client.Socket mSocket;

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Connextion au serveur du véhicule
    ////////////////////////////////////////////////////////////////////////////////////////////

    public SocketManager() {
        try {
            this.mSocket = IO.socket(Config.SOCKET_SERVER_URL);
            this.mSocket.connect();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    public void disconnect() {
        mSocket.disconnect();
    }
}
