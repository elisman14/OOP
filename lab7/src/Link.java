import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Link {

    private final String url;

    private final int depth;

    public Link(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return getUrl().equals(link.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getDocPath(), getWebHost()) * 37;
    }

    @Override
    public String toString() {
        return "Link{" +
                "url='" + url + '\'' +
                ", depth=" + depth +
                '}';
    }

    public String getWebHost() {
        try {
            URL currentUrl = new URL(url);
            return currentUrl.getHost();
        }
        catch (MalformedURLException e) {
            System.err.println("MalformedURLException: " + e.getMessage());
            return null;
        }
    }

    public String getDocPath() {
        try {
            URL currentUrl = new URL(url);
            return currentUrl.getPath();
        }
        catch (MalformedURLException e) {
            System.err.println("MalformedURLException: " + e.getMessage());
            return null;
        }
    }
}
