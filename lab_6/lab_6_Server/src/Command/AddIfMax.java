package Command;

import TCPServer.CollectionManager;

import Object.*;
import java.util.TreeSet;

public class AddIfMax extends Command {
    public AddIfMax(CollectionManager manager) {
        super(manager);
        setDescription("добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.");
    }

    @Override
    public String execute(Object args) {
        TreeSet<StudyGroup> groups = getManager().getGroups();
        StudyGroup studyGroup = (StudyGroup) args;
        if (groups.higher(studyGroup) == null) {
            studyGroup.setId(getManager().getNowId());
            groups.add(studyGroup);
            return "Элемент добавлен.";
        }
        return "Элемент не превосходит максимальный";
    }
}
