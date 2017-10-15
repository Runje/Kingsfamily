package blue.koenig.kingsfamilylibrary.model.family;

/**
 * Created by Thomas on 14.10.2017.
 */

public class Plugin {
    private String name;
    private String uri;
    private boolean isInstalled;

    public Plugin(String name, String uri, boolean isInstalled) {
        this.name = name;
        this.uri = uri;
        this.isInstalled = isInstalled;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
