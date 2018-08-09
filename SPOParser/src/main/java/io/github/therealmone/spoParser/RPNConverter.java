package io.github.therealmone.spoParser;

import io.github.therealmone.translatorAPI.Beans.Token;

import java.util.*;

/**
 * priorities of operators:
 * <p>
 * 10 - new
 * 9 - get
 * 8 - / , *, div, mod
 * 7 - + , -
 * 6 - < , > , <= , >= , ==, !=
 * 5 - !
 * 4 - &
 * 3 - |, ^
 * 2 - ( , ), ++
 * 1 - =. typeof, put, get, remove, rewrite, print
 **/

final class RPNConverter {
    private static final Map<String, Integer> priority;
    private static Stack<String> stack;
    private static List<String> out;

    static {
        priority = new HashMap<String, Integer>() {{
            put("new", 10);
            put("get", 9);
            put("size", 9);
            put("/", 8);
            put("*", 8);
            put("div", 8);
            put("mod", 8);
            put("+", 7);
            put("-", 7);
            put("<", 6);
            put(">", 6);
            put("<=", 6);
            put(">=", 6);
            put("==", 6);
            put("!", 5);
            put("&", 4);
            put("|", 3);
            put("^", 3);
            put("(", 2);
            put(")", 2);
            put("++", 2);
            put("=", 1);
            put("typeof", 1);
            put("put", 1);
            put("remove", 1);
            put("rewrite", 1);
            put("print", 1);
        }};
    }

    static synchronized List<String> convertToRPN(final TreeNode root) {
        stack = new Stack<>();
        out = new ArrayList<>();

        lang(root);
        return out;
    }

