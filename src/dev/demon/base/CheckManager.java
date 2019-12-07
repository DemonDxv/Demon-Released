package dev.demon.base;

import dev.demon.checks.combat.aimassist.*;
import dev.demon.checks.combat.autoclicker.*;
import dev.demon.checks.combat.killaura.*;
import dev.demon.checks.combat.reach.ReachA;
import dev.demon.checks.combat.velocity.VelocityA;
import dev.demon.checks.combat.velocity.VelocityB;
import dev.demon.checks.combat.velocity.VelocityC;
import dev.demon.checks.misc.impossible.ImpossibleA;
import dev.demon.checks.misc.impossible.ImpossibleB;
import dev.demon.checks.misc.impossible.ImpossibleC;
import dev.demon.checks.misc.timer.TimerA;
import dev.demon.checks.movement.flight.*;
import dev.demon.checks.movement.inventory.InventoryA;
import dev.demon.checks.movement.prediction.*;
import dev.demon.checks.movement.scaffold.ScaffoldA;
import dev.demon.checks.movement.scaffold.ScaffoldB;
import dev.demon.checks.movement.scaffold.ScaffoldC;
import dev.demon.Demon;
import dev.demon.checks.movement.speed.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.base
 */
public class CheckManager {
    private List<Check> checkList = Collections.synchronizedList(new ArrayList<>());

    public CheckManager() {
        loadChecks();
    }

    private void loadChecks() {

        addCheck(new AutoClickerA("AutoClicker", "A", CheckType.COMBAT, false, true));
        addCheck(new AutoClickerB("AutoClicker", "B", CheckType.COMBAT, false, true));
        addCheck(new AutoClickerC("AutoClicker", "C", CheckType.COMBAT, false, true));

        addCheck(new AimAssistA("AimAssist", "A", CheckType.COMBAT, false, true));
        addCheck(new AimAssistB("AimAssist", "B", CheckType.COMBAT, false, true));
        addCheck(new AimAssistC("AimAssist", "C", CheckType.COMBAT, false, true));
        addCheck(new AimAssistD("AimAssist", "D", CheckType.COMBAT, false, true));
        addCheck(new AimAssistE("AimAssist", "E", CheckType.COMBAT, false, true));

        addCheck(new KillauraB("Killaura", "B", CheckType.COMBAT, false, true));
        addCheck(new KillauraA("Killaura", "A", CheckType.COMBAT, false, true));
        addCheck(new KillauraC("Killaura", "C", CheckType.COMBAT, false, true));


        addCheck(new VelocityA("Velocity", "A", CheckType.COMBAT, true, true));
        addCheck(new VelocityB("Velocity", "B", CheckType.COMBAT, true, true));
        addCheck(new VelocityC("Velocity", "C", CheckType.COMBAT, false, true));

        addCheck(new InventoryA("Inventory", "A", CheckType.COMBAT, false, true));

        addCheck(new ReachA("Reach", "A", CheckType.COMBAT, true, true));


        addCheck(new ScaffoldA("Scaffold", "A", CheckType.MOVEMENT, false, true));
        addCheck(new ScaffoldB("Scaffold", "B", CheckType.MOVEMENT, false, true));
        addCheck(new ScaffoldC("Scaffold", "C", CheckType.MOVEMENT, false, true));

        addCheck(new FlightA("Flight", "A", CheckType.MOVEMENT, false, true));
        addCheck(new FlightB("Flight", "B", CheckType.MOVEMENT, false, true));
        addCheck(new FlightC("Flight", "C", CheckType.MOVEMENT, false, true));
        addCheck(new FlightD("Flight", "D", CheckType.MOVEMENT, false, true));

        addCheck(new SpeedA("Speed", "A", CheckType.MOVEMENT, false, true));
        addCheck(new SpeedB("Speed", "B", CheckType.MOVEMENT, false, true));
        addCheck(new SpeedC("Speed", "C", CheckType.MOVEMENT, false, true));
        addCheck(new SpeedD("Speed", "D", CheckType.MOVEMENT, false, true));


        addCheck(new ImpossibleA("Impossible", "A", CheckType.OTHER, false, true));
        addCheck(new ImpossibleB("Impossible", "B", CheckType.OTHER, false, true));
        addCheck(new ImpossibleC("Impossible", "C", CheckType.OTHER, false, true));

        addCheck(new TimerA("Timer", "A", CheckType.OTHER, true, true));

        addCheck(new PredictionA("Prediction", "A", CheckType.MOVEMENT, false, true));
        addCheck(new PredictionB("Prediction", "B", CheckType.MOVEMENT, true, true));
        addCheck(new PredictionC("Prediction", "C", CheckType.MOVEMENT, false, true));
        addCheck(new PredictionE("Prediction", "D", CheckType.MOVEMENT, false, true));


        registerAll();
    }

    private void registerAll() {
        checkList.parallelStream().forEach(check -> Demon.getInstance().getEventManager().registerListeners(check, Demon.getInstance()));
    }

    public void unRegisterAll() {
        checkList.parallelStream().forEach(check -> Demon.getInstance().getEventManager().unregisterListener(check));
    }

    private void addCheck(Check check) {
        if (check.isEnabled() && !checkList.contains(check)) checkList.add(check);
    }
}
