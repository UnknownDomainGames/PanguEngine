package nullengine.client.rendering.scene;

import nullengine.client.rendering.camera.FreeCamera;
import nullengine.client.rendering.display.Window;
import nullengine.client.rendering.display.callback.FramebufferSizeCallback;
import nullengine.util.Color;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public abstract class BaseViewPort implements ViewPort {

    private int width;
    private int height;

    private Scene3D scene;

    private Color clearColor = new Color(0f, 0f, 0f, 1f);
    private boolean isClearColor;
    private boolean isClearDepth;
    private boolean isClearStencil;

    private FreeCamera camera = new FreeCamera();

    protected final Matrix4f projectionMatrix = new Matrix4f();
    protected final Matrix4f projectionViewMatrix = new Matrix4f();

    protected final FrustumIntersection frustum = new FrustumIntersection();

    private Window window;
    private FramebufferSizeCallback framebufferSizeCallback;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        onFrameSizeChanged();
    }

    @Override
    public Scene3D getScene() {
        return scene;
    }

    @Override
    public void setScene(Scene3D scene) {
        this.scene = scene;
    }

    @Override
    public Color getClearColor() {
        return clearColor;
    }

    @Override
    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }

    @Override
    public boolean isClearColor() {
        return isClearColor;
    }

    @Override
    public void setClearColor(boolean clearColor) {
        isClearColor = clearColor;
    }

    @Override
    public boolean isClearDepth() {
        return isClearDepth;
    }

    @Override
    public void setClearDepth(boolean clearDepth) {
        isClearDepth = clearDepth;
    }

    @Override
    public boolean isClearStencil() {
        return isClearStencil;
    }

    @Override
    public void setClearStencil(boolean clearStencil) {
        isClearStencil = clearStencil;
    }

    @Override
    public void setClearMask(boolean color, boolean depth, boolean stencil) {
        setClearColor(color);
        setClearDepth(depth);
        setClearStencil(stencil);
    }

    protected abstract void onFrameSizeChanged();

    protected abstract void updateProjectionMatrix();

    protected abstract void updateProjectionViewMatrix();

    @Override
    public FreeCamera getCamera() {
        return camera;
    }

    @Override
    public void setCamera(FreeCamera camera) {
        if (this.camera != null) {
            this.camera.setChangeListener(null);
        }
        this.camera = camera;
        if (camera != null) {
            camera.setChangeListener($ -> updateProjectionViewMatrix());
            updateProjectionViewMatrix();
        }
    }

    @Override
    public Matrix4fc getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4fc getViewMatrix() {
        return camera.getViewMatrix();
    }

    @Override
    public Matrix4fc getProjectionViewMatrix() {
        return projectionViewMatrix;
    }

    @Override
    public FrustumIntersection getFrustum() {
        return frustum;
    }

    @Override
    public void bindWindow(Window window) {
        if (this.window != null) this.window.removeFramebufferSizeCallback(framebufferSizeCallback);
        if (window != null) {
            if (framebufferSizeCallback == null) {
                framebufferSizeCallback = (_window, width, height) -> setSize(width, height);
            }
            setSize(window.getWidth(), window.getHeight());
            window.addFramebufferSizeCallback(framebufferSizeCallback);
        }
        this.window = window;
    }

    @Override
    public void unbindWindow() {
        bindWindow(null);
    }
}