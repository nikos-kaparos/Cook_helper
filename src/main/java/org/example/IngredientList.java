package org.example;

import java.util.ArrayList;
import java.util.List;

public class IngredientList extends ItemList<Ingredient>{
    public IngredientList(int size) {
        super(size);
    }

        public boolean nameExists(String name) {
        for (int i = 0; i < this.getSize(); i++) {
            Ingredient ingredient = this.getItem(i);
            if (ingredient != null && ingredient.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public int getPosition(String name){
        for(int i = 0; i < this.getSize(); i++){
            Ingredient ingredient = this.getItem(i);
            if(ingredient!=null && ingredient.getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    public void printList() {
        System.out.println("\nΥλικά:");
        for (int j = 0; j< this.getSize(); j++) {
            if (this.getItem(j) != null) {
                 if (this.getItem(j).getQuantity() == null) {
                    System.out.println(this.getItem(j).getName() );
                 } else if (this.getItem(j).getUnit() == null) {
                     System.out.println(this.getItem(j).getName() +" "+ this.getItem(j).getQuantity());
                 } else {
                    System.out.println(this.getItem(j).getName() + " " + this.getItem(j).getQuantity() + " " + this.getItem(j).getUnit());
                }
            }
        }
    }
}
