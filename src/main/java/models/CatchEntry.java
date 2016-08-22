package models;

import java.util.Date;

public class CatchEntry {
    public Pokemon getPokemon() {
        return pokemon;
    }

    public Date getCatchTime() {
        return catchTime;
    }

    private Pokemon pokemon;
    private Date catchTime;

    public CatchEntry(Pokemon pokemon, Date catchTime) {
        this.pokemon = pokemon;
        this.catchTime = catchTime;
    }

    @Override
    public String toString() {
        return "CatchEntry{" +
                "pokemon=" + pokemon +
                ", catchTime=" + catchTime +
                '}';
    }
}
