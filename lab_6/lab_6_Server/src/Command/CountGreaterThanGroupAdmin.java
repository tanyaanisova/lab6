package Command;

import Object.*;
import TCPServer.CollectionManager;
import java.util.TreeSet;

/**
 * вывести количество элементов, значение поля groupAdmin которых больше заданного.
 */
public class CountGreaterThanGroupAdmin extends Command {
    public CountGreaterThanGroupAdmin(CollectionManager manager) {
        super(manager);
        setDescription("вывести количество элементов, значение поля groupAdmin которых больше заданного.");
    }

    @Override
    public String execute(Object args) {
        TreeSet<StudyGroup> groups = getManager().getGroups();
        Person groupAdmin = (Person) args;
        if (groups.size() != 0) {
            return "Количество элементов, значение поля groupAdmin которых равно " + groupAdmin.toString() + ": " +
                    groups.stream()
                            .filter(element -> element.getGroupAdmin().compareTo(groupAdmin) > 0)
                            .count();
        }
        return "В коллекции отсутствуют элементы. Выполнение команды невозможно.";
    }
}
