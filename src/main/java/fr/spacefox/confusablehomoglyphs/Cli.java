package fr.spacefox.confusablehomoglyphs;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
public class Cli {

    public static void main(String[] args) {

        if (args == null || (args.length != 2 && args.length != 4)) {
            System.out.println(
                    "Command line tool to regenerate homoglyph confusion data." + System.lineSeparator()
                  + "This will generate 2 json files: one for Unicode categories, another for the "
                  + "confusion mapping." + System.lineSeparator()
                  + "If you choose to generate these json and don't use the provided ones, don't "
                  + "forget to use them on lib classes initialization." + System.lineSeparator()
                  + "CLI usage: java -jar confusablehomoglyphs.jar {categories.json} {confusables.json} "
                  + "[{categories URL} {confusables URL}]" + System.lineSeparator()
                  + "with parameters :" + System.lineSeparator()
                  + "- {categories.json} (mandatory) path where categories.json will be stored." + System.lineSeparator()
                  + "- {confusables.json} (mandatory) path where confusables.json will be stored." + System.lineSeparator()
                  + "- {categories URL} (optional) URL to retrieve Unicode source data. "
                  + "Default http://unicode.org/Public/UNIDATA/Scripts.txt"  + System.lineSeparator()
                  + "- {confusables URL} (optional) path where categories.json will be stored. "
                  + "Default http://unicode.org/Public/security/latest/confusables.txt" + System.lineSeparator()
                  + "None or both URL must be provided.");
            System.exit(1);
        }
        final String categoriesPath = args[0];
        final String confusablesPath = args[1];
        final String categoriesUrl = args.length > 2 ? args[2] : null;
        final String confusablesUrl = args.length > 2 ? args[3] : null;

        System.out.print("Generating categories data... ");
        UnicodeToCategoriesConverter categories = new UnicodeToCategoriesConverter();
        categories.regenerateJson(categoriesUrl, categoriesPath);
        System.out.println("done. JSON saved at " + categoriesPath);
        System.out.print("Generating confusables data... ");
        UnicodeToConfusablesConverter confusables = new UnicodeToConfusablesConverter();
        confusables.regenerateJson(confusablesUrl, confusablesPath);
        System.out.println("done. JSON saved at " + confusablesPath);
    }
}
