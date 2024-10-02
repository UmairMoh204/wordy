package wordy.ast;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import javax.lang.model.element.ModuleElement.DirectiveVisitor;

import wordy.interpreter.EvaluationContext;

import static wordy.ast.Utils.orderedMap;

/**
 * Two expressions joined by an operator (e.g. “x plus y”) in a Wordy abstract syntax tree.
 */
public class BinaryExpressionNode extends ExpressionNode {
    public enum Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIATION
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;

    public BinaryExpressionNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs);
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }

    @Override
    public double doEvaluate(EvaluationContext context) {
        double leftValue = lhs.evaluate(context);
        double rightValue = rhs.evaluate(context);
        switch(operator) {
            case ADDITION:
                return leftValue + rightValue;
            case SUBTRACTION:
                return leftValue - rightValue;
            case MULTIPLICATION:
                return leftValue * rightValue;
            case DIVISION:
                return leftValue / rightValue;
            case EXPONENTIATION:
                return Math.pow(leftValue, rightValue);

            default:
                throw new UnsupportedOperationException(
                    "The following is an unknown operator: "
                );
        }

    }
    @Override
    public void compile(PrintWriter out) {
        if (operator != Operator.EXPONENTIATION) {
            out.print("(");
        }
        switch (operator) {
            case ADDITION:
                lhs.compile(out);
                out.print(" + ");
                rhs.compile(out);
                break;
            case SUBTRACTION:
                lhs.compile(out);
                out.print(" - ");
                rhs.compile(out);
                break;
            case MULTIPLICATION:
                lhs.compile(out);
                out.print(" * ");
                rhs.compile(out);
                break;
            case DIVISION:
                lhs.compile(out);
                out.print(" / ");
                rhs.compile(out);
                break;
            case EXPONENTIATION:
                out.print("Math.pow(");
                lhs.compile(out);
                out.print(", ");
                rhs.compile(out);
                out.print(")");
                break;
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator);
        }
        if (operator != Operator.EXPONENTIATION) {
            out.print(")");
        }
    }
}
