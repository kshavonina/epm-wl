package com.epam.wl.services;

import com.epam.wl.dao.BookDAO;
import com.epam.wl.dao.UserOrderDAO;
import com.epam.wl.db.JdbcConnector;
import com.epam.wl.entities.UserOrder;
import com.epam.wl.enums.UserOrderStatus;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public class UserOrderService {

    private static UserOrderService instance;
//    private static DataSource dataSource = MockDBHelper.getEmbeddedDatabase();
    private static DataSource dataSource = JdbcConnector.getDataSource();

    private UserOrderDAO userOrderDAO;
    private BookDAO bookDAO;

    private UserOrderService() {
    }

    public static synchronized UserOrderService getInstance() {
        if (instance == null) {
            instance = new UserOrderService();
            instance.userOrderDAO = new UserOrderDAO(dataSource);
            instance.bookDAO = new BookDAO(dataSource);
        }
        return instance;
    }

    public List<UserOrder> getNewUserOrders() {
        List<UserOrder> resultList = new ArrayList<>();
        try {
            resultList = userOrderDAO.getAllUserOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<Integer> getFreeBookInstancesForThisBook(int bookId) {
        //TODO Remove this awful mock
        return TestUserOrderService.getFreeBookInstancesForThisBook(bookId);
    }

    public Map<UserOrder, List<Integer>> getUserOrderAndFreeBookInstanceMap(List<UserOrder> userOrderList) {
        Map<UserOrder, List<Integer>> resultMap = new TreeMap<>(Comparator.comparingInt(UserOrder::getId));
        userOrderList.forEach(userOrder -> resultMap.put(userOrder, getFreeBookInstancesForThisBook(userOrder.getId())));
        return resultMap;
    }

    public void setUserOrderStatus(int userOrderId, UserOrderStatus newStatus) {
        //TODO
    }
}