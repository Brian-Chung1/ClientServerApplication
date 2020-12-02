package BasicClientServer.BackEnd;

public class Vehicle {

    String Type, Make, Model, Color, VIN;
    Integer Year;


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public Vehicle(String type, String make, String model, String color, Integer year, String VIN) {
        this.Type = type;
        this.Make = make;
        this.Model = model;
        this.Color = color;
        this.Year = year;
        this.VIN = VIN;
    }

    public Vehicle() {
        this.Type = null;
        this.Make = null;
        this.Model = null;
        this.Color = null;
        this.Year = 0;
        this.VIN = null;
    }
}
