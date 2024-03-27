package com.example.lab5_ph32598;

public class Model {
    String _id;
    String ten;

    public Model() {
    }

    public Model(String ten) {
        this.ten = ten;
    }

    public Model(String _id, String ten) {
        this._id = _id;
        this.ten = ten;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
