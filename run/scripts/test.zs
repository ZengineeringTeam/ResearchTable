import mods.ResearchTable;
import ResearchTable.Category;
import ResearchTable.Builder;

// use `/researchtable @p all 0` to clear all progress

var cat = ResearchTable.addCategory(<minecraft:grass>, "hello");
var cat2 = ResearchTable.addCategory(<minecraft:stone>);

ResearchTable.builder("testResearch1", cat) // The second parameter has no use currently
  .setIcons(<minecraft:grass>)
  .setTitle("Alchemy") // I18n support: use language key
  .setDescription("Input your description")
  .addCondition(<ore:ingotIron> * 8, <liquid:lava> * 2000)
  .setRewardStages("stage")
  .setRewardCommands("/say \"wow, @s has found a gold!\"")
  .setRewardItems(<minecraft:gold_ingot>, <minecraft:gold_nugget>)
//.setNoMaxCount()
  .build();

ResearchTable.builder("testResearch2", cat2)
  .setTitle("Energetic Wool loooooooong")
  .setIcons(<ore:plankWood>)
  .addCondition(<minecraft:wool:32767>, 2048, "Any Wool") // language key is better
  .addEnergyCondition(123456)
  .setMaxCount(2) // How many times can a player do this research?
  .build();

ResearchTable.builder("testResearch3", cat2)
  .setTitle("Produce Seller")
  .setIcons(<minecraft:bread>)
  .setRequiredResearches("testResearch1")
//.setRequiredStages("stage", "stageYouWillNeverGet")
//.setOptionalStages(2, "stage", "stage2", "stage3")
//.setOptionalResearches(1, "testResearch1", "testResearch2", "testResearch3")
  .addCondition(<minecraft:apple> * 2147483647)
  .addCondition(<minecraft:wheat> * 2147483647)
  .addCondition(<minecraft:wheat_seeds> * 2147483647)
  // CraftTweaker cannot handle item size more than Integer.MAX_VALUE, so here is another method
  .addCondition(<minecraft:carrot>, 9223372036854775807)
  .addCondition(<minecraft:potato>, 9223372036854775807)
  .addCondition(<minecraft:egg>, 9223372036854775807)
  .build();

// ItemStages Integration
mods.ItemStages.addItemStage("one", <minecraft:wool:5>);

// Scoreboard
// Run this before using:
// /scoreboard objectives add points dummy
// /scoreboard objectives setdisplay sidebar points
// /scoreboard players add @p points 1

ResearchTable.builder("testResearch4", cat)
  .setTitle("Scoreboard Test")
  .setIcons(<minecraft:command_block>)
  .setRequiredScore("points", "your.language.key", 1, 3) // inclusive
  .setTriggerCommands("/scoreboard players remove @s points 1")
  .build();

ResearchTable.scoreIndicator("points: %d", "points");

// Reskillable Integration
ResearchTable.builder("testResearch5", cat)
  .setTitle("Reskillable Test")
  .setIcons(<minecraft:command_block>)
  .setRequiredSkill("reskillable.building", 3)
  .setRewardSkill("reskillable.building")
  .build();

// Grand Economy Integration
ResearchTable.builder("testResearch6", cat)
  .setTitle("Click for Money")
  .setIcons(<minecraft:emerald>)
//  .setRequiredMoneyGE(10000)
//  .setTriggerMoneyGE(-9999)
  .setNoMaxCount()
//  .setRewardMoneyGE(10000)
  .build();