/*
 * Expression.java
 *
 * Copyright (c) 2013 Mike Strobel
 *
 * This source code is subject to terms and conditions of the Apache License, Version 2.0.
 * A copy of the license can be found in the License.html file at the root of this distribution.
 * By using this source code in any fashion, you are agreeing to be bound by the terms of the
 * Apache License, Version 2.0.
 *
 * You must not remove this notice, or any other, from this software.
 */

package com.strobel.decompiler.languages.java.ast;

import com.strobel.core.VerifyArgument;
import com.strobel.decompiler.patterns.BacktrackingInfo;
import com.strobel.decompiler.patterns.INode;
import com.strobel.decompiler.patterns.Match;
import com.strobel.decompiler.patterns.Pattern;
import com.strobel.decompiler.patterns.Role;

public abstract class Expression extends AstNode {
    // <editor-fold defaultstate="collapsed" desc="Null Expression">

    public final static Expression NULL = new NullExpression();

    private static final class NullExpression extends Expression {
        @Override
        public final boolean isNull() {
            return true;
        }

        @Override
        public <T, R> R acceptVisitor(final IAstVisitor<? super T, ? extends R> visitor, final T data) {
            return null;
        }

        @Override
        public boolean matches(final INode other, final Match match) {
            return other == null || other.isNull();
        }
    }

    // </editor-fold>

    @Override
    public NodeType getNodeType() {
        return NodeType.EXPRESSION;
    }

    // <editor-fold defaultstate="collapsed" desc="Pattern Placeholder">

    public static Expression forPattern(final Pattern pattern) {
        return new PatternPlaceholder(VerifyArgument.notNull(pattern, "pattern"));
    }

    private final static class PatternPlaceholder extends Expression {
        final Pattern child;

        PatternPlaceholder(final Pattern child) {
            this.child = child;
        }

        @Override
        public NodeType getNodeType() {
            return NodeType.PATTERN;
        }

        @Override
        public <T, R> R acceptVisitor(final IAstVisitor<? super T, ? extends R> visitor, final T data) {
            return visitor.visitPatternPlaceholder(this, child, data);
        }

        @Override
        public boolean matchesCollection(final Role role, final INode position, final Match match, final BacktrackingInfo backtrackingInfo) {
            return child.matchesCollection(role, position, match, backtrackingInfo);
        }

        @Override
        public boolean matches(final INode other, final Match match) {
            return child.matches(other, match);
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fluent Interface">

    public InvocationExpression invoke(final Expression... arguments) {
        return new InvocationExpression(this, arguments);
    }

    public InvocationExpression invoke(final Iterable<Expression> arguments) {
        return new InvocationExpression(this, arguments);
    }

    public InvocationExpression invoke(final String methodName, final Expression... arguments) {
        return invoke(methodName, null, arguments);
    }

    public InvocationExpression invoke(final String methodName, final Iterable<Expression> arguments) {
        return invoke(methodName, null, arguments);
    }

    public InvocationExpression invoke(final String methodName, final Iterable<AstType> typeArguments, final Expression... arguments) {
        final MemberReferenceExpression mre = new MemberReferenceExpression(this, methodName, typeArguments);
        return new InvocationExpression(mre, arguments);
    }

    public InvocationExpression invoke(final String methodName, final Iterable<AstType> typeArguments, final Iterable<Expression> arguments) {
        final MemberReferenceExpression mre = new MemberReferenceExpression(this, methodName, typeArguments);
        return new InvocationExpression(mre, arguments);
    }

    public MemberReferenceExpression member(final String memberName) {
        return new MemberReferenceExpression(this, memberName);
    }

    // </editor-fold>
}