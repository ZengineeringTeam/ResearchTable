import mods.ResearchTable;

// use `/researchtable @p all 0` to clear all progress

var cat = ResearchTable.addCategory(<minecraft:grass>);

ResearchTable.builder("testResearch1", cat) // The second parameter has no use currently
  .setIcons(<minecraft:grass>)
  .setTitle("Alchemy")
  .setDescription("Input your description")
  .addCondition(<ore:ingotIron> * 8)
  .addCondition(<liquid:lava> * 2000)
  .setRewardStages("stage")
  .setRewardCommands("/say \"wow, @s has found a gold!\"")
  .setRewardItems(<minecraft:gold_ingot>, <minecraft:gold_nugget>)
//.setNoMaxCount()
  .build();

ResearchTable.builder("testResearch2", cat)
  .setTitle("Energetic Wool")
  .setIcons(<minecraft:wool:3>)
  .addCondition(<minecraft:wool:32767> * 2048)
  .addEnergyCondition(123456)
  .setMaxCount(2) // How many times can a player do this research?
  .build();

ResearchTable.builder("testResearch3", cat)
  .setTitle("Produce Seller")
  .setIcons(<minecraft:bread>)
  .setRequiredResearches("testResearch1")
  .setRequiredStages("stage", "stageYouWillNeverGet")
  .addCondition(<minecraft:apple> * 2147483647)
  .addCondition(<minecraft:wheat> * 2147483647)
  .addCondition(<minecraft:wheat_seeds> * 2147483647)
  // CraftTweaker cannot handle item size more than Integer.MAX_VALUE, so here is another method
  .addCondition(<minecraft:carrot>, 9223372036854775807)
  .addCondition(<minecraft:potato>, 9223372036854775807)
  .addCondition(<minecraft:egg>, 9223372036854775807)
  .build();
