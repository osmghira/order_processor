package main;

import model.Order;
import service.DatabaseConnection;
import service.OrderService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessor {

    private static final String INPUT_FILE = "data/input.json";
    private static final String OUTPUT_FILE = "data/output.json";
    private static final String ERROR_FILE = "data/error.json";

    public static void main(String[] args) {
        OrderService orderService = new OrderService();

        try (Connection connection = DatabaseConnection.getConnection()) {
            List<Order> inputOrders = orderService.loadOrders(INPUT_FILE);
            List<Order> outputOrders = new ArrayList<>();
            List<Order> errorOrders = new ArrayList<>();

            for (Order order : inputOrders) {
                if (orderService.customerExists(order.getCustomerId(), connection)) {
                    orderService.saveOrderToDatabase(order, connection);
                    outputOrders.add(order);
                } else {
                    errorOrders.add(order);
                }
            }

            orderService.saveToFile(OUTPUT_FILE, outputOrders);
            orderService.saveToFile(ERROR_FILE, errorOrders);

            System.out.println("Processing complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
