package ru.therealmone.spoParser;

import ru.therealmone.translatorAPI.Token;
import ru.therealmone.translatorAPI.Visitable;
import ru.therealmone.translatorAPI.Visitor;

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

final class OPNConverter implements Visitable {
    private static Map<String, Integer> priority = new HashMap<>();
    private static Stack<String> stack = new Stack<>();
    private static StringBuilder out = new StringBuilder();

    static String convertToOPN(TreeNode root) {
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
        priority.put("typeof", 0);
        priority.put("put", 0);
        priority.put("get", 6);
        priority.put("remove", 0);
        priority.put("rewrite", 0);

        lang(root);
        return out.toString();

    }

    @Override
    public void accept(Visitor v) {
        v.visit(out.toString());
    }

    private static void lang(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "expr" : {expr(child); break;}
                case "expr_continue" : {expr_continue(child); break;}
                case "$" : {out.append("$"); break;}
            }
        }
    }

    private static void expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "while_loop" : {while_loop(child); break;}
                case "if_statement" : {if_statement(child); break;}
                case "do_while_loop" : {do_while_loop(child); break;}
                case "for_loop" : {for_loop(child); break;}
                case "assign_expr" : {assign_expr(child); break;}
                case "init_expr" : {init_expr(child); break;}
                case "put_expr" : {put_expr(child); break;}
                case "remove_expr" : {remove_expr(child); break;}
                case "rewrite_expr" : {rewrite_expr(child); break;}
                case "DEL" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop()).append(",");
                    }
                }
            }
        }
    }

    private static void expr_continue(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "expr" : {expr(child); break;}
                case "expr_continue" : {expr_continue(child); break;}
            }
        }
    }

    private static void while_loop(TreeNode root) {
        //while(a < b) {a + b} -> (index1)ab<!F@pab+!@p(index2) ; !@Fp - to index2 , !@p - to index1
        int index1 = 0; //at a
        int index2 = 0; //after !@p
        int iteration = Thread.getAllStackTraces().hashCode();

        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "WHILE" : break;
                case "LB" : break;
                case "condition" : {
                    index1 = out.toString().split(",").length - 1;
                    condition(child);
                    break;
                }
                case "RB" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop()).append(",");
                    }
                    out.append("!F@p").append(iteration).append(",");
                    break;
                }
                case "FLB" : break;
                case "expr_continue" : {
                    expr_continue(child);
                    break;
                }
                case "FRB" : {
                    out.append("!@p").append(iteration).append(",");
                    index2 = out.toString().split(",").length;
                    break;
                }
            }
        }

        out.replace(out.indexOf("!F@p" + iteration) + 3, out.indexOf("!F@p" + iteration) + 4 + String.valueOf(iteration).length(), "" + index2);
        out.replace(out.indexOf("!@p" + iteration) + 2, out.indexOf("!@p" + iteration) + 3 + String.valueOf(iteration).length(), "" + index1);

    }

    private static void if_statement(TreeNode root) {
        //if (a < b) {a + b} -> ab<!F@pab+!@p (index1) ' else ' (index2) ; !F@p - to index1 , !@p - to index2 // if no else, then index2 = index1
        int index1 = 0;
        int index2 = 0;
        int iteration = Thread.getAllStackTraces().hashCode();

        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "IF" : break;
                case "LB" : break;
                case "condition" : {
                    condition(child);
                    break;
                }
                case "RB" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop()).append(",");
                    }
                    out.append("!F@p").append(iteration).append(",");
                }
                case "FLB" : break;
                case "expr_continue" : {expr_continue(child); break;}
                case "FRB" : {
                    out.append("!@p").append(iteration).append(",");
                    index1 = out.toString().split(",").length;
                    index2 = index1;
                    break;
                }
                case "else" : {
                    else_(child);
                    index2 = out.toString().split(",").length;
                    break;
                }
            }
        }

        out.replace(out.indexOf("!F@p" + iteration) + 3, out.indexOf("!F@p" + iteration) + 4 + String.valueOf(iteration).length(), "" + index1);
        out.replace(out.indexOf("!@p" + iteration) + 2, out.indexOf("!@p" + iteration) + 3 + String.valueOf(iteration).length(), "" + index2);
    }

    private static void else_(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "ELSE" : break;
                case "FLB" : break;
                case "expr_continue" : {expr_continue(child); break;}
                case "FRB" : break;
            }
        }
    }

    private static void assign_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "VAR" : {out.append("#").append(child.getToken().getValue()).append(","); break;}
                case "ASSIGN_OP" : {stack.push(child.getToken().getValue()); break;}
                case "value_expr" : {value_expr(child); break;}
            }
        }
    }

    private static void do_while_loop(TreeNode root) {
        //do {a + b} while (a < b) -> (index1)ab+ab<!@T ; !@T - to index1
        int iteration = Thread.getAllStackTraces().hashCode();
        int index1 = 0;

        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "DO" : break;
                case "FLB" : break;
                case "expr_continue" : {
                    index1 = out.toString().split(",").length - 1;
                    expr_continue(child);
                    break;
                }
                case "FRB" : break;
                case "WHILE" : break;
                case "LB" : break;
                case "condition" : {condition(child); break;}
                case "RB" : {
                    while (stack.size() != 0) {
                        out.append(stack.pop()).append(",");
                    }
                    out.append("!T@p").append(iteration).append(",");
                }
            }
        }

        out.replace(out.indexOf("!T@p" + iteration) + 3, out.indexOf("!T@p" + iteration) + 4 + String.valueOf(iteration).length(), "" + index1);
    }

    private static void for_loop(TreeNode root) {
        //for(i = 1; i < 1; i = i + 1) {a = 0;} -> i1= (index1) i1<!F@pa0=ii1+=!@p (index2) ; !@Fp - to index2, !@p - to index1
        int iteration = Thread.getAllStackTraces().hashCode();
        int index1 = 0; //at i
        int index2 = 0; //after !@p

        TreeNode condition = new TreeNode("");
        TreeNode assign = new TreeNode("");
        TreeNode increment = new TreeNode("");
        TreeNode expr_continue = new TreeNode("");

        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "FOR" : break;
                case "LB" : break;
                case "assign_expr" : {
                    if(assign.getName().equals(""))
                        assign = child;
                    else
                        increment = child;
                    break;
                }
                case "DEL" : break;
                case "condition" : {condition = child; break;}
                case "FLB" : break;
                case "expr_continue" : {expr_continue = child; break;}
                case "FRB" : break;
            }
        }

        assign_expr(assign);
        while (stack.size() != 0) {
            out.append(stack.pop()).append(",");
        }

        index1 = out.toString().split(",").length;
        condition(condition);
        while (stack.size() != 0) {
            out.append(stack.pop()).append(",");
        }
        out.append("!F@p").append(iteration).append(",");

        expr_continue(expr_continue);
        while (stack.size() != 0) {
            out.append(stack.pop()).append(",");
        }

        assign_expr(increment);
        while (stack.size() != 0) {
            out.append(stack.pop()).append(",");
        }
        out.append("!@p").append(iteration).append(",");
        index2 = out.toString().split(",").length;

        out.replace(out.indexOf("!F@p" + iteration) + 3, out.indexOf("!F@p" + iteration) + 4 + String.valueOf(iteration).length(), "" + index2);
        out.replace(out.indexOf("!@p" + iteration) + 2, out.indexOf("!@p" + iteration) + 3 + String.valueOf(iteration).length(), "" + index1);
    }

    private static void init_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "NEW" : {break;}
                case "VAR" : {out.append("#").append(child.getToken().getValue()).append(","); break;}
                case "init_expr_continue" : {init_expr_continue(child); break;}
            }
        }
    }

    private static void init_expr_continue(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "TYPEOF" : {pushOP(child.getToken()); break;}
                case "type" : {type(child); break;}
                case "ASSIGN_OP" : {pushOP(child.getToken()); break;}
                case "value_expr" : {value_expr(child); break;}
            }
        }
    }

    private static void type(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "HASHMAP" : {out.append(child.getToken().getValue()).append(","); break;}
            }
        }
    }

    private static void put_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "PUT" : {pushOP(child.getToken());}
                case "LB" : break;
                case "VAR" : {out.append("#").append(child.getToken().getValue()).append(","); break;}
                case "COMMA" : break;
                case "RB" : break;
            }
        }
    }

    private static void remove_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "REMOVE" : {pushOP(child.getToken()); break;}
                case "LB" : break;
                case "VAR" : {out.append("#").append(child.getToken().getValue()).append(","); break;}
                case "COMMA" : break;
                case "RB" : break;
            }
        }
    }

    private static void rewrite_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "REWRITE" : pushOP(child.getToken());
                case "LB" : break;
                case "VAR" : {out.append("#").append(child.getToken().getValue()).append(","); break;}
                case "COMMA" : break;
                case "value_expr" : {value_expr(child); break;}
                case "RB" : break;
            }
        }
    }


    private static void condition(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "condition_with_br" : {condition_with_br(child); break;}
                case "condition_without_br" : {condition_without_br(child); break;}
                case "condition_continue" : {condition_continue(child); break;}
            }
        }
    }

    private static void condition_with_br(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch(child.getName()) {
                case "LB" : {pushOP(child.getToken()); break;}
                case "condition" : {condition(child); break;}
                case "RB" : {pushOP(child.getToken()); break;}
            }
        }
    }

    private static void condition_without_br(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "compare_expr" : {compare_expr(child); break;}
            }
        }
    }

    private static void condition_continue(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "LOP" : {pushOP(child.getToken()); break;}
                case "condition" : {condition(child); break;}
            }
        }
    }

    private static void compare_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "value_expr" : {value_expr(child); break;}
                case "COP" : {pushOP(child.getToken()); break;}
            }
        }
    }

    private static void value_expr(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "value_expr_with_br" : {value_expr_with_br(child); break;}
                case "value_expr_without_br" : {value_without_br(child); break;}
                case "value_expr_continue" : {value_expr_continue(child); break;}
            }
        }
    }

    private static void value_expr_with_br(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "LB" : {pushOP(child.getToken()); break;}
                case "value_expr" : {value_expr(child); break;}
                case "RB" : {pushOP(child.getToken()); break;}
            }
        }
    }

    private static void value_without_br(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "value" : {value(child); break;}
                case "value_expr_continue" : {value_expr_continue(child); break;}

                case "GET" : {pushOP(child.getToken()); break;}
                case "LB" : break;
                case "VAR" : {out.append("#").append(child.getToken().getValue()).append(","); break;}
                case "COMMA" : break;
            }
        }
    }

    private static void value(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
                case "VAR" : {out.append(child.getToken().getValue()).append(","); break;}
                case "DIGIT" : {out.append(child.getToken().getValue()).append(","); break;}
            }
        }
    }

    private static void value_expr_continue(TreeNode root) {
        for(TreeNode child: root.getChildes()) {
            switch (child.getName()) {
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
                    out.append(stack.pop()).append(",");
                }
                stack.pop();
                break;
            }
            default: {
                if(stack.size() != 0 && priority.get(op.getValue()) <= priority.get(stack.peek())) {
                    try {
                        while (priority.get(op.getValue()) <= priority.get(stack.peek())) {
                            out.append(stack.pop()).append(",");
                        }
                    } catch (EmptyStackException e) {}
                }
                stack.push(op.getValue());
                break;
            }
        }
    }
}
