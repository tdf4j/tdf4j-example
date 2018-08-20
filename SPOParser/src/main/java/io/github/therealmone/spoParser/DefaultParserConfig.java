package io.github.therealmone.spoParser;

public class DefaultParserConfig extends AbstractParserConfig {
    @Override
    public void configure() {
        addLangRule("lang")
                .openByDefault("expr", "expr_continue", "$")
                .openWith("$", "$");

        addLangRule("expr")
                .openWith("WHILE", "while_loop")
                .openWith("FOR", "for_loop")
                .openWith("IF", "if_statement")
                .openWith("DO", "do_while_loop")
                .openWith("PRINT", "print_expr", "DEL")
                .openWith("VAR", "assign_expr", "DEL")
                .openWith("NEW", "init_expr", "DEL")
                .openWith("PUT", "put_expr", "DEL")
                .openWith("REMOVE", "remove_expr", "DEL")
                .openWith("REWRITE", "rewrite_expr", "DEL");

        addLangRule("expr_continue")
                .openByDefault("expr", "expr_continue")
                .openWith("RB", "EMPTY")
                .openWith("FRB", "EMPTY")
                .openWith("$", "EMPTY");

        addLangRule("while_loop")
                .openWith("WHILE", "WHILE", "LB", "condition", "RB", "FLB", "expr_continue", "FRB");

        addLangRule("for_loop")
                .openWith("FOR", "FOR", "LB", "assign_expr", "DEL", "condition", "DEL", "assign_expr", "RB", "FLB", "expr_continue", "FRB");

        addLangRule("if_statement")
                .openWith("IF", "IF", "LB", "condition", "RB", "FLB", "expr_continue", "FRB", "else");

        addLangRule("assign_expr")
                .openWith("VAR", "VAR", "ASSIGN_OP", "value_expr");

        addLangRule("do_while_loop")
                .openWith("DO", "DO", "FLB", "expr_continue", "FRB", "WHILE", "LB", "condition", "RB");

        addLangRule("print_expr")
                .openWith("PRINT", "PRINT", "LB", "print_parameters", "RB");

        addLangRule("put_expr")
                .openWith("PUT", "PUT", "LB", "VAR", "COMMA", "value", "RB");

        addLangRule("remove_expr")
                .openWith("REMOVE", "REMOVE", "LB", "VAR", "COMMA", "value", "RB");

        addLangRule("rewrite_expr")
                .openWith("REWRITE", "REWRITE", "LB", "VAR", "COMMA", "value", "COMMA", "value_expr", "RB");

        addLangRule("init_expr")
                .openWith("NEW", "NEW", "VAR", "init_expr_continue");

        addLangRule("init_expr_continue")
                .openByDefault("EMPTY")
                .openWith("ASSIGN_OP", "ASSIGN_OP", "value_expr")
                .openWith("TYPEOF", "TYPEOF", "type");

        addLangRule("type")
                .openWith("ARRAYLIST", "ARRAYLIST")
                .openWith("HASHSET", "HASHSET");

        addLangRule("condition")
                .openByDefault("condition_without_br", "condition_continue")
                .openWith("LB", "condition_with_br", "condition_continue");

        addLangRule("condition_with_br")
                .openByDefault("LB", "condition", "RB");

        addLangRule("condition_without_br")
                .openByDefault("compare_expr");

        addLangRule("condition_continue")
                .openByDefault("EMPTY")
                .openWith("LOP", "LOP", "condition");

        addLangRule("compare_expr")
                .openByDefault("value_expr", "COP", "value_expr");

        addLangRule("value_expr")
                .openByDefault("value_expr_without_br", "value_expr_continue")
                .openWith("LB", "value_expr_with_br", "value_expr_continue");

        addLangRule("value_expr_with_br")
                .openByDefault("LB", "value_expr", "RB");

        addLangRule("value_expr_without_br")
                .openWith("VAR", "value", "value_expr_continue")
                .openWith("DIGIT", "value", "value_expr_continue")
                .openWith("DOUBLE", "value", "value_expr_continue")
                .openWith("STRING", "value", "value_expr_continue")
                .openWith("GET", "get_expr")
                .openWith("SIZE", "size_expr");

        addLangRule("value")
                .openWith("VAR", "VAR")
                .openWith("DIGIT", "DIGIT")
                .openWith("DOUBLE", "DOUBLE")
                .openWith("STRING", "STRING");

        addLangRule("value_expr_continue")
                .openByDefault("EMPTY")
                .openWith("OP", "OP", "value_expr");

        addLangRule("else")
                .openByDefault("EMPTY")
                .openWith("ELSE", "ELSE", "FLB", "expr_continue", "FRB");

        addLangRule("get_expr")
                .openWith("GET", "GET", "LB", "VAR", "COMMA", "value", "RB", "value_expr_continue");

        addLangRule("size_expr")
                .openWith("SIZE", "SIZE", "LB", "VAR", "RB", "value_expr_continue");

        addLangRule("print_parameters")
                .openByDefault("value_expr", "print_parameters_continue");

        addLangRule("print_parameters_continue")
                .openByDefault("EMPTY")
                .openWith("CONCAT", "CONCAT", "print_parameters");
    }
}
