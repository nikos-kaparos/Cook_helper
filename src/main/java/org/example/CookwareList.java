package org.example;

public class CookwareList {
    private Cookware[] cookwares;

    public CookwareList(int i) {
        cookwares = new Cookware[i];
    }

    public void add(Cookware cookware, int i) {
        this.cookwares[i] = cookware;
    }

    public Cookware get(int i) {
        return this.cookwares[i];
    }

    public int length() {
        return this.cookwares.length;
    }

    public boolean exists(String name) {
        for (int i = 0; i < this.cookwares.length; i++) {
            if (this.cookwares[i] != null && this.cookwares[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void printList() {
        System.out.println("\nΣκεύη:");
        for (int i = 0; i < this.cookwares.length; i++) {
            if (this.cookwares[i] != null) {
                System.out.println(this.cookwares[i].getName());
            }
        }
    }
}
