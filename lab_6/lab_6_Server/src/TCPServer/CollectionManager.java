package TCPServer;

import Object.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.time.*;
import java.util.*;

public class CollectionManager {
    private TreeSet<StudyGroup> groups;
    private String collectionPath;
    private File xmlCollection;
    private Date initDate;
    private Integer nowId;
    static Logger LOGGER;
    private static java.util.logging.LogManager LogManager;
    static {
        LOGGER = Logger.getLogger(CollectionManager.class.getName());
    }

    public CollectionManager(String collectionPath)  {
        File file = new File(collectionPath);
        groups = new TreeSet<StudyGroup>();
        if (file.exists()) {
            this.xmlCollection = file;
            this.collectionPath = collectionPath;
        } else {
            LOGGER.log(Level.SEVERE, "Файл по указанному пути не существует.");
            System.exit(1);
        }
        this.load();
        this.initDate = new Date();
    }

    public Integer getNowId(){
        return ++nowId;
    }

    public TreeSet<StudyGroup> getGroups() {
        return groups;
    }

    /**
     * Сериализует коллекцию в файл xml.
     */
    public void save() {
        try  {
            Document doc = new Document();
            // создаем корневой элемент с пространством имен
            doc.setRootElement(new Element("Groups"));
            // формируем JDOM документ из объектов Student
            for (StudyGroup group : groups) {
                Element element = new Element("StudyGroup");
                element.addContent(new Element("id").setText( String.valueOf(group.getId())));
                element.addContent(new Element("name").setText(group.getName()));
                Element element_c = new Element("Coordinates");
                element_c.addContent(new Element("x").setText(String.valueOf(group.getCoordinates().getX())));
                element_c.addContent(new Element("y").setText(String.valueOf(group.getCoordinates().getY())));
                element.addContent(element_c);
                element.addContent(new Element("creationDate").setText(String.valueOf(group.getCreationDate())));
                element.addContent(new Element("studentsCount").setText(String.valueOf(group.getStudentsCount())));
                element.addContent(new Element("expelledStudents").setText(String.valueOf(group.getExpelledStudents())));
                element.addContent(new Element("averageMark").setText(String.valueOf(group.getAverageMark())));
                element.addContent(new Element("Semester").setText(String.valueOf(group.getSemesterEnum())));
                Element element_d = new Element("groupAdmin");
                element_d.addContent(new Element("name").setText(group.getGroupAdmin().getName()));
                element_d.addContent(new Element("height").setText(String.valueOf(group.getGroupAdmin().getHeight())));
                element_d.addContent(new Element("weight").setText(String.valueOf(group.getGroupAdmin().getWeight())));
                if (group.getGroupAdmin().getLocation() == null) {
                    element_d.addContent(new Element("location").setText(String.valueOf(group.getGroupAdmin().getLocation())));
                } else {
                    Element element_l = new Element("location");
                    element_l.addContent(new Element("x").setText(String.valueOf(group.getGroupAdmin().getLocation().getX())));
                    element_l.addContent(new Element("y").setText(String.valueOf(group.getGroupAdmin().getLocation().getY())));
                    element_l.addContent(new Element("z").setText(String.valueOf(group.getGroupAdmin().getLocation().getZ())));
                    element_l.addContent(new Element("name").setText(group.getGroupAdmin().getLocation().getName()));
                    element_d.addContent(element_l);
                }
                element.addContent(element_d);
                doc.getRootElement().addContent(element);
            }
            if (!xmlCollection.canWrite())
                System.out.println("Файл защищён от записи. Невозможно сохранить коллекцию.");
            else{
                // Документ JDOM сформирован и готов к записи в файл
                XMLOutputter xmlWriter = new XMLOutputter(Format.getPrettyFormat());
                // сохнаряем в файл
                xmlWriter.output(doc, new FileOutputStream(xmlCollection));
                System.out.println("Коллекция успешно сохранена в файл.");
            }
        } catch (IOException ex) {
            System.out.println("Коллекция не может быть записана в файл");
        }
    }

