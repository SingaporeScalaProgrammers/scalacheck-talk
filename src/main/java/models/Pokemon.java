package models;


import java.util.Objects;

public class Pokemon {

    private final int id;
    private final String name;
    private final String type;

    public Pokemon(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return id == pokemon.id &&
                Objects.equals(name, pokemon.name) &&
                Objects.equals(type, pokemon.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString(){
        return "Pokemon("+id+","+name+","+type+")";
    }
}
