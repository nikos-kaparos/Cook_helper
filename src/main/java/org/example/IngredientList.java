package org.example;

import java.util.ArrayList;
import java.util.List;

public class IngredientList {
    private Ingredient[] ingredients;

    public IngredientList(int i) {
        this.ingredients = new Ingredient[i];
    }

    public void addIngredient(Ingredient ingredient, int i) {
        this.ingredients[i] = ingredient;
    }

    public Ingredient getIngredient(int i ) {
        return ingredients[i];
    }

    public int getLength() {
        return ingredients.length;
    }

    public boolean nameExists(String name) {
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] != null && ingredients[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getPosition(String name) {
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] != null && ingredients[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void printList() {
        System.out.println("\nΥλικά:");
        for (int j = 0; j< ingredients.length; j++) {
            if (ingredients[j] != null) {
                 if (ingredients[j].getQuantity() == null) {
                    System.out.println(ingredients[j].getName() );
                 } else if (ingredients[j].getUnit() == null) {
                     System.out.println(ingredients[j].getName() +" "+ ingredients[j].getQuantity());
                 } else {
                    System.out.println(ingredients[j].getName() + " " + ingredients[j].getQuantity() + " " + ingredients[j].getUnit());
                }
            }
        }
    }

}
