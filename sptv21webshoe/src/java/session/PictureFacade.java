
package session;

import entity.Picture;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PictureFacade extends AbstractFacade<Picture> {

    @PersistenceContext(unitName = "SPTV21WebShoePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PictureFacade() {
        super(Picture.class);
    }

}
