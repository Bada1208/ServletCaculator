package servlets;

import calc.CalcOperations;
import calc.OperationType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CalcServlet extends HttpServlet {
    private ArrayList<String> listOperations = new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet CalcServlet</title>");
        out.println("</head>");
        out.println("<body>");

        try {

            // считывание параметров
            double one = Double.valueOf(request.getParameter("one").toString()).doubleValue();
            double two = Double.valueOf(request.getParameter("two").toString()).doubleValue();
            String opearation = String.valueOf(request.getParameter("operation"));

            // определение или создание сессии
            HttpSession session = request.getSession(true);

            // получение типа операции
            OperationType operType = OperationType.valueOf(opearation.toUpperCase());

            // калькуляция
            double result = calcResult(operType, one, two);

            // для новой сессии создаем новый список
            if (session.isNew()) {
                listOperations.clear();
            }
//            else { // иначе получаем список из атрибутов сессии
//                listOperations = (ArrayList<String>) session.getAttribute("formula");
//            }

            // добавление новой операции в список и атрибут сессии
            listOperations.add(one + " " + operType.getStringValue() + " " + two + " = " + result);
            session.setAttribute("formula", listOperations);


            // вывод всех операций
            out.println("<h1>ID вашей сессии равен: " + session.getId() + "</h1>");
            out.println("<h3>Список операций (всего:" + listOperations.size() + ") </h3>");

            for (String oper : listOperations) {
                out.println("<h3>" + oper + "</h3>");
            }


        } catch (Exception ex) {
           response.sendError(HttpServletResponse.SC_BAD_REQUEST);

        } finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
        return "Сервлет - калькулятор";
    }// </editor-fold>

    // калькуляция
    private double calcResult(OperationType operationType, double one, double two) {
        double result = 0;
        switch (operationType) {
            case ADD: {
                result = CalcOperations.add(one, two);
                break;
            }
            case SUBTRACT: {
                result = CalcOperations.subtract(one, two);
                break;
            }
            case DIVIDE: {
                result = CalcOperations.divide(one, two);
                break;
            }
            case MULTIPLY: {
                result = CalcOperations.multiply(one, two);
                break;
            }
        }
        return result;
    }

}
