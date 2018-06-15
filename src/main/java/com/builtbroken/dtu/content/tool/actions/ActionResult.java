package com.builtbroken.dtu.content.tool.actions;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public enum ActionResult
{
    /** Continue running the action, used for actions that run over several ticks */
    CONTINUE, //Will almost always return client side
    /** Return to stop the action, used to end actions */
    STOP, //Action finished
    /** Return to skip action due to invalid block or position */
    NO_RUN, //Action ran but nothing happened
    /** Return to skip the action with no trigger */
    PASS; //No action to run
}
