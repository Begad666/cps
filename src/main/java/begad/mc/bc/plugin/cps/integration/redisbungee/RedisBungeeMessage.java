package begad.mc.bc.plugin.cps.integration.redisbungee;

import java.util.ArrayList;
import java.util.Arrays;

public class RedisBungeeMessage {
    public final String command;
    public final ArrayList<String> body;
    public final String serverId;

    private RedisBungeeMessage(String command, String[] body, String serverId) {
        this.command = command;
        this.body = new ArrayList<>(Arrays.asList(body));
        this.serverId = serverId;
    }

    private RedisBungeeMessage(String command, ArrayList<String> body, String serverId) {
        this.command = command;
        this.body = body;
        this.serverId = serverId;
    }

    public static RedisBungeeMessage construct(String command, String[] body, RedisBungeeIntegration integration) {
        return new RedisBungeeMessage(command, body, integration.getApi().getServerId());
    }

    public static RedisBungeeMessage construct(String command, ArrayList<String> body, RedisBungeeIntegration integration) {
        return new RedisBungeeMessage(command, body, integration.getApi().getServerId());
    }

    public static String encode(RedisBungeeMessage message, RedisBungeeIntegration integration) {
        return message.serverId + integration.splitter + message.command + integration.splitter + String.join(integration.splitter, message.body);
    }

    public static RedisBungeeMessage decode(String[] message) {
        if (message.length < 3) {
            return null;
        }
        String id = message[0];
        String command = message[1];
        String[] body = new String[message.length - 2];
        System.arraycopy(message, 2, body, 0, message.length - 2);
        return new RedisBungeeMessage(command, body, id);
    }

    public static RedisBungeeMessage decode(String messageString, RedisBungeeIntegration integration) {
        String[] message = messageString.split(integration.splitter);

        if (message.length < 3) {
            return null;
        }
        String id = message[0];
        String command = message[1];
        String[] body = new String[message.length - 2];
        System.arraycopy(message, 2, body, 0, message.length - 2);
        return new RedisBungeeMessage(command, body, id);
    }

    public static RedisBungeeMessage decode(String messageString, String splitter) {
        String[] message = messageString.split(splitter);

        if (message.length < 3) {
            return null;
        }
        String id = message[0];
        String command = message[1];
        String[] body = new String[message.length - 2];
        System.arraycopy(message, 2, body, 0, message.length - 2);
        return new RedisBungeeMessage(command, body, id);
    }

}
