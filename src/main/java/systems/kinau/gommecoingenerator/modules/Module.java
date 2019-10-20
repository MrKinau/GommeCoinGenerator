/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.modules;

import systems.kinau.gommecoingenerator.GommeCoinGenerator;

public abstract class Module {

    private boolean enabled = false;

    public void enable() {
        this.enabled = true;
        onEnable();
        GommeCoinGenerator.getLog().info("Module \"" + this.getClass().getSimpleName() + "\" enabled!");
    }

    public void disable() {
        this.enabled = false;
        onDisable();
        GommeCoinGenerator.getLog().info("Module \"" + this.getClass().getSimpleName() + "\" disabled!");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public abstract void onEnable();

    public abstract void onDisable();

}
