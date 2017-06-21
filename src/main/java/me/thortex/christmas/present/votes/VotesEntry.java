package me.thortex.christmas.present.votes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VotesEntry {

    private final UUID uuid;
    private final int votes;

}
