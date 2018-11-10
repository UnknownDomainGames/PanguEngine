package unknowndomain.engine.client.gui.renderer;

import unknowndomain.engine.client.gui.Component;
import unknowndomain.engine.client.gui.Graphics;
import unknowndomain.engine.client.gui.Region;

public class RegionRenderer extends ComponentRenderer<Region> {

    public RegionRenderer(Region component) {
        super(component);
    }

    @Override
    public void render(Graphics graphics) {
        for (Component child : getComponent().getUnmodifiableChildren()) {
            if (!child.isVisible()) {
                continue;
            }
            child.getRenderer().render(graphics);
        }
    }
}