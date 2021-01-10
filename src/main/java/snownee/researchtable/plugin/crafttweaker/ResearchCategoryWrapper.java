package snownee.researchtable.plugin.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import snownee.researchtable.core.ResearchCategory;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("ResearchTable.Category")
@ZenRegister
public class ResearchCategoryWrapper {
    protected final ResearchCategory category;

    public ResearchCategoryWrapper(ResearchCategory category) {
        this.category = category;
    }

}
