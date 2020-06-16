package Command;

import Object.*;
import TCPServer.CollectionManager;

import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * удалить элемент из коллекции по его id.
 */
public class RemoveById extends Command {
    public RemoveById(CollectionManager manager) {
        super(manager);
        setDescription("удалить элемент из коллекции по его id.");
    }

    @Override
    public String execute(Object args) {
        Integer id = (Integer)args;
        TreeSet<StudyGroup> groups = getManager().getGroups();
        if (groups.size() != 0) {
            if (groups.stream().anyMatch(element -> element.getId() == id)) {
                groups.stream()
                        .filter(element -> element.getId() == id)
                        .collect(Collectors.toList())
                        .forEach(groups::remove);
                return "Команда успешно выполнена.";
            }
            return("В коллекции не найдено элементов с соответствующим id.");
        } else return ("В коллекции отсутствуют элементы. Выполнение команды невозможно.");
    }
}
