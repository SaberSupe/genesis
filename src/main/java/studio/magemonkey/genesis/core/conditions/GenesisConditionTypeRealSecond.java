package studio.magemonkey.genesis.core.conditions;

import org.bukkit.entity.Player;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;

import java.util.Calendar;

public class GenesisConditionTypeRealSecond extends GenesisConditionTypeNumber {

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"realsecond", "second"};
    }


    @Override
    public void enableType() {
    }


}
