package dev.pypylia.lightningoverflow;

import java.util.ArrayList;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ConfigMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("LightningOverflow"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory category = builder.getOrCreateCategory(Text.of("main"));
            category.addEntry(
                entryBuilder.startBooleanToggle(Text.of("Anonymize your username"), LightningOverflowMod.CONFIG.ANONYMIZE_USERNAME)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> LightningOverflowMod.CONFIG.ANONYMIZE_USERNAME = newValue)
                    .build()
            );
            category.addEntry(
                entryBuilder.startBooleanToggle(Text.of("Don't send coords/direction"), LightningOverflowMod.CONFIG.LINE_ONLY)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> LightningOverflowMod.CONFIG.LINE_ONLY = newValue)
                    .build()
            );
            category.addEntry(
                entryBuilder.startIntField(Text.of("Minimum thunder distance"), LightningOverflowMod.CONFIG.MIN_DISTANCE)
                    .setDefaultValue(0)
                    .setSaveConsumer(newValue -> LightningOverflowMod.CONFIG.MIN_DISTANCE = newValue)
                    .build()
            );
            category.addEntry(
                entryBuilder.startStrField(Text.of("Discord webhook URL"), LightningOverflowMod.CONFIG.WEBHOOK_URL)
                    .setDefaultValue("")
                    .setSaveConsumer(newValue -> LightningOverflowMod.CONFIG.WEBHOOK_URL = newValue)
                    .build()
            );
            category.addEntry(
                entryBuilder.startStrList(Text.of("Whitelisted servers"), LightningOverflowMod.CONFIG.WHITELISTED_SERVERS)
                    .setDefaultValue(new ArrayList<String>())
                    .setSaveConsumer(newValue -> LightningOverflowMod.CONFIG.WHITELISTED_SERVERS = (ArrayList<String>)newValue)
                    .build()
            );

            builder.setSavingRunnable(() -> {
                LightningOverflowMod.CONFIG.save();
            });

            return builder.build();
        };
    }
}
