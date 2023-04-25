
package session;

import entity.Role;
import entity.Customer;
import entity.UserRoles;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserRolesFacade extends AbstractFacade<UserRoles> {
    @EJB private RoleFacade roleFacade;
    
    @PersistenceContext(unitName = "SPTV21WebShoePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRolesFacade() {
        super(UserRoles.class);
    }

    public boolean isRole(String roleName, Customer user) {
        List<String> userRoleNameList = em.createQuery("SELECT ur.role.roleName FROM UserRoles ur WHERE ur.user = :user")
                .setParameter("user", user)
                .getResultList();
        return userRoleNameList.contains(roleName);
    }
    
    public String getTopRole(Customer user) {
        List<String> listRoleNames = em.createQuery("SELECT ur.role.roleName FROM UserRoles ur WHERE ur.user = :user")
                .setParameter("user", user)
                .getResultList();
        if(listRoleNames.contains("ADMINISTRATOR"))return "ADMINISTRATOR";
        if(listRoleNames.contains("SELLER"))return "SELLER";
        if(listRoleNames.contains("CUSTOMER"))return "CUSTOMER";
        return null;
    }
    
    public void setRoleToUser(Role role, Customer user) {
        removeAllUserRoles(user);
        UserRoles userRoles = null;
        if("ADMINISTRATOR".equals(role.getRoleName())){
            Role roleREADER = roleFacade.findRoleByRoleName("CUSTOMER");
            userRoles = new UserRoles();
            userRoles.setRole(roleREADER);
            userRoles.setUser(user);
            this.create(userRoles);
            Role roleMANAGER = roleFacade.findRoleByRoleName("SELLER");
            userRoles = new UserRoles();
            userRoles.setRole(roleMANAGER);
            userRoles.setUser(user);
            this.create(userRoles);
            Role roleADMINISTRATOR = roleFacade.findRoleByRoleName("ADMINISTRATOR");
            userRoles = new UserRoles();
            userRoles.setRole(roleADMINISTRATOR);
            userRoles.setUser(user);
            this.create(userRoles);
        }
        if("SELLER".equals(role.getRoleName())){
            Role roleREADER = roleFacade.findRoleByRoleName("CUSTOMER");
            userRoles = new UserRoles();
            userRoles.setRole(roleREADER);
            userRoles.setUser(user);
            this.create(userRoles);
            Role roleMANAGER = roleFacade.findRoleByRoleName("SELLER");
            userRoles = new UserRoles();
            userRoles.setRole(roleMANAGER);
            userRoles.setUser(user);
            this.create(userRoles);
        }
        if("CUSTOMER".equals(role.getRoleName())){
            Role roleREADER = roleFacade.findRoleByRoleName("CUSTOMER");
            userRoles = new UserRoles();
            userRoles.setRole(roleREADER);
            userRoles.setUser(user);
            this.create(userRoles);
        }
        
    }
    private void removeAllUserRoles(Customer user){
        em.createQuery("DELETE FROM UserRoles ur WHERE ur.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }
}