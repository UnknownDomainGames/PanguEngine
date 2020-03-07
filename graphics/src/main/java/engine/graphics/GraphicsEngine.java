package engine.graphics;

import engine.graphics.backend.GraphicsBackend;
import engine.graphics.backend.GraphicsBackendFactory;
import engine.graphics.font.FontManager;
import engine.graphics.image.ImageLoader;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.windows.User32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public final class GraphicsEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger("Graphics");

    public static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("graphics.debug", "false"));

    private static Settings settings;
    private static GraphicsBackend graphicsBackend;

    public static GraphicsBackend getGraphicsBackend() {
        return graphicsBackend;
    }

    public static boolean isDebug() {
        return settings.debug;
    }

    public static void start(Settings settings) {
        if (graphicsBackend != null) {
            throw new IllegalArgumentException("Graphics engine has been started.");
        }
        GraphicsEngine.settings = settings;
        initEnvironment();
        ImageLoader.initialize(settings.imageLoader);
        graphicsBackend = ServiceLoader.load(GraphicsBackendFactory.class)
                .stream()
                .filter(provider -> provider.get().getName().equals(settings.backend))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No found graphics backend: " + settings.backend))
                .get()
                .create();
        LOGGER.info("Graphics backend: {}", settings.backend);
        graphicsBackend.init();
        FontManager.initialize(settings.fontManager);
    }

    private static void initEnvironment() {
        if (SystemUtils.IS_OS_MAC) {
            System.setProperty("java.awt.headless", "true");
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            if (User32.Functions.SetThreadDpiAwarenessContext != MemoryUtil.NULL) {
                User32.SetThreadDpiAwarenessContext(User32.IsValidDpiAwarenessContext(User32.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2) ? User32.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2 : User32.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE);
            }
        }
    }

    public static void doRender(float tpf) {
        graphicsBackend.render(tpf);
    }

    public static void stop() {
        if (graphicsBackend == null) {
            return;
        }
        graphicsBackend.dispose();
        graphicsBackend = null;
    }

    public static class Settings {
        private boolean debug = DEBUG;
        private String backend = "opengl";
        private String imageLoader = "stb";
        private String fontManager = "stb";

        public Settings setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Settings setGraphicsBackend(String backend) {
            this.backend = backend;
            return this;
        }

        public Settings setImageLoader(String imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public Settings setFontManager(String fontManager) {
            this.fontManager = fontManager;
            return this;
        }
    }

    private GraphicsEngine() {
    }
}
