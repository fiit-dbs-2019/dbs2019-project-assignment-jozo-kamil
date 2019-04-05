package persistancemanagers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Car;
import model.Employee;
import model.ServiceRecord;

import java.sql.*;

public class CarManager {

    public Boolean addNewServisToSpecificCar(Car car, String servisNameAndLocation, Date dateOfService,
                                             Float priceOfService) {
        ServiceRecord serviceRecord;

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        Integer servisID;
        Integer repairID;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT s.servis_id, s.servis_name, s.servis_location " +
                    "FROM servis s " +
                    "WHERE (s.servis_name || ', ' || s.servis_location) = '" + servisNameAndLocation + "';"
            );
            ResultSet rs = st.executeQuery();

            rs.next();

            servisID = rs.getInt("servis_id");
            String serviceName = rs.getString("servis_name");
            String serviceLocation = rs.getString("servis_location");

            if(servisID == null) {
                return false;
            }

            st = conn.prepareStatement("INSERT INTO repair(date, price, servis_id) VALUES(?,?,?) RETURNING repair_id, type;"
            );
            st.setDate(1,dateOfService);
            st.setFloat(2,priceOfService);
            st.setInt(3,servisID);

            rs = st.executeQuery();

            rs.next();

            repairID = rs.getInt("repair_id");
            String typeOfService = rs.getString("type");

            st = conn.prepareStatement("INSERT INTO car_repair(car_vin, repair_id) VALUES(?,?)"
            );
            st.setString(1,car.getCar_vin());
            st.setInt(2,repairID);

            st.executeUpdate();

            serviceRecord = new ServiceRecord(serviceName,serviceLocation,typeOfService,dateOfService,priceOfService);

