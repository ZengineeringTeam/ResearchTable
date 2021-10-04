import mods.ResearchTable;
import ResearchTable.Category;
import ResearchTable.Builder;

var cat = ResearchTable.addCategory(<minecraft:grass>, "hello");

// Reskillable Integration
ResearchTable.builder("testResearch5", cat)
  .setTitle("Reskillable Test")
  .setIcons(<minecraft:command_block>)
  .setRequiredSkill("reskillable.building", 3)
  .setRewardSkill("reskillable.building")
  .build();
