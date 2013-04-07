/*
 * ObjectCreationExpression.java
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

import com.strobel.decompiler.patterns.INode;
import com.strobel.decompiler.patterns.Match;

public class ObjectCreationExpression extends Expression {
    public final static TokenRole NEW_KEYWORD_ROLE = new TokenRole("new", TokenRole.FLAG_KEYWORD);

    public ObjectCreationExpression() {
    }

    public ObjectCreationExpression(final AstType type) {
        setType(type);
    }

    public ObjectCreationExpression(final AstType type, final Iterable<Expression> arguments) {
        setType(type);

        if (arguments != null) {
            for (final Expression argument : arguments) {
                addChild(argument, Roles.ARGUMENT);
            }
        }
    }

    public ObjectCreationExpression(final AstType type, final Expression... arguments) {
        setType(type);

        if (arguments != null) {
            for (final Expression argument : arguments) {
                addChild(argument, Roles.ARGUMENT);
            }
        }
    }

    public final AstNodeCollection<Expression> getArguments() {
        return getChildrenByRole(Roles.ARGUMENT);
    }

    public final JavaTokenNode getNewToken() {
        return getChildByRole(NEW_KEYWORD_ROLE);
    }

    public final AstType getType() {
        return getChildByRole(Roles.TYPE);
    }

    public final void setType(final AstType type) {
        setChildByRole(Roles.TYPE, type);
    }

    public final JavaTokenNode getLeftParenthesisToken() {
        return getChildByRole(Roles.LEFT_PARENTHESIS);
    }

    public final JavaTokenNode getRightParenthesisToken() {
        return getChildByRole(Roles.RIGHT_PARENTHESIS);
    }

    @Override
    public <T, R> R acceptVisitor(final IAstVisitor<? super T, ? extends R> visitor, final T data) {
        return visitor.visitObjectCreationExpression(this, data);
    }

    @Override
    public boolean matches(final INode other, final Match match) {
        if (other instanceof ObjectCreationExpression) {
            final ObjectCreationExpression otherExpression = (ObjectCreationExpression) other;

            return !otherExpression.isNull() &&
                   getType().matches(otherExpression.getType(), match) &&
                   getArguments().matches(otherExpression.getArguments(), match);
        }

        return false;
    }
}