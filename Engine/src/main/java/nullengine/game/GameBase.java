package nullengine.game;

import nullengine.Engine;
import nullengine.event.EventBus;
import nullengine.event.game.GameCreateEvent;
import nullengine.event.game.GameStartEvent;
import nullengine.event.game.GameTerminationEvent;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public abstract class GameBase implements Game {

    protected final Engine engine;

    protected final Path storagePath;

    protected final Logger logger;
    protected final Marker marker = MarkerFactory.getMarker("Game");

    protected EventBus eventBus;

    protected boolean markedTermination = false;
    protected boolean terminated = false;

    public GameBase(Engine engine, Path storagePath) {
        this.engine = engine;
        this.logger = engine.getLogger();
        this.eventBus = engine.getEventBus();
        this.storagePath = storagePath;
    }

    /**
     * Construct stage, collect mod and resource according to it option
     */
    protected void constructStage() {
    }

    /**
     * let mod and resource related module load resources.
     */
    protected void resourceStage() {

    }

    /**
     * final stage of the
     */
    protected void finishStage() {
    }

    @Nonnull
    @Override
    public Engine getEngine() {
        return engine;
    }

    @Nonnull
    @Override
    public Path getStoragePath() {
        return storagePath;
    }

    @Nonnull
    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public boolean isMarkedTermination() {
        return markedTermination;
    }

    @Override
    public void init() {
        logger.info(marker, "Initializing Game.");
        eventBus.post(new GameCreateEvent.Pre(this));
        eventBus.post(new GameStartEvent.Pre(this));
        constructStage();
        resourceStage();
        finishStage();
        eventBus.post(new GameCreateEvent.Post(this));
        eventBus.post(new GameStartEvent.Post(this));
        // TODO: loop to check if we need to gc the world

        // for (WorldCommon worldCommon : internalWorlds) {
        // worldCommon.stop();
        // }
    }

    @Override
    public synchronized void terminate() {
        markedTermination = true;
        logger.info(marker, "Marked game terminated!");
        eventBus.post(new GameTerminationEvent.Marked(this));
    }

    protected void tryTerminate() {
        terminated = true;
    }

    @Override
    public boolean isTerminated() {
        return terminated;
    }

    // TODO: unload mod/resource
}
