package me.thortex.christmas.util;

import net.minecraft.server.v1_11_R1.EnumParticle;
import net.minecraft.server.v1_11_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Particles {

    private Particles() {}

    public static void playWorldParticle(EnumParticle particle, Location location, float offsetX, float offsetY,
                                         float offsetZ, float speed, int count) {
        PacketPlayOutWorldParticles packet =
                new PacketPlayOutWorldParticles(particle, true, (float) location.getX(), (float) location.getY(),
                        (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void playParticleToPlayer(Player player, EnumParticle particle, Location location, float offsetX,
                                            float offsetY, float offsetZ, float speed, int count) {
        PacketPlayOutWorldParticles packet =
                new PacketPlayOutWorldParticles(particle, true, (float) location.getX(), (float) location.getY(),
                        (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
