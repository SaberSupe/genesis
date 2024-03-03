package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSMultiplier;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultiplierHandler {

    private Set<BSMultiplier> multipliers = new HashSet<BSMultiplier>();

    public MultiplierHandler(BossShop plugin) {
        if (plugin.getConfig().getBoolean("MultiplierGroups.Enabled") == false) {
            return;
        }
        List<String> lines = plugin.getConfig().getStringList("MultiplierGroups.List");
        if (lines == null) {
            return;
        }
        setup(plugin, lines);
    }

    public void setup(BossShop plugin, List<String> configLines) {
        multipliers.clear();
        for (String s : configLines) {
            BSMultiplier m = new BSMultiplier(s);
            if (m.isValid()) {
                multipliers.add(m);
            }
        }
    }


    public String calculatePriceDisplayWithMultiplier(Player p,
                                                      BSBuy buy,
                                                      ClickType clickType,
                                                      double d,
                                                      String message) {
        BSPriceType t = buy.getPriceType(clickType);
        return calculatePriceDisplayWithMultiplier(p,
                buy,
                clickType,
                d,
                message,
                MathTools.getFormatting(t),
                MathTools.isIntegerValue(t));
    }

    public String calculatePriceDisplayWithMultiplier(Player p,
                                                      BSBuy buy,
                                                      ClickType clickType,
                                                      double d,
                                                      String message,
                                                      List<String> formatting,
                                                      boolean integerValue) {
        d = calculatePriceWithMultiplier(p, buy, clickType, d);

        if (buy.getRewardType(clickType) == BSRewardType.ItemAll) {
            if (ClassManager.manager.getSettings().getItemAllShowFinalReward() && p != null) {
                ItemStack i     = (ItemStack) buy.getReward(clickType);
                int       count = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, i);

                if (count == 0) {
                    return ClassManager.manager.getMessageHandler()
                            .get("Display.ItemAllEach")
                            .replace("%value%",
                                    message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
                }

                d *= count;
            } else {
                return ClassManager.manager.getMessageHandler()
                        .get("Display.ItemAllEach")
                        .replace("%value%",
                                message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
            }
        }

        return message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue));
    }

    public double calculatePriceWithMultiplier(Player p, BSBuy buy, ClickType clickType, double d) {
        return calculatePriceWithMultiplier(p, buy.getPriceType(clickType), d);
    }

    public double calculatePriceWithMultiplier(Player p, BSPriceType priceType, double d) { //Used for prices
        for (BSMultiplier m : multipliers) {
            d = m.calculateValue(p, priceType, d, BSMultiplier.RANGE_PRICE_ONLY);
        }
        return MathTools.round(d, 2);
    }


    public String calculateRewardDisplayWithMultiplier(Player p,
                                                       BSBuy buy,
                                                       ClickType clickType,
                                                       double d,
                                                       String message) {
        BSPriceType t = BSPriceType.detectType(buy.getRewardType(clickType).name());
        return calculateRewardDisplayWithMultiplier(p,
                buy,
                clickType,
                d,
                message,
                MathTools.getFormatting(t),
                MathTools.isIntegerValue(t));
    }

    public String calculateRewardDisplayWithMultiplier(Player p,
                                                       BSBuy buy,
                                                       ClickType clickType,
                                                       double d,
                                                       String message,
                                                       List<String> formatting,
                                                       boolean integerValue) {
        d = calculateRewardWithMultiplier(p, buy, clickType, d);

        if (buy.getPriceType(clickType) == BSPriceType.ItemAll) {
            if (ClassManager.manager.getSettings().getItemAllShowFinalReward() && p != null) {
                ItemStack i     = (ItemStack) buy.getPrice(clickType);
                int       count = ClassManager.manager.getItemStackChecker().getAmountOfSameItems(p, i, buy);

                if (count == 0) {
                    return ClassManager.manager.getMessageHandler()
                            .get("Display.ItemAllEach")
                            .replace("%value%",
                                    message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
                }

                d *= count;
            } else {
                return ClassManager.manager.getMessageHandler()
                        .get("Display.ItemAllEach")
                        .replace("%value%",
                                message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
            }
        }

        return message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue));
    }

    public double calculateRewardWithMultiplier(Player p,
                                                BSBuy buy,
                                                ClickType clickType,
                                                double d) { //Used for reward; Works the other way around
        return calculateRewardWithMultiplier(p, buy.getRewardType(clickType), d);
    }

    public double calculateRewardWithMultiplier(Player p,
                                                BSRewardType rewardtype,
                                                double d) { //Used for reward; Works the other way around
        for (BSMultiplier m : multipliers) {
            d = m.calculateValue(p, BSPriceType.detectType(rewardtype.name()), d, BSMultiplier.RANGE_REWARD_ONLY);
        }
        return MathTools.round(d, 2);
    }


    public Set<BSMultiplier> getMultipliers() {
        return multipliers;
    }

    public boolean hasMultipliers() {
        if (multipliers == null) {
            return false;
        }
        return !multipliers.isEmpty();
    }


}