    private static void lang(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "expr": {
                    expr(child);
                    break;
                }
                case "expr_continue": {
                    expr_continue(child);
                    break;
                }
                case "$": {
                    out.add("$");
                    break;
                }
            }
        }
    }

    private static void expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "while_loop": {
                    while_loop(child);
                    break;
                }
                case "if_statement": {
                    if_statement(child);
                    break;
                }
                case "do_while_loop": {
                    do_while_loop(child);
                    break;
                }
                case "for_loop": {
                    for_loop(child);
                    break;
                }
                case "assign_expr": {
                    assign_expr(child);
                    break;
                }
                case "init_expr": {
                    init_expr(child);
                    break;
                }
                case "put_expr": {
                    put_expr(child);
                    break;
                }
                case "remove_expr": {
                    remove_expr(child);
                    break;
                }
                case "rewrite_expr": {
                    rewrite_expr(child);
                    break;
                }
                case "print_expr": {
                    print_expr(child);
                    break;
                }
                case "DEL": {
                    while (stack.size() != 0) {
                        out.add(stack.pop());
                    }
                }
            }
        }
    }

    private static void expr_continue(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "expr": {
                    expr(child);
                    break;
                }
                case "expr_continue": {
                    expr_continue(child);
                    break;
                }
            }
        }
    }

    private static void while_loop(final TreeNode root) {
        //while(a < b) {a + b} -> (index1)ab<!F@pab+!@p(index2) ; !@Fp - to index2 , !@p - to index1
        int index1 = 0; //at a
        int index2 = 0; //after !@p
        int iteration = Thread.getAllStackTraces().hashCode();

        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "WHILE":
                    break;
                case "LB":
                    break;
                case "condition": {
                    if (out.size() == 1 && out.get(0).equals("")) {
                        index1 = 0;
                    } else {
                        index1 = out.size();
                    }
                    condition(child);
                    break;
                }
                case "RB": {
                    while (stack.size() != 0) {
                        out.add(stack.pop());
                    }
                    out.add("!F@p" + iteration);
                    break;
                }
                case "FLB":
                    break;
                case "expr_continue": {
                    expr_continue(child);
                    break;
                }
                case "FRB": {
                    out.add("!@p" + iteration);
                    index2 = out.size();
                    break;
                }
            }
        }

        out.set(out.indexOf("!F@p" + iteration), "!F@" + index2);
        out.set(out.indexOf("!@p" + iteration), "!@" + index1);

    }

    private static void if_statement(final TreeNode root) {
        //if (a < b) {a + b} -> ab<!F@pab+!@p (index1) ' else ' (index2) ; !F@p - to index1 , !@p - to index2 // if no else, then index2 = index1
        int index1 = 0;
        int index2 = 0;
        int iteration = Thread.getAllStackTraces().hashCode();

        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "IF":
                    break;
                case "LB":
                    break;
                case "condition": {
                    condition(child);
                    break;
                }
                case "RB": {
                    while (stack.size() != 0) {
                        out.add(stack.pop());
                    }
                    out.add("!F@p" + iteration);
                }
                case "FLB":
                    break;
                case "expr_continue": {
                    expr_continue(child);
                    break;
                }
                case "FRB": {
                    out.add("!@p" + iteration);
                    index1 = out.size();
                    index2 = index1;
                    break;
                }
                case "else": {
                    else_(child);
                    index2 = out.size();
                    break;
                }
            }
        }

        out.set(out.indexOf("!F@p" + iteration), "!F@" + index1);
        out.set(out.indexOf("!@p" + iteration), "!@" + index2);
    }

    private static void else_(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "ELSE":
                    break;
                case "FLB":
                    break;
                case "expr_continue": {
                    expr_continue(child);
                    break;
                }
                case "FRB":
                    break;
            }
        }
    }

    private static void assign_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "ASSIGN_OP": {
                    stack.push(child.getToken().getValue());
                    break;
                }
                case "value_expr": {
                    value_expr(child);
                    break;
                }
            }
        }
    }

    private static void do_while_loop(final TreeNode root) {
        //do {a + b} while (a < b) -> (index1)ab+ab<!@T ; !@T - to index1
        int iteration = Thread.getAllStackTraces().hashCode();
        int index1 = 0;

        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "DO":
                    break;
                case "FLB":
                    break;
                case "expr_continue": {
                    index1 = out.size();
                    expr_continue(child);
                    break;
                }
                case "FRB":
                    break;
                case "WHILE":
                    break;
                case "LB":
                    break;
                case "condition": {
                    condition(child);
                    break;
                }
                case "RB": {
                    while (stack.size() != 0) {
                        out.add(stack.pop());
                    }
                    out.add("!T@p" + iteration);
                }
            }
        }

        out.set(out.indexOf("!T@p" + iteration), "!T@" + index1);
    }

    private static void for_loop(final TreeNode root) {
        //for(i = 1; i < 1; i = i + 1) {a = 0;} -> i1= (index1) i1<!F@pa0=ii1+=!@p (index2) ; !@Fp - to index2, !@p - to index1
        int iteration = Thread.getAllStackTraces().hashCode();
        int index1; //at i
        int index2; //after !@p

        TreeNode condition = new TreeNode("");
        TreeNode assign = new TreeNode("");
        TreeNode increment = new TreeNode("");
        TreeNode expr_continue = new TreeNode("");

        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "FOR":
                    break;
                case "LB":
                    break;
                case "assign_expr": {
                    if (assign.getName().equals(""))
                        assign = child;
                    else
                        increment = child;
                    break;
                }
                case "DEL":
                    break;
                case "condition": {
                    condition = child;
                    break;
                }
                case "FLB":
                    break;
                case "expr_continue": {
                    expr_continue = child;
                    break;
                }
                case "FRB":
                    break;
            }
        }

        assign_expr(assign);
        while (stack.size() != 0) {
            out.add(stack.pop());
        }

        index1 = out.size();
        condition(condition);
        while (stack.size() != 0) {
            out.add(stack.pop());
        }
        out.add("!F@p" + iteration);

        expr_continue(expr_continue);
        while (stack.size() != 0) {
            out.add(stack.pop());
        }

        assign_expr(increment);
        while (stack.size() != 0) {
            out.add(stack.pop());
        }
        out.add("!@p" + iteration);
        index2 = out.size();

        out.set(out.indexOf("!F@p" + iteration), "!F@" + index2);
        out.set(out.indexOf("!@p" + iteration), "!@" + index1);
    }

    private static void init_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "NEW": {
                    pushOP(child.getToken());
                    break;
                }
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "init_expr_continue": {
                    init_expr_continue(child);
                    break;
                }
            }
        }
    }

    private static void init_expr_continue(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "TYPEOF": {
                    pushOP(child.getToken());
                    break;
                }
                case "type": {
                    type(child);
                    break;
                }
                case "ASSIGN_OP": {
                    pushOP(child.getToken());
                    break;
                }
                case "value_expr": {
                    value_expr(child);
                    break;
                }
            }
        }
    }

    private static void print_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "PRINT": {
                    pushOP(child.getToken());
                    break;
                }
                case "LB":
                    break;
                case "print_parameters": {
                    print_parameters(child);
                    break;
                }
                case "RB":
                    break;
            }
        }
    }

    private static void type(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "HASHSET": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "ARRAYLIST": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
            }
        }
    }

    private static void put_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "PUT": {
                    pushOP(child.getToken());
                    break;
                }
                case "LB":
                    break;
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "COMMA":
                    break;
                case "value": {
                    value(child);
                    break;
                }
                case "RB":
                    break;
            }
        }
    }

    private static void remove_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "REMOVE": {
                    pushOP(child.getToken());
                    break;
                }
                case "LB":
                    break;
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "COMMA":
                    break;
                case "value": {
                    value(child);
                    break;
                }
                case "RB":
                    break;
            }
        }
    }

    private static void rewrite_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "REWRITE": {
                    pushOP(child.getToken());
                    break;
                }
                case "LB":
                    break;
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "COMMA":
                    break;
                case "value_expr": {
                    value_expr(child);
                    break;
                }
                case "value": {
                    value(child);
                    break;
                }
                case "RB":
                    break;
            }
        }
    }


    private static void condition(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "condition_with_br": {
                    condition_with_br(child);
                    break;
                }
                case "condition_without_br": {
                    condition_without_br(child);
                    break;
                }
                case "condition_continue": {
                    condition_continue(child);
                    break;
                }
            }
        }
    }

    private static void condition_with_br(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "LB": {
                    pushOP(child.getToken());
                    break;
                }
                case "condition": {
                    condition(child);
                    break;
                }
                case "RB": {
                    pushOP(child.getToken());
                    break;
                }
            }
        }
    }

    private static void condition_without_br(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "compare_expr": {
                    compare_expr(child);
                    break;
                }
            }
        }
    }

    private static void condition_continue(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "LOP": {
                    pushOP(child.getToken());
                    break;
                }
                case "condition": {
                    condition(child);
                    break;
                }
            }
        }
    }

    private static void compare_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "value_expr": {
                    value_expr(child);
                    break;
                }
                case "COP": {
                    pushOP(child.getToken());
                    break;
                }
            }
        }
    }

    private static void value_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "value_expr_with_br": {
                    value_expr_with_br(child);
                    break;
                }
                case "value_expr_without_br": {
                    value_without_br(child);
                    break;
                }
                case "value_expr_continue": {
                    value_expr_continue(child);
                    break;
                }
            }
        }
    }

    private static void value_expr_with_br(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "LB": {
                    pushOP(child.getToken());
                    break;
                }
                case "value_expr": {
                    value_expr(child);
                    break;
                }
                case "RB": {
                    pushOP(child.getToken());
                    break;
                }
            }
        }
    }

    private static void value_without_br(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "value": {
                    value(child);
                    break;
                }
                case "value_expr_continue": {
                    value_expr_continue(child);
                    break;
                }
                case "get_expr": {
                    get_expr(child);
                    break;
                }
                case "size_expr": {
                    size_expr(child);
                    break;
                }
            }
        }
    }

    private static void value(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "VAR": {
                    out.add(checkTrace() + child.getToken().getValue());
                    break;
                }
                case "DIGIT": {
                    out.add(child.getToken().getValue());
                    break;
                }
                case "DOUBLE": {
                    out.add(child.getToken().getValue());
                    break;
                }
            }
        }
    }

    private static String checkTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String methodToCheck = trace[3].getMethodName();
        return methodToCheck.equals("put_expr")
                || methodToCheck.equals("remove_expr")
                || methodToCheck.equals("rewrite_expr")
                || methodToCheck.equals("get_expr")
                ? "#"
                : "%";
    }

    private static void value_expr_continue(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "OP": {
                    pushOP(child.getToken());
                    break;
                }
                case "value_expr": {
                    value_expr(child);
                    break;
                }
            }
        }
    }

    private static void get_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "GET": {
                    pushOP(child.getToken());
                    break;
                }
                case "LB":
                    break;
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "COMMA":
                    break;
                case "value": {
                    value(child);
                    break;
                }
                case "RB":
                    break;
            }
        }
    }


    private static void size_expr(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "SIZE": {
                    pushOP(child.getToken());
                    break;
                }
                case "LB":
                    break;
                case "VAR": {
                    out.add("#" + child.getToken().getValue());
                    break;
                }
                case "RB":
                    break;
            }
        }
    }

    private static void print_parameters(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "STRING": {
                    String value = child.getToken().getValue();
                    out.add(value);
                    break;
                }
                case "value_expr": {
                    value_expr(child);
                    break;
                }
                case "print_parameters_continue": {
                    print_parameters_continue(child);
                    break;
                }
            }
        }
    }

    private static void print_parameters_continue(final TreeNode root) {
        for (TreeNode child : root.getChildes()) {
            switch (child.getName()) {
                case "CONCAT": {
                    pushOP(child.getToken());
                    break;
                }
                case "print_parameters": {
                    print_parameters(child);
                    break;
                }
            }
        }
    }

    private static void pushOP(final Token op) {
        switch (op.getValue()) {
            case "(": {
                stack.push(op.getValue());
                break;
            }
            case ")": {
                while (!stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
                break;
            }
            default: {
                while (!stack.isEmpty() && priority.get(op.getValue()) <= priority.get(stack.peek())) {
                    out.add(stack.pop());
                }
                stack.push(op.getValue());
                break;
            }
        }
    }
}
