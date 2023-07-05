package com.example.sidemanagementbe.resume.entity.value;

import java.util.List;
import lombok.Getter;


@Getter
public class TechnologyStacks {
    private final List<TechnologyStack> stacks;

    public TechnologyStacks(List<TechnologyStack> stacks) {
        this.stacks = stacks;
    }
}
