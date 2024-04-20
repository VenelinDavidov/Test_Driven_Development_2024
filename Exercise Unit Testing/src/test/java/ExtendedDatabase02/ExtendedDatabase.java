package ExtendedDatabase02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import p02_ExtendedDatabase.Database;
import p02_ExtendedDatabase.Person;

import javax.naming.OperationNotSupportedException;

public class ExtendedDatabase {
    private Database database;
    private static final Person[] PEOPLE = {new Person(1, "Venko"),
            new Person(2, "Raya"),
            new Person(3, "Viki")};

    @Before
    public void prepareDatabase() throws OperationNotSupportedException {
        database = new Database(PEOPLE);
    }

    //1. конструктор
    //1.1. дали създава правилен обек
    @Test
    public void testConstructorHasCorrectObject() {
        Person[] databaseElement = database.getElements();
        Assert.assertEquals("Arrays are not the same!", PEOPLE, databaseElement);
        Assert.assertEquals("Count of element is incorrect!", database.getElementsCount(), PEOPLE.length);
        Assert.assertEquals("Index is incorrect!", PEOPLE.length - 1, database.getIndex());

    }
    //1.2. създаваме обект с повече от 16 човека

    //1.2. създаваме обект с повече от 16 човека
    @Test(expected = OperationNotSupportedException.class)
    public void testConstructorThrowExceptionMoreThanSixteenElements() throws OperationNotSupportedException {
        Person[] people = new Person[17];
        new Database(people);
    }

    //1.3. създаваме обект с по-малко от 1 елемент
    @Test(expected = OperationNotSupportedException.class)
    public void testConstructorThrowExceptionWhenLessOneElement() throws OperationNotSupportedException {
        Person[] people = new Person[0];
        new Database(people);
    }
    //2. add
    //2.1. добавяме null

    @Test(expected = OperationNotSupportedException.class)
    public void testShouldTrowExceptionsNullParam() throws OperationNotSupportedException {
        database.add(null);
    }

    //2.2. успешно добавяне на елемент
    @Test
    public void testAddShouldAddElement() throws OperationNotSupportedException {
        Person[] elementBefore = database.getElements();
        // -> 3 броя
        database.add(new Person(4, "Hristo"));
        Person[] elementsAfter = database.getElements();
        // ->4

        //1. броя на елементите се е увеличил с 1
        Assert.assertEquals("Invalid add operation!", elementBefore.length + 1, elementsAfter.length);
        //2. добавен правилния елемент
        //последния ми елемент да е {4, Hristo}
        Person lastPerson = elementsAfter[elementsAfter.length - 1];
        Assert.assertEquals(lastPerson.getId(), 4);
        Assert.assertEquals(lastPerson.getUsername(), "Hristo");
    }

    //3. remove
    //3.1. успешно премахваме елемент
    @Test
    public void testRemoveLastElement() throws OperationNotSupportedException {
        Person[] elementBefore = database.getElements();
        database.remove();
        Person[] elementAfter = database.getElements();
        Assert.assertEquals("Invalid remove operation!", elementBefore.length - 1, elementAfter.length);
        Person currentLast = elementAfter[elementAfter.length - 1];
        Assert.assertEquals(currentLast.getUsername(), "Raya");
        Assert.assertEquals(currentLast.getId(), 2);
    }

    //3.2. неуспешно премахване заради ArrayIndexOutOfBoundException
    @Test(expected = OperationNotSupportedException.class)
    public void testRemoveThrowExceptionInvalidIndex() throws OperationNotSupportedException {
        for (int i = 0; i < PEOPLE.length; i++) {
            database.remove();
        }
        //празен database: {} => 0 елемента; index = -1
        database.remove();
    }

    //4. findByUsername
    //4.1. username e null
    @Test(expected = OperationNotSupportedException.class)
    public void testFindByUsernameThrowExceptionNullParam() throws OperationNotSupportedException {
        database.findByUsername(null);
    }

    //4.2. подаваме валиден username -> връщаме Person с този username
    public void testFindByUsername() throws OperationNotSupportedException {
        //database => elements: [{1, veni}, {2, Raya}, {3, Viki}] -> 3 броя
        Person person = database.findByUsername("Raya");
        //person{username: "Raya", id: 2}
        Assert.assertEquals("Invalid id of the taken person!", person.getId(), 2);
        Assert.assertEquals("Invalid username of the taken person!", person.getUsername(), "Raya");

    }

    //4.3. имаме повече от 1 човек с даденото име
    @Test(expected = OperationNotSupportedException.class)
    public void testFindByUsernameMoreThanPerson() throws OperationNotSupportedException {
        //database => elements: [{1, veni}, {2, Raya}, {3, Viki}] -> 3 броя
        database.add(new Person(4, "Viki"));
        //database => elements: [{1, Veni}, {2, Raya}, {3, Viki}, {4, Viki}] -> 4 броя
        database.findByUsername("Viki");
    }

    //4.4. нямаме човек с това име
    @Test (expected = OperationNotSupportedException.class)
    public void testFindByUsernameNonExistingUsername() throws OperationNotSupportedException {
        database.findByUsername("Pesho");
    }

    //5. findById
    //5.1. подаваме валидно id -> получаваме човек с даденото id
    @Test
    public void testFindById() throws OperationNotSupportedException {
    //database => elements: [{1, veni}, {2, Raya}, {3, Viki}] -> 3 броя
       Person  person = database.findById(3);
       Assert.assertEquals("Invalid id of the taken person!", person.getId(),3);
       Assert.assertEquals("Invalid username of the taken person!",person.getUsername(),"Viki");

    }
    //5.2. имаме повече от 1 човек с това id
    @Test (expected = OperationNotSupportedException.class)
    public void testFindByIdMoreThenOneId () throws OperationNotSupportedException {
        //database => elements: [{1, veni}, {2, Raya}, {3, Viki}] -> 3 броя
        database.add(new Person(3,"Ivan"));

        //database => elements: [[{1, veni}, {2, Raya}, {3, Viki}] , {3, Ivan}] -> 4 броя
        database.findById(3);
    }
    //5.3. нямаме човек с такова id
    @Test(expected = OperationNotSupportedException.class)
    public void testFindByIdThrowExceptionNonExistingId() throws OperationNotSupportedException {
        database.findById(4);
    }
}