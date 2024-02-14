package org.ccffee;

import org.ccffee.args.ArgumentParser;
import org.ccffee.cmd.*;
import org.ccffee.utils.*;
import org.ccffee.utils.exceptions.*;
import org.ccffee.utils.functional.*;
import org.ccffee.utils.io.*;
import org.ccffee.utils.iteration.*;
import org.ccffee.utils.tag.*;


import java.util.Collection;
import java.util.List;

public class LibsModule  {

    private static final List<Class<?>> classes;

    String getName(){
        return "Libs";
    }
    Collection<Class<?>> getClasses(){
        return classes;
    }

    static {
        classes = List.of(
                ArgumentParser.class,
                Command.class,
                CommandManager.class,
                CommandSender.class,
                ConsoleCommandListener.class,
                ICommand.class,
                InputStreamCommandListener.class,
                CreationException.class,
                ExceptionalGetter.class,
                ExceptionalRunnable.class,
                ExceptionUtils.class,
                ObjectNotFoundException.class,
                Getter.class,
                Setter.class,
                Waiter.class,
                ByteComparator.class,
                ByteConvertable.class,
                ByteConverter.class,
                ByteUtils.class,
                ClassSeeker.class,
                Data.class,
                DataLoader.class,
                FileUtils.class,
                FromByteConverter.class,
                MultipleOutputStream.class,
                MultipleSavable.class,
                Savable.class,
                SimpleByteConverter.class,
                StreamFormatException.class,
                ToByteConverter.class,
                ArrayIterator.class,
                ArrayUtils.class,
                ConversionIteration.class,
                Final2DArray.class,
                Final3DArray.class,
                Final4DArray.class,
                FinalArray.class,
                Iteration.class,
                SimpleIterable.class,
                IllegalSyntaxException.class,
                TagTree.class,
                TagTreeBuilder.class,
                DateFormat.class,
                Final.class,
                FinalInt.class,
                MathUtils.class,
                NumberUtils.class,
                Tree.class
        );
    }

}
