package nullengine.client.rendering.texture;

import nullengine.client.asset.AssetURL;

public interface TextureAtlasPart {

    AssetURL getUrl();

    Texture2DBuffer getData();

    float getMinU();

    float getMinV();

    float getMaxU();

    float getMaxV();
}
