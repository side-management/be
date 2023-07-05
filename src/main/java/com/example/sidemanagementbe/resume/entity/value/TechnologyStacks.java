package com.example.sidemanagementbe.resume.entity.value;

import lombok.Getter;

import java.util.List;


@Getter
public class TechnologyStacks {
    private final List<TechnologyStack> stacks;

    public TechnologyStacks(List<TechnologyStack> stacks) {
        this.stacks = stacks;
    }
}
