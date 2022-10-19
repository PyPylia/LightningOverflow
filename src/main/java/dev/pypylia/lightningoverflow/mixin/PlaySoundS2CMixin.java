package dev.pypylia.lightningoverflow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.pypylia.lightningoverflow.WebhookThread;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

@Mixin(PlaySoundS2CPacket.class)
public class PlaySoundS2CMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
		PlaySoundS2CPacket packet = (PlaySoundS2CPacket)(Object)this;
         if (packet.getSound().getId().getPath() == "entity.lightning_bolt.thunder") {
            MinecraftClient client = MinecraftClient.getInstance();
            WebhookThread.main(client, "Thunder sound", packet.getX(), packet.getY(), packet.getZ());
         }
	}
}
