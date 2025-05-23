abstract class Expression {
    public abstract int interpret();
}

class NumberExpression extends Expression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int interpret() {
        return number;
    }
}

class MultiplicationExpression extends Expression {
    private Expression left;
    private Expression right;

    public MultiplicationExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() * right.interpret();
    }
}

class AdditionExpression extends Expression {
    private Expression left;
    private Expression right;

    public AdditionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() + right.interpret();
    }
}

class SubtractionExpression extends Expression {
    private Expression left;
    private Expression right;

    public SubtractionExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() - right.interpret();
    }
}

public class Interpreter {
    public static void main(String[] args) {
        Expression expression = new MultiplicationExpression(
            new AdditionExpression(new NumberExpression(5), new NumberExpression(3)),
            new SubtractionExpression(new NumberExpression(10), new NumberExpression(2))
        );

        int result = expression.interpret();
        System.out.println(result);
    }
}
