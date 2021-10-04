public abstract class AST {
};

abstract class Expr extends AST {
    abstract public Double eval(Environment env);
}

class Addition extends Expr {
    Expr e1, e2;

    Addition(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Double eval(Environment env) {
        return e1.eval(env) + e2.eval(env);
    }
}

class Multiplication extends Expr {
    Expr e1, e2;

    Multiplication(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Double eval(Environment env) {
        return e1.eval(env) * e2.eval(env);
    }
}

class Constant extends Expr {
    Double d;

    Constant(Double d) {
        this.d = d;
    }

    public Double eval(Environment env) {
        return d;
    }
}

class Variable extends Expr {
    String varname;

    Variable(String varname) {
        this.varname = varname;
    }

    public Double eval(Environment env) {
        return env.getVariable(varname);
    }
}

abstract class Command extends AST {
    abstract public void eval(Environment env);
}

// Do nothing command
class NOP extends Command {
    public void eval(Environment env) {
    };
}

class Sequence extends Command {
    Command c1, c2;

    Sequence(Command c1, Command c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public void eval(Environment env) {
        c1.eval(env);
        c2.eval(env);
    }
}

class Assignment extends Command {
    String v;
    Expr e;

    Assignment(String v, Expr e) {
        this.v = v;
        this.e = e;
    }

    public void eval(Environment env) {
        Double d = e.eval(env);
        env.setVariable(v, d);
    }
}

class Output extends Command {
    Expr e;

    Output(Expr e) {
        this.e = e;
    }

    public void eval(Environment env) {
        Double d = e.eval(env);
        System.out.println(d);
    }
}

class While extends Command {
    Condition c;
    Command body;

    While(Condition c, Command body) {
        this.c = c;
        this.body = body;
    }

    public void eval(Environment env) {
        while (c.eval(env))
            body.eval(env);
    }
}

abstract class Condition extends AST {
    abstract public Boolean eval(Environment env);
}

class Conditional extends Condition {
    Expr e1, e2;
    String comparison;

    Conditional(Expr e1, Expr e2, String comparison) {
        this.e1 = e1;
        this.e2 = e2;
        this.comparison = comparison;
    }

    public Boolean eval(Environment env) {
        switch (comparison) {
            case "==":
                return e1.eval(env).equals(e2.eval(env));
            case "!=":
                return !e1.eval(env).equals(e2.eval(env));
            case ">=":
                return e1.eval(env).doubleValue() >= (e2.eval(env)).doubleValue();
            case "<=":
                return e1.eval(env).doubleValue() <= (e2.eval(env)).doubleValue();
            case ">":
                return e1.eval(env).doubleValue() > (e2.eval(env)).doubleValue();
            case "<":
                return e1.eval(env).doubleValue() < (e2.eval(env)).doubleValue();
            default:
                return false;
        }
    }
}

class IfStatement extends Command {
    Condition c;
    Command body;
    Command elseBody;

    IfStatement(Condition c, Command body, Command elseBody) {
        this.c = c;
        this.body = body;
        this.elseBody = elseBody;
    }

    public void eval(Environment env) {
        if (c.eval(env))
            body.eval(env);
        else
            elseBody.eval(env);
    }
}

class ForLoopStuff extends Command {
    LoopStuff l;
    Command body;

    ForLoopStuff(LoopStuff l, Command body) {
        this.l = l;
        this.body = body;
    }

    // for(i=2..n)
    public void eval(Environment env) {
        var start = l.num;
        var end = l.num1;
        if (l.idx != null)
            start = env.getVariable(l.idx);
        if (l.idx1 != null)
            end = env.getVariable(l.idx1);

        env.setVariable(l.name, start);
        while (env.getVariable(l.name) < end) { // TODO: Is end inclusive?
            body.eval(env);
            env.setVariable(l.name, env.getVariable(l.name) + 1);
        }
    }
}

class LoopStuff extends AST {
    String name;
    String idx;
    String idx1;
    double num;
    double num1;

    public LoopStuff(String name, String idx, String idx1, double num, double num1) {
        this.name = name;
        this.idx = idx;
        this.idx1 = idx1;
        this.num = num;
        this.num1 = num1;
    }
}
