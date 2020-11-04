package pl.coderslab.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    // metody CRUD


    private final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";

    private static final String FIND_ALL_USERS_QUERY =
            "SELECT * FROM users";

    // metoda hashowania hasła
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user){
        try(Connection conn = DbUtil.getConnection()){ // łączymy sie z baza danych try with resources więc nie trzeba conn.close()
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);//tworzymy obiekt statment klasy preparedstatement, dzięki wywołaniu metody preparedstatement() na obiekcie conn
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getEmail());
            statement.setString(3,hashPassword(user.getPassword())); // metoda do szyfrowania hasła
//            statement.setString(3, user.getPassword());
            statement.executeUpdate();//wywołanie zapytania executeUpdate(np. dla INSERT, UPDATE, DELETE, CREATE, ALTER)
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                user.setId(resultSet.getInt(1)); // 1 to nr klumny w tablicy w tym przypadku id więc pobierze sobie kolejny nr
            }
            return user;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public void update(User user){
        try(Connection conn = DbUtil.getConnection()) { // łaczenie się z bzą danych
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY); // zapisanie do zmiennej statement naszego zapytania
            statement.setString(1, user.getUserName());// wywołanie na tej zmiennej metody setString(ustawienie na pierwszym pytajniku gettera obiektu user wartością którą chcemy nadpisać)
            statement.setString(2, user.getEmail());  // tylko te parametry które sa określone w wywołanej wczesniej metodzie read bedą sie wykonywały w metodzie update
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public User read(int userId){
        try(Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);//
            statement.setInt(1,userId); // podajemy wpisany przez użytkownika parametr id
            ResultSet resultSet = statement.executeQuery(); // zmienna resultSet przechowuje wynik naszego zapytania executeQuery np. dla SELECT
            if(resultSet.next()) {   // jeśli resultSet ma jakieś elementy z tablicy bo to jest select * wiec raczej posiada chyba że tablica jest pusta
            User user = new User(); // to tworzymy nowy obiekt user który będziemy zwracać
            user.setId(resultSet.getInt("id")); // i ustawiamy nowe dane dla tego obiektu czyli id = wynik zapytania.zwrócona wartość typu int z kolumny o nazwie id
            user.setEmail(resultSet.getString("email"));
            user.setUserName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            return user; // zwracamy nowy obiekt z pobranymi danymi
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null; // zwracamy null jeśli nie ma takiego id w tabeli
    }

    public void delete(int userId){
        try(Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1,userId);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public User[] findAll(){
        try(Connection conn = DbUtil.getConnection()) {
            User[] users = new User[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery(); // wynik zapytania
            while (resultSet.next()){ // sprawdzenie czy zapytanie ma co zwracać jak nie to return null;
                User user = new User();// tworzymy obiekt setterami ustawiamy jakie parametry z tablicy chcemy pobrać do wyświetlenia
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                users = addTOArray(user,users); // przypisujemy wywołanie metody do pustej tablicy
            }
            return users; // zwracamy tą tablice wcześniej przypisaną do wywołania metody bo inaczej zwróci nam pustą wywołaną wcześniej
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // metoda ma za zadanie przyjąć obiekt typu User określony w metodzie findAll
    // (czyli po prostu jeden wiersz tablicy, parametry dowolne zależy jakie sobie podamy w metodzie findAll)
    //oraz przyjąć tablicę również określoną w metodzie findAll i dodać do niej wczesniejszy element User i rozmiar tablicy zwiększyć o jeden
    //i następnie zwrócić nową tablicę do zmiennej oreślonej w metodzie findAll,
    // sama zmienna również będzie zwracana i wyświetli wszystkie wiesze pobrane z tablicy i będzie się to wykonywało dopóki
    // while(resultSet.next()) czyli dopóki nasze zapytanie do SQL ma co pobierac z tablicy
    private User[] addTOArray(User user, User[] users){
        User[] usersArr = Arrays.copyOf(users,users.length+1);
        usersArr[users.length] = user; // usersArr[users.length-1] = user; ???
        return usersArr;
    }



}
