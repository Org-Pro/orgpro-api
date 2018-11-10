package fr.orgpro.api.remote.trello.models;

public class TrelloBoard {
    private String id;
    private String name;
    private String desc;
    private String descData;
    private boolean closed;
    private String idOrganisation;
    private boolean pinned;
    private String url;
    private String shortUrl;
    private Object prefs;
    private Object labelNames;
    private boolean starred;
    private Object limit;
    private Object[] memberships;

    @Override
    public String toString() {
        return "TrelloBoard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", descData='" + descData + '\'' +
                ", closed=" + closed +
                ", idOrganisation='" + idOrganisation + '\'' +
                ", pinned=" + pinned +
                ", url='" + url + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", prefs=" + prefs +
                ", labelNames=" + labelNames +
                ", starred=" + starred +
                ", limit=" + limit +
                '}';
    }

    public TrelloBoard() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setIdOrganisation(String idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setPrefs(Object prefs) {
        this.prefs = prefs;
    }

    public void setLabelNames(Object labelNames) {
        this.labelNames = labelNames;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public void setLimit(Object limit) {
        this.limit = limit;
    }

    public void setMemberships(Object[] memberships) {
        this.memberships = memberships;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescData() {
        return descData;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getIdOrganisation() {
        return idOrganisation;
    }

    public boolean isPinned() {
        return pinned;
    }

    public String getUrl() {
        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public Object getPrefs() {
        return prefs;
    }

    public Object getLabelNames() {
        return labelNames;
    }

    public boolean isStarred() {
        return starred;
    }

    public Object getLimit() {
        return limit;
    }

    public Object[] getMemberships() {
        return memberships;
    }
}
