import mods.ResearchTable;

var cat = ResearchTable.addCategory(<minecraft:grass>);

ResearchTable.builder("testResearch1", cat) // The second parameter has no use currently
  .setIcons(<minecraft:grass>)
  .setTitle("Hot Topic")
  .addCondition(<ore:ingotIron> * 8)
  .setRewardStages("stage")
  .build();

ResearchTable.builder("testResearch2", cat)
  .setTitle("Any Wools")
  .setIcons(<minecraft:wool:3>)
  .addCondition(<minecraft:wool:32767> * 2048)
  .setRequiredStages("stageYouWillNeverGet")
  .build();

ResearchTable.builder("testResearch3", cat)
  .setTitle("Produce Seller")
  .setIcons(<minecraft:bread>)
  .setRequiredStages("stage")
  .addCondition(<minecraft:apple> * 2147483647)
  .addCondition(<minecraft:wheat> * 2147483647)
  .addCondition(<minecraft:wheat_seeds> * 2147483647)
  .addCondition(<minecraft:carrot> * 2147483647)
  .addCondition(<minecraft:potato> * 2147483647)
  .addCondition(<minecraft:egg> * 2147483647)
  .build();
