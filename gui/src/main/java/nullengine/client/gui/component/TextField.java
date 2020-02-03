package nullengine.client.gui.component;

import nullengine.client.gui.rendering.ComponentRenderer;
import nullengine.client.gui.rendering.TextFieldRenderer;
import nullengine.client.rendering.font.FontHelper;

public class TextField extends TextInput {

    private float lineScrollOffset = 0;

    public float getLineScrollOffset() {
        return lineScrollOffset;
    }

    public TextField() {
        super();
    }

    //    public void onKeyHold(KeyEvent event) {
//        switch (event.getKey()) {
//            case KEY_X:
//            case KEY_C:
//            case KEY_A:
//                //No need to repeat
//                break;
//            default:
//                onKeyDown(new KeyEvent_.KeyDownEvent(event.getNode(), event.getKey(), event.getMode(), event.getModifier()));
//        }
//    }

    @Override
    protected int getNearestMousePos(float posX, float posY) {
        float adjustedX = posX - lineScrollOffset;
        int posExclusive = 1;
        float x = 0;
        while (posExclusive <= length()) {
            x += FontHelper.instance().computeTextWidth(getTextInRange(posExclusive - 1, posExclusive), font().getValue());
            if (x > adjustedX)
                break;
            posExclusive++;
        }
        return posExclusive - 1;
    }

    @Override
    protected ComponentRenderer createDefaultRenderer() {
        return new TextFieldRenderer(this);
    }

    @Override
    protected void updatePointer() {
        var viewableWidth = width().get() - padding().getValue().getLeft() - padding().getValue().getRight();
        var caretOffset = FontHelper.instance().computeTextWidth(getTextInRange(0, caret().get()), font().getValue());
        var adjusted = caretOffset + lineScrollOffset;
        if (viewableWidth < adjusted) {
            lineScrollOffset = viewableWidth - caretOffset;
        } else if (adjusted < 0) {
            lineScrollOffset = lineScrollOffset - adjusted;
        }
        lineScrollOffset = Math.min(lineScrollOffset, 0);
    }

    //    private void updateContent()

}
