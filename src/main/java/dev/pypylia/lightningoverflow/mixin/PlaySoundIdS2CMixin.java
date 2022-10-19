package dev.pypylia.lightningoverflow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.pypylia.lightningoverflow.WebhookThread;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;

@Mixin(PlaySoundIdS2CPacket.class)
public class PlaySoundIdS2CMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
		PlaySoundIdS2CPacket packet = (PlaySoundIdS2CPacket)(Object)this;
         if (packet.getSoundId().getPath() == "entity.lightning_bolt.thunder") {
            MinecraftClient client = MinecraftClient.getInstance();
            WebhookThread.main(client, "Thunder sound (ID)", packet.getX(), packet.getY(), packet.getZ());
         }
	}
}
