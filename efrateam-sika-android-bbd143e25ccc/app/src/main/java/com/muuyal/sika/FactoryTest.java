package com.muuyal.sika;

import com.muuyal.sika.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isra on 5/24/17.
 */

public class FactoryTest {

    public static List<String> getStates() {
        List<String> mStates = new ArrayList<>();
        mStates.add("Ciudad de México");
        mStates.add("Estado de México");
        mStates.add("Aguascalientes");
        mStates.add("Jalisco");
        mStates.add("Oaxaca");
        mStates.add("Veracruz");
        mStates.add("Morelos");
        mStates.add("Zacatecas");
        /*mStates.add("Guadalajara");
        mStates.add("Campeche");
        mStates.add("Quintana Roo");
        mStates.add("Guanajuato");
        */

        return mStates;
    }

    public static List<String> getCities() {
        List<String> mStates = new ArrayList<>();
        mStates.add("Álvaro Obregón");
        mStates.add("Benito Juárez");
        mStates.add("Hipodromo Condesa");
        mStates.add("Iztapalapa");
        mStates.add("Tlahuac");
        mStates.add("Gustavo A. Madero");
        mStates.add("Iztacalco");
        mStates.add("Xochimilco");

        return mStates;
    }

    public static List<String> getStores() {
        List<String> mStates = new ArrayList<>();
        mStates.add("Roma Sur");
        mStates.add("SLP");
        mStates.add("Atlixco");

        return mStates;
    }

    public static List<String> getProducts() {
        List<String> mStates = new ArrayList<>();
        mStates.add("Adhesivos epóxicos");
        mStates.add("Aditivos");
        mStates.add("Anclajes");
        mStates.add("Bandas");
        mStates.add("Complementarios concreto");
        mStates.add("Complementarios Sellado");
        mStates.add("Complementos Pisos");

        return mStates;
    }

    public static List<String> getHomeItems() {
        List<String> mStates = new ArrayList<>();
        mStates.add("Filtración de Humedad");
        mStates.add("Fisuras");
        mStates.add("Desgaste");

        return mStates;
    }

    public static List<String> getProductItems() {
        List<String> mStates = new ArrayList<>();
        mStates.add("Product 1");
        mStates.add("Product 2");
        mStates.add("Product 3");
        mStates.add("Product 4");
        mStates.add("Product 5");
        mStates.add("Product 6");
        mStates.add("Product 7");

        return mStates;
    }

    public static List<Step> getSteps() {
        List<Step> mSteps = new ArrayList<>();
        //mSteps.add(new Step(R.drawable.step_1, "Limpia la superficie, debe estar seca y sin restos de polvo o material desprendido."));
        //mSteps.add(new Step(R.drawable.step_2, "Aplica una capa de Emulsika Primer si la losa es nueva o Igol® Imprimante si la losa a tiene producto aplicado."));
        //mSteps.add(new Step(R.drawable.step_3, "Coloca Sika® Manto, calienta la cara interna por medio de soplete de gas hasta fundir la película transparente y de inmediato presiónalo ligeramente sobre la superficie."));

        return mSteps;
    }

}
