package orm_playground.app;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import orm_playground.models.Movie;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class ManageMovie {

    private static SessionFactory factory;
    public static void main(String[] args) {

        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        ManageMovie MM = new ManageMovie();

        /* Add few employee records in database */
        Integer movID1 = MM.addMovie("The Exorcist", new BigDecimal("5.5"), true, 0, null, 6);
        Integer movID2 = MM.addMovie("Rosemary's Baby", new BigDecimal("6.23"), true, 0, null, 6);
        Integer movID3 = MM.addMovie("Heredity", new BigDecimal("7.23"), true, 0, null, 6);

        /* List down all the movies */
        MM.listMovies();

        /* Update movie's records */
        MM.updateMoviePrice(movID1, new BigDecimal(50));

        /* Delete an employee from the database */
        MM.deleteMovie(movID1);
        MM.deleteMovie(movID2);
        MM.deleteMovie(movID3);

        /* List down new list of the employees */
        MM.listMovies();
    }

    /* Method to CREATE an employee in the database */
    public Integer addMovie(String title, BigDecimal price, boolean available, long return_date, Integer director_id, int genre_id){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer movieID = null;

        // Example of Creating a record!
        try {
            tx = session.beginTransaction();
            Movie movie = new Movie(title, price, available, return_date, director_id, genre_id);
            movieID = (Integer) session.save(movie);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return movieID;
    }

    /* Method to  READ all the movies */
    public void listMovies( ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List movieList = session.createQuery("FROM Movie").list();
            for (Iterator iterator = movieList.iterator(); iterator.hasNext();){
                Movie movie = (Movie) iterator.next();
                System.out.print(movie);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to UPDATE price for an movie */
    public void updateMoviePrice(Integer movieID, BigDecimal price ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Movie movie = session.get(Movie.class, movieID); // Get Method
            movie.setPrice( price );
            session.update(movie);                           // Set Method
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /* Method to DELETE a movie from the records */
    public void deleteMovie(Integer EmployeeID){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Movie employee = session.get(Movie.class, EmployeeID);
            session.delete(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}
