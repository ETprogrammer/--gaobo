package servlet;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @ClassName Inbound
 * @Description TODO
 * @Author 付小雷
 * @Date 2020/2/1114:32
 * @Version1.0
 **/
@WebServlet("/inbound")
public class GoodsPutAwayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stringStock = req.getParameter("stock");
        String unit = req.getParameter("unit");
        String stringPrice = req.getParameter("price");
        String stringDiscount = req.getParameter("discount");

        Integer stock = Integer.valueOf(stringStock);
        double a= Double.parseDouble(stringPrice)*100;
        int b = (int)a;
        Integer price = b;
        Integer discount = Integer.valueOf(stringDiscount);

        System.out.println(name+" "+introduce+" "+stock+" "+unit+" "+price+" "+discount);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into goods (name,introduce,stock,unit,price," +
                    "discount) values(?,?,?,?,?,?)";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql,Statement
            .RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,introduce);
            preparedStatement.setInt(3,stock);
            preparedStatement.setString(4,unit);
            preparedStatement.setInt(5,price);
            preparedStatement.setInt(6,discount);
            int ret = preparedStatement.executeUpdate();
            if(ret==1){
                resp.sendRedirect("index.html");
            }else{
                resp.sendRedirect("index.html");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,null);
        }
    }
}
