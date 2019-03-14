package persistancemanagers;

import java.sql.*;

public class CarManager {

    public void addNewCarInfo(String brand,String model) throws SQLException {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "INSERT INTO car_info (brand, model) VALUES " +
                            "(?::car_brand,?::car_model);"
            );
            st.setString(1,brand);
            st.setString(2,model);

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public boolean addNewCarToDatabase(String car_vin, String brand, String model, String body_style,
                                       Float engine_capacity, Integer engine_power, String gear_box,
                                       String fuel, String color, Integer price_per_day,
                                       Date year_of_production, Integer mileage, String SPZ) throws SQLException {

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement(
                    "SELECT EXISTS (SELECT * FROM car WHERE car_vin = '" + car_vin + "') AS exist;"
            );
            ResultSet rsExist = st.executeQuery();

            rsExist.next();

            if(!rsExist.getBoolean("exist")) {
                st = conn.prepareStatement(
                        "INSERT INTO car_info (brand, model, body_style, engine_capacity, engine_power, gear_box, fuel, color, price_per_day) VALUES " +
                                "(?::car_brand,?::car_model,?::car_body_style,?,?,?::car_gear_box,?::car_fuel,?::car_color,?) RETURNING car_info_id;"
                );
                st.setString(1,brand);
                st.setString(2,model);
                st.setString(3,body_style);
                st.setFloat(4,engine_capacity);
                st.setInt(5,engine_power);
                st.setString(6,gear_box);
                st.setString(7,fuel);
                st.setString(8,color);
                st.setInt(9,price_per_day);

                ResultSet rsID = st.executeQuery();

                rsID.next();

                Integer car_info_id = rsID.getInt("car_info_id");

                st = conn.prepareStatement("INSERT INTO car(car_vin, car_info_id, year_of_production, mileage, spz) VALUES (?,?,?,?,?);");
                st.setString(1,car_vin);
                st.setInt(2,car_info_id);
                st.setDate(3,year_of_production);
                st.setInt(4,mileage);
                st.setString(5,SPZ);

                st.executeUpdate();

                return true;
            } else {
                // car with this VIN already exists
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }
}
