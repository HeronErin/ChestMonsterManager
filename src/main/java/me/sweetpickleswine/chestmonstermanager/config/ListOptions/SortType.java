package me.sweetpickleswine.chestmonstermanager.config.ListOptions;


import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;

public enum SortType implements IConfigOptionListEntry {

    NAME    ("name",    "CMM.config.by_name"),
    BY_CAT   ("cat",   "CMM.config.by_cat"),
    RANDOM   ("rand",   "CMM.config.by_rand"),

    JUST_MERGE ("just", "CMM.config.just_merge");
//    SQUARE  ("square",  "CMM.config.by_name");

    private final String configString;
    private final String translationKey;

private SortType(String configString, String translationKey)
    {
        this.configString = configString;
        this.translationKey = translationKey;
    }

    @Override
    public String getStringValue()
    {
        return this.configString;
    }

    @Override
    public String getDisplayName()
    {
        return StringUtils.translate(this.translationKey);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward)
    {
        int id = this.ordinal();

        if (forward)
        {
            if (++id >= values().length)
            {
                id = 0;
            }
        }
        else
        {
            if (--id < 0)
            {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public SortType fromString(String name)
    {
        switch (name){
            case "name":
                return NAME;
            case "cat":
                return BY_CAT;
            case "just":
                return JUST_MERGE;
            default:
                ChestMonsterManager.LOGGER.error("String of " + name + " given to fromString() SortType");
                return NAME;
        }
    }


}