package io.github.therealmone.jtrAPI;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;

import java.util.*;

public class ParserModule extends AbstractParserModule {
    private final List<String> rpn = new ArrayList<>();
    private final Stack<String> stack = new Stack<>();

    public List<String> getRpn() {
        return rpn;
    }

    @Override
    public void configure() {

        environment()
                .dependencies(
                        dependency(List.class, "rpn", rpn),
                        dependency(Stack.class, "stack", stack),
                        dependency(Map.class, "priority", new HashMap<String, Integer>() {{
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
                        }})
                )
                .code(
                        "private void pushOP(final String op) {\n" +
                                "    switch (op) {\n" +
                                "        case \"(\": {\n" +
                                "            stack.push(op);\n" +
                                "            break;\n" +
                                "        }\n" +
                                "        case \")\": {\n" +
                                "            while (!stack.peek().equals(\"(\")) {\n" +
                                "                rpn.add(stack.pop());\n" +
                                "            }\n" +
                                "            stack.pop();\n" +
                                "            break;\n" +
                                "        }\n" +
                                "        default: {\n" +
                                "            while (!stack.isEmpty() && ( (int) priority.get(op) <= (int) priority.get(stack.peek()) )) {\n" +
                                "                rpn.add(stack.pop());\n" +
                                "            }\n" +
                                "            stack.push(op);\n" +
                                "            break;\n" +
                                "        }\n" +
                                "    }\n" +
                                "}"
                );

        prod("lang")
                .is(
                        inline(
                                "rpn.clear();\n" +
                                        "stack.clear();\n"
                        ),
                        repeat(nt("expr")),
                        t("$"),
                        inline("rpn.add(\"$\");")
                );

        prod("expr")
                .then(oneOf(
                        nt("while_loop"),
                        nt("for_loop"),
                        nt("if_statement"),
                        nt("do_while_loop"),
                        nt("print_expr"),
                        nt("assign_expr"),
                        nt("init_expr"),
                        nt("put_expr"),
                        nt("remove_expr"),
                        nt("rewrite_expr")
                ));

        //while(a < b) {a + b} -> (index1)ab<!F@pab+!@p(index2) ; !@Fp - to index2 , !@p - to index1
        prod("while_loop")
                .is(
                        inline(
                                "int index1 = 0; //at a\n" +
                                        "int index2 = 0; //after !@p\n" +
                                        "int iteration = Thread.getAllStackTraces().hashCode();\n"
                        ),
                        t("WHILE"),
                        t("LB"),
                        inline(
                                "if (rpn.size() == 1 && rpn.get(0).equals(\"\")) {\n" +
                                        "    index1 = 0;\n" +
                                        "} else {\n" +
                                        "    index1 = rpn.size();\n" +
                                        "}\n"
                        ),
                        nt("condition"),
                        t("RB"),
                        inline(
                                "while (stack.size() != 0) {\n" +
                                        "    rpn.add(stack.pop());\n" +
                                        "}\n" +
                                        "rpn.add(\"!F@p\" + iteration);\n"
                        ),
                        t("FLB"),
                        repeat(nt("expr")),
                        t("FRB"),
                        inline(
                                "rpn.add(\"!@p\" + iteration);\n" +
                                        "index2 = rpn.size();\n" +
                                        "rpn.set(rpn.indexOf(\"!F@p\" + iteration), \"!F@\" + index2);\n" +
                                        "rpn.set(rpn.indexOf(\"!@p\" + iteration), \"!@\" + index1);\n"
                        )
                );

        //for(i = 1; i < 1; i = i + 1) {a = 0;} -> i1= (index1) i1<!F@pa0=ii1+=!@p (index2) ; !@Fp - to index2, !@p - to index1
        prod("for_loop")
                .is(
                        inline(
                                "int iteration = Thread.getAllStackTraces().hashCode();\n" +
                                        "int index1; //at i\n" +
                                        "int index2; //after !@p"
                        ),
                        t("FOR"),
                        t("LB"),
                        nt("assign_expr_without_del"),
                        nt("del"),
                        inline(
                                "index1 = rpn.size();\n"
                        ),
                        nt("condition"),
                        nt("del"),
                        inline(
                                "rpn.add(\"!F@p\" + iteration);\n" +
                                        "final List<String> rpnCopy = new ArrayList<>(rpn);\n" +
                                        "rpn.clear();\n"
                        ),
                        nt("assign_expr_without_del"),
                        inline(
                                "while (stack.size() != 0) {\n" +
                                        "    rpn.add(stack.pop());\n" +
                                        "}\n" +
                                        "" +
                                        "final List<String> increment = new ArrayList<>(rpn);\n" +
                                        "rpn.clear();\n" +
                                        "rpn.addAll(rpnCopy);\n"
                        ),
                        t("RB"),
                        t("FLB"),
                        repeat(nt("expr")),
                        t("FRB"),
                        inline(
                                "while (stack.size() != 0) {\n" +
                                        "    rpn.add(stack.pop());\n" +
                                        "}\n" +
                                        "" +
                                        "rpn.addAll(increment);\n" +
                                        "" +
                                        "rpn.add(\"!@p\" + iteration);\n" +
                                        "index2 = rpn.size();\n" +
                                        "rpn.set(rpn.indexOf(\"!F@p\" + iteration), \"!F@\" + index2);\n" +
                                        "rpn.set(rpn.indexOf(\"!@p\" + iteration), \"!@\" + index1);"
                        )
                );

        //if (a < b) {a + b} -> ab<!F@pab+!@p (index1) ' else ' (index2) ; !F@p - to index1 , !@p - to index2 // if no else, then index2 = index1
        prod("if_statement")
                .is(
                        inline(
                                "int index1 = 0;\n" +
                                        "int index2 = 0;\n" +
                                        "int iteration = Thread.getAllStackTraces().hashCode();\n"
                        ),
                        t("IF"),
                        t("LB"),
                        nt("condition"),
                        t("RB"),
                        inline(
                                "while (stack.size() != 0) {\n" +
                                        "    rpn.add(stack.pop());\n" +
                                        "}\n" +
                                        "rpn.add(\"!F@p\" + iteration);\n"
                        ),
                        t("FLB"),
                        repeat(nt("expr")),
                        t("FRB"),
                        inline(
                                "rpn.add(\"!@p\" + iteration);\n" +
                                        "index1 = rpn.size();\n" +
                                        "index2 = index1;"
                        ),
                        optional(nt("else_stmt")),
                        inline(
                                "if(ast.moveCursor(AST.Movement.TO_LAST_ADDED_NODE).onCursor().asNode().tag().equals(\"else_stmt\")) {\n" +
                                        "   index2 = rpn.size();\n" +
                                        "   ast.moveCursor(AST.Movement.TO_PARENT);\n" +
                                        "}\n" +
                                        "rpn.set(rpn.indexOf(\"!F@p\" + iteration), \"!F@\" + index1);\n" +
                                        "rpn.set(rpn.indexOf(\"!@p\" + iteration), \"!@\" + index2);\n"
                        )
                );

        prod("else_stmt")
                .is(
                        t("ELSE"),
                        t("FLB"),
                        repeat(nt("expr")),
                        t("FRB")
                );

        prod("assign_expr")
                .is(
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("ASSIGN_OP"),
                        inline(
                                "stack.push(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        nt("value_expr"),
                        nt("del")
                );

        prod("assign_expr_without_del")
                .is(
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("ASSIGN_OP"),
                        inline(
                                "stack.push(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        nt("value_expr")
                );

        //do {a + b} while (a < b) -> (index1)ab+ab<!@T ; !@T - to index1
        prod("do_while_loop")
                .is(
                        inline(
                                "int iteration = Thread.getAllStackTraces().hashCode();\n" +
                                        "int index1 = 0;\n"
                        ),
                        t("DO"),
                        t("FLB"),
                        inline(
                                "index1 = rpn.size();\n"
                        ),
                        repeat(nt("expr")),
                        t("FRB"),
                        t("WHILE"),
                        t("LB"),
                        nt("condition"),
                        t("RB"),
                        inline(
                                "while (stack.size() != 0) {\n" +
                                        "    rpn.add(stack.pop());\n" +
                                        "}\n" +
                                        "rpn.add(\"!T@p\" + iteration);\n" +
                                        "rpn.set(rpn.indexOf(\"!T@p\" + iteration), \"!T@\" + index1);\n"
                        )
                );

        prod("print_expr")
                .is(
                        t("PRINT"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("LB"),
                        nt("print_parameters"),
                        t("RB"),
                        nt("del")
                );

        prod("print_parameters")
                .is(
                        nt("value_expr"),
                        repeat(
                                t("CONCAT"),
                                inline(
                                        "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                ),
                                nt("print_parameters")
                        )
                );

        prod("put_expr")
                .is(
                        t("PUT"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("LB"),
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("COMMA"),
                        or(
                                group(
                                        t("VAR"),
                                        inline(
                                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("value")
                        ),
                        t("RB"),
                        nt("del")
                );

        prod("remove_expr")
                .is(
                        t("REMOVE"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("LB"),
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("COMMA"),
                        or(
                                group(
                                        t("VAR"),
                                        inline(
                                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("value")
                        ),
                        t("RB"),
                        nt("del")
                );

        prod("rewrite_expr")
                .is(
                        t("REWRITE"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("LB"),
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("COMMA"),
                        or(
                                group(
                                        t("VAR"),
                                        inline(
                                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("value")
                        ),
                        t("COMMA"),
                        nt("value_expr"),
                        t("RB"),
                        nt("del")
                );

        prod("init_expr")
                .is(
                        t("NEW"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        optional(nt("inst_expr")),
                        nt("del")
                );

        prod("inst_expr")
                .is(
                        or(
                                group(
                                        t("ASSIGN_OP"),
                                        inline(
                                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        ),
                                        nt("value_expr")
                                ),
                                group(
                                        t("TYPEOF"),
                                        inline(
                                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        ),
                                        nt("type")
                                )
                        )
                );

        prod("type")
                .is(
                        oneOf(
                                t("ARRAYLIST"),
                                t("HASHSET")
                        )
                        ,inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        )
                );

        prod("condition")
                .is(
                        or(
                                group(
                                        t("LB"),
                                        inline(
                                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        ),
                                        nt("condition"),
                                        t("RB"),
                                        inline(
                                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("compare_expr")
                        ),
                        repeat(
                                t("LOP"),
                                inline(
                                        "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                ),
                                nt("condition")
                        )
                );

        prod("compare_expr")
                .is(
                        nt("value_expr"),
                        t("COP"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        nt("value_expr")
                );

        prod("value_expr")
                .is(
                        or(
                                group(
                                        t("LB"),
                                        inline(
                                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        ),
                                        nt("value_expr"),
                                        t("RB"),
                                        inline(
                                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("value_expr_1")
                        ),
                        repeat(
                                t("OP"),
                                inline(
                                        "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                ),
                                nt("value_expr")
                        )
                );

        prod("value_expr_1")
                .is(
                        oneOf(
                                group(
                                        t("VAR"),
                                        inline(
                                                "rpn.add(\"%\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("value"),
                                nt("get_expr"),
                                nt("size_expr")
                        ),
                        repeat(
                                t("OP"),
                                inline(
                                        "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                ),
                                nt("value_expr")
                        )
                );

        prod("value")
                .is(
                        oneOf(
                                t("DIGIT"),
                                t("DOUBLE"),
                                t("STRING")
                        ),
                        inline(
                                "rpn.add(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        )
                );

        prod("get_expr")
                .is(
                        t("GET"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("LB"),
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("COMMA"),
                        or(
                                group(
                                        t("VAR"),
                                        inline(
                                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                                        )
                                ),
                                nt("value")
                        ),
                        t("RB")
                );

        prod("size_expr")
                .is(
                        t("SIZE"),
                        inline(
                                "pushOP(ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("LB"),
                        t("VAR"),
                        inline(
                                "rpn.add(\"#\" + ast.moveCursor(AST.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().token().value());\n" +
                                        "ast.moveCursor(AST.Movement.TO_PARENT);\n"
                        ),
                        t("RB")
                );

        prod("del")
                .is(
                        t("DEL"),
                        inline(
                                "while (stack.size() != 0) {\n" +
                                        "    rpn.add(stack.pop());\n" +
                                        "}"
                        )
                );
    }
}
