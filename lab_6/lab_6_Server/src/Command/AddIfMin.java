package Command;

import TCPServer.CollectionManager;
import Object.*;
import java.util.TreeSet;

/**
 * добавить новый элемент.
 */
public class AddIfMin extends Command {
    public AddIfMin(CollectionManager manager) {
        super(manager);
        setDescription("добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.");
    }

    @Override
    public String execute(Object args) {
        TreeSet<StudyGroup> groups = getManager().getGroups();
        StudyGroup studyGroup = (StudyGroup) args;
        if (groups.lower(studyGroup) == null) {
            studyGroup.setId(getManager().getNowId());
            groups.add(studyGroup);
            return "Элемент добавлен.";
        }
        return "Элемент не меньше минимального";
    }
}