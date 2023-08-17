package diplom2.order;

public class Order {
        private String[] ingredients;
        private String name;
        private String number;
        public Order(String[] ingredients) {
            this.ingredients = ingredients;
        }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
