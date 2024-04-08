package models;

public class CategoryItem {
    private int id;
    private int type;
    private String name;

    public CategoryItem(int id, int type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}
