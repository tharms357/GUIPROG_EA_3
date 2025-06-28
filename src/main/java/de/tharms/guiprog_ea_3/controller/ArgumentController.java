package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Constants;

public class ArgumentController
{
    String filepath;

    public ArgumentController(String[] args)
    {
        if (args.length == 0)
        {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        else if (args.length != Constants.ALLOWED_ARGS)
        {
            throw new IllegalArgumentException(Constants.INVALID_NUMBER_OF_ARGS);
        }
        else
        {
            filepath = Constants.DEFAULT_FILEPATH + args[Constants.INDEX_ZERO];
        }
    }

    public String getFilepath()
    {
        return filepath;
    }
}
