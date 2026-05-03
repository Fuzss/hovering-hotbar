package fuzs.hoveringhotbar.common.data.client;

import fuzs.hoveringhotbar.common.HoveringHotbar;
import fuzs.hoveringhotbar.common.client.HoveringHotbarClient;
import fuzs.puzzleslib.common.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;

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
