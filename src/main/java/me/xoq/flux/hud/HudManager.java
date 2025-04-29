package me.xoq.flux.hud;

import me.xoq.flux.utils.misc.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HudManager {
    private static final HudManager INSTANCE = new HudManager();
    private final Map<String, HudEntry> entries = new ConcurrentHashMap<>();

    public static HudManager get() { return INSTANCE; }

    public void register(HudEntry entry) {
        entries.put(Utils.titleToName(entry.getId()), entry);
    }

    public List<HudEntry> getEnabledEntries() {
        return entries.values().stream()
                .filter(HudEntry::isEnabled)
                .sorted(Comparator.comparingInt(HudEntry::getPriority))
                .toList();
    }

    public Collection<HudEntry> getAllEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }
}