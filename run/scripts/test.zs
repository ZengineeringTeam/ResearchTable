import crafttweaker.player.IPlayer;
import mods.ResearchTable;

events.onPlayerCrafted(function(event as crafttweaker.event.PlayerCraftedEvent) {
    event.player.addGameStage("stage");
});

var cat = ResearchTable.addCategory(<minecraft:grass>);

ResearchTable.builder("testResearch1", cat) // The second parameter has no use currently
  .setIcons(<minecraft:grass>)
  .addCondition(<ore:ingotIron> * 64)
  .setRewardStages("stage")
  .build();

ResearchTable.builder("testResearch2", cat)
  .setTitle("羊毛收购")
  .setIcons(<minecraft:wool:3>)
  .addCondition(<minecraft:wool:32767> * 2048)
  .setRequiredStages("stageYouWillNeverGet")
  .build();

ResearchTable.builder("testResearch3", cat)
  .setIcons(<minecraft:bread>)
  .setRequiredStages("stage")
  .addCondition(<minecraft:apple> * 1024)
  .addCondition(<minecraft:wheat> * 1024)
  .addCondition(<minecraft:wheat_seeds> * 1024)
  .build();
