package net.solmey.eslium.config;

import net.solmey.eslium.config.predictions.Cooldown;
import net.solmey.eslium.config.predictions.Crystal;
import net.solmey.eslium.config.predictions.Minecart;

public class Config {

    public boolean enabled = true;
    public int version = 1;
    //public boolean advancedMode; // anticheat bypass toggle
    public Crystal crystal = new Crystal();
    public Minecart minecart = new Minecart();
    public Cooldown cooldown = new Cooldown();
    public int simulatedDesync = 50; // Percentage
}
