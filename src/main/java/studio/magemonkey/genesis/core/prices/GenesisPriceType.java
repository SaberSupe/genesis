package studio.magemonkey.genesis.core.prices;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;

import java.util.ArrayList;
import java.util.List;

public abstract class GenesisPriceType {


    public static GenesisPriceType
            Item,
            ItemAll,
            Money,
            Nothing,
            Points,
            Exp,
            ThirdCurrencyVariable;

    private static List<GenesisPriceType> types;
    private        String[]               names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();

        Item = registerType(new GenesisPriceTypeItem());
        ItemAll = registerType(new GenesisPriceTypeItemAll());
        Money = registerType(new GenesisPriceTypeMoney());
        Nothing = registerType(new GenesisPriceTypeNothing());
        Points = registerType(new GenesisPriceTypePoints());
        Exp = registerType(new GenesisPriceTypeExp());
    }

    public static GenesisPriceType registerType(GenesisPriceType type) {
        types.add(type);
        return type;
    }

    public static GenesisPriceType detectType(String s) {
        for (GenesisPriceType type : types) {
            if (type.isType(s)) {
                return type;
            }
        }
        return GenesisPriceType.Nothing;
    }

    public static List<GenesisPriceType> values() {
        return types;
    }

    public boolean isType(String s) {
        if (names != null) {
            for (String name : names) {
                if (name.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void register() {
        GenesisPriceType.registerType(this);
    }

    public String name() {
        return names[0].toUpperCase();
    }

    public void updateNames() {
        names = createNames();
    }


    public abstract Object createObject(Object o,
                                        boolean forceFinalState); // Used to transform the config input into a functional object

    public abstract boolean validityCheck(String itemName, Object o); // Used to check if the object is valid

    public abstract void enableType(); // Here you can register classes that the type depends on

    public abstract boolean hasPrice(Player p,
                                     GenesisBuy buy,
                                     Object price,
                                     ClickType clickType,
                                     boolean messageOnFailure);

    public abstract String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType);

    public abstract String getDisplayPrice(Player p, GenesisBuy buy, Object price, ClickType clickType);

    public abstract String[] createNames();

    public abstract boolean mightNeedShopUpdate();


    public boolean isPlayerDependend(GenesisBuy buy, ClickType clickType) {
        return supportsMultipliers() && ClassManager.manager.getMultiplierHandler().hasMultipliers();
    }

    public boolean overridesReward() {
        return false; // Can be overwritten
    }

    public boolean supportsMultipliers() {
        return false; // can be overwritten
    }


}
