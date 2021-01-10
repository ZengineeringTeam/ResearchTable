package snownee.researchtable.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class ResearchList {
    public static final List<ResearchCategory> CATEGORIES = Lists.newLinkedList();
    public static final Map<String, Research> LIST = Maps.newLinkedHashMap();

    private ResearchList() {
    }

    public static synchronized boolean add(Research research) {
        if (LIST.containsKey(research.getName())) {
            return false;
        }
        if (!CATEGORIES.contains(research.getCategory())) {
            CATEGORIES.add(research.getCategory());
        }
        LIST.put(research.getName(), research);
        return true;
    }

    public static Optional<Research> find(String name) {
        return Optional.ofNullable(ResearchList.LIST.get(name));
    }
}
