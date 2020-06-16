package Command;

import TCPServer.CollectionManager;
import Object.*;
import java.util.TreeSet;

/**
 *  вывести среднее значение поля studentsCount для всех элементов коллекции.
 */
public class AverageOfStudentsCount extends Command {
    public AverageOfStudentsCount(CollectionManager manager) {
        super(manager);
        setDescription("вывести среднее значение поля studentsCount для всех элементов коллекции.");
    }

    @Override
    public String execute(Object args) {
        TreeSet<StudyGroup> groups = getManager().getGroups();
        if (groups.size() != 0) {
            return "Среднее значение поля studentsCount для всех элементов коллекции: " + groups.stream()
                    .mapToInt(element -> element.getStudentsCount())
                    .sum() / groups.size();
        } return "В коллекции отсутствуют элементы. Выполнение команды невозможно.";
    }
}
