package Command;

import TCPServer.CollectionManager;
import Object.*;

import java.util.TreeSet;

/**
 * добавить новый элемент с заданным ключом.
 */
public class Add extends Command {
    public Add(CollectionManager manager) {
        super(manager);
        setDescription("добавить новый элемент.");
    }

    @Override
    public String execute(Object args) {
        StudyGroup studyGroup = (StudyGroup) args;
        studyGroup.setId(getManager().getNowId());
        getManager().getGroups().add(studyGroup);
        return "Элемент добавлен.";
    }
}
