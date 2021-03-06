package engine.client.launch;

import engine.Engine;
import engine.Platform;
import engine.client.EngineClientImpl;
import engine.player.Profile;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

public class Bootstrap {

    public static void main(String[] args) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getPid());

        var playerName = "Ifor";
        Engine engine = new EngineClientImpl(Path.of("run"), new Profile(UUID.nameUUIDFromBytes(playerName.getBytes(StandardCharsets.UTF_8)), playerName));
        injectEngine(engine);
        engine.initEngine();
        engine.runEngine();
    }

    private static void injectEngine(Engine engine) {
        try {
            Field engineField = Platform.class.getDeclaredField("engine");
            engineField.setAccessible(true);
            engineField.set(null, engine);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot initialize engine");
        }
    }
}
