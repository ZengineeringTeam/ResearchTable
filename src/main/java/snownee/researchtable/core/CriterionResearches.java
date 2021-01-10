package snownee.researchtable.core;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;

public class CriterionResearches implements ICriterion {
    private final Collection<String> researches;
    private final int r;

    public CriterionResearches(Collection<String> researches, int requirement) {
        this.r = requirement > 0 ? requirement : researches.size();
        this.researches = researches;
    }

    @Override
    public boolean matches(EntityPlayer player, NBTTagCompound data) {
        int c = 0;
        for (String research : researches) {
            if (DataStorage.count(player.getGameProfile().getId(), research) > 0) {
                ++c;
            }
        }
        return c >= r;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getFailingText(EntityPlayer player, NBTTagCompound data) {
        String string = "";
        boolean first = true;
        for (String research : researches) {
            Optional<Research> result = ResearchList.find(research);
            if (!result.isPresent())
                continue;
            if (!first) {
                string += Util.color(0) + ", ";
            }
            first = false;
            if (DataStorage.count(player.getGameProfile().getId(), research) == 0) {
                string += Util.color(0xFFFF0000);
            }
            string += result.get().getTitle();
        }
        string += TextFormatting.RESET;
        if (r == researches.size()) {
            string = I18n.format(ResearchTable.MODID + ".gui.requiredResearch", string);
        } else {
            string = I18n.format(ResearchTable.MODID + ".gui.optionalResearch", string);
            string += I18n.format(ResearchTable.MODID + ".gui.of", r, researches.size());
        }
        return string;
    }

}
