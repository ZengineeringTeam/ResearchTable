import mods.ResearchTable;
import ResearchTable.Category;
import ResearchTable.Builder;

var cat = ResearchTable.addCategory(<minecraft:grass>, "hello"); //category icon and title (optional)

// Grand Economy Integration
ResearchTable.builder("testResearch6", cat)
  .setTitle("Click for Money")
  .setIcons(<minecraft:emerald>)
//  .setRequiredMoneyGE(10000)
//  .setTriggerMoneyGE(-9999)
  .setNoMaxCount()
//  .setRewardMoneyGE(10000)
  .build();
