package me.xoq.flux.utils.misc;

import net.minecraft.screen.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static me.xoq.flux.FluxClient.mc;

public final class SlotUtils {
    public static final int HOTBAR_START = 0, HOTBAR_END = 8;
    public static final int MAIN_START   = 9, MAIN_END   = 35;
    public static final int ARMOR_START  = 36, ARMOR_END  = 39;
    public static final int OFFHAND      = 45;

    private static final List<Map.Entry<Class<? extends ScreenHandler>, BiFunction<ScreenHandler,Integer,Integer>>>
            MAPPERS = new ArrayList<>();

    static {
        // Survival inventory
        register(PlayerScreenHandler.class, (h, i) -> mapSurvival(i));

        // Generic containers of varying row count
        register(GenericContainerScreenHandler.class, (h, i) ->
                mapGeneric(i, ((GenericContainerScreenHandler)h).getRows()));
        register(ShulkerBoxScreenHandler.class, (h, i) -> mapGeneric(i, 3));

        // Furnace / Blast Furnace / Smoker share same layout
        BiFunction<ScreenHandler,Integer,Integer> furnaceMapper = (h, i) -> mapTwoSlot(i, 30, 3);
        register(FurnaceScreenHandler.class, furnaceMapper);
        register(BlastFurnaceScreenHandler.class, furnaceMapper);
        register(SmokerScreenHandler.class, furnaceMapper);

        // Crafting table (3×3)
        register(CraftingScreenHandler.class, (h, i) -> mapTwoSlot(i, 37, 1));

        // Generic 3×3 container (e.g. chest)
        register(Generic3x3ContainerScreenHandler.class, (h, i) -> mapTwoSlot(i, 36, 0));

        // Enchantment / Anvil / Villager / Cartography / Grindstone almost identical
        BiFunction<ScreenHandler,Integer,Integer> twoSlotPlusOffset1 = (h, i) -> mapTwoSlot(i, 29, 2);
        register(EnchantmentScreenHandler.class, twoSlotPlusOffset1);
        register(AnvilScreenHandler.class, twoSlotPlusOffset1);
        register(MerchantScreenHandler.class, twoSlotPlusOffset1);
        register(CartographyTableScreenHandler.class, twoSlotPlusOffset1);
        register(GrindstoneScreenHandler.class, twoSlotPlusOffset1);

        // Brewing stand
        register(BrewingStandScreenHandler.class, (h, i) -> mapTwoSlot(i, 32, 5));

        // Beacon
        register(BeaconScreenHandler.class, (h, i) -> mapTwoSlot(i, 28, 1));

        // Loom
        register(LoomScreenHandler.class, (h, i) -> mapTwoSlot(i, 31, 4));

        // Stonecutter
        register(StonecutterScreenHandler.class, (h, i) -> mapTwoSlot(i, 29, 2));

        // Hopper
        register(HopperScreenHandler.class, (h, i) -> mapTwoSlot(i, 32, 5));

        // Lectern and other single-slot GUIs have no inventory slots
        register(LecternScreenHandler.class, (h, i) -> -1);
    }

    private SlotUtils() {}

    private static void register(Class<? extends ScreenHandler> cls,
                                 BiFunction<ScreenHandler,Integer,Integer> mapper) {
        MAPPERS.add(Map.entry(cls, mapper));
    }

    /** Convert logical index -> raw slot ID, or -1 if unmapped. */
    public static int indexToId(int index) {
        ScreenHandler handler = mc.player.currentScreenHandler;
        for (var entry : MAPPERS) {
            if (entry.getKey().isInstance(handler)) {
                return entry.getValue().apply(handler, index);
            }
        }
        return -1;
    }

    // === Mapping implementations ===

    /** Survival: hotbar IDs 36+index; armor 5+(index−36); else identity. */
    private static int mapSurvival(int i) {
        if (isHotbar(i)) return 36 + i;
        if (isArmor(i))  return 5 + (i - ARMOR_START);
        return i;
    }

    /** Generic container with `rows` main rows. */
    private static int mapGeneric(int i, int rows) {
        if (isHotbar(i))    return (rows + 3) * 9 + i;
        if (isMain(i))      return rows * 9 + (i - MAIN_START);
        return -1;
    }

    /**
     * Two‐section GUI: hotbar IDs = offsetHotbar + index,
     * main IDs = offsetMain + (index − MAIN_START).
     */
    private static int mapTwoSlot(int i, int offsetHotbar, int offsetMain) {
        if (isHotbar(i))    return offsetHotbar + i;
        if (isMain(i))      return offsetMain + (i - MAIN_START);
        return -1;
    }

    // === Helpers ===

    public static boolean isHotbar(int i) { return i >= HOTBAR_START && i <= HOTBAR_END; }
    public static boolean isMain(int i)   { return i >= MAIN_START   && i <= MAIN_END; }
    public static boolean isArmor(int i)  { return i >= ARMOR_START  && i <= ARMOR_END; }
}
