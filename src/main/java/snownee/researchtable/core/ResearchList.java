package snownee.researchtable.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ResearchList
{
    public static final List<ResearchCategory> CATEGORIES = new ArrayList<>(8);
    public static final List<Research> LIST = new ArrayList<>();

    static
    {
        CATEGORIES.add(ResearchCategory.GENERAL);
    }

    private ResearchList()
    {
    }

    public static Optional<Research> find(String name)
    {
        return ResearchList.LIST.stream().filter(v -> v.getName().equals(name)).findFirst();
    }
}
