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
import dev.astrup.cocktailindex.Utility.CocktailController;

public class PopulateDatabase {
    private boolean hasBeenPopulated;
    private AppDatabase db;
    private CocktailController cocktailController;

    public PopulateDatabase() {
        hasBeenPopulated = false;
        cocktailController = CocktailController.getInstance();
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
        cocktailController.addCocktail(createCocktail(context,
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
        cocktailController.addCocktail(createCocktail(context,
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
        cocktailController.addCocktail(createCocktail(context,
                "Clover Club",
                "Shake all ingredients together with ice and strain into a chilled coupe. Garnish with fresh raspberries",
                R.drawable.cocktail_cloverclub,
                cloverclubIngredients), db);


        /** MOJITO */
        ArrayList<Ingredient> mojitoIngredients = new ArrayList<>();
        mojitoIngredients.add(new Ingredient("White Rum", "2", "oz."));
        mojitoIngredients.add(new Ingredient("Lime Juice", "1", "oz."));
        mojitoIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        mojitoIngredients.add(new Ingredient("Mint", "10", "leaves"));
        cocktailController.addCocktail(createCocktail(context,
                "Mojito",
                "Gently muddle the mint and simple syrup. Add the remaining ingredients and top with crushed ice and stir until the glass is frosty. Garnish with a mint sprig.",
                R.drawable.cocktail_mojito,
                mojitoIngredients), db);


        /** DAIQUIRI */
        ArrayList<Ingredient> daiquiriIngredients = new ArrayList<>();
        daiquiriIngredients.add(new Ingredient("Rum", "2", "oz."));
        daiquiriIngredients.add(new Ingredient("Lime Juice", "0.75", "oz."));
        daiquiriIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        cocktailController.addCocktail(createCocktail(context,
                "Daiquiri",
                "Add all the items to the cocktail shaker. Shake with ice and double strain into a chilled cocktail coupe.",
                R.drawable.cocktail_daiquiri,
                daiquiriIngredients), db);


        /** GIMLET */
        ArrayList<Ingredient> gimletIngredients = new ArrayList<>();
        gimletIngredients.add(new Ingredient("Gin", "2", "oz."));
        gimletIngredients.add(new Ingredient("Lime Cordial", "0.75", "oz."));
        gimletIngredients.add(new Ingredient("Lime Juice", "0.75", "oz."));
        cocktailController.addCocktail(createCocktail(context,
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
        cocktailController.addCocktail(createCocktail(context,
                "Manhattan",
                "Add all items to a mixing glass and stir with ice. Strain into a chilled cocktail coupe and garnish with brandied cherries.",
                R.drawable.cocktail_manhattan,
                manhattanIngredients), db);


        /** OLD FASHIONED */
        ArrayList<Ingredient> oldfashionedIngredients = new ArrayList<>();
        oldfashionedIngredients.add(new Ingredient("Rye", "3", "oz."));
        oldfashionedIngredients.add(new Ingredient("Simple Syrup", "1", "tsp."));
        oldfashionedIngredients.add(new Ingredient("Angostura Bitters", "3", "dashes"));
        cocktailController.addCocktail(createCocktail(context,
                "Old Fashioned",
                "Stir ingredients in a mixing glass with ice. Strain into a rocks glass with a large ice cube and express an orange or lemon peel.",
                R.drawable.cocktail_oldfashioned,
                oldfashionedIngredients), db);


        /** BEES KNEES */
        ArrayList<Ingredient> beeskneesIngredients = new ArrayList<>();
        beeskneesIngredients.add(new Ingredient("Gin", "2", "oz."));
        beeskneesIngredients.add(new Ingredient("Honey Syrup", "0.75", "oz."));
        beeskneesIngredients.add(new Ingredient("Lemon Juice", "0.75", "oz."));
        cocktailController.addCocktail(createCocktail(context,
                "Bee's Knees",
                "Add all ingredients to a cocktail shaker and shake with ice. Strain into chilled cocktail coupe. Garnish with a lemon twist or rosemary.",
                R.drawable.cocktail_beesknees,
                beeskneesIngredients), db);


        /** GIN BASIL SMASH */
        ArrayList<Ingredient> ginbasilsmashIngredients = new ArrayList<>();
        ginbasilsmashIngredients.add(new Ingredient("Gin", "2", "oz."));
        ginbasilsmashIngredients.add(new Ingredient("Lemon Juice", "1", "oz."));
        ginbasilsmashIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        ginbasilsmashIngredients.add(new Ingredient("Basil", "10", "leaves"));
        cocktailController.addCocktail(createCocktail(context,
                "Gin Basil Smash",
                "Gently muddle basil leaves in a cocktail shaker. Add the other ingredients and shake with ice. Double strain into an old fashioned glass and garnish with basil leaves..",
                R.drawable.cocktail_ginbasilsmash,
                ginbasilsmashIngredients), db);


        /** MAI TAI */
        ArrayList<Ingredient> maitaiIngredients = new ArrayList<>();
        maitaiIngredients.add(new Ingredient("Rum", "2", "oz."));
        maitaiIngredients.add(new Ingredient("Curacao", "0.5", "oz."));
        maitaiIngredients.add(new Ingredient("Lime Juice", "0.75", "oz."));
        maitaiIngredients.add(new Ingredient("Orgeat", "0.25", "oz."));
        maitaiIngredients.add(new Ingredient("Simple Syrup", "0.25", "oz."));
        cocktailController.addCocktail(createCocktail(context,
                "Mai Tai",
                "Add all ingredients to a cocktail shaker and shake with ice. Strain into a rocks glass or tiki mug. Garnish with lots of mint.",
                R.drawable.cocktail_maitai,
                maitaiIngredients), db);


        /** MARTINI */
        ArrayList<Ingredient> martiniIngredients = new ArrayList<>();
        martiniIngredients.add(new Ingredient("Gin", "2", "oz."));
        martiniIngredients.add(new Ingredient("Dry Vermouth", "1", "oz."));
        martiniIngredients.add(new Ingredient("Orange Bitters", "1", "dash"));
        Cocktail martinicocktail = createCocktail(context,
                "Martini",
                "Add ingredients to a mixing glass and stir with ice. Strain into chilled coupe and garnish with lemon twist.",
                R.drawable.cocktail_martini,
                martiniIngredients);
        martinicocktail.comments = "Reduce or increase the amount of vermouth if you prefer a more dry or more wet martini.";
        cocktailController.addCocktail(martinicocktail, db);



        /** WHISKY SOUR */
        ArrayList<Ingredient> whiskysourIngredients = new ArrayList<>();
        whiskysourIngredients.add(new Ingredient("Whisky", "2", "oz."));
        whiskysourIngredients.add(new Ingredient("Lemon Juice", "0.75", "oz."));
        whiskysourIngredients.add(new Ingredient("Simple Syrup", "0.75", "oz."));
        whiskysourIngredients.add(new Ingredient("Egg White", "0.5", "oz."));
        cocktailController.addCocktail(createCocktail(context,
                "Whisky Sour",
                "Add all ingredients to a cocktail shaker and shake without ice. Add ice and shake for a short time. Strain into a rocks glass. Garnish with an orange twist.",
                R.drawable.cocktail_whiskysour,
                whiskysourIngredients), db);


    }
    private void createMetricDb(Context context) {
        /** NEGRONI */
        ArrayList<Ingredient> negroniIngredients = new ArrayList<>();
        negroniIngredients.add(new Ingredient("Gin", "30", "mL"));
        negroniIngredients.add(new Ingredient("Campari", "30", "mL"));
        negroniIngredients.add(new Ingredient("Sweet Vermouth", "30", "mL"));
        cocktailController.addCocktail(createCocktail(context,
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
        cocktailController.addCocktail(createCocktail(context,
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
        cocktailController.addCocktail(createCocktail(context,
                "Clover Club",
                "Shake all ingredients together with ice and strain into a chilled coupe. Garnish with fresh raspberries",
                R.drawable.cocktail_cloverclub,
                cloverclubIngredients), db);



        /** MOJITO */
        ArrayList<Ingredient> mojitoIngredients = new ArrayList<>();
        mojitoIngredients.add(new Ingredient("White Rum", "60", "mL"));
        mojitoIngredients.add(new Ingredient("Lime Juice", "30", "mL"));
        mojitoIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        mojitoIngredients.add(new Ingredient("Mint", "10", "leaves"));
        cocktailController.addCocktail(createCocktail(context,
                "Mojito",
                "Gently muddle the mint and simple syrup. Add the remaining ingredients and top with crushed ice and stir until the glass is frosty. Garnish with a mint sprig.",
                R.drawable.cocktail_mojito,
                mojitoIngredients), db);


        /** DAIQUIRI */
        ArrayList<Ingredient> daiquiriIngredients = new ArrayList<>();
        daiquiriIngredients.add(new Ingredient("Rum", "60", "mL"));
        daiquiriIngredients.add(new Ingredient("Lime Juice", "22.5", "mL"));
        daiquiriIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        cocktailController.addCocktail(createCocktail(context,
                "Daiquiri",
                "Add all the items to the cocktail shaker. Shake with ice and double strain into a chilled cocktail coupe.",
                R.drawable.cocktail_daiquiri,
                daiquiriIngredients), db);


        /** GIMLET */
        ArrayList<Ingredient> gimletIngredients = new ArrayList<>();
        gimletIngredients.add(new Ingredient("Gin", "60", "mL"));
        gimletIngredients.add(new Ingredient("Lime Cordial", "22.5", "mL"));
        gimletIngredients.add(new Ingredient("Lime Juice", "22.5", "mL"));
        cocktailController.addCocktail(createCocktail(context,
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
        cocktailController.addCocktail(createCocktail(context,
                "Manhattan",
                "Add all items to a mixing glass and stir with ice. Strain into a chilled cocktail coupe and garnish with brandied cherries.",
                R.drawable.cocktail_manhattan,
                manhattanIngredients), db);


        /** OLD FASHIONED */
        ArrayList<Ingredient> oldfashionedIngredients = new ArrayList<>();
        oldfashionedIngredients.add(new Ingredient("Rye", "90", "mL"));
        oldfashionedIngredients.add(new Ingredient("Simple Syrup", "5", "mL"));
        oldfashionedIngredients.add(new Ingredient("Angostura Bitters", "3", "dashes"));
        cocktailController.addCocktail(createCocktail(context,
                "Old Fashioned",
                "Stir ingredients in a mixing glass with ice. Strain into a rocks glass with a large ice cube and express an orange or lemon peel.",
                R.drawable.cocktail_oldfashioned,
                oldfashionedIngredients), db);


        /** BEES KNEES */
        ArrayList<Ingredient> beeskneesIngredients = new ArrayList<>();
        beeskneesIngredients.add(new Ingredient("Gin", "60", "mL"));
        beeskneesIngredients.add(new Ingredient("Honey Syrup", "22.5", "mL"));
        beeskneesIngredients.add(new Ingredient("Lemon Juice", "22.5", "mL"));
        cocktailController.addCocktail(createCocktail(context,
                "Bee's Knees",
                "Add all ingredients to a cocktail shaker and shake with ice. Strain into chilled cocktail coupe. Garnish with a lemon twist or rosemary.",
                R.drawable.cocktail_beesknees,
                beeskneesIngredients), db);



        /** GIN BASIL SMASH */
        ArrayList<Ingredient> ginbasilsmashIngredients = new ArrayList<>();
        ginbasilsmashIngredients.add(new Ingredient("Gin", "60", "mL"));
        ginbasilsmashIngredients.add(new Ingredient("Lemon Juice", "30", "mL"));
        ginbasilsmashIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        ginbasilsmashIngredients.add(new Ingredient("Basil", "10", "leaves"));
        cocktailController.addCocktail(createCocktail(context,
                "Gin Basil Smash",
                "Gently muddle basil leaves in a cocktail shaker. Add the other ingredients and shake with ice. Double strain into an old fashioned glass and garnish with basil leaves..",
                R.drawable.cocktail_ginbasilsmash,
                ginbasilsmashIngredients), db);


        /** MAI TAI */
        ArrayList<Ingredient> maitaiIngredients = new ArrayList<>();
        maitaiIngredients.add(new Ingredient("Rum", "60", "mL"));
        maitaiIngredients.add(new Ingredient("Curacao", "15", "mL"));
        maitaiIngredients.add(new Ingredient("Lime Juice", "22.5", "mL"));
        maitaiIngredients.add(new Ingredient("Orgeat", "7.5", "mL"));
        maitaiIngredients.add(new Ingredient("Simple Syrup", "7.5", "mL"));
        cocktailController.addCocktail(createCocktail(context,
                "Mai Tai",
                "Add all ingredients to a cocktail shaker and shake with ice. Strain into a rocks glass or tiki mug. Garnish with lots of mint.",
                R.drawable.cocktail_maitai,
                maitaiIngredients), db);


        /** MARTINI */
        ArrayList<Ingredient> martiniIngredients = new ArrayList<>();
        martiniIngredients.add(new Ingredient("Gin", "60", "mL"));
        martiniIngredients.add(new Ingredient("Dry Vermouth", "30", "mL"));
        martiniIngredients.add(new Ingredient("Orange Bitters", "30", "dash"));
        Cocktail martinicocktail = createCocktail(context,
                "Martini",
                "Add ingredients to a mixing glass and stir with ice. Strain into chilled coupe and garnish with lemon twist.",
                R.drawable.cocktail_martini,
                martiniIngredients);
        martinicocktail.comments = "Reduce or increase the amount of vermouth if you prefer a more dry or more wet martini.";
        cocktailController.addCocktail(martinicocktail, db);



        /** WHISKY SOUR */
        ArrayList<Ingredient> whiskysourIngredients = new ArrayList<>();
        whiskysourIngredients.add(new Ingredient("Whisky", "60", "mL"));
        whiskysourIngredients.add(new Ingredient("Lemon Juice", "22.5", "mL"));
        whiskysourIngredients.add(new Ingredient("Simple Syrup", "22.5", "mL"));
        whiskysourIngredients.add(new Ingredient("Egg White", "15", "mL"));
        cocktailController.addCocktail(createCocktail(context,
                "Whisky Sour",
                "Add all ingredients to a cocktail shaker and shake without ice. Add ice and shake for a short time. Strain into a rocks glass. Garnish with an orange twist.",
                R.drawable.cocktail_whiskysour,
                whiskysourIngredients), db);
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
