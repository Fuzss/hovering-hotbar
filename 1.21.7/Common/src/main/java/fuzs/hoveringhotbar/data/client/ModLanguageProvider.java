package fuzs.hoveringhotbar.data.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.addKeyCategory(HoveringHotbar.MOD_ID, HoveringHotbar.MOD_NAME);
        builder.add(HoveringHotbarClient.MOVE_HOTBAR_UP_KEY_MAPPING, "Move Hotbar Up");
        builder.add(HoveringHotbarClient.MOVE_HOTBAR_DOWN_KEY_MAPPING, "Move Hotbar Down");
    }
}
