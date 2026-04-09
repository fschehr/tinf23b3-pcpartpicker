package de.ase.pcpartpicker.part_assembly;

import de.ase.pcpartpicker.domain.Component;

public record BottleneckResult(boolean hasBottleneck, Component bottleneckComponent, boolean isUnderperforming, Component alternativeComponent) {}
