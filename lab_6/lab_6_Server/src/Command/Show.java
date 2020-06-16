package Command;

import TCPServer.CollectionManager;

import java.util.*;
import java.util.stream.Collectors;

import Object.*;

/**
 * вывести в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class Show extends Command {
    public Show(CollectionManager manager) {
        super(manager);
        setDescription("вывести в стандартный поток вывода все элементы коллекции в строковом представлении.");
    }

    @Override
    public String execute(Object args) {
        TreeSet<StudyGroup> groups = getManager().getGroups();
        if (groups.size() != 0) {
            return groups.stream()
                    .sorted(Comparator.comparing(element -> (element.getName())))
                    .map(element -> element.toString())
                    .collect(Collectors.joining("\n\n"));
        }
        else return "В коллекции отсутствуют элементы. Выполнение команды невозможно.";
    }
}
