package servlet;

import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName UpdateGoodsServlet
 * @Description TODO
 * @Author 付小雷
 * @Date 2020/2/1218:24
 * @Version1.0
 **/
@WebServlet("/updateGoods")
public class UpdateGoodsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String idString = req.getParameter("goodsID");
        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stringStock = req.getParameter("stock");
        String unit = req.getParameter("unit");
        String stringPrice = req.getParameter("price");
        String stringDiscount = req.getParameter("discount");

        Integer id = Integer.valueOf(idString);
        Integer stock = Integer.valueOf(stringStock);
        double a= Double.parseDouble(stringPrice)*100;
        int b = (int)a;
        Integer price = b;
        Integer discount = Integer.valueOf(stringDiscount);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Goods goods = getGoods(id);
        if(goods==null){
            System.out.println("没有该商品");
            resp.sendRedirect("index.html");
        }else{
            goods.setName(name);
            goods.setDiscount(discount);
            goods.setIntroduce(introduce);
            goods.setStock(stock);
            goods.setPrice(price);
            goods.setUnit(unit);

            boolean effect = modifyGoods(goods);

            if(effect){
                System.out.println("更新成功！");
                resp.sendRedirect("goodsbrowse.html");
            }else{
                System.out.println("更新失败！");
                resp.sendRedirect("index.html");
            }
        }

    }
    private boolean modifyGoods(Goods goods){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean effect = false;
        try {
            String sql = "update goods set name=?,introduce=?,stock=?,unit=?,price=?," +
                    "discount=? where id=?";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,goods.getName());
            preparedStatement.setString(2,goods.getIntroduce());
            preparedStatement.setInt(3,goods.getStock());
            preparedStatement.setString(4,goods.getUnit());
            preparedStatement.setInt(5,goods.getPriceInt());
            preparedStatement.setInt(6,goods.getDiscount());
            preparedStatement.setInt(7,goods.getId());
            effect = (preparedStatement.executeUpdate()==1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return effect;
    }
    private Goods getGoods(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Goods goods = null;

        try {
            String sql = "select * from goods where id=?";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                goods = extractGoods(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return goods;
    }
    private Goods extractGoods(ResultSet resultSet) throws SQLException {
        Goods goods = new Goods();
        goods.setId(resultSet.getInt("id"));
        goods.setName(resultSet.getString("name"));
        goods.setDiscount(resultSet.getInt("discount"));
        goods.setIntroduce(resultSet.getString("introduce"));
        goods.setStock(resultSet.getInt("stock"));
        goods.setPrice(resultSet.getInt("price"));
        goods.setUnit(resultSet.getString("unit"));
        return goods;
    }
}
