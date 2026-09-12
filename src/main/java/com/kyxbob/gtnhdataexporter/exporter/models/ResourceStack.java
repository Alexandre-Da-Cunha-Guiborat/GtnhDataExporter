package com.kyxbob.gtnhdataexporter.exporter.models;

public class ResourceStack {

    public ResourceStack(Resource resource, long amount) {
        _resource = resource;
        _amount = amount;
    }

    public Resource getResource() {
        return _resource;
    }

    public long getAmount() {
        return _amount;
    }

    private final Resource _resource;
    private final long _amount;

}
