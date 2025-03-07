import org.apache.log4j.Logger;
import ru.nsu.Factory;
import ru.nsu.context.Context;
import ru.nsu.exceptons.FactoryConfigurationException;
import ru.nsu.instructions.Instruction;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;


public class Interpreter {
    static final Logger logger = Logger.getLogger(Interpreter.class);
    private static Factory factory;
    private static Context context;
    private static final HashMap<Character, Instruction> instructions = new HashMap<>();


    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            logger.warn("No input file to execute");
            return;
        }
        if (!initialConfigure(args[0])) {
            logger.error("Configuration failed, terminating...");
            return;
        }


        while (true) {
            Character instructionCharValue = context.getCurrentInstruction();
            Instruction currentInstruction = instructions.get(instructionCharValue);

            if (currentInstruction == null) {
                instructions.put(instructionCharValue,
                        factory.createInstruction(instructionCharValue));
                currentInstruction = instructions.get(instructionCharValue);
            }

            try {
                logger.trace(String.format(
                        "Executing %s, %s",
                        currentInstruction.toString(),
                        context.instructionPointerToStr()
                ));
                if (currentInstruction != null) {
                    if (!currentInstruction.exec(context, instructionCharValue)) {
                        logger.info("Terminating...");
                        break;
                    }
                } else {
                    logger.error("Further execution is impossible, terminating ...");
                }

            } catch (NoSuchElementException e) {
                logger.error("Trying to pop a value from empty stack, further execution is impossible, terminating... ");
                return;
            }

            context.step();
            logger.trace(context.toString());
            if (logger.isDebugEnabled()) {

            }
        }
    }

    private static boolean initialConfigure(String playfieldPath) {
        logger.debug("Initializing started");
        try {
            factory = new Factory();
        } catch (FactoryConfigurationException e) {
            logger.error("Can't configure factory");
            return false;
        }

        try {
            context = new Context(playfieldPath);
        } catch (IOException e) {
            logger.error("Can't configure execution context");
            return false;
        }
        return true;
    }
}
