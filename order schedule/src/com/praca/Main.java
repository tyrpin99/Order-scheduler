package com.praca;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        // odczytujemy argumenty wywołania programu - ścieżki do plików json
        if (args.length != 2) {
            System.err.println("Nieprawidłowa liczba argumentów. Podaj ścieżkę do pliku konfiguracyjnego oraz do pliku z zamówieniami.");
            System.exit(1);
        }
        String storeConfigPath = args[0];
        String ordersPath = args[1];

        // wczytujemy konfigurację sklepu
        StoreConfig storeConfig = StoreConfig.fromJson(storeConfigPath); // Files.readString(Path.of(storeConfigPath)));

        // wczytujemy listę zamówień
        List<Order> orders = new ArrayList<>();
        String ordersJson = Files.readString(Path.of(ordersPath));
        JSONArray ordersArray = new JSONArray(ordersJson);
        for (int i = 0; i < ordersArray.length(); i++) {
            JSONObject orderJson = ordersArray.getJSONObject(i);
            String orderId = orderJson.getString("orderId");
            BigDecimal orderValue = orderJson.getBigDecimal("orderValue");
            Duration pickingTime = Duration.parse(orderJson.getString("pickingTime"));
            LocalTime completeBy = LocalTime.parse(orderJson.getString("completeBy"));
            orders.add(new Order(orderId, orderValue, pickingTime, completeBy));
        }

        // sortujemy zamówienia malejąco po wartości, żeby zaczynać od największych //.reversed
        orders.sort(Comparator.comparing(Order::getOrderValue).reversed()); // to dziala dobrze dla optimize-rorder-count ale slabo dla advenced order value
      //  orders.sort(Comparator.comparing(Order::getPickingTime));

        List<Picker> pickers = storeConfig.getPickers();
        // rozdzielamy zamówienia między pickerów
        for (Order order : orders) {
            Picker pickerFound = null;
            for (Picker picker : pickers) {
                if (picker.isAvailable(order)) {
                    if (picker.getStartTime().plus(order.getPickingTime()).isBefore(order.getCompleteBy()) || picker.getStartTime().plus(order.getPickingTime()).equals(order.getCompleteBy()) ) { // ten warunek mi zle dziala z danymi z allocation
                        if (pickerFound != null) {
                            if (picker.getStartTime().isBefore(pickerFound.getStartTime())) {
                                pickerFound = picker;
                            }
                        } else {
                            pickerFound = picker;

                        }
                    }
                }
            }
            if (pickerFound == null) {
                // nie znaleziono harmonogramu, który może przyjąć to zamówienie
                System.err.println("Nie znaleziono wolnego pracownika dla zamówienia " + order.getOrderId());
                continue;
            } else {
                pickerFound.addOrder(order);
            }

        }

        // wyświetlamy harmonogramy na standardowym wyjściu
        for (Picker picker : pickers) {
            for (Order order : picker.getOrders()) {
                System.out.println(picker.getId() + " " + order.getOrderId() + " " + order.getStartTimeAssigned());
            }
        }
    }
}
