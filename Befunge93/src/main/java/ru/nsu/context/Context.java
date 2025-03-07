package ru.nsu.context;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Execution context of interpreter, contains instruction pointer, playfield and stack, including API for operating
 * with them.
 */
public class Context {

    private static final Logger logger = Logger.getLogger(Context.class);

    private boolean skipNextInstruction = false;

    private PrintStream output = System.out;
    private InputStream input = System.in;
    private Deque<Integer> stack;
    private InstructionPointer instructionPointer;
    private Playfield playfield;

    /**
     * Constructs execution context with empty playfield (ONLY FOR TESTING)
     */
    public Context() {
        stack = new ArrayDeque<>();
        instructionPointer = new InstructionPointer();
        playfield = new Playfield();
    }

    /**
     * Constructs execution context and loads playfield (code) from input file
     *
     * @param playfieldPath path of executing .bfg file
     * @throws IOException thrown if error occurs while operating with input file
     */
    public Context(String playfieldPath) throws IOException {
        logger.debug("Initializing execution context...");
        stack = new ArrayDeque<>();
        instructionPointer = new InstructionPointer();
        playfield = PlayfieldLoader.loadField(playfieldPath);
    }

    public void setOutputStream(PrintStream output) {
        this.output = output;
    }

    public void setInputStream(InputStream input) {
        this.input = input;
    }

    public void setDirection(Direction direction) {
        instructionPointer.setDirection(direction);
    }

    public void setSkipTrue() {
        skipNextInstruction = true;
    }

    public void step() {
        instructionPointer.step();
        if (skipNextInstruction) {
            instructionPointer.step();
            skipNextInstruction = false;
        }
    }

    public InputStream getInput() {
        return input;
    }

    public PrintStream getOutput() {
        return output;
    }

    public Direction getDirection() {
        return instructionPointer.getDirection();
    }

    public Character getCurrentInstruction() {
        return playfield.getInstruction(instructionPointer.getRow(), instructionPointer.getColumn());
    }

    public Character getInstruction(Integer row, Integer column) {
        return playfield.getInstruction(row, column);
    }

    public Integer getRow() {
        return instructionPointer.getRow();
    }

    public Integer getColumn() {
        return instructionPointer.getColumn();
    }

    public void setInstruction(Integer row, Integer column, Character instruction) {
        playfield.setInstruction(row, column, instruction);
    }

    public void push(Integer value) {
        stack.push(value);
    }

    public Integer pop() throws NoSuchElementException {
        return stack.pop();
    }

    public Integer getStackTop() {
        return stack.getFirst();
    }

    public String instructionPointerToStr() {
        return instructionPointer.toString();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        stack.forEach((item) -> {
            buffer.append(item.toString());
            buffer.append(" ");
        });
        return buffer.toString();
    }
}
