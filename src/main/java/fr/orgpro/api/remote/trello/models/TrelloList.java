package fr.orgpro.api.remote.trello.models;

public class TrelloList {
    private String id;
    private String name;
    private boolean closed;
    private String idBoard;
    private float pos;

    @Override
    public String toString() {
        return "TrelloList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", closed=" + closed +
                ", idBoard='" + idBoard + '\'' +
                ", pos=" + pos +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public float getPos() {
        return pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }
}
