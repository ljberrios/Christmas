package me.thortex.christmas.present.votes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.thortex.christmas.common.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
@Data
@EqualsAndHashCode(callSuper = true)
public class VotesConfig extends Configuration {

    @Inject
    public VotesConfig(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "votes.yml"), "votes.yml");
    }

    public List<VotesEntry> getVotes() {
        List<VotesEntry> votes = new ArrayList<>();
        ConfigurationSection section = getVotesSection();
        section.getKeys(false).stream()
            .filter(key -> votes.size() < 10)
            .forEach(key -> votes.add(new VotesEntry(UUID.fromString(key), (int) section.get(key))));
        votes.sort((o1, o2) -> o2.getVotes() - o1.getVotes());
        return votes;
    }

    public VotesEntry getVote(UUID uuid) {
        for (VotesEntry entry : getVotes()) {
            if (entry.getUuid().equals(uuid)) {
                return entry;
            }
        }
        return null;
    }

    public void addVote(UUID uuid) {
        ConfigurationSection section = getVotesSection();
        String str = uuid.toString();
        Object value = section.get(str);
        if (value == null) {
            section.set(str, 1);
        } else {
            section.set(str, (int) value + 1);
        }
        save();
    }

    public void removeVote(UUID uuid) {
        ConfigurationSection section = getVotesSection();
        if (section.contains(uuid.toString())) {
            section.set(uuid.toString(), null);
            save();
        }
    }

    private ConfigurationSection getVotesSection() {
        ConfigurationSection section = getConfig().getConfigurationSection("votes");
        if (section == null) {
            section = getConfig().createSection("votes");
        }
        return section;
    }

}
