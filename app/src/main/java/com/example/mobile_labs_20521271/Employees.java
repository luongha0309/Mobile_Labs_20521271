package com.example.mobile_labs_20521271;
import java.text.DecimalFormat;

public class Employees {
    protected String name;
    protected double salary;
    protected double net_Salary;


    public Employees(String a, double b) {
        this.name = a;
        this.salary = b;
    }

    public String getName() {
        return name;
    }

    public void setName(){
        this.name = name;
    }

    public double getSalary(){
        return salary;
    }

    public void setSalary(){
        this.salary = salary;
    }

    public double getNetSalary(){
        return net_Salary;
    }

    public void setNetSalary(){
        this.net_Salary = net_Salary;
    }

    public String calculationNetSalary(){
        double a, tax;
        DecimalFormat df = new DecimalFormat("#,###.#");

        a = this.salary - this.salary * 0.105;
        tax = (this.salary - 11000000) * 0.05;

        if (a <= 11000000){
            net_Salary = a;
        } else {
            net_Salary = a - tax;
        }
        return df.format(net_Salary);
    }
}

