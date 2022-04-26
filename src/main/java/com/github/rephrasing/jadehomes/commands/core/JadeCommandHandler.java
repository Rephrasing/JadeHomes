package com.github.rephrasing.jadehomes.commands.core;

import com.github.rephrasing.galaxy.api.commands.AbstractGalaxyCommandHandler;
import com.github.rephrasing.galaxy.api.exceptions.GalaxyInitializationException;

public class JadeCommandHandler extends AbstractGalaxyCommandHandler {

    public JadeCommandHandler() throws GalaxyInitializationException {
        super();
        registerCommands();
    }
}
