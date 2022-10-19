package dev.pypylia.lightningoverflow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.pypylia.lightningoverflow.LightningOverflowMod;
import dev.pypylia.lightningoverflow.WebhookThread;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

@Mixin(EntitySpawnS2CPacket.class)
public class EntitySpawnS2CMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
		EntitySpawnS2CPacket packet = (EntitySpawnS2CPacket)(Object)this;
        if (EntityType.getId(packet.getEntityTypeId()).getPath() == "lightning_bolt") {
            MinecraftClient client = MinecraftClient.getInstance();
            if (packet.getUuid() == LightningOverflowMod.lastId) return;
            LightningOverflowMod.lastId = packet.getUuid();
            WebhookThread.main(client, "Lighting spawn", packet.getX(), packet.getY(), packet.getZ());
        }
	}
}
