package de.ase.pcpartpicker.adapters.cli.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.ComputerDraft;
import de.ase.pcpartpicker.adapters.cli.Menu;
import de.ase.pcpartpicker.adapters.cli.MenuFactory;
import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
import de.ase.pcpartpicker.domain.CPU;
import de.ase.pcpartpicker.domain.Case;
import de.ase.pcpartpicker.domain.Component;
import de.ase.pcpartpicker.domain.GPU;
import de.ase.pcpartpicker.domain.Mainboard;
import de.ase.pcpartpicker.domain.PSU;
import de.ase.pcpartpicker.domain.RAM;
import de.ase.pcpartpicker.domain.Storage;
import de.ase.pcpartpicker.part_assembly.Bottleneck;
import de.ase.pcpartpicker.part_assembly.BottleneckResult;
import de.ase.pcpartpicker.part_assembly.Computer;

public class AutomaticFixCommand implements ICommand {
    AppContext context;
    private final Map<Class<?>, BiConsumer<ComputerDraft, Component>> componentSetters = new HashMap<>();

    public AutomaticFixCommand(AppContext context) {
        this.context = context;
        initComponentSetters();
    }

    @Override
    public void execute() {
        Computer selectedComputer = context.getSelectedComputer();
        context.computerDraft.editDraft(selectedComputer);

        BottleneckResult result = Bottleneck.calculateBottleneck(selectedComputer);

        if (result.hasBottleneck()) {
            Component bottleneck = result.bottleneckComponent();
            Component alternative = result.alternativeComponent();

            if (alternative == null) {
                ExceptionUtils.printInfo("Es wird kein Alternativteil angeboten, da dein momentanes Teil sehr stark ist.");
                context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");
            } else {
                BiConsumer<ComputerDraft, Component> setter = componentSetters.get(bottleneck.getClass());
                if (setter == null && bottleneck instanceof Storage) {
                    setter = componentSetters.get(Storage.class);
                }

                if (setter != null) {
                    setter.accept(context.computerDraft, alternative);
                    Menu configuratorMenu = new MenuFactory(context).createConfiguratorMenu();
                    configuratorMenu.setInfoMessage("Das Bottleneck wurde automatisch ersetzt. Bitte speichern Sie den Entwurf, um die Änderung zu übernehmen!");
                    configuratorMenu.execute();
                }
                else {
                    ExceptionUtils.printError("Tiel konnte nicht getauscht werden!");
                }
            }
        } else {
            ExceptionUtils.printInfo("Da der Computer kein Bottlneck hat, kann nichts automatisch ersetzt werden!");
            context.inputReader.waitForEnter("Enter drücken um zurückzukehren...");
        }
    
    }

    private void initComponentSetters() {
        componentSetters.put(CPU.class, (draft, comp) -> draft.setCpu((CPU) comp));
        componentSetters.put(GPU.class, (draft, comp) -> draft.setGpu((GPU) comp));
        componentSetters.put(RAM.class, (draft, comp) -> draft.setRam((RAM) comp, context.getSelectedComputer().getRamModule()));
        componentSetters.put(Mainboard.class, (draft, comp) -> draft.setMainboard((Mainboard) comp));
        componentSetters.put(PSU.class, (draft, comp) -> draft.setPsu((PSU) comp));
        componentSetters.put(Case.class, (draft, comp) -> draft.setComputerCase((Case) comp));
        componentSetters.put(Storage.class, (draft, comp) -> {
            List<Storage> current = context.getSelectedComputer().getStorageDevices();
            List<Storage> updated = new ArrayList<>();
            for (Storage s : current) {
                if (comp != null && s.getClass().equals(comp.getClass())) {
                    updated.add((Storage) comp);
                } else {
                    updated.add(s);
                }
            }
            draft.setStorage(updated);
        });
    }
}