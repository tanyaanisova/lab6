package Command;

import Object.*;
import TCPServer.CollectionManager;

import java.util.TreeSet;

/**
 * обновить значение элемента коллекции, id которого равен заданному.
 */
public class Update extends Command{
    private int id;
    public Update(CollectionManager manager, Integer id) {
        super(manager);
        this.id = id;
        setDescription("обновить значение элемента коллекции, id которого равен заданному.");
    }

    @Override
    public String execute(Object args) {
        TreeSet<StudyGroup> groups = getManager().getGroups();
        StudyGroup studyGroup = (StudyGroup) args;
        if (groups.size() != 0) {
                if (groups.stream().anyMatch(group -> group.getId() == id)) {
                    groups.stream()
                            .filter(group -> group.getId() == id)
                            .forEach(group -> {
                                groups.remove(group);
                                groups.add(studyGroup);
                            });
                    return "Элемент коллекции успешно обновлен.";
                }
                return("В коллекции не найдено элемента с указанным id.");
        } else return ("В коллекции отсутствуют элементы. Выполнение команды невозможно.");
    }
}