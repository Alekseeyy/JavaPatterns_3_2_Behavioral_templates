public class FrogCommands {

    // возвращаете объект команды, у которого
    // если вызвать .doit(), то лягушка её выполнит,
    // если вызвать .undo(), то лягушка её отменит
    public static FrogCommand jumpRightCommand(Frog frog, int steps) {
        return new FrogCommand() {

            @Override
            public boolean doit() {
                return frog.jump(steps);
            }

            @Override
            public boolean undo() {
                return frog.jump(-steps);
            }
        };
    }

    public static FrogCommand jumpLeftCommand(Frog frog, int steps) {
        return new FrogCommand() {

            @Override
            public boolean doit() {
                return frog.jump(-steps);
            }

            @Override
            public boolean undo() {
                return frog.jump(steps);
            }
        };
    }
}
