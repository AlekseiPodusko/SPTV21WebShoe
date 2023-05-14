/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Picture;
import entity.UserRoles;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import session.PictureFacade;

/**
 *
 * @author user
 */
@WebServlet(name = "UploadServlet", urlPatterns = {
    "/addPicture", 
    "/uploadPicture"
})
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @EJB PictureFacade pictureFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if(session == null){
            request.setAttribute("info", "У вас нет прав, авторизуйтесь!");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        UserRoles mapUsers = (UserRoles) session.getAttribute("user");
        if(mapUsers == null || !mapUsers.getRole().contains(CustomerServlet.Role.CUSTOMER.toString())){
            request.setAttribute("info", "У вас нет прав, авторизуйтесь!");
            request.getRequestDispatcher("/showLogin").forward(request, response);
            return;
        }
        String uploadFolder = "C:\\Users\\user\\UploadDir\\SPTV21WebShoe";
        String path = request.getServletPath();
        switch (path) {
            case "/addPicture":
                request.getRequestDispatcher("/WEB-INF/products/addPicture.jsp").forward(request, response);
                break;
            case "/uploadPicture":
                List<Part> fileParts = request.getParts().stream().filter(
                        part -> "file".equals(part.getName()))
                        .collect(Collectors.toList());
                StringBuilder sb = new StringBuilder();
                for (Part filePart : fileParts) {
                    sb.append(uploadFolder + File.separator + getFileName(filePart));
                    File file = new File(sb.toString());
                    file.mkdirs();
                    try(InputStream fileContent = filePart.getInputStream()){
                        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    Picture picture = new Picture();
                    picture.setDescription(request.getParameter("description"));
                    picture.setPathToFile(sb.toString());
                    pictureFacade.create(picture);
                }
                request.setAttribute("info", "Файлы успешно загруженны");
                request.getRequestDispatcher("/addProduct").forward(request, response);
                break;
            
        }   
        
        
    }
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")){
            if(content.trim().startsWith("filename")){
                return content
                        .substring(content.indexOf('=')+1)
                        .trim()
                        .replace("\"",""); 
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
