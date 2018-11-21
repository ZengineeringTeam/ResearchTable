package snownee.researchtable.core;

import java.util.ArrayList;
import java.util.List;

public final class ReseachList
{
    public static final List<ResearchCategory> CATEGORIES = new ArrayList<>(8);
    public static final List<Research> LIST = new ArrayList<>();

    static
    {
        CATEGORIES.add(ResearchCategory.GENERAL);
    }

    private ReseachList()
    {
    }
}
