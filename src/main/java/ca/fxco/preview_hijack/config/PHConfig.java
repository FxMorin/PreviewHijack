package ca.fxco.preview_hijack.config;

import ca.fxco.preview_hijack.utils.FullAction;
import ca.fxco.preview_hijack.utils.FullCondition;

public class PHConfig {

    @Weight(5)
    public boolean enabled = true;

    @Weight(4)
    public boolean hideChangesFromPlayer = false;

    @Weight(3)
    public FullCondition[] conditions = null; // If all conditions match

    @Weight(2)
    public FullAction[] actions = null;

    public PHConfig validateOnLoad() { // Always runs when config is loaded from file
        String testString = "aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890,<.>/?'\";:\\|]}[{=+-_)(*&^%$#@!~`";
        if (this.conditions != null)
            for (FullCondition condition : this.conditions)
                condition.getCheck().validateOption(testString, condition.getArgs());
        if (this.actions != null)
            for (FullAction action : this.actions)
                action.getStep().validateOption(testString, action.getArgs());
        return this;
    }
}
