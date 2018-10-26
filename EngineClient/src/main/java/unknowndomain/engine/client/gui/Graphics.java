package unknowndomain.engine.client.gui;

import unknowndomain.engine.client.texture.GLTexture;
import unknowndomain.engine.client.util.Color;

public interface Graphics {

    Color getColor();

    void setColor(Color color);

    void drawLine(float x1, float y1, float x2, float y2);

    void drawRect(float x, float y, float width, float height);

    void fillRect(float x, float y, float width, float height);

    void drawRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight);

    void fillRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight);

    void drawQuadraticBelzierCurve(float startX, float startY, float endX, float endY, float px, float py);

    void drawBelzierCurve(float startX, float startY, float endX, float endY, float px1, float py1, float px2, float py2);

    void drawEllipticalArc(float startX, float startY, float endX, float endY, float rx, float ry, float xRotation, boolean largeArc, boolean sweep);

    void drawText(CharSequence text, float x, float y);

    void drawTexture(GLTexture texture, float x, float y, float width, float height, float u, float v);
}
