package com.strobel.assembler;

/**
 * User: Mike Strobel
 * Date: 1/6/13
 * Time: 2:15 PM
 */
public interface InstructionVisitor<P> {
    void visit(final P parameter, final Instruction instruction);

    void visit(final P parameter, final OpCode opCode);
    void visit(final P parameter, final OpCode opCode, final int value);
    void visit(final P parameter, final OpCode opCode, final long value);
    void visit(final P parameter, final OpCode opCode, final float value);
    void visit(final P parameter, final OpCode opCode, final double value);
    void visit(final P parameter, final OpCode opCode, final String value);
    
    void visitBranch(final P parameter, final OpCode opCode, final Instruction target);
    void visitVariable(final P parameter, final OpCode opCode, final VariableReference variable);
    void visitVariable(final P parameter, final OpCode opCode, final VariableReference variable, int operand);
    void visitType(final P parameter, final OpCode opCode, final TypeReference variable);
    void visitMethod(final P parameter, final OpCode opCode, final MethodReference variable);
    void visitField(final P parameter, final OpCode opCode, final FieldReference variable);

    void visitLabel(final P parameter, final Label label);

    void visitSwitch(final P parameter, final OpCode opCode, final SwitchInfo switchInfo);
}