            car.addServiceRecord(serviceRecord);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public void updateCarInfo(Car car) {
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("UPDATE car " +
                    "SET spz = ?, mileage = ? " +
                    "WHERE car_vin = ?;"
            );
            st.setString(1,car.getSpz());
            st.setInt(2,car.getMileage());
            st.setString(3,car.getCar_vin());

            st.executeUpdate();

            st = conn.prepareStatement("UPDATE car_info " +
                    "SET engine_power = ?, price_per_day = ? " +
                    "WHERE car_info_id = ?"
            );
            st.setInt(1,car.getEngine_power());
            st.setFloat(2,car.getPrice_per_day());
            st.setInt(3,car.getCarInfoID());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public void getCarInfo(Car car) {
        ObservableList<ServiceRecord> serviceRecords = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            // get car info for specific car
            st = conn.prepareStatement("SELECT brand, model, body_style, engine_capacity, engine_power, gear_box, fuel, color, price_per_day FROM car_info WHERE car_info_id = ?;"
            );
            st.setInt(1,car.getCarInfoID());

            ResultSet rs = st.executeQuery();

            rs.next();

            String brand = rs.getString("brand");
            String model = rs.getString("model");
            String body_style = rs.getString("body_style");
            Float engine_capacity = rs.getFloat("engine_capacity");
            Integer engine_power = rs.getInt("engine_power");
            String gear_box = rs.getString("gear_box");
            String fuel = rs.getString("fuel");
            String color = rs.getString("color");
            Float price = rs.getFloat("price_per_day");

            car.setBrand(brand);
            car.setModel(model);
            car.setBody_style(body_style);
            car.setEngine_capacity(engine_capacity);
            car.setEngine_power(engine_power);
            car.setGear_box(gear_box);
            car.setFuel(fuel);
            car.setColor(color);
            car.setPrice_per_day(price);


            // get all service records for specific car
            st = conn.prepareStatement("select s.servis_name, s.servis_location, r.type, r.date, r.price " +
                    "from repair r " +
                    "join car_repair cr on r.repair_id = cr.repair_id " +
                    "join servis s on r.servis_id = s.servis_id " +
                    "where cr.car_vin = '" + car.getCar_vin() + "';"
            );
            rs = st.executeQuery();

            while(rs.next()) {
                String serviceName = rs.getString("servis_name");
                String serviceLocation = rs.getString("servis_location");
                String repairType = rs.getString("type");
                Date dateOfService = rs.getDate("date");
                Float priceOfService = rs.getFloat("price");

                serviceRecords.add(new ServiceRecord(serviceName,serviceLocation,repairType,dateOfService,priceOfService));
            }

            car.setServiceRecords(serviceRecords);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public ObservableList<Car> getCarsByVINorSPZ(String pattern, Integer offSet) {
        ObservableList<Car> listOfCars = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT * " +
                    "FROM car " +
                    "WHERE car_vin ILIKE '" + pattern + "%' OR spz ILIKE '" + pattern + "%'" +
                    "ORDER BY car_vin, spz " +
                    "LIMIT 500 " +
                    "OFFSET " + offSet + ";");
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String carVIN = rs.getString("car_vin");
                Integer carInfoID = rs.getInt("car_info_id");
                Date yearOfProduction = rs.getDate("year_of_production");
                Integer mileAge = rs.getInt("mileage");
                String SPZ = rs.getString("spz");

                listOfCars.add(new Car(carVIN,carInfoID,yearOfProduction,mileAge,SPZ));
            }

            return listOfCars;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public ObservableList<Car> getCars(Integer offSet) {
        ObservableList<Car> listOfCars = FXCollections.observableArrayList();

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement("SELECT * " +
                    "FROM car " +
                    "ORDER BY car_vin, spz " +
                    "LIMIT 500 " +
                    "OFFSET " + offSet + ";");
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                String carVIN = rs.getString("car_vin");
                Integer carInfoID = rs.getInt("car_info_id");
                Date yearOfProduction = rs.getDate("year_of_production");
                Integer mileAge = rs.getInt("mileage");
                String SPZ = rs.getString("spz");

                listOfCars.add(new Car(carVIN,carInfoID,yearOfProduction,mileAge,SPZ));
            }

            return listOfCars;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public Car getCarFromDatabase(String car_vin) throws SQLException {
        Car car = null;
        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();

            st = conn.prepareStatement(
                    "SELECT car_info_id,year_of_production,mileage,spz FROM car WHERE car_vin ILIKE '" + car_vin + "';"
            );
            ResultSet rs = st.executeQuery();
            rs.next();

            Integer car_info_id = rs.getInt("car_info_id");
            Date year_of_production = rs.getDate("year_of_production");
            Integer mileage = rs.getInt("mileage");
            String SPZ = rs.getString("spz");

            st = conn.prepareStatement(
                    "SELECT * FROM car_info WHERE car_info_id = '" + car_info_id + "';"
            );
            rs = st.executeQuery();
            rs.next();

            String brand = rs.getString("brand");
            String model = rs.getString("model");
            String body_style = rs.getString("body_style");
            Float engine_capacity = rs.getFloat("engine_capacity");
            Integer engine_power = rs.getInt("engine_power");
            String gear_box = rs.getString("gear_box");
            String fuel = rs.getString("fuel");
            String color = rs.getString("color");
            Float price_per_day = rs.getFloat("price_per_day");

            car = new Car(car_vin,model,brand,body_style,engine_capacity,engine_power,gear_box,fuel,color,price_per_day,year_of_production,mileage,SPZ);

            return car;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            st.close();
            if (conn != null)
            {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

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
                                       String fuel, String color, Float price_per_day,
                                       Date year_of_production, Integer mileage, String SPZ) throws SQLException {

        AllTablesManager atm;
        Connection conn = null;
        PreparedStatement st = null;

        try {
            atm = new AllTablesManager();
            conn = atm.connect();


            // test if login name already exists in database
            st = conn.prepareStatement(
                    "SELECT EXISTS (SELECT * FROM car WHERE car_vin ILIKE '" + car_vin + "') AS exist;"
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
                st.setFloat(9,price_per_day);

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
