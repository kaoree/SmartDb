package com.dagger.interfaces;

import java.util.List;

import com.dagger.operator.BaseBuilder;

public interface RequestHandler {
    public List<Object> handler(BaseBuilder statementBuilder);
}
