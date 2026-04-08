package de.ase.pcpartpicker.adapters.cli.commands;

import de.ase.pcpartpicker.adapters.cli.AppContext;
import de.ase.pcpartpicker.adapters.cli.Renderable;
import de.ase.pcpartpicker.adapters.cli.utils.ExceptionUtils;
import de.ase.pcpartpicker.domain.Component;
import de.ase.pcpartpicker.part_assembly.Bottleneck;
import de.ase.pcpartpicker.part_assembly.BottleneckResult;
import de.ase.pcpartpicker.part_assembly.Computer;

public class RunBottleneckCheckCommand implements Renderable {
    private final AppContext context;

    public RunBottleneckCheckCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void render(String title) {
        Computer selectedComputer = context.getSelectedComputer();
        BottleneckResult result = Bottleneck.calculateBottleneck(selectedComputer);

        if (!result.hasBottleneck()) {
            ExceptionUtils.printSuccess("Der Computer hat keine Bottlenecks!\n");
            return;
        }

        Component bottleneckComponent = result.bottleneckComponent();
        Component alternativeComponent = result.alternativeComponent();

        if (bottleneckComponent == null) {
            ExceptionUtils.printInfo("Es konnte kein Bottleneck-Bauteil bestimmt werden.\n");
            return;
        }

        String componentType = bottleneckComponent.getClass().getSimpleName();
        String componentName = bottleneckComponent.getName();
        String componentManufacturer = bottleneckComponent.getManufacturer().getName();

        if (alternativeComponent == null) {
            ExceptionUtils.printInfo("Das Bauteil [" + componentType + "] " + componentName + " ist sehr stark und nicht unbedingt für deinen Computer nötig.\n");
            return;
        }

        String alternativeName = alternativeComponent.getName();
        String alternativeManufacturer = alternativeComponent.getManufacturer().getName();

        if (result.isUnderperforming()) {
            printResult(componentType, componentManufacturer, componentName, alternativeManufacturer, alternativeName);
        }
    }

    private void printResult(String componentType, String componentManufacturer, String componentName, String alternativeManufacturer, String alternativeName) {
        ExceptionUtils.printWarning("Problem erkannt:\nDas Bauteil vom Typ [" + componentType + "] " + componentManufacturer + " " + componentName + " ist zu schwach.\n");
        System.out.println("Empfehlung: Tausche gegen [" + componentType + "] " + alternativeManufacturer + " " + alternativeName + ".\n");
    }
}