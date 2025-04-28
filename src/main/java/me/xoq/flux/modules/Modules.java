package me.xoq.flux.modules;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import me.xoq.flux.FluxClient;
import me.xoq.flux.events.EventHandler;
import me.xoq.flux.events.KeyEvent;
import me.xoq.flux.modules.impl.AntiBreak;
import me.xoq.flux.modules.impl.AutoFish;
import me.xoq.flux.modules.impl.AutoTool;
import me.xoq.flux.modules.impl.FpsDisplay;
import me.xoq.flux.utils.input.Input;
import me.xoq.flux.utils.input.KeyAction;
import me.xoq.flux.utils.input.Keybind;
import me.xoq.flux.utils.misc.Utils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static me.xoq.flux.FluxClient.mc;

public class Modules {
    private static final Modules INSTANCE = new Modules();

    private static final List<Module> MODULES = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> moduleInstances = new Reference2ReferenceOpenHashMap<>();

    private Module moduleToBind;

    public static Modules get() {
        return INSTANCE;
    }

    public static void init() {
        // register(new TestModule());

        register(new AntiBreak());
        register(new AutoFish());
        register(new AutoTool());
        register(new FpsDisplay());


        FluxClient.EVENT_BUS.register(INSTANCE);
    }

    public static <T extends Module> void register(T module) {
        MODULES.add(module);
        INSTANCE.moduleInstances.put(module.getClass(), module);
    }

    public static Module getByName(String name) {
        return MODULES.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public static List<Module> getAll() {
        return Collections.unmodifiableList(MODULES);
    }

    // Binding
    public void setModuleToBind(Module module) {
        this.moduleToBind = module;
        moduleToBind.info("Press the new bind for " + module.getName());
    }

    @EventHandler
    private void onKeyEvent(KeyEvent event) {
        // ignore in GUIs or when F3 is down
        if (mc.currentScreen != null || Input.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        // ---- Binding flow ----
        if (moduleToBind != null) {
            // capture the next press (or clear on Escape)
            if (event.action == KeyAction.Press) {
                if (event.key == GLFW.GLFW_KEY_ESCAPE) {
                    moduleToBind.keybind.set(Keybind.none());
                    moduleToBind.info("Bind cleared.");
                } else {
                    moduleToBind.keybind.set(true, event.key, event.modifiers);
                    moduleToBind.info("Bound to " + Utils.getKeyName(moduleToBind.keybind.getValue()) + ".");
                }
                moduleToBind = null;
            }
            event.cancel();
            return;
        }

        // ---- Normal toggle-on-key behavior ----
        if (event.action == KeyAction.Press) {
            for (Module module : moduleInstances.values()) {
                if (module.keybind.matches(true, event.key, event.modifiers)) {
                    module.toggle();
                    event.cancel();
                    break;
                }
            }
        }
    }
}