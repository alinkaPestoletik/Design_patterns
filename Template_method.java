abstract class RecipeTemplate {
    protected String[] ingredients;
    public void listIngredients() {
        System.out.println("List the ingredients");
    };
    abstract void prepareRecipe();
    public void serve(){
        System.out.println("Serve the dish");
    };
    final public void makeRecipe(){
        System.out.println("The dish is done");
    };
}

class CakeRecipe extends RecipeTemplate {
    @Override
    public void prepareRecipe(){
        System.out.println("Lets mix all ingredients for dough. Then bake it for 20 minutes. Decorate it with with cream and berries");
    }
}

class SaladRecipe extends RecipeTemplate {
    @Override
    public void prepareRecipe(){
        System.out.println("Cut the vegetables, mix them, add some spices and sauce");
    }
}

public class Template_method {
    public static void main(String[] args) {
        RecipeTemplate cakeRecipe = new CakeRecipe();
        cakeRecipe.prepareRecipe();
        cakeRecipe.makeRecipe();
    
        RecipeTemplate saladRecipe = new SaladRecipe();
        saladRecipe.prepareRecipe();
        saladRecipe.makeRecipe();
    }
}
    