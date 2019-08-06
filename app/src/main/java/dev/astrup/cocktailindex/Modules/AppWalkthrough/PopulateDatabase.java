package dev.astrup.cocktailindex.Modules.AppWalkthrough;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;

import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.Objects.Ingredient;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailSingleton;

public class PopulateDatabase {
    private boolean hasBeenPopulated;
    private AppDatabase db;
    private CocktailSingleton cocktailSingleton;

    public PopulateDatabase() {
        hasBeenPopulated = false;
        cocktailSingleton = CocktailSingleton.getInstance();
    }
    public void populateDatabase(Context context, boolean metric) {
        hasBeenPopulated = true;
        db = AppDatabase.getDatabase(context);
        
        if(metric) createMetricDb(context);
        else createImperialDb(context);


    }

    private void createImperialDb(Context context) {
        /** NEGRONI */
        ArrayList<Ingredient> negroniIngredients = new ArrayList<>();
        negroniIngredients.add(new Ingredient("Gin", "1", "oz."));
        negroniIngredients.add(new Ingredient("Campari", "1", "oz."));
        negroniIngredients.add(new Ingredient("Sweet Vermouth", "1", "oz."));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Negroni",
                "Add all items to a rocks glass with a big ice cube. Garnish with an orange twist",
                R.drawable.cocktail_negroni,
                negroniIngredients), db);

        /** MARTINEZ */
        ArrayList<Ingredient> martinezIngredients = new ArrayList<>();
        martinezIngredients.add(new Ingredient("Gin", "1.5", "oz."));
        martinezIngredients.add(new Ingredient("Sweet Vermouth", "1.5", "oz."));
        martinezIngredients.add(new Ingredient("Luxardo Maraschino", "0.25", "oz."));
        martinezIngredients.add(new Ingredient("Orange Bitters", "2", "dashes"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Martinez",
                "Stir all ingredients with ice and strain into a chilled coupe. Garnish with an orange twist.",
                R.drawable.cocktail_martinez,
                martinezIngredients), db);


        /** CLOVER CLUB */
        ArrayList<Ingredient> cloverclubIngredients = new ArrayList<>();
        cloverclubIngredients.add(new Ingredient("Gin", "2", "oz."));
        cloverclubIngredients.add(new Ingredient("Lemon Juice", "0.75", "oz."));
        cloverclubIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        cloverclubIngredients.add(new Ingredient("Fresh Raspberries", "5", ""));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Clover Club",
                "Shake all ingredients together with ice and strain into a chilled coupe. Garnish with fresh raspberries",
                R.drawable.cocktail_cloverclub,
                cloverclubIngredients), db);


        /** AMARETTO SOUR CLUB */
        ArrayList<Ingredient> amarettosourIngredients = new ArrayList<>();
        amarettosourIngredients.add(new Ingredient("Amaretto", "1.5", "oz."));
        amarettosourIngredients.add(new Ingredient("Bourbon", "0.75", "oz."));
        amarettosourIngredients.add(new Ingredient("Lemon Juice", "1", "oz."));
        amarettosourIngredients.add(new Ingredient("Rich Simple Syrup", "1", "tsp."));
        amarettosourIngredients.add(new Ingredient("Egg White", "0.5", "oz."));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Amaretto Sour",
                "Lightly beat the egg white. Add all ingredients to a cocktail shaker and shake without ice. Add ice and shake again. Strain into a rocks glass and garnish with a lemon peel and cherries.",
                R.drawable.cocktail_amarettosour,
                amarettosourIngredients), db);


        /** MOJITO */
        ArrayList<Ingredient> mojitoIngredients = new ArrayList<>();
        mojitoIngredients.add(new Ingredient("White Rum", "2", "oz."));
        mojitoIngredients.add(new Ingredient("Lime Juice", "1", "oz."));
        mojitoIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        mojitoIngredients.add(new Ingredient("Mint", "10", "leaves"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Mojito",
                "Gently muddle the mint and simple syrup. Add the remaining ingredients and top with crushed ice and stir until the glass is frosty. Garnish with a mint sprig.",
                R.drawable.cocktail_mojito,
                mojitoIngredients), db);


        /** DAIQUIRI */
        ArrayList<Ingredient> daiquiriIngredients = new ArrayList<>();
        daiquiriIngredients.add(new Ingredient("Rum", "2", "oz."));
        daiquiriIngredients.add(new Ingredient("Lime Juice", "0.75", "oz."));
        daiquiriIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Daiquiri",
                "Add all the items to the cocktail shaker. Shake with ice and double strain into a chilled cocktail coupe.",
                R.drawable.cocktail_daiquiri,
                daiquiriIngredients), db);


        /** GIMLET */
        ArrayList<Ingredient> gimletIngredients = new ArrayList<>();
        gimletIngredients.add(new Ingredient("Gin", "2", "oz."));
        gimletIngredients.add(new Ingredient("Lime Cordial", "0.75", "oz."));
        gimletIngredients.add(new Ingredient("Lime Juice", "0.75", "oz."));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Gimlet",
                "Add all items to the cocktail shaker and shake with ice. Double strain into a chilled cocktail coupe.",
                R.drawable.cocktail_gimlet,
                gimletIngredients), db);


        /** MANHATTAN */
        ArrayList<Ingredient> manhattanIngredients = new ArrayList<>();
        manhattanIngredients.add(new Ingredient("Rye", "2", "oz."));
        manhattanIngredients.add(new Ingredient("Sweet Vermouth", "1", "oz."));
        manhattanIngredients.add(new Ingredient("Angostura Bitters", "2", "dashes"));
        manhattanIngredients.add(new Ingredient("Orange Bitters", "1", "dashes"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Manhattan",
                "Add all items to a mixing glass and stir with ice. Strain into a chilled cocktail coupe and garnish with brandied cherries.",
                R.drawable.cocktail_manhattan,
                manhattanIngredients), db);


        /** OLD FASHIONED */
        ArrayList<Ingredient> oldfashionedIngredients = new ArrayList<>();
        oldfashionedIngredients.add(new Ingredient("Rye", "3", "oz."));
        oldfashionedIngredients.add(new Ingredient("Simple Syrup", "1", "tsp."));
        oldfashionedIngredients.add(new Ingredient("Angostura Bitters", "3", "dashes"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Old Fashioned",
                "Stir ingredients in a mixing glass with ice. Strain into a rocks glass with a large ice cube and express an orange or lemon peel.",
                R.drawable.cocktail_oldfashioned,
                oldfashionedIngredients), db);


        /** OLD FASHIONED */
        ArrayList<Ingredient> beeskneesIngredients = new ArrayList<>();
        beeskneesIngredients.add(new Ingredient("Gin", "2", "oz."));
        beeskneesIngredients.add(new Ingredient("Honey Syrup", "0.75", "oz."));
        beeskneesIngredients.add(new Ingredient("Lemon Juice", "0.75", "oz."));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Bee's Knees",
                "Add all ingredients to a cocktail shaker and shake with ice. Strain into chilled cocktail coupe. Garnish with a lemon twist or rosemary.",
                R.drawable.cocktail_beesknees,
                beeskneesIngredients), db);
    }
    private void createMetricDb(Context context) {
        /** NEGRONI */
        ArrayList<Ingredient> negroniIngredients = new ArrayList<>();
        negroniIngredients.add(new Ingredient("Gin", "30", "mL"));
        negroniIngredients.add(new Ingredient("Campari", "30", "mL"));
        negroniIngredients.add(new Ingredient("Sweet Vermouth", "30", "mL"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Negroni",
                "Add all items to a rocks glass with a big ice cube. Garnish with an orange twist",
                R.drawable.cocktail_negroni,
                negroniIngredients), db);

        /** MARTINEZ */
        ArrayList<Ingredient> martinezIngredients = new ArrayList<>();
        martinezIngredients.add(new Ingredient("Gin", "45", "mL"));
        martinezIngredients.add(new Ingredient("Sweet Vermouth", "45", "mL"));
        martinezIngredients.add(new Ingredient("Luxardo Maraschino", "7.5", "mL"));
        martinezIngredients.add(new Ingredient("Orange Bitters", "2", "dashes"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Martinez",
                "Stir all ingredients with ice and strain into a chilled coupe. Garnish with an orange twist.",
                R.drawable.cocktail_martinez,
                martinezIngredients), db);


        /** CLOVER CLUB */
        ArrayList<Ingredient> cloverclubIngredients = new ArrayList<>();
        cloverclubIngredients.add(new Ingredient("Gin", "60", "mL"));
        cloverclubIngredients.add(new Ingredient("Lemon Juice", "22.5", "mL"));
        cloverclubIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        cloverclubIngredients.add(new Ingredient("Fresh Raspberries", "5", ""));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Clover Club",
                "Shake all ingredients together with ice and strain into a chilled coupe. Garnish with fresh raspberries",
                R.drawable.cocktail_cloverclub,
                cloverclubIngredients), db);


        /** AMARETTO SOUR CLUB */
        ArrayList<Ingredient> amarettosourIngredients = new ArrayList<>();
        amarettosourIngredients.add(new Ingredient("Amaretto", "45", "mL"));
        amarettosourIngredients.add(new Ingredient("Bourbon", "22.5", "mL"));
        amarettosourIngredients.add(new Ingredient("Lemon Juice", "30", "mL"));
        amarettosourIngredients.add(new Ingredient("Rich Simple Syrup", "5", "mL"));
        amarettosourIngredients.add(new Ingredient("Egg White", "15", "mL"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Amaretto Sour",
                "Lightly beat the egg white. Add all ingredients to a cocktail shaker and shake without ice. Add ice and shake again. Strain into a rocks glass and garnish with a lemon peel and cherries.",
                R.drawable.cocktail_amarettosour,
                amarettosourIngredients), db);


        /** MOJITO */
        ArrayList<Ingredient> mojitoIngredients = new ArrayList<>();
        mojitoIngredients.add(new Ingredient("White Rum", "60", "mL"));
        mojitoIngredients.add(new Ingredient("Lime Juice", "30", "mL"));
        mojitoIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        mojitoIngredients.add(new Ingredient("Mint", "10", "leaves"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Mojito",
                "Gently muddle the mint and simple syrup. Add the remaining ingredients and top with crushed ice and stir until the glass is frosty. Garnish with a mint sprig.",
                R.drawable.cocktail_mojito,
                mojitoIngredients), db);


        /** DAIQUIRI */
        ArrayList<Ingredient> daiquiriIngredients = new ArrayList<>();
        daiquiriIngredients.add(new Ingredient("Rum", "60", "mL"));
        daiquiriIngredients.add(new Ingredient("Lime Juice", "22.5", "mL"));
        daiquiriIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Daiquiri",
                "Add all the items to the cocktail shaker. Shake with ice and double strain into a chilled cocktail coupe.",
                R.drawable.cocktail_daiquiri,
                daiquiriIngredients), db);


        /** GIMLET */
        ArrayList<Ingredient> gimletIngredients = new ArrayList<>();
        gimletIngredients.add(new Ingredient("Gin", "60", "mL"));
        gimletIngredients.add(new Ingredient("Lime Cordial", "22.5", "mL"));
        gimletIngredients.add(new Ingredient("Lime Juice", "22.5", "mL"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Gimlet",
                "Add all items to the cocktail shaker and shake with ice. Double strain into a chilled cocktail coupe.",
                R.drawable.cocktail_gimlet,
                gimletIngredients), db);


        /** MANHATTAN */
        ArrayList<Ingredient> manhattanIngredients = new ArrayList<>();
        manhattanIngredients.add(new Ingredient("Rye", "60", "mL"));
        manhattanIngredients.add(new Ingredient("Sweet Vermouth", "30", "mL"));
        manhattanIngredients.add(new Ingredient("Angostura Bitters", "2", "dashes"));
        manhattanIngredients.add(new Ingredient("Orange Bitters", "1", "dashes"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Manhattan",
                "Add all items to a mixing glass and stir with ice. Strain into a chilled cocktail coupe and garnish with brandied cherries.",
                R.drawable.cocktail_manhattan,
                manhattanIngredients), db);


        /** OLD FASHIONED */
        ArrayList<Ingredient> oldfashionedIngredients = new ArrayList<>();
        oldfashionedIngredients.add(new Ingredient("Rye", "90", "mL"));
        oldfashionedIngredients.add(new Ingredient("Simple Syrup", "5", "mL"));
        oldfashionedIngredients.add(new Ingredient("Angostura Bitters", "3", "dashes"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Old Fashioned",
                "Stir ingredients in a mixing glass with ice. Strain into a rocks glass with a large ice cube and express an orange or lemon peel.",
                R.drawable.cocktail_oldfashioned,
                oldfashionedIngredients), db);


        /** OLD FASHIONED */
        ArrayList<Ingredient> beeskneesIngredients = new ArrayList<>();
        beeskneesIngredients.add(new Ingredient("Gin", "60", "mL"));
        beeskneesIngredients.add(new Ingredient("Honey Syrup", "22.5", "mL"));
        beeskneesIngredients.add(new Ingredient("Lemon Juice", "22.5", "mL"));
        cocktailSingleton.addCocktail(createCocktail(context,
                "Bee's Knees",
                "Add all ingredients to a cocktail shaker and shake with ice. Strain into chilled cocktail coupe. Garnish with a lemon twist or rosemary.",
                R.drawable.cocktail_beesknees,
                beeskneesIngredients), db);
    }


    private static Cocktail createCocktail(Context context, String name, String recipe, int drawable, ArrayList<Ingredient> ingredients) {
        ArrayList<String> images = new ArrayList();
        images.add(getUriToResource(context, drawable).toString());
        Cocktail cocktail = new Cocktail(name, recipe, "", images, false, false);

        // Setting foreign key and adding ingredients to object
        int count = 0;
        for(Ingredient i : ingredients) {
            i.setNumber(count);
            i.setCocktailIdFk(cocktail.id);
            count++;
        }
        cocktail.setIngredients(ingredients);

        return cocktail;
    }

    /**
     * get uri to any resource type
     * @param context - context
     * @param resId - resource id
     * @throws Resources.NotFoundException if the given ID does not exist.
     * @return - Uri to resource by given id
     */
    public static Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }
}
