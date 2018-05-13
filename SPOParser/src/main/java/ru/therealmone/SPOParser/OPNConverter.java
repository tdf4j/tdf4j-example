package ru.therealmone.SPOParser;

import ru.therealmone.TranslatorAPI.Node;
import ru.therealmone.TranslatorAPI.Token;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/* 5 - / , *
*  4 - + , -
*  3 - < , > , <= , >= , ==
*  2 - & , | , ^
*  1 - ( , )
*  0 - =
* */

public final class OPNConverter {
    private static Map<String, Integer> priority = new HashMap<>();
    private static Stack<String> stack = new Stack<>();
    private static StringBuilder out = new StringBuilder();

    public static String convertToOPN(Node root) {
        priority.clear();
        stack.clear();
        out.delete(0, out.length());

        priority.put("/", 5);
        priority.put("*", 5);
        priority.put("+", 4);
        priority.put("-", 4);
        priority.put("<", 3);
        priority.put(">", 3);
        priority.put("<=", 3);
        priority.put(">=", 3);
        priority.put("==", 3);
        priority.put("&", 2);
        priority.put("|", 2);
        priority.put("^", 2);
        priority.put("(", 1);
        priority.put(")", 1);
        priority.put("=", 0);

        lang(root);
        return out.toString();

    }

    private static void lang(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "expr" : {expr(child); break;}
                case "expr_continue" : {expr_continue(child); break;}
                case "$" : {out.append("$"); break;}
            }
        }
    }

    private static void expr(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "while_loop" : {while_loop(child); break;}
                case "if_statement" : {if_statement(child); break;}
                case "do_while_loop" : {do_while_loop(child); break;}
                case "assign_expr" : {assign_expr(child); break;}
                case "DEL" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop());
                    }
                }
            }
        }
    }

    private static void expr_continue(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "expr" : {expr(child); break;}
                case "expr_continue" : {expr_continue(child); break;}
            }
        }
    }

    private static void while_loop(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "WHILE" : break;
                case "LB" : break;
                case "condition" : {condition(child); break;}
                case "RB" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop());
                    }
                    out.append("!F@p");
                }
                case "FLB" : break;
                case "expr_continue" : {expr_continue(child); out.append("!@p"); break;}
            }
        }
    }

    private static void if_statement(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "IF" : break;
                case "LB" : break;
                case "condition" : {condition(child); break;}
                case "RB" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop());
                    }
                    out.append("!F@p");
                }
                case "FLB" : break;
                case "expr_continue" : {expr_continue(child); break;}
                case "FRB" : break;
                case "else" : {else_(child); break;}
            }
        }
    }

    private static void else_(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "ELSE" : {out.append("!@p"); break;}
                case "FLB" : break;
                case "expr_continue" : {expr_continue(child); break;}
                case "FRB" : break;
            }
        }
    }

    private static void assign_expr(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "VAR" : {out.append(child.getToken().getValue()); break;}
                case "ASSIGN_OP" : {stack.push(child.getToken().getValue()); break;}
                case "value_expr" : {value_expr(child); break;}
            }
        }
    }

    private static void do_while_loop(Node root) {

    }

    private static void condition(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "condition_with_br" : {condition_with_br(child); break;}
                case "condition_without_br" : {condition_without_br(child); break;}
                case "condition_continue" : {condition_continue(child); break;}
            }
        }
    }

    private static void condition_with_br(Node root) {
        for(Node child: root.getChildes()) {
            switch(child.getValue()) {
                case "LB" : {pushOP(child.getToken()); break;}
                case "condition" : {condition(child); break;}
                case "RB" : {pushOP(child.getToken()); break;}
            }
        }
    }

    private static void condition_without_br(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "compare_expr" : {compare_expr(child); break;}
            }
        }
    }

    private static void condition_continue(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "LOP" : {pushOP(child.getToken()); break;}
                case "condition" : {condition(child); break;}
            }
        }
    }

    private static void compare_expr(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "value_expr" : {value_expr(child); break;}
                case "COP" : {pushOP(child.getToken()); break;}
            }
        }
    }

    private static void value_expr(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "value_expr_with_br" : {value_expr_with_br(child); break;}
                case "value_expr_without_br" : {value_without_br(child); break;}
                case "value_expr_continue" : {value_expr_continue(child); break;}
            }
        }
    }

    private static void value_expr_with_br(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "LB" : {pushOP(child.getToken()); break;}
                case "value_expr" : {value_expr(child); break;}
                case "RB" : {pushOP(child.getToken()); break;}
            }
        }
    }

    private static void value_without_br(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "value" : {value(child); break;}
                case "value_expr_continue" : {value_expr_continue(child); break;}
            }
        }
    }

    private static void value(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "VAR" : {out.append(child.getToken().getValue()); break;}
                case "DIGIT" : {out.append(child.getToken().getValue()); break;}
            }
        }
    }

    private static void value_expr_continue(Node root) {
        for(Node child: root.getChildes()) {
            switch (child.getValue()) {
                case "OP" : {pushOP(child.getToken()); break;}
                case "value_expr" : {value_expr(child); break;}
            }
        }
    }

    private static void pushOP(Token op) {
        switch (op.getValue()) {
            case "(" : {stack.push(op.getValue()); break;}
            case ")" : {
                while (!stack.peek().equals("(")) {
                    out.append(stack.pop());
                }
                stack.pop();
                break;
            }
            default: {
                if(stack.size() != 0 && priority.get(op.getValue()) <= priority.get(stack.peek())) {
                    try {
                        while (priority.get(op.getValue()) <= priority.get(stack.peek())) {
                            out.append(stack.pop());
                        }
                    } catch (EmptyStackException e) {}
                }
                stack.push(op.getValue());
                break;
            }
        }
    }
}