    /**
     *  Десериализует коллекцию из файла xml.
     */
    public void load() {
        int beginSize = groups.size();
        if (!xmlCollection.exists()) {
            System.out.println("Файла по указанному пути не существует.");
            System.exit(1);
        } else if (!xmlCollection.canRead() || !xmlCollection.canWrite()) {
            System.out.println("Файл защищён от чтения и/или записи. Для работы программы нужны оба разрешения.");
            System.exit(1);
        } else {
            if (xmlCollection.length() == 0) {
                System.out.println("Файл пуст.");
                System.exit(1);
            }
            System.out.println("Идёт загрузка коллекции " + xmlCollection.getAbsolutePath());
            // мы можем создать экземпляр JDOM Document из классов DOM, SAX и STAX Builder
            try {
                org.jdom2.Document jdomDocument = createJDOMusingSAXParser(collectionPath);
                Element root = jdomDocument.getRootElement();
                // получаем список всех элементов
                List<Element> groupListElements = root.getChildren("StudyGroup");
                // список объектов Student, в которых будем хранить
                // считанные данные по каждому элементу

                int maxId = 0;
                for (Element group : groupListElements) {
                    int id = Integer.parseInt(group.getChildText("id"));
                    if (id > maxId) maxId = id;
                    String name = group.getChildText("name");
                    List<Element> lab_c = group.getChildren("Coordinates");
                    Float x = Float.parseFloat(lab_c.get(0).getChildText("x"));
                    Long y = Long.parseLong(lab_c.get(0).getChildText("y"));
                    ZonedDateTime creationDate = ZonedDateTime.parse(group.getChildText("creationDate"));
                    int studentsCount = Integer.parseInt(group.getChildText("studentsCount"));
                    long expelledStudents = Long.parseLong(group.getChildText("expelledStudents"));
                    Float averageMark = Float.parseFloat(group.getChildText("averageMark"));
                    Semester semester = Semester.valueOf(group.getChildText("Semester"));
                    List<Element> lab_d = group.getChildren("groupAdmin");
                    String nameAdmin = lab_d.get(0).getChildText("name");
                    Double height = Double.parseDouble(lab_d.get(0).getChildText("height"));
                    long weight = Long.parseLong(lab_d.get(0).getChildText("weight"));
                    Location location;
                    if (lab_d.get(0).getChildText("location").equals("null")) location = null;
                    else {
                        List<Element> lab_l = lab_d.get(0).getChildren("location");
                        double xl = Double.parseDouble(lab_l.get(0).getChildText("x"));
                        long yl = Long.parseLong(lab_l.get(0).getChildText("y"));
                        Integer zl =Integer.parseInt(lab_l.get(0).getChildText("z"));
                        String namel = lab_l.get(0).getChildText("name");
                        location = new Location(xl,yl,zl,namel);
                    }
                    Person admin = new Person(nameAdmin,height,weight,location);
                    groups.add(new StudyGroup(id, name, new Coordinates(x, y), creationDate, studentsCount, expelledStudents, averageMark, semester,admin));
                }
                System.out.println("Коллекция успешно загружена. Добавлено " + (groups.size() - beginSize) + " элементов.");
                nowId = maxId;
            } catch (IOException | JDOMException ex) {
                System.out.println("Коллекция не может быть загружена. Файл некорректен");
                System.exit(1);
            }
        }
    }
    private static org.jdom2.Document createJDOMusingSAXParser(String fileName)
            throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(new File(fileName));
    }

    /**
     * Выводит информацию о коллекции.
     */
    @Override
    public String toString() {
        return "Тип коллекции: " + groups.getClass() +
                "\nДата инициализации: " + initDate +
                "\nКоличество элементов: " + groups.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionManager)) return false;
        CollectionManager manager = (CollectionManager) o;
        return groups.equals(manager.groups) &&
                xmlCollection.equals(manager.xmlCollection) &&
                initDate.equals(manager.initDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, initDate);
    }
}