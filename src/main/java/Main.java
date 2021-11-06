import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Frog frog = new Frog();

        List<FrogCommand> commands = new ArrayList<>();
        int curCommand = -1;
        
        paintField(frog);

        while (true) {
            //считываем ввод и конструируем комманду, если
            //пользователь не хотел выйти
            outputCommand();
            System.out.println("Введите команду:");
            String newCommand = scanner.nextLine();

            switch (newCommand) {
                case "0":
                    System.out.println("Завершение программы...");
                    return;
                case "<<":
                    if (undoCommand(commands, curCommand)) {
                        curCommand--;
                    }
                    break;
                case ">>":
                    if (redoCommand(commands, curCommand)) {
                        curCommand++;
                    }
                    break;
                case "!!":
                    if (repeatCommand(commands, curCommand)) {
                        curCommand++;
                    }
                    break;
                default:
                    FrogCommand frogCommand;
                    try {
                        frogCommand = tryParseCommand(newCommand, frog);
                    } catch (Exception e) {
                        System.out.println("Неизвестная команда");
                        break;
                    }
                    if (doCommand(commands, curCommand, frogCommand)) {
                        curCommand++;
                    }
                    break;
            }

            // рисуем поле после команды
            paintField(frog);
        }
    }

    private static void outputCommand() {
        System.out.println("+N - прыгни на N шагов направо");
        System.out.println("-N - прыгни на N шагов налево");
        System.out.println("<< - Undo (отмени последнюю команду)");
        System.out.println(">> - Redo (повтори отменённую команду)");
        System.out.println("!! - повтори последнюю команду");
        System.out.println("0 - выход");
    }

    private static boolean undoCommand(List<FrogCommand> commands, int curCommand) {
        if (curCommand < 0) {
            System.out.println("Нечего отменять!");
            return false;
        } else {
            commands.get(curCommand).undo();
            return true;
        }
    }

    private static boolean redoCommand(List<FrogCommand> commands, int curCommand) {
        if (curCommand == commands.size() - 1) {
            System.out.println("Нечего повторять!");
            return false;
        } else {
            curCommand++;
            commands.get(curCommand).doit();
            return true;
        }
    }

    private static boolean repeatCommand(List<FrogCommand> commands, int curCommand) {
        if (curCommand < 0) {
            System.out.println("Нечего повторять!");
            return false;
        } else {
            deleteRedundantCommands(commands, curCommand);
            FrogCommand frogCommand = commands.get(curCommand);
            commands.add(frogCommand);
            frogCommand.doit();
            return true;
        }
    }

    private static void deleteRedundantCommands(List<FrogCommand> commands, int curCommand) {
        if (curCommand != commands.size() - 1) {
            // Удаляем все команды которые были отменены
            commands.subList(curCommand + 1, commands.size()).clear();
        }
    }

    private static FrogCommand tryParseCommand(String newCommand, Frog frog) {
        if (newCommand.length() < 2) {
            throw new RuntimeException("Неизвестная команда");
        }
        String sing = newCommand.substring(0, 1);
        int steps = Integer.parseInt(newCommand.substring(1));

        switch (sing) {
            case "-":
                return FrogCommands.jumpLeftCommand(frog, steps);
            case "+":
                return FrogCommands.jumpRightCommand(frog, steps);
            default:
                throw new RuntimeException("Неизвестная команда");
        }
    }

    private static boolean doCommand(List<FrogCommand> commands, int curCommand, FrogCommand frogCommand) {
        deleteRedundantCommands(commands, curCommand);
        if (frogCommand.doit()) {
            commands.add(frogCommand);
            return true;
        } else {
            System.out.println("Не удалось выполнить команду");
            return false;
        }
    }

    private static void paintField(Frog frog) {
        for (int i = Frog.MIN_POSITION; i <= Frog.MAX_POSITION; i++) {
            if (i == frog.position) {
                System.out.print("x");
            } else {
                System.out.print("_");
            }
        }
        System.out.println();
    }
}
