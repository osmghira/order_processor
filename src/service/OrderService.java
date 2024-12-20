package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Order;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class OrderService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Order> loadOrders(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class));
    }

    public boolean customerExists(int customerId, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM customers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }

    public void saveOrderToDatabase(Order order, Connection connection) throws SQLException {
        String insertOrderQuery = "INSERT INTO orders (id, date, amount, customer_id, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertOrderQuery)) {
            stmt.setInt(1, order.getId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            stmt.setDouble(3, order.getAmount());
            stmt.setInt(4, order.getCustomerId());
            stmt.setString(5, order.getStatus());
            stmt.executeUpdate();
        }
    }

    public void saveToFile(String filePath, Object data) throws IOException {
        objectMapper.writeValue(new File(filePath), data);
    }
}
