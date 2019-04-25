package io.github.therealmone.stackmachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public interface StackMachine {
    void process(final List<String> rpn);

    static StackMachine newInstance(final AbstractStackMachineModule module) {
        return new StackMachineImpl(module);
    }

    class Context {
        private final Stack<Object> stack;
        private final Map<String, Object> variables;
        private int cursor;

        public Context() {
            this.stack = new Stack<>();
            this.variables = new HashMap<>();
            this.cursor = 0;
        }

        public Map<String, Object> getVariables() {
            return variables;
        }

        public Stack<Object> getStack() {
            return stack;
        }

        public void increaseCursor() {
            cursor++;
        }

        public void decreaseCursor() {
            cursor--;
        }

        public void setCursor(final int index) {
            cursor = index;
        }

        public int getCursor() {
            return cursor;
        }

        public void stackPush(final Object o) {
            stack.push(o);
        }

        public Object stackPop() {
            if(stack.size() != 0) {
                return stack.pop();
            }
            return null;
        }

        public Object stackPeek() {
            if(stack.size() != 0) {
                return stack.peek();
            }
            return null;
        }

        public Object stackGet(final int index) {
            if(index >= 0 && index <= stack.size()) {
                return stack.get(index);
            }
            return null;
        }
    }
}
