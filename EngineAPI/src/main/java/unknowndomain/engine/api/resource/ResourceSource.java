package unknowndomain.engine.api.resource;

import java.io.IOException;

public interface ResourceSource {
    Resource load(ResourcePath path) throws IOException;

    PackInfo info() throws IOException;

    String type();
}
