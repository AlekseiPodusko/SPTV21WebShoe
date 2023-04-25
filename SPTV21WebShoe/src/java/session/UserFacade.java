
package session;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "SPTV21WebShoePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(Customer.class);
    }
    
    public Customer findByLogin(String login){
        try {
            return (Customer) em.createQuery("SELECT u FROM User u WHERE u.login=:login").setParameter("login", login).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
