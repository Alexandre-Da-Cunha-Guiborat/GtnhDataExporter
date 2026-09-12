package com.kyxbob.gtnhdataexporter.exporter.models;

import java.util.List;

public class Recipe {

    public Recipe(String id, List<ResourceStack> inputs, List<ResourceStack> outputs) {
        _id = id;
        _inputs = inputs;
        _outputs = outputs;
    }

    public String getId() {
        return _id;
    }

    public List<ResourceStack> getInput() {
        return _inputs;
    }

    public List<ResourceStack> getOutput() {
        return _outputs;
    }

    private final String _id;
    private final List<ResourceStack> _inputs;
    private final List<ResourceStack> _outputs;
}
