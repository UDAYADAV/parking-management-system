package org.squadstack.pms.processor;

public class DefaultCommandProcessor implements CommandProcessor {
    @Override
    public void process(String inputString) {
        System.out.printf("Invalid Command %s%n", inputString);
    }
}